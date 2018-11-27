package mysticmod.relics;

import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import mysticmod.patches.MysticTags;

public class CrystalBall extends AbstractSpellArteLogicAffectingRelic {
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

    @Override
    public boolean modifyIsSpell(AbstractCard card, boolean isSpell) {
        boolean retVal = isSpell;
        if (!card.hasTag(MysticTags.IS_ARTE) && card.type == AbstractCard.CardType.SKILL) {
            retVal = true;
        }
        return retVal;
    }

    @Override
    public boolean modifyIsArte(AbstractCard card, boolean isArte) {
        boolean retVal = isArte;
        if (!card.hasTag(MysticTags.IS_SPELL) && card.type == AbstractCard.CardType.ATTACK) {
            retVal = true;
        }
        return retVal;
    }

    @Override
    public AbstractRelic makeCopy() {
        return new CrystalBall();
    }
}