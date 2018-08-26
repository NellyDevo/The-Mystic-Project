package mysticmod.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import mysticmod.MysticMod;

public class RabbitsFoot extends CustomRelic {
    public static final String ID = "mysticmod:RabbitsFoot";
    public static final Texture IMG = new Texture("mysticmod/images/relics/rabbitsfoot.png");
    public static final Texture OUTLINE = new Texture("mysticmod/images/relics/rabbitsfoot_p.png");
    private static final int BLOCK_AMT = 1;

    public RabbitsFoot() {
        super(ID, IMG, OUTLINE, RelicTier.UNCOMMON, LandingSound.SOLID);
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public void onPlayCard(final AbstractCard c, final AbstractMonster m) {
        if (MysticMod.isThisATechnique(c, true)) {
            AbstractDungeon.actionManager.addToBottom(new GainBlockAction(AbstractDungeon.player, AbstractDungeon.player, BLOCK_AMT));
            flash();
        }
    }

    @Override
    public AbstractRelic makeCopy() { // always override this method to return a new instance of your relic
        return new RabbitsFoot();
    }
}