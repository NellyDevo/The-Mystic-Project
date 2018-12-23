package mysticmod.patches;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.evacipated.cardcrawl.modthespire.patcher.PatchingException;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.events.beyond.SensoryStone;
import com.megacrit.cardcrawl.localization.EventStrings;
import javassist.CannotCompileException;
import javassist.CtBehavior;

import java.util.ArrayList;
import java.util.Collections;

@SpirePatch(
        clz = SensoryStone.class,
        method = "getRandomMemory"
)
public class SensoryStonePatch {
    public static final String ID = "mysticmod:SensoryStonePatch";
    public static EventStrings eventStrings;
    public static String[] DESCRIPTIONS;

    @SpireInsertPatch(
            locator = Locator.class,
            localvars = {"memories"}
    )
    public static void Insert(SensoryStone obj, ArrayList<String> memories) {
        if (eventStrings == null) {
            eventStrings = CardCrawlGame.languagePack.getEventString(ID);
            DESCRIPTIONS = eventStrings.DESCRIPTIONS;
        }
        String MEMORY_TEXT = DESCRIPTIONS[0];
        memories.add(MEMORY_TEXT);
    }

    public static class Locator extends SpireInsertLocator {
        public int[] Locate(CtBehavior ctMethodToPatch) throws CannotCompileException, PatchingException {
            Matcher finalMatcher = new Matcher.MethodCallMatcher(Collections.class, "shuffle");

            return LineFinder.findInOrder(ctMethodToPatch, new ArrayList<>(), finalMatcher);
        }
    }
}
