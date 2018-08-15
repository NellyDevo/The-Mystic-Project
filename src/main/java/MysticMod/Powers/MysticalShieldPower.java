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
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.core.CardCrawlGame;

//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;

public class MysticalShieldPower extends AbstractPower {
    public static final String POWER_ID = "MysticMod:MysticalShieldPower";
    public static final PowerStrings cardStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = cardStrings.NAME;
    public static final String[] DESCRIPTIONS = cardStrings.DESCRIPTIONS;
    private static int startOfTurnBlock;

    //public static final Logger logger = LogManager.getLogger(MysticalShieldPower.class);

    public MysticalShieldPower(AbstractCreature owner) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;

        this.img = new Texture("MysticMod/images/powers/mystical shield power.png");
        this.type = PowerType.BUFF;
        this.amount = -1;
        this.updateDescription();
        this.priority = 0;

    }

    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[0];
    }

    @Override
    public void atStartOfTurn() {
        //logger.info("start of turn block: " + startOfTurnBlock + ". hasbarricade equals " + hasBarricade + ". hasBlur equals " + hasBlur + ".");
        if (!AbstractDungeon.player.hasPower("Barricade") && !AbstractDungeon.player.hasPower("Blur")) {
            if (startOfTurnBlock >= 10) {
                flash();
                AbstractDungeon.actionManager.addToBottom(new com.megacrit.cardcrawl.actions.common.GainBlockAction(AbstractDungeon.player, AbstractDungeon.player, 10));
            } else if (startOfTurnBlock <= 0) {
                return;
            } else {
                flash();
                AbstractDungeon.actionManager.addToBottom(new com.megacrit.cardcrawl.actions.common.GainBlockAction(AbstractDungeon.player, AbstractDungeon.player, startOfTurnBlock));
            }
        }
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        if (isPlayer) {
            startOfTurnBlock = AbstractDungeon.player.currentBlock;
        }
    }

    @Override
    public int onAttacked(final DamageInfo info, final int damageAmount) {
        //logger.info("onAttacked publish.");
        startOfTurnBlock = AbstractDungeon.player.currentBlock;
        //logger.info("took " + damageAmount + " damage. Block Count = " + startOfTurnBlock);
        return damageAmount;
    }
}