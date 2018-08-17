package mysticmod.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import mysticmod.cards.cantrips.*;

public class BlessedBook extends CustomRelic {
    public static final String ID = "mysticmod:BlessedBook";
    private static final Texture IMG = new Texture("mysticmod/images/relics/blessedbook.png");
    private static final int TURN_COUNT = 2;

    public BlessedBook() {
        super(ID, IMG, RelicTier.BOSS, LandingSound.MAGICAL);
        this.counter = 0;
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0] + TURN_COUNT + DESCRIPTIONS[1];
    }

    @Override
    public void atTurnStart() {
        ++this.counter;
        if (this.counter == TURN_COUNT) {
            this.counter = 0;
            this.flash();
            AbstractDungeon.actionManager.addToBottom(new RelicAboveCreatureAction(AbstractDungeon.player, this));
            int randomlyGeneratedNumber = AbstractDungeon.cardRandomRng.random(4);
            AbstractDungeon.actionManager.addToBottom(new RelicAboveCreatureAction(AbstractDungeon.player, this));
            switch (randomlyGeneratedNumber) {
                case 0:
                    AbstractDungeon.actionManager.addToBottom(new MakeTempCardInHandAction(new AcidSplash(), 1, false));
                    break;
                case 1:
                    AbstractDungeon.actionManager.addToBottom(new MakeTempCardInHandAction(new Prestidigitation(), 1, false));
                    break;
                case 2:
                    AbstractDungeon.actionManager.addToBottom(new MakeTempCardInHandAction(new RayOfFrost(), 1, false));
                    break;
                case 3:
                    AbstractDungeon.actionManager.addToBottom(new MakeTempCardInHandAction(new Spark(), 1, false));
                    break;
                case 4:
                    AbstractDungeon.actionManager.addToBottom(new MakeTempCardInHandAction(new ReadMagic(), 1, false));
                    break;
            }
        }
    }

    @Override
    public void obtain() {
        if (AbstractDungeon.player.hasRelic(SpellBook.ID)) {
            for (int i=0; i<AbstractDungeon.player.relics.size(); ++i) {
                if (AbstractDungeon.player.relics.get(i).relicId.equals(SpellBook.ID)) {
                    instantObtain(AbstractDungeon.player, i, true);
                    break;
                }
            }
        } else {
            super.obtain();
        }
    }

    @Override
    public AbstractRelic makeCopy() { // always override this method to return a new instance of your relic
        return new BlessedBook();
    }
}