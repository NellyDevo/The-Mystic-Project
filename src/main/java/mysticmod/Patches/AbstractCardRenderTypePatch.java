package mysticmod.patches;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.evacipated.cardcrawl.modthespire.patcher.PatchingException;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import javassist.CannotCompileException;
import javassist.CtBehavior;
import mysticmod.cards.AbstractMysticCard;
import mysticmod.relics.CrystalBall;

import java.util.ArrayList;

@SpirePatch(
        cls="com.megacrit.cardcrawl.cards.AbstractCard",
        method="renderType"
)
public class AbstractCardRenderTypePatch
{
    @SpireInsertPatch(
            localvars={"text"},
            locator = Locator.class
    )
    public static void Insert(AbstractCard __instance, SpriteBatch sb, @ByRef String[] text)
    {
        if (__instance instanceof AbstractMysticCard) {
            if (((AbstractMysticCard)__instance).isTechnique()
                    || (!((AbstractMysticCard)__instance).isSpell()
                    && (AbstractDungeon.player != null && AbstractDungeon.player.hasRelic(CrystalBall.ID))
                    && __instance.type == AbstractCard.CardType.ATTACK)) {
                text[0] = "Arte";
            }
            if (((AbstractMysticCard)__instance).isSpell() || (!((AbstractMysticCard)__instance).isTechnique()
                    && (AbstractDungeon.player != null && AbstractDungeon.player.hasRelic(CrystalBall.ID)) && __instance.type == AbstractCard.CardType.SKILL)) {
                text[0] = "Spell";
            }
        } else if (AbstractDungeon.player != null && AbstractDungeon.player.hasRelic(CrystalBall.ID)) {
            switch (__instance.type) {
                case ATTACK : text[0] = "Arte";
                    break;
                case SKILL : text[0] = "Spell";
                    break;
            }
        }
    }

    public static class Locator extends SpireInsertLocator
    {
        public int[] Locate(CtBehavior ctMethodToPatch) throws CannotCompileException, PatchingException
        {
            Matcher finalMatcher = new Matcher.MethodCallMatcher("com.megacrit.cardcrawl.helpers.FontHelper", "renderRotatedText");

            return LineFinder.findInOrder(ctMethodToPatch, new ArrayList<>(), finalMatcher);
        }
    }
}