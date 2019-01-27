package mysticmod.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import mysticmod.MysticMod;

public class BlessedBook extends CustomRelic {
    public static final String ID = "mysticmod:BlessedBook";
    private static final Texture IMG = new Texture("mysticmod/images/relics/blessedbook.png");
    private static final Texture OUTLINE = new Texture("mysticmod/images/relics/blessedbook_p.png");
    private static final int TURN_COUNT = 2;

    public BlessedBook() {
        super(ID, IMG, OUTLINE, RelicTier.BOSS, LandingSound.MAGICAL);
        counter = 0;
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0] + TURN_COUNT + DESCRIPTIONS[1];
    }

    @Override
    public void atTurnStart() {
        ++counter;
        if (counter == TURN_COUNT) {
            counter = 0;
            flash();
            AbstractDungeon.actionManager.addToBottom(new RelicAboveCreatureAction(AbstractDungeon.player, this));
            AbstractCard randomCantrip = MysticMod.cantripsGroup.get(AbstractDungeon.cardRandomRng.random(MysticMod.cantripsGroup.size()-1)).makeCopy();
            AbstractDungeon.actionManager.addToBottom(new MakeTempCardInHandAction(randomCantrip, 1, false));
        }
    }

    @Override
    public void obtain() {
        if (AbstractDungeon.player.hasRelic(SpellBook.ID)) {
            for (int i = 0; i < AbstractDungeon.player.relics.size(); ++i) {
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
    public AbstractRelic makeCopy() {
        return new BlessedBook();
    }
}
