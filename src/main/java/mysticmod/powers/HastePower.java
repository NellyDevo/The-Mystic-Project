package mysticmod.powers;

import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.core.CardCrawlGame;

public class HastePower extends AbstractPower {
    public static final String POWER_ID = "mysticmod:HastePower";
    public static final PowerStrings cardStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = cardStrings.NAME;
    public static final String[] DESCRIPTIONS = cardStrings.DESCRIPTIONS;

    public HastePower(AbstractCreature owner) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;

        this.img = new Texture("mysticmod/images/powers/haste power.png");
        this.type = PowerType.BUFF;
        this.amount = -1;
        this.updateDescription();

    }

    @Override
    public void updateDescription() {
            description = DESCRIPTIONS[0];
    }

    @Override
    public void atStartOfTurn() {
        AbstractDungeon.actionManager.addToBottom(new com.megacrit.cardcrawl.actions.common.DrawCardAction(AbstractDungeon.player, 1));
        AbstractDungeon.actionManager.addToBottom(new com.megacrit.cardcrawl.actions.common.GainEnergyAction(1));
    }
}
