package mysticmod.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import mysticmod.MysticMod;

@SpirePatch(
        clz= AbstractCard.class,
        method="applyPowers"
)
public class SpellArteLogicLimiter {
    public static void Postfix(AbstractCard __instance) {
        MysticMod.refreshSpellArteLogicChecks();
    }
}
