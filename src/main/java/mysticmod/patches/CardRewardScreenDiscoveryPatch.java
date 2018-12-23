package mysticmod.patches;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.evacipated.cardcrawl.modthespire.patcher.PatchingException;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.screens.CardRewardScreen;
import javassist.CannotCompileException;
import javassist.CtBehavior;
import mysticmod.MysticMod;

import java.util.ArrayList;

@SpirePatch(
        clz = CardRewardScreen.class,
        method="discoveryOpen",
        paramtypes={}
)
public class CardRewardScreenDiscoveryPatch {

    @SpireInsertPatch(
            locator = Locator.class
    )
    public static void Insert(CardRewardScreen __cardRewardScreen_instance){
        if (MysticMod.isDiscoveryLookingForSpells) {
            final ArrayList<AbstractCard> derp = new ArrayList<>();
            while (derp.size() != 3) {
                boolean dupe = false;
                final AbstractCard tmp = MysticMod.returnTrulyRandomSpell();
                for (final AbstractCard c : derp) {
                    if (c.cardID.equals(tmp.cardID)) {
                        dupe = true;
                        break;
                    }
                }
                if (!dupe) {
                    derp.add(tmp.makeCopy());
                }
            }
            __cardRewardScreen_instance.rewardGroup = derp;
        }
    }

    public static class Locator extends SpireInsertLocator {
        public int[] Locate(CtBehavior ctMethodToPatch) throws CannotCompileException, PatchingException {
            Matcher finalMatcher = new Matcher.FieldAccessMatcher(AbstractDungeon.class, "isScreenUp");

            return LineFinder.findInOrder(ctMethodToPatch, new ArrayList<>(), finalMatcher);
        }
    }
}
