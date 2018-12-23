package mysticmod.patches;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.evacipated.cardcrawl.modthespire.patcher.PatchingException;
import com.megacrit.cardcrawl.events.beyond.SensoryStone;
import javassist.CannotCompileException;
import javassist.CtBehavior;

import java.util.ArrayList;
import java.util.Collections;

@SpirePatch(
        clz = SensoryStone.class,
        method = "getRandomMemory"
)
public class SensoryStonePatch {

    @SpireInsertPatch(
            locator = Locator.class,
            localvars = {"memories"}
    )
    public static void Insert(SensoryStone obj, ArrayList<String> memories) {
        String MEMORY_TEXT = "#pLOSS. NL NL You must flee. NL NL #b@\"BETRAYER!\"@ NL #r@\"MURDERER!\"@ NL #g@\"BLASHPHEMER!\"@ NL NL Pursuers. Those who you once called friends and family. Those whose faces now contort in #r~terrible~ #r~rage.~ Those who lied to you. NL NL You have to learn the truth. You have to see for yourself. You have to get out... have to get out... ~have~ ~to~ ~get~ ~out~ ...";
        memories.add(MEMORY_TEXT);
    }

    public static class Locator extends SpireInsertLocator {
        public int[] Locate(CtBehavior ctMethodToPatch) throws CannotCompileException, PatchingException {
            Matcher finalMatcher = new Matcher.MethodCallMatcher(Collections.class, "shuffle");

            return LineFinder.findInOrder(ctMethodToPatch, new ArrayList<>(), finalMatcher);
        }
    }
}
