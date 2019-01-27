package mysticmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import mysticmod.character.MysticAnimation;
import mysticmod.character.MysticCharacter;
import mysticmod.powers.ArtesPlayed;
import mysticmod.vfx.PoisedActivatedEffect;

public class ApplyPoisedAction extends AbstractGameAction {
    private AbstractPlayer p;
    private int amount;

    public ApplyPoisedAction(AbstractPlayer p, int amount) {
        this.p = p;
        this.amount = amount;
    }

    @Override
    public void update() {
        if (!p.hasPower(ArtesPlayed.POWER_ID) && p instanceof MysticCharacter) {
            AbstractDungeon.actionManager.addToTop(new VFXAction(new PoisedActivatedEffect(MysticAnimation.bookX, MysticAnimation.bookY, 2.0f)));
        }
        AbstractDungeon.actionManager.addToTop(new ApplyPowerAction(p, p, new ArtesPlayed(p, amount), amount));
        isDone = true;
    }
}
