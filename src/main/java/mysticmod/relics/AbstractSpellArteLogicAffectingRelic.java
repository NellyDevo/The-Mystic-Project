package mysticmod.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.cards.AbstractCard;

public abstract class AbstractSpellArteLogicAffectingRelic extends CustomRelic {

    public AbstractSpellArteLogicAffectingRelic(String id, Texture texture, RelicTier tier, LandingSound sfx) {
        super(id, texture, tier, sfx);
    }

    public AbstractSpellArteLogicAffectingRelic(String id, Texture texture, Texture outline, RelicTier tier, LandingSound sfx) {
        super(id, texture, outline, tier, sfx);
    }

    public AbstractSpellArteLogicAffectingRelic(String id, String imgName, RelicTier tier, LandingSound sfx) {
        super(id, imgName, tier, sfx);
    }

    public boolean modifyIsSpell(AbstractCard card, boolean isSpell) {
        return isSpell;
    }

    public boolean modifyIsArte(AbstractCard card, boolean isArte) {
        return isArte;
    }
}