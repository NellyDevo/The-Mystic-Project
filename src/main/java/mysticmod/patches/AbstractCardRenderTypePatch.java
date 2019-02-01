package mysticmod.patches;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.evacipated.cardcrawl.modthespire.patcher.PatchingException;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.localization.UIStrings;
import javassist.CannotCompileException;
import javassist.CtBehavior;
import mysticmod.MysticMod;

import java.util.ArrayList;

@SpirePatch(
        clz=AbstractCard.class,
        method="renderType"
)
public class AbstractCardRenderTypePatch {
    private static UIStrings uiStrings;
    private static String[] TEXT;
    @SpireInsertPatch(
            localvars={"text"},
            locator = Locator.class
    )
    public static void Insert(AbstractCard __instance, SpriteBatch sb, @ByRef String[] text) {
        if (uiStrings == null) {
            String ID = "mysticmod:RenderType";
            uiStrings = CardCrawlGame.languagePack.getUIString(ID);
            TEXT = uiStrings.TEXT;
        }
        boolean isSpell = MysticMod.isThisASpell(__instance);
        boolean isArte = MysticMod.isThisAnArte(__instance);
        if (isSpell && isArte) {
            text[0] = TEXT[2];
        } else if (isArte) {
            text[0] = TEXT[1];
        } else if (isSpell) {
            text[0] = TEXT[0];
        }
    }

    public static class Locator extends SpireInsertLocator {
        public int[] Locate(CtBehavior ctMethodToPatch) throws CannotCompileException, PatchingException {
            Matcher finalMatcher = new Matcher.MethodCallMatcher(FontHelper.class, "renderRotatedText");

            return LineFinder.findInOrder(ctMethodToPatch, new ArrayList<>(), finalMatcher);
        }
    }
}
