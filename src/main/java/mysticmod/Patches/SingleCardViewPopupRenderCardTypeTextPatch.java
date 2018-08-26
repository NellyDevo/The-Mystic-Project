package mysticmod.patches;

import basemod.ReflectionHacks;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.evacipated.cardcrawl.modthespire.patcher.PatchingException;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.screens.SingleCardViewPopup;
import javassist.CannotCompileException;
import javassist.CtBehavior;
import mysticmod.cards.AbstractMysticCard;
import mysticmod.relics.CrystalBall;

import java.util.ArrayList;

@SpirePatch(
        cls="com.megacrit.cardcrawl.screens.SingleCardViewPopup",
        method="renderCardTypeText"
)
public class SingleCardViewPopupRenderCardTypeTextPatch
{
    @SpireInsertPatch(
            localvars={"label"},
            locator=Locator.class
    )
    public static void Insert(SingleCardViewPopup __instance, SpriteBatch sb, @ByRef String[] label)
    {
        AbstractCard reflectedCard = (AbstractCard)ReflectionHacks.getPrivate(__instance, SingleCardViewPopup.class, "card");
        if (reflectedCard instanceof AbstractMysticCard) {
            if (((AbstractMysticCard)reflectedCard).isTechnique()
                    || (!((AbstractMysticCard)reflectedCard).isSpell()
                    && (AbstractDungeon.player != null && AbstractDungeon.player.hasRelic(CrystalBall.ID))
                    && reflectedCard.type == AbstractCard.CardType.ATTACK)) {
                label[0] = "Arte";
            }
            if (((AbstractMysticCard)reflectedCard).isSpell() || (!((AbstractMysticCard)reflectedCard).isTechnique()
                    && (AbstractDungeon.player != null && AbstractDungeon.player.hasRelic(CrystalBall.ID)) && reflectedCard.type == AbstractCard.CardType.SKILL)) {
                label[0] = "Spell";
            }
        }  else if (AbstractDungeon.player != null && AbstractDungeon.player.hasRelic(CrystalBall.ID)) {
            switch (reflectedCard.type) {
                case ATTACK : label[0] = "Arte";
                break;
                case SKILL : label[0] = "Spell";
                break;
            }
        }
    }

    public static class Locator extends SpireInsertLocator
    {
        public int[] Locate(CtBehavior ctMethodToPatch) throws CannotCompileException, PatchingException
        {
            Matcher finalMatcher = new Matcher.MethodCallMatcher("com.megacrit.cardcrawl.helpers.FontHelper", "renderFontCentered");

            return LineFinder.findInOrder(ctMethodToPatch, new ArrayList<>(), finalMatcher);
        }
    }
}