package MysticMod.Relics;

import java.util.Random;
import MysticMod.Cards.Cantrips.AcidSplash;
import MysticMod.Cards.Cantrips.Prestidigitation;
import MysticMod.Cards.Cantrips.RayOfFrost;
import MysticMod.Cards.Cantrips.Spark;
import MysticMod.Cards.Cantrips.ReadMagic;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;

public class MysticSpellBook extends CustomRelic {
    public static final String ID = "MysticMod:SpellBook";
    public static final Texture IMG = new Texture("MysticMod/images/relics/SpellBook.png");

    public MysticSpellBook() {
        super(ID, IMG, RelicTier.STARTER, LandingSound.MAGICAL);
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
    public AbstractRelic makeCopy() { // always override this method to return a new instance of your relic
        return new MysticSpellBook();
    }

}