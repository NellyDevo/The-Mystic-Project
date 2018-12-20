package mysticmod.interfaces;

import com.megacrit.cardcrawl.cards.AbstractCard;

public interface SpellArteLogicAffector {
    boolean modifyIsSpell(AbstractCard card, boolean isSpell);
    boolean modifyIsArte(AbstractCard card, boolean isArte);
}