package mysticmod.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import mysticmod.patches.MysticTags;

public class BentSpoon extends CustomRelic {
    public static final String ID = "mysticmod:BentSpoon";
    private static final Texture IMG = new Texture("mysticmod/images/relics/bentspoon.png");
    private static final Texture OUTLINE = new Texture("mysticmod/images/relics/bentspoon_p.png");

    public BentSpoon() {
        super(ID, IMG, OUTLINE ,RelicTier.SHOP, LandingSound.CLINK);
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public void onPlayCard(final AbstractCard c, final AbstractMonster m) { //damage effects hardcoded into cantrips' ApplyPowers overrides
        if (c.hasTag(MysticTags.IS_CANTRIP)) {
            if (c.baseBlock > -1 || c.baseDamage > -1) {
                this.flash();
            }
        }
    }

    @Override
    public AbstractRelic makeCopy() {
        return new BentSpoon();
    }
}
