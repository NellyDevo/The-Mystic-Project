package mysticmod.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.RelicLibrary;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import mysticmod.cards.cantrips.*;

public class SpellBook extends CustomRelic {
    public static final String ID = "mysticmod:SpellBook";
    public static final Texture IMG = new Texture("mysticmod/images/relics/spellbook.png");
    public static final Texture OUTLINE = new Texture("mysticmod/images/relics/book_p.png");

    public SpellBook() {
        super(ID, IMG, OUTLINE, RelicTier.STARTER, LandingSound.MAGICAL);
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public void atBattleStartPreDraw() {
        int randomlyGeneratedNumber = AbstractDungeon.cardRandomRng.random(4);
        AbstractDungeon.actionManager.addToBottom(new RelicAboveCreatureAction(AbstractDungeon.player, this));
        switch (randomlyGeneratedNumber) {
            case 0: AbstractDungeon.actionManager.addToBottom(new MakeTempCardInHandAction(new AcidSplash(), 1, false));
                break;
            case 1: AbstractDungeon.actionManager.addToBottom(new MakeTempCardInHandAction(new Prestidigitation(), 1, false));
                break;
            case 2: AbstractDungeon.actionManager.addToBottom(new MakeTempCardInHandAction(new RayOfFrost(), 1, false));
                break;
            case 3: AbstractDungeon.actionManager.addToBottom(new MakeTempCardInHandAction(new Spark(), 1, false));
                break;
            case 4: AbstractDungeon.actionManager.addToBottom(new MakeTempCardInHandAction(new ReadMagic(), 1, false));
                break;
        }
        flash();
    }

    @Override
    public void onUnequip() {
        RelicLibrary.bossList.remove(BlessedBook.ID);
    }

    @Override
    public AbstractRelic makeCopy() { // always override this method to return a new instance of your relic
        return new SpellBook();
    }
}