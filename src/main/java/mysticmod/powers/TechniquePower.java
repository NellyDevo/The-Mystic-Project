package mysticmod.powers;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class TechniquePower extends AbstractPower {
    public static final String POWER_ID = "mysticmod:MightyMagicPower";
    public static final PowerStrings cardStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = cardStrings.NAME;
    public static final String[] DESCRIPTIONS = cardStrings.DESCRIPTIONS;

    public TechniquePower(AbstractCreature owner, int amount) {
        name = NAME;
        ID = POWER_ID;
        this.owner = owner;
        region128 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage("mysticmod/images/powers/technique power 84.png"), 0, 0, 84, 84);
        region48 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage("mysticmod/images/powers/technique power 32.png"), 0, 0, 32, 32);
        type = PowerType.BUFF;
        this.amount = amount;
        updateDescription();
    }

    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1];
    }

    @Override
    public float atDamageGive(float damage, DamageInfo.DamageType type) {
        //refer to ApplyPowers patch for full effects
        return damage;
    }
}
