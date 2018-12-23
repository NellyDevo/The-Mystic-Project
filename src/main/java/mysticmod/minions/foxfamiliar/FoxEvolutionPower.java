package mysticmod.minions.foxfamiliar;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class FoxEvolutionPower extends AbstractPower {
    public static final String POWER_ID = "mysticmod:FoxEvolutionPower";
    public static final PowerStrings cardStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = cardStrings.NAME;
    public static final String[] DESCRIPTIONS = cardStrings.DESCRIPTIONS;

    public FoxEvolutionPower(AbstractCreature owner, int amount) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.region128 = new TextureAtlas.AtlasRegion(new Texture("mysticmod/images/powers/fox evolution power 84.png"), 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(new Texture("mysticmod/images/powers/fox evolution power 32.png"), 0, 0, 32, 32);
        this.type = PowerType.BUFF;
        this.amount = amount;
        this.updateDescription();
        if (this.amount > 3) {
            this.amount = 3;
        }
    }

    @Override
    public void stackPower(final int stackAmount) {
        this.amount+=stackAmount;
        if (this.amount > 3) {
            this.amount = 3;
        }
    }

    @Override
    public void updateDescription() {
        switch (this.amount) {
            case 1 : description = DESCRIPTIONS[0] + DESCRIPTIONS[4];
            break;
            case 2 : description = DESCRIPTIONS[0] + DESCRIPTIONS[3] + DESCRIPTIONS[1] + DESCRIPTIONS[4];
            break;
            case 3 : description = DESCRIPTIONS[0] + DESCRIPTIONS[5] + DESCRIPTIONS[1] + DESCRIPTIONS[3] + DESCRIPTIONS[2] + DESCRIPTIONS[4];
            break;
            default : description = "WARNING: amount out of bounds.";
            break;
        }
    }
}
