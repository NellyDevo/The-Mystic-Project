package mysticmod.powers;

import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDiscardAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndAddToDiscardEffect;
import mysticmod.MysticMod;
import mysticmod.actions.ReplaceCardAction;

//import java.util.ArrayList;

public class SpontaneousCasterPower extends AbstractPower {
    public static final String POWER_ID = "mysticmod:SpontaneousCasterPower";
    public static final PowerStrings cardStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = cardStrings.NAME;
    public static final String[] DESCRIPTIONS = cardStrings.DESCRIPTIONS;
    //private static ArrayList<AbstractCard> affectedCards = new ArrayList<AbstractCard>();
    //private static ArrayList<AbstractCard> toRestoreCost = new ArrayList<AbstractCard>();

    public SpontaneousCasterPower(AbstractCreature owner) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;

        this.img = new Texture("mysticmod/images/powers/spontaneous caster power.png");
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
        //if card is a spell, set exhaust = true, generate a random spell, add a copy of random spell to discard pile
        if (MysticMod.isThisASpell(card, true)) {
            this.flash();
            action.exhaustCard = true;
            AbstractCard newCard = MysticMod.returnTrulyRandomSpell();
            UnlockTracker.markCardAsSeen(newCard.cardID);
            AbstractDungeon.actionManager.addToBottom(new ReplaceCardAction(card, newCard));
        }
    }

    /* ===Card seems too broken. Removing cost reduction functionality for now.===
    @Override
    public void onInitialApplication() {
        //Iterate through hand. For each card in hand, if cost is not already reduced and card is a Spell, reduce card's cost for combat by 1.
        for (final AbstractCard handCard : AbstractDungeon.player.hand.group) {
            //reduce cost if card is a spell, if the cost is not 0, and if cost is unmodified
            if (!affectedCards.contains(handCard) && MysticMod.isThisASpell(handCard, false) && handCard.costForTurn != 0) {
                handCard.modifyCostForTurn(-1);
                affectedCards.add(handCard);
            }
        }
    }

    @Override
    public void atStartOfTurnPostDraw() {
        //Iterate through hand. For each card in hand, if cost is not already reduced and card is a Spell, reduce card's cost for combat by 1.
        for (final AbstractCard handCard : AbstractDungeon.player.hand.group) {
            //reduce cost if card is a spell and if cost is unmodified
            if (!affectedCards.contains(handCard) && MysticMod.isThisASpell(handCard, false) && handCard.costForTurn != 0) {
                handCard.modifyCostForTurn(-1);
                affectedCards.add(handCard);
            }
        }
    }

    @Override
    public void duringTurn() { //necessary?
        //Iterate through hand. For each card in hand, if cost is not already reduced and card is a Spell, reduce card's cost for combat by 1.
        for (final AbstractCard handCard : AbstractDungeon.player.hand.group) {
            //reduce cost if card is a spell and if cost is unmodified
            if (!affectedCards.contains(handCard) && MysticMod.isThisASpell(handCard, false) && handCard.costForTurn != 0) {
                handCard.modifyCostForTurn(-1);
                affectedCards.add(handCard);
                //set to restore cost if cost was reduced and card is no longer considered a spell
            } else if (affectedCards.contains(handCard) && !MysticMod.isThisASpell(handCard, false)) {
                toRestoreCost.add(handCard);
            }
        }
        //restore costs of appropriate cards
        if (toRestoreCost.size() > 0) {
            for (AbstractCard cardToRestore : toRestoreCost) {
                if (cardToRestore.costForTurn == 0) {
                    cardToRestore.costForTurn++;
                }
                else {
                    cardToRestore.modifyCostForTurn(1);
                }
                affectedCards.remove(cardToRestore);
            }
            toRestoreCost.clear();
        }
    }

    @Override
    public void onDrawOrDiscard() {
        //Iterate through hand. For each card in hand, if cost is not already reduced and card is a Spell, reduce card's cost for combat by 1.
        for (final AbstractCard handCard : AbstractDungeon.player.hand.group) {
            //reduce cost if card is a spell and if cost is unmodified
            if (!affectedCards.contains(handCard) && MysticMod.isThisASpell(handCard, false) && handCard.costForTurn != 0) {
                handCard.modifyCostForTurn(-1);
                affectedCards.add(handCard);
                //set to restore cost if cost was reduced and card is no longer considered a spell
            } else if (affectedCards.contains(handCard) && !MysticMod.isThisASpell(handCard, false)) {
                toRestoreCost.add(handCard);
            }
        }
        //restore costs of appropriate cards
        if (toRestoreCost.size() > 0) {
            for (AbstractCard cardToRestore : toRestoreCost) {
                if (cardToRestore.costForTurn == 0) {
                    cardToRestore.costForTurn++;
                }
                else {
                    cardToRestore.modifyCostForTurn(1);
                }
                affectedCards.remove(cardToRestore);
            }
            toRestoreCost.clear();
        }
    }

    @Override
    public void atEndOfTurn(final boolean isPlayer) {
        affectedCards.clear();
    }

    @Override
    public void onRemove() {
        for (AbstractCard card : affectedCards) {
            if (card.costForTurn == 0) {
                card.costForTurn++;
            } else {
                card.modifyCostForTurn(1);
            }
        }
        affectedCards.clear();
        toRestoreCost.clear();
    }
    */
}