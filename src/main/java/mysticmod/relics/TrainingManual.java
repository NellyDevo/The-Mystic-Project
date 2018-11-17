package mysticmod.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import mysticmod.MysticMod;

public class TrainingManual extends CustomRelic {
    public static final String ID = "mysticmod:TrainingManual";
    public static final Texture IMG = new Texture("mysticmod/images/relics/trainingmanual.png");
    public static final Texture OUTLINE = new Texture("mysticmod/images/relics/trainingmanual_p.png");

    public TrainingManual() {
        super(ID, IMG, OUTLINE, RelicTier.COMMON, LandingSound.FLAT);
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public void atBattleStartPreDraw() {
        AbstractCard c = MysticMod.returnTrulyRandomArte();
        UnlockTracker.markCardAsSeen(c.cardID);
        AbstractDungeon.actionManager.addToBottom(new MakeTempCardInHandAction(c, 1, false));
        flash();
    }

    @Override
    public AbstractRelic makeCopy() { // always override this method to return a new instance of your relic
        return new TrainingManual();
    }
}