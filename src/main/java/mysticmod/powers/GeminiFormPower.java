package mysticmod.powers;

import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.core.CardCrawlGame;

public class GeminiFormPower extends AbstractPower {
    public static final String POWER_ID = "mysticmod:GeminiFormPower";
    public static final PowerStrings cardStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = cardStrings.NAME;
    public static final String[] DESCRIPTIONS = cardStrings.DESCRIPTIONS;
    public static int attacksAndSkillsPlayedThisTurn;

    public GeminiFormPower(AbstractCreature owner, int amount) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;

        this.img = new Texture("mysticmod/images/powers/gemini form power.png");
        this.type = PowerType.BUFF;
        this.amount = amount;
        this.updateDescription();
        this.priority = 0;

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
