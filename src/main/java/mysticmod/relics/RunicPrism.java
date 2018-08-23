package mysticmod.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import mysticmod.MysticMod;
import mysticmod.actions.RunicPrismAction;

public class RunicPrism extends CustomRelic {
    public static final String ID = "mysticmod:RunicPrism";
    public static final Texture IMG = new Texture("mysticmod/images/relics/runicprism.png");
    public static final Texture OUTLINE = new Texture("mysticmod/images/relics/runicprism_p.png");
    private static final int DAMAGE_AMT = 3;

    public RunicPrism() {
        super(ID, IMG, OUTLINE, RelicTier.UNCOMMON, LandingSound.MAGICAL);
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public void onPlayCard(final AbstractCard c, final AbstractMonster m) {
        if (MysticMod.isThisASpell(c, true)) {
            AbstractDungeon.actionManager.addToBottom(
                    new RunicPrismAction(AbstractDungeon.getMonsters().getRandomMonster(true), new DamageInfo(AbstractDungeon.player, DAMAGE_AMT, DamageInfo.DamageType.THORNS)));
            flash();
        }
    }

    @Override
    public AbstractRelic makeCopy() { // always override this method to return a new instance of your relic
        return new RunicPrism();
    }
}