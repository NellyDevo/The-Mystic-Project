package MysticMod.Powers;

import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import MysticMod.Powers.GeminiFormPower;
import MysticMod.MysticMod;
import java.util.*;
import java.io.*;

//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;

public class SpontaneousCasterPower extends AbstractPower {
    public static final String POWER_ID = "MysticMod:SpontaneousCasterPower";
    public static final PowerStrings cardStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = cardStrings.NAME;
    public static final String[] DESCRIPTIONS = cardStrings.DESCRIPTIONS;
    private static ArrayList<AbstractCard> affectedCards = new ArrayList<AbstractCard>();
    private static ArrayList<AbstractCard> toRestoreCost = new ArrayList<AbstractCard>();

    //public static final Logger logger = LogManager.getLogger(SpontaneousCasterPower.class);

    public SpontaneousCasterPower(AbstractCreature owner) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;

        this.img = new Texture("MysticMod/images/powers/spontaneous caster power.png");
        this.type = PowerType.BUFF;
        this.amount = -1;
        this.updateDescription();

    }

    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[0];
    }

    @Override
    public void onUseCard(final AbstractCard card, final UseCardAction action) {
        //if card is a spell, set exhaust = true, generate randomCard, make sure randomCard is a spell, add a copy of randomCard to discard pile
        int attacksAndSkillsPlayedThisTurn = 0;
        for (AbstractCard playedCard : AbstractDungeon.actionManager.cardsPlayedThisTurn) {
            if (playedCard.type == AbstractCard.CardType.ATTACK || playedCard.type == AbstractCard.CardType.SKILL) {
                attacksAndSkillsPlayedThisTurn++;
            }
        }
        //logger.info("onUseCard; " + attacksAndSkillsPlayedThisTurn + " Attacks and Skills played this turn.");
        if (
                card.rawDescription.startsWith("Spell.")
                        ||
                        (AbstractDungeon.player.hasPower(GeminiFormPower.POWER_ID) && attacksAndSkillsPlayedThisTurn <= AbstractDungeon.player.getPower(GeminiFormPower.POWER_ID).amount)
                        ||
                        ((card.rawDescription.startsWith("Cantrip.")) && (!AbstractDungeon.player.hasPower(SpellsPlayed.POWER_ID) || (AbstractDungeon.player.getPower(SpellsPlayed.POWER_ID).amount == 1)))
        ) {
            if (card.type == AbstractCard.CardType.ATTACK || card.type == AbstractCard.CardType.SKILL) {
                this.flash();
                //logger.info("onUseCard; " + card.name + " found to be a spell, and exhausted");
                action.exhaustCard = true;
                AbstractCard newCard = MysticMod.returnTrulyRandomSpell();
                AbstractDungeon.actionManager.addToBottom(new com.megacrit.cardcrawl.actions.common.MakeTempCardInDiscardAction(newCard.makeStatEquivalentCopy(), 1));
                /*boolean newCardIsSpell = false;
                while (!newCardIsSpell) {
                    newCard = AbstractDungeon.returnTrulyRandomCard(AbstractDungeon.cardRandomRng);
                    //logger.info("onUseCard; " + newCard.name + " generated");
                    if (newCard.rawDescription.startsWith("Spell.")) {
                        newCardIsSpell = true;
                        AbstractDungeon.actionManager.addToBottom(new com.megacrit.cardcrawl.actions.common.MakeTempCardInDiscardAction(newCard.makeStatEquivalentCopy(), 1));
                        //logger.info("onUseCard; " + newCard.name + " found to be a spell and generated in discard pile");
                    }
                }
                newCardIsSpell = false; */
            }
        }
    }

    @Override
    public void onInitialApplication() {
        //logger.info("onInitialApplication; log on call");
        boolean isSpell = false;
        int attacksAndSkillsPlayedThisTurn = 0;
        for (AbstractCard playedCard : AbstractDungeon.actionManager.cardsPlayedThisTurn) {
            if (playedCard.type == AbstractCard.CardType.ATTACK || playedCard.type == AbstractCard.CardType.SKILL) {
                attacksAndSkillsPlayedThisTurn++;
            }
        }
        //logger.info("onInitialApplication; " + attacksAndSkillsPlayedThisTurn + " Attacks and Skills played this turn.");
        //Iterate through hand. For each card in hand, if cost is not already reduced and card is a Spell, reduce card's cost for combat by 1.
        for (final AbstractCard handCard : AbstractDungeon.player.hand.group) {
            //determine if the card is a spell
            //logger.info("onInitialApplication; checking if " + handCard.name + " is a spell...");
            if (
                    handCard.rawDescription.startsWith("Spell.")
                            ||
                            (AbstractDungeon.player.hasPower(GeminiFormPower.POWER_ID) && attacksAndSkillsPlayedThisTurn < AbstractDungeon.player.getPower(GeminiFormPower.POWER_ID).amount)
            ) {
                if (handCard.type == AbstractCard.CardType.ATTACK || handCard.type == AbstractCard.CardType.SKILL) {
                    isSpell = true;
                    //logger.info("onDrawOrDiscard; " + handCard.name + " found to be a spell");
                }
            }
            //reduce cost if card is a spell, if the cost is not 0, and if cost is unmodified
            if (!this.affectedCards.contains(handCard) && isSpell && handCard.costForTurn != 0) {
                handCard.modifyCostForTurn(-1);
                this.affectedCards.add(handCard);
                //logger.info("onInitialApplication; " + handCard.name + " found to have cost != 0, cost modified and added to affectedCards");
            }
            isSpell = false;
        }
    }

    @Override
    public void atStartOfTurnPostDraw() {
        //logger.info("atStartOfTurnPostDraw; log on call");
        boolean isSpell = false;
        int attacksAndSkillsPlayedThisTurn = 0;
        for (AbstractCard playedCard : AbstractDungeon.actionManager.cardsPlayedThisTurn) {
            if (playedCard.type == AbstractCard.CardType.ATTACK || playedCard.type == AbstractCard.CardType.SKILL) {
                attacksAndSkillsPlayedThisTurn++;
            }
        }
        //logger.info("atStartOfTurnPostDraw; " + attacksAndSkillsPlayedThisTurn + " Attacks and Skills played this turn.");
        //Iterate through hand. For each card in hand, if cost is not already reduced and card is a Spell, reduce card's cost for combat by 1.
        for (final AbstractCard handCard : AbstractDungeon.player.hand.group) {
            //determine if the card is a spell
            //logger.info("atStartOfTurnPostDraw; checking if " + handCard.name + " is a spell...");
            if (handCard.rawDescription.startsWith("Spell.") || (AbstractDungeon.player.hasPower(GeminiFormPower.POWER_ID) && attacksAndSkillsPlayedThisTurn < AbstractDungeon.player.getPower(GeminiFormPower.POWER_ID).amount)) {
                if (handCard.type == AbstractCard.CardType.ATTACK || handCard.type == AbstractCard.CardType.SKILL) {
                    isSpell = true;
                    //logger.info("atStartOfTurnPostDraw; " + handCard.name + " found to be a spell");
                }
            }
            //reduce cost if card is a spell and if cost is unmodified
            if (!this.affectedCards.contains(handCard) && isSpell && handCard.costForTurn != 0) {
                handCard.modifyCostForTurn(-1);
                this.affectedCards.add(handCard);
                //logger.info("onInitialApplication; " + handCard.name + " found to have cost != 0, cost modified and added to affectedCards");
            }
            isSpell = false;
        }
    }

    @Override
    public void duringTurn() {
        //logger.info("duringTurn; log on call");
        boolean isSpell = false;
        int attacksAndSkillsPlayedThisTurn = 0;
        for (AbstractCard playedCard : AbstractDungeon.actionManager.cardsPlayedThisTurn) {
            if (playedCard.type == AbstractCard.CardType.ATTACK || playedCard.type == AbstractCard.CardType.SKILL) {
                attacksAndSkillsPlayedThisTurn++;
            }
        }
        //logger.info("duringTurn; " + attacksAndSkillsPlayedThisTurn + " Attacks and Skills played this turn.");
        //Iterate through hand. For each card in hand, if cost is not already reduced and card is a Spell, reduce card's cost for combat by 1.
        for (final AbstractCard handCard : AbstractDungeon.player.hand.group) {
            //determine if the card is a spell
            //logger.info("duringTurn; checking if " + handCard.name + " is a spell...");
            if (handCard.rawDescription.startsWith("Spell.") || (AbstractDungeon.player.hasPower(GeminiFormPower.POWER_ID) && attacksAndSkillsPlayedThisTurn < AbstractDungeon.player.getPower(GeminiFormPower.POWER_ID).amount)) {
                if (handCard.type == AbstractCard.CardType.ATTACK || handCard.type == AbstractCard.CardType.SKILL) {
                    isSpell = true;
                    //logger.info("duringTurn; " + handCard.name + " found to be a spell");
                }
            }
            //reduce cost if card is a spell and if cost is unmodified
            if (!this.affectedCards.contains(handCard) && isSpell && handCard.costForTurn != 0) {
                handCard.modifyCostForTurn(-1);
                this.affectedCards.add(handCard);
                //logger.info("onInitialApplication; " + handCard.name + " found to have cost != 0, cost modified and added to affectedCards");
                //set to restore cost if cost was reduced and card is no longer considered a spell
            } else if (this.affectedCards.contains(handCard) && !isSpell) {
                this.toRestoreCost.add(handCard);
                //logger.info("duringTurn; " + handCard.name + " found to not be a spell and exists on affectedCards");
            }
            isSpell = false;
        }
        //restore costs of appropriate cards
        if (this.toRestoreCost.size() > 0) {
            for (AbstractCard cardToRestore : this.toRestoreCost) {
                if (cardToRestore.costForTurn == 0) {
                    cardToRestore.costForTurn++;
                    //logger.info("duringTurn; " + cardToRestore.name + "has cost 0; manually restoring cost");
                }
                else {
                    cardToRestore.modifyCostForTurn(1);
                }
                this.affectedCards.remove(cardToRestore);
                //logger.info("duringTurn; " + cardToRestore.name + " cost restored and removed from affectedCards");
            }
            this.toRestoreCost.clear();
            //logger.info("duringTurn: toRestoreCost cleared.");
        }
    }

    @Override
    public void onDrawOrDiscard() {
        //logger.info("onDrawOrDiscard; log on call");
        boolean isSpell = false;
        int attacksAndSkillsPlayedThisTurn = 0;
        for (AbstractCard playedCard : AbstractDungeon.actionManager.cardsPlayedThisTurn) {
            if (playedCard.type == AbstractCard.CardType.ATTACK || playedCard.type == AbstractCard.CardType.SKILL) {
                attacksAndSkillsPlayedThisTurn++;
            }
        }
        //logger.info("onDrawOrDiscard; " + attacksAndSkillsPlayedThisTurn + " Attacks and Skills played this turn.");
        //Iterate through hand. For each card in hand, if cost is not already reduced and card is a Spell, reduce card's cost for combat by 1.
        for (final AbstractCard handCard : AbstractDungeon.player.hand.group) {
            //determine if the card is a spell
            //logger.info("onDrawOrDiscard; checking if " + handCard.name + " is a spell...");
            if (handCard.rawDescription.startsWith("Spell.") || (AbstractDungeon.player.hasPower(GeminiFormPower.POWER_ID) && attacksAndSkillsPlayedThisTurn < AbstractDungeon.player.getPower(GeminiFormPower.POWER_ID).amount)) {
                if (handCard.type == AbstractCard.CardType.ATTACK || handCard.type == AbstractCard.CardType.SKILL) {
                    isSpell = true;
                    //logger.info("onDrawOrDiscard; " + handCard.name + " found to be a spell");
                }
            }
            //reduce cost if card is a spell and if cost is unmodified
            if (!this.affectedCards.contains(handCard) && isSpell && handCard.costForTurn != 0) {
                handCard.modifyCostForTurn(-1);
                this.affectedCards.add(handCard);
                //logger.info("onDrawOrDiscard; " + handCard.name + " cost modified and added to affectedCards");
                //set to restore cost if cost was reduced and card is no longer considered a spell
            } else if (this.affectedCards.contains(handCard) && !isSpell) {
                this.toRestoreCost.add(handCard);
                //logger.info("onDrawOrDiscard; " + handCard.name + " found to not be a spell and exists on affectedCards");
            }
            isSpell = false;
        }
        //restore costs of appropriate cards
        if (this.toRestoreCost.size() > 0) {
            for (AbstractCard cardToRestore : this.toRestoreCost) {
                if (cardToRestore.costForTurn == 0) {
                    cardToRestore.costForTurn++;
                    //logger.info("onDrawOrDiscard; " + cardToRestore.name + "has cost 0; manually restoring cost");
                }
                else {
                    cardToRestore.modifyCostForTurn(1);
                }
                this.affectedCards.remove(cardToRestore);
                //logger.info("onDrawOrDiscard; " + cardToRestore.name + " cost restored and removed from affectedCards");
            }
            this.toRestoreCost.clear();
            //logger.info("onDrawOrDiscard; toRestoreCost cleared.");
        }
    }

    @Override
    public void atEndOfTurn(final boolean isPlayer) {
        //logger.info("atEndOfTurn; log on call");
        this.affectedCards.clear();
        //logger.info("atEndOfTurn: affectedCards cleared.");
    }

    @Override
    public void onRemove() {
        //logger.info("onRemove; log on call");
        for (AbstractCard card : this.affectedCards) {
            if (card.costForTurn == 0) {
                card.costForTurn++;
                //logger.info("onRemove; " + card.name + "has cost 0; manually restoring cost");
            } else {
                card.modifyCostForTurn(1);
            }
            //logger.info("onRemove; " + card.name + " cost restored");
        }
        this.affectedCards.clear();
        //logger.info("onRemove; affectedCards cleared");
        this.toRestoreCost.clear();
        //logger.info("onRemove; toRestoreCost cleared");
    }
}
