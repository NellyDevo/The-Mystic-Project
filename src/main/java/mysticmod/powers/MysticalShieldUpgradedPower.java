package mysticmod.powers;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class MysticalShieldUpgradedPower extends AbstractPower {
    public static final String POWER_ID = "mysticmod:MysticalShieldUpgradedPower";
    public static final PowerStrings cardStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = cardStrings.NAME;
    public static final String[] DESCRIPTIONS = cardStrings.DESCRIPTIONS;
    private boolean hasFlashed;

    public MysticalShieldUpgradedPower(AbstractCreature owner) {
        name = NAME;
        ID = POWER_ID;
        this.owner = owner;
        region128 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage("mysticmod/images/powers/mystical shield power 84.png"), 0, 0, 84, 84);
        region48 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage("mysticmod/images/powers/mystical shield power 32.png"), 0, 0, 32, 32);
        type = PowerType.BUFF;
        amount = -1;
        updateDescription();
        hasFlashed = false;
    }

    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[0];
    }

    public int onMoreSpecificTrigger(int amount) {
        if (AbstractDungeon.player.currentBlock - amount < 8) {
            amount = AbstractDungeon.player.currentBlock - 8;
            if (!hasFlashed) {
                flash();
                hasFlashed = true;
            }
        }
        return amount;
    }

    @Override
    public void atStartOfTurn() {
        if (AbstractDungeon.player.currentBlock < 8) {
            if (!hasFlashed) {
                flash();
                hasFlashed = true;
            }
            AbstractDungeon.actionManager.addToBottom(new GainBlockAction(AbstractDungeon.player, AbstractDungeon.player, 8 - AbstractDungeon.player.currentBlock));
        }
        hasFlashed = false;
    }
}
