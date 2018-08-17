package mysticmod.relics;

import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.cards.AbstractCard;
import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;

public class BentSpoon extends CustomRelic {
    public static final String ID = "mysticmod:BentSpoon";
    private static final Texture IMG = new Texture("mysticmod/images/relics/bentspoon.png");

    public BentSpoon() {
        super(ID, IMG, RelicTier.SHOP, LandingSound.MAGICAL);
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public void onPlayCard(final AbstractCard c, final AbstractMonster m) {
        if (c.rawDescription.startsWith("Cantrip.")) {
            this.flash();
            //Effects unfortunately hardcoded into cantrips' ApplyPowers overrides
        }
    }

    @Override
    public AbstractRelic makeCopy() { // always override this method to return a new instance of your relic
        return new BentSpoon();
    }
}