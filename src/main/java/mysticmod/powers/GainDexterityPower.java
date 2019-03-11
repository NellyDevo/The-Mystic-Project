package mysticmod.powers;

import basemod.interfaces.CloneablePowerInterface;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.DexterityPower;

public class GainDexterityPower extends AbstractPower implements CloneablePowerInterface {
    public static final String POWER_ID = "mysticmod:GainDexterityPower";
    public static final PowerStrings cardStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = cardStrings.NAME;
    public static final String[] DESCRIPTIONS = cardStrings.DESCRIPTIONS;

    public GainDexterityPower(AbstractCreature owner, int amount) {
        name = NAME;
        ID = POWER_ID;
        this.owner = owner;
        loadRegion("shackle");
        type = PowerType.BUFF;
        this.amount = amount;
        updateDescription();
    }

    @Override
    public void updateDescription() {
            description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1];
    }

    @Override
    public void stackPower(final int stackAmount) {
        fontScale = 8.0f;
        amount += stackAmount;
        if (amount == 0) {
            AbstractDungeon.actionManager.addToTop(new RemoveSpecificPowerAction(owner, owner, POWER_ID));
        }
        if (amount >= 999) {
            amount = 999;
        }
        if (amount <= -999) {
            amount = -999;
        }
    }

    @Override
    public void reducePower(final int reduceAmount) {
        fontScale = 8.0f;
        amount -= reduceAmount;
        if (amount == 0) {
            AbstractDungeon.actionManager.addToTop(new RemoveSpecificPowerAction(owner, owner, POWER_ID));
        }
        if (amount >= 999) {
            amount = 999;
        }
        if (amount <= -999) {
            amount = -999;
        }
    }

    @Override
    public void atEndOfTurn(final boolean isPlayer) {
        flash();
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(owner, owner, new DexterityPower(owner, amount), amount));
        AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(owner, owner, POWER_ID));
    }

    @Override
    public AbstractPower makeCopy() {
        return new GainDexterityPower(owner, amount);
    }
}
