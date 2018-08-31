package mysticmod.patches;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.evacipated.cardcrawl.modthespire.patcher.PatchingException;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.RoomEventDialog;
import com.megacrit.cardcrawl.events.beyond.SpireHeart;
import javassist.CannotCompileException;
import javassist.CtBehavior;

import java.util.ArrayList;

@SpirePatch(
        clz=SpireHeart.class,
        method="buttonEffect"
)
public class SpireHeartButtonEffectMysticTextPatch
{
    @SpireInsertPatch(
            locator = Locator.class
    )
    public static void Insert(SpireHeart __instance, int buttonPressed) {
        if (AbstractDungeon.player.chosenClass == MysticEnum.MYSTIC_CLASS) {
            __instance.roomEventText.updateBodyText("NL You tighten your grip on Book and Blade...");
            System.out.println("UPDATE BODY TEXT"); //UPDATE BODY TEXT
        }
    }

    public static class Locator extends SpireInsertLocator
    {
        public int[] Locate(CtBehavior ctMethodToPatch) throws CannotCompileException, PatchingException
        {
            Matcher finalMatcher = new Matcher.MethodCallMatcher(RoomEventDialog.class, "updateDialogOption");

            return LineFinder.findInOrder(ctMethodToPatch, new ArrayList<>(), finalMatcher);
        }
    }
}