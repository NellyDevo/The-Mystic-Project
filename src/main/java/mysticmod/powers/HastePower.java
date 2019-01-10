package mysticmod.powers;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class HastePower extends AbstractPower {
    public static final String POWER_ID = "mysticmod:HastePower";
    public static final PowerStrings cardStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = cardStrings.NAME;
    public static final String[] DESCRIPTIONS = cardStrings.DESCRIPTIONS;

    public HastePower(AbstractCreature owner) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.region128 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage("mysticmod/images/powers/haste power 84.png"), 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage("mysticmod/images/powers/haste power 32.png"), 0, 0, 32, 32);
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
        AbstractDungeon.player.gameHandSize++;
        AbstractDungeon.actionManager.addToBottom(new GainEnergyAction(1));
    }

    @Override
    public void atStartOfTurnPostDraw() {
        AbstractDungeon.player.gameHandSize--;
    }
}
