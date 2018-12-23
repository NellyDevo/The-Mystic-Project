package mysticmod.patches;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.evacipated.cardcrawl.modthespire.patcher.PatchingException;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.helpers.FontHelper;
import javassist.CannotCompileException;
import javassist.CtBehavior;
import mysticmod.MysticMod;

import java.util.ArrayList;

@SpirePatch(
        clz=AbstractCard.class,
        method="renderType"
)
public class AbstractCardRenderTypePatch {
    @SpireInsertPatch(localvars={"text"}, locator = Locator.class)
    public static void Insert(AbstractCard __instance, SpriteBatch sb, @ByRef String[] text) {
        boolean isSpell = MysticMod.isThisASpell(__instance);
        boolean isArte = MysticMod.isThisAnArte(__instance);
        if (isSpell && isArte) {
            text[0] = "Sperte";
        } else if (isArte) {
            text[0] = "Arte";
        } else if (isSpell) {
            text[0] = "Spell";
        }
    }

    public static class Locator extends SpireInsertLocator {
        public int[] Locate(CtBehavior ctMethodToPatch) throws CannotCompileException, PatchingException {
            Matcher finalMatcher = new Matcher.MethodCallMatcher(FontHelper.class, "renderRotatedText");

            return LineFinder.findInOrder(ctMethodToPatch, new ArrayList<>(), finalMatcher);
        }
    }
}