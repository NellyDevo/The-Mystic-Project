package mysticmod.relics;

import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.cards.AbstractCard;
import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import mysticmod.MysticMod;

public class RabbitsFoot extends CustomRelic {
    public static final String ID = "mysticmod:RabbitsFoot";
    public static final Texture IMG = new Texture("mysticmod/images/relics/rabbitsfoot.png");
    private static final int BLOCK_AMT = 1;

    public RabbitsFoot() {
        super(ID, IMG, RelicTier.UNCOMMON, LandingSound.MAGICAL);
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public void onPlayCard(final AbstractCard c, final AbstractMonster m) {
        if (MysticMod.isThisATechnique(c, true)) {
            AbstractDungeon.actionManager.addToBottom(new com.megacrit.cardcrawl.actions.common.GainBlockAction(AbstractDungeon.player, AbstractDungeon.player, BLOCK_AMT));
            flash();
        }
    }

    @Override
    public AbstractRelic makeCopy() { // always override this method to return a new instance of your relic
        return new RabbitsFoot();
    }
}