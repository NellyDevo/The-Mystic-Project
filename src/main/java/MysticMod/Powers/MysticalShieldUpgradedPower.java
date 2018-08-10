package MysticMod.Powers;

import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.powers.BlurPower;
import com.megacrit.cardcrawl.powers.BarricadePower;

public class MysticalShieldUpgradedPower extends AbstractPower {
    public static final String POWER_ID = "MysticMod:MysticalShieldUpgradedPower";
    public static final String NAME = "Mystical Shield";
    public static final String DESCRIPTIONS = "Block is set to 10 instead of 0 at the start of the turn.";
    private static int startOfTurnBlock;
    private boolean hasBarricade;
    private boolean hasBlur;

    public MysticalShieldUpgradedPower(AbstractCreature owner) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;

        this.img = new Texture("MysticMod/images/powers/mystical shield power.png");
        this.type = PowerType.BUFF;
        this.amount = -1;
        this.updateDescription();

    }

    @Override
    public void updateDescription() {
        description = DESCRIPTIONS;
    }

    @Override
    public void atStartOfTurn() {
        //logger.info("atEnergyGain publish. Stored Block amount:" + this.startOfTurnBlock + ".");
        if (AbstractDungeon.player.hasPower("Barricade")) {
            hasBarricade = true;
        }
        if (AbstractDungeon.player.hasPower("Blur")) {
            hasBlur = true;
        }
        //logger.info("start of turn block: " + startOfTurnBlock + ". hasbarricade equals " + hasBarricade + ". hasBlur equals " + hasBlur + ".");
        if (!hasBarricade && !hasBlur) {
                flash();
                AbstractDungeon.actionManager.addToBottom(new com.megacrit.cardcrawl.actions.common.GainBlockAction(AbstractDungeon.player, AbstractDungeon.player, 10));
            } else {
                if (AbstractDungeon.player.currentBlock < 10) {
                    flash();
                    AbstractDungeon.actionManager.addToBottom(new com.megacrit.cardcrawl.actions.common.GainBlockAction(AbstractDungeon.player, AbstractDungeon.player, 10 - AbstractDungeon.player.currentBlock));
                }
            }
        hasBarricade = false;
        hasBlur = false;
    }

    @Override
    public int onAttacked(final DamageInfo info, final int damageAmount) {
        //logger.info("onAttacked publish.");
        startOfTurnBlock = AbstractDungeon.player.currentBlock;
        //logger.info("took " + damageAmount + " damage. Block Count = " + startOfTurnBlock);
        return damageAmount;
    }
}
