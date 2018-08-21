package mysticmod.powers;

import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;


public class GeminiFormPower extends AbstractPower {
    public static final String POWER_ID = "mysticmod:GeminiFormPower";
    public static final PowerStrings cardStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = cardStrings.NAME;
    public static final String[] DESCRIPTIONS = cardStrings.DESCRIPTIONS;
    public static int HPMultiplier = 0;
    private boolean isUpgraded;

    public GeminiFormPower(AbstractCreature owner, int amount, boolean isUpgraded) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;

        this.img = new Texture("mysticmod/images/powers/gemini form power.png");
        this.type = PowerType.BUFF;
        this.amount = amount;
        this.updateDescription();
        this.priority = 0;
        this.isUpgraded = isUpgraded;

    }

    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[0];
        if (this.amount == 1) {
            description += DESCRIPTIONS[1];
        } else {
            description += this.amount + DESCRIPTIONS[2];
        }
        if (HPMultiplier > 0) {
            description += DESCRIPTIONS[3];
            if (HPMultiplier > 2) {
                description += HPMultiplier + DESCRIPTIONS[5];
            } else {
                description += DESCRIPTIONS[4];
            }
        } else {
            description += ".";
        }
    }

    @Override
    public void onVictory() {
        int goldAndHPGain = 0;
        if (AbstractDungeon.player.hasPower(SpellsPlayed.POWER_ID)) {
            goldAndHPGain += AbstractDungeon.player.getPower(SpellsPlayed.POWER_ID).amount;
        }
        if (AbstractDungeon.player.hasPower(TechniquesPlayed.POWER_ID)) {
            goldAndHPGain += AbstractDungeon.player.getPower(TechniquesPlayed.POWER_ID).amount;
        }
        if (HPMultiplier > 0) {
            AbstractDungeon.actionManager.addToBottom(new HealAction(AbstractDungeon.player, AbstractDungeon.player, HPMultiplier * goldAndHPGain));
        }
        AbstractDungeon.player.gainGold(this.amount * goldAndHPGain);
    }

    @Override
    public void onInitialApplication() {
        if (this.isUpgraded) {
            HPMultiplier++;
        }
    }
}
