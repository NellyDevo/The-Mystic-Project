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
import java.util.*;

public class GeminiFormPower extends AbstractPower {
    public static final String POWER_ID = "MysticMod:GeminiFormPower";
    public static final String NAME = "Gemini Form";
    public static final String[] DESCRIPTIONS = new String[]{ "The first ", "Attack or Skill you play each turn counts as both a Spell and a Technique.", "Attacks or Skills you play each turn counts as both a Spell and a Technique." };
    private int attacksAndSkillsPlayedThisTurn;

    public GeminiFormPower(AbstractCreature owner, int amount) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;

        this.img = new Texture("MysticMod/images/powers/gemini form power.png");
        this.type = PowerType.BUFF;
        this.amount = amount;
        this.updateDescription();

    }

    @Override
    public void updateDescription() {
        if (amount == 1) {
            description = DESCRIPTIONS[0] + DESCRIPTIONS[1];
        } else {
            description = DESCRIPTIONS[0] + " "+amount+" " + DESCRIPTIONS[2];
        }

    }
    @Override
    public void onInitialApplication() {
        if (this.amount == 1) {
            for (AbstractCard playedCard : AbstractDungeon.actionManager.cardsPlayedThisTurn) {
                if (playedCard.type == AbstractCard.CardType.ATTACK || playedCard.type == AbstractCard.CardType.SKILL) {
                    attacksAndSkillsPlayedThisTurn++;
                }
            }
        }
    }

    @Override
    public void atStartOfTurn() {
        this.attacksAndSkillsPlayedThisTurn = 0;
    }

    @Override
    public void onUseCard(final AbstractCard card, final UseCardAction action) {
        if (card.type == AbstractCard.CardType.SKILL || card.type == AbstractCard.CardType.ATTACK) {
            this.attacksAndSkillsPlayedThisTurn++;
            if (this.attacksAndSkillsPlayedThisTurn > this.amount) {
                return;
            }
            if (card.rawDescription.startsWith("Spell")) {
                AbstractDungeon.actionManager.addToBottom(new com.megacrit.cardcrawl.actions.common.ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new TechniquesPlayed(AbstractDungeon.player, 1), 1));
            } else if (card.rawDescription.startsWith("Technique")) {
                AbstractDungeon.actionManager.addToBottom(new com.megacrit.cardcrawl.actions.common.ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new SpellsPlayed(AbstractDungeon.player, 1), 1));
            } else if (card.rawDescription.startsWith("Cantrip.")) {
                AbstractDungeon.actionManager.addToBottom(new com.megacrit.cardcrawl.actions.common.ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new TechniquesPlayed(AbstractDungeon.player, 1), 1));
                if (AbstractDungeon.player.hasPower(SpellsPlayed.POWER_ID) && (AbstractDungeon.player.getPower(SpellsPlayed.POWER_ID).amount > 1)) {
                    AbstractDungeon.actionManager.addToBottom(new com.megacrit.cardcrawl.actions.common.ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new SpellsPlayed(AbstractDungeon.player, 1), 1));
                }
            } else {
                AbstractDungeon.actionManager.addToBottom(new com.megacrit.cardcrawl.actions.common.ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new TechniquesPlayed(AbstractDungeon.player, 1), 1));
                AbstractDungeon.actionManager.addToBottom(new com.megacrit.cardcrawl.actions.common.ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new SpellsPlayed(AbstractDungeon.player, 1), 1));
            }
        }
    }
}
