package mysticmod.patches;

import basemod.ReflectionHacks;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.evacipated.cardcrawl.modthespire.patcher.PatchingException;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.screens.SingleCardViewPopup;
import javassist.CannotCompileException;
import javassist.CtBehavior;
import mysticmod.MysticMod;

import java.util.ArrayList;

@SpirePatch(
        clz = SingleCardViewPopup.class,
        method = "renderCardTypeText"
)
public class SingleCardViewPopupRenderCardTypeTextPatch {
    private static UIStrings uiStrings;
    private static String[] TEXT;
    @SpireInsertPatch(
            localvars = {"label"},
            locator = Locator.class
    )
    public static void Insert(SingleCardViewPopup __instance, SpriteBatch sb, @ByRef String[] label) {
        if (uiStrings == null) {
            String ID = "mysticmod:RenderType";
            uiStrings = CardCrawlGame.languagePack.getUIString(ID);
            TEXT = uiStrings.TEXT;
        }
        AbstractCard reflectedCard = (AbstractCard) ReflectionHacks.getPrivate(__instance, SingleCardViewPopup.class, "card");
        boolean isSpell = MysticMod.isThisASpell(reflectedCard);
        boolean isArte = MysticMod.isThisAnArte(reflectedCard);
        if (isSpell && isArte) {
            label[0] = TEXT[2];
        } else if (isArte) {
            label[0] = TEXT[1];
        } else if (isSpell) {
            label[0] = TEXT[0];
        }
    }

    public static class Locator extends SpireInsertLocator {
        public int[] Locate(CtBehavior ctMethodToPatch) throws CannotCompileException, PatchingException {
            Matcher finalMatcher = new Matcher.MethodCallMatcher(FontHelper.class, "renderFontCentered");
            return LineFinder.findInOrder(ctMethodToPatch, new ArrayList<>(), finalMatcher);
        }
    }
}
