package mysticmod.powers;

import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class MysticalShieldPower extends AbstractPower {
    public static final String POWER_ID = "mysticmod:MysticalShieldPower";
    public static final PowerStrings cardStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = cardStrings.NAME;
    public static final String[] DESCRIPTIONS = cardStrings.DESCRIPTIONS;
    private static int startOfTurnBlock;

    public MysticalShieldPower(AbstractCreature owner) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;

        this.img = new Texture("mysticmod/images/powers/mystical shield power.png");
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
        if (!AbstractDungeon.player.hasPower("Barricade") && !AbstractDungeon.player.hasPower("Blur")) {
            if (startOfTurnBlock >= 10) {
                flash();
                AbstractDungeon.actionManager.addToBottom(new GainBlockAction(AbstractDungeon.player, AbstractDungeon.player, 10));
            } else if (startOfTurnBlock <= 0) {
                return;
            } else {
                flash();
                AbstractDungeon.actionManager.addToBottom(new GainBlockAction(AbstractDungeon.player, AbstractDungeon.player, startOfTurnBlock));
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
        startOfTurnBlock = AbstractDungeon.player.currentBlock;
        return damageAmount;
    }
}