package mysticmod.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpireField;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.AbstractCard;

@SpirePatch(
        clz= AbstractCard.class,
        method= SpirePatch.CLASS
)
public class RefreshSpellArteLogicField {
    public static SpireField<Boolean> checkSpell = new SpireField<>(() -> true);
    public static SpireField<Boolean> checkArte = new SpireField<>(() -> true);
    public static SpireField<Boolean> isConditionalSpell = new SpireField<>(() -> false);
    public static SpireField<Boolean> isConditionalArte = new SpireField<>(() -> false);
}