package mysticmod.powers;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.powers.AbstractPower;

public abstract class AbstractSpellArteLogicAffectingPower extends AbstractPower {

    public boolean modifyIsSpell(AbstractCard card, boolean isSpell) {
        return isSpell;
    }

    public boolean modifyIsArte(AbstractCard card, boolean isArte) {
        return isArte;
    }
}