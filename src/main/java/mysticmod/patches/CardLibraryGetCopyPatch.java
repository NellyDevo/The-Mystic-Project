package mysticmod.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import mysticmod.cards.HeavyStrike;

@SpirePatch(
        clz = CardLibrary.class,
        method = "getCopy",
        paramtypes = {"java.lang.String", "int", "int"}
)
public class CardLibraryGetCopyPatch {
    public static AbstractCard Postfix(AbstractCard retVal, String key, int upgradeTime, int misc) {
        if (retVal instanceof HeavyStrike) {
            retVal.misc = misc;
            retVal.applyPowers();
        }
        return retVal;
    }
}
