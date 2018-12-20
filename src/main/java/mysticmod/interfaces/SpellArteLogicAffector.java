package mysticmod.interfaces;

import com.megacrit.cardcrawl.cards.AbstractCard;

import java.util.Comparator;

public interface SpellArteLogicAffector {

    default boolean modifyIsSpell(AbstractCard card, boolean isSpell) {
        return isSpell;
    }

    default boolean modifyIsArte(AbstractCard card, boolean isArte) {
        return isArte;
    }

    default int getSpellArteLogicPriority() {
        return 0;
    }

    class SortByPriority implements Comparator<SpellArteLogicAffector> {
        public int compare(SpellArteLogicAffector a, SpellArteLogicAffector b)
        {
            return a.getSpellArteLogicPriority() - b.getSpellArteLogicPriority();
        }
    }
}