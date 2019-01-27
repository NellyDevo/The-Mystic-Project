package mysticmod.powers;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import mysticmod.MysticMod;

public class ArtesPlayedNextTurn extends AbstractPower {
    public static final String POWER_ID = "mysticmod:ArtesPlayedNextTurnPower";
    public static final PowerStrings cardStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = cardStrings.NAME;
    public static final String[] DESCRIPTIONS = cardStrings.DESCRIPTIONS;

    public ArtesPlayedNextTurn(AbstractCreature owner, int amount) {
        priority = -199;
        name = NAME;
        ID = POWER_ID;
        this.owner = owner;
        region128 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage("mysticmod/images/powers/artes played next turn 84.png"), 0, 0, 84, 84);
        region48 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage("mysticmod/images/powers/artes played next turn 32.png"), 0, 0, 32, 32);
        type = PowerType.BUFF;
        this.amount = amount;
        updateDescription();
    }

    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1];
    }

    @Override
    public void atStartOfTurn() {
        MysticMod.applyPoiseStacks(AbstractDungeon.player, amount);
        AbstractDungeon.actionManager.addToBottom(new ReducePowerAction(AbstractDungeon.player, AbstractDungeon.player, ArtesPlayedNextTurn.POWER_ID, AbstractDungeon.player.getPower(ArtesPlayedNextTurn.POWER_ID).amount)
        );
    }
}
