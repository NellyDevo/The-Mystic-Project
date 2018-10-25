package mysticmod.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class CrystalBall extends CustomRelic {
    public static final String ID = "mysticmod:CrystalBall";
    public static final Texture IMG = new Texture("mysticmod/images/relics/crystalball.png");
    public static final Texture OUTLINE = new Texture("mysticmod/images/relics/crystalball_p.png");

    public CrystalBall() {
        super(ID, IMG, OUTLINE, RelicTier.BOSS, LandingSound.MAGICAL);
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    //Most interaction effects unfortunately hardcoded... everywhere.

    @Override
    public AbstractRelic makeCopy() {
        return new CrystalBall();
    }
}