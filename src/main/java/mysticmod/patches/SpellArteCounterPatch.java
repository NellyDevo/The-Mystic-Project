package mysticmod.patches;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.evacipated.cardcrawl.modthespire.patcher.PatchingException;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import javassist.CannotCompileException;
import javassist.CtBehavior;
import mysticmod.MysticMod;
import mysticmod.character.MysticAnimation;
import mysticmod.character.MysticCharacter;
import mysticmod.powers.ArtesPlayed;
import mysticmod.powers.SpellsPlayed;
import mysticmod.vfx.PoisedActivatedEffect;
import mysticmod.vfx.PowerfulActivatedEffect;

import java.util.ArrayList;

@SpirePatch(clz=AbstractPlayer.class, method="useCard")
public class SpellArteCounterPatch {

    @SpireInsertPatch(
            locator = Locator.class
    )
    public static void Insert(AbstractPlayer __instance, AbstractCard c, AbstractMonster monster, int energyOnUse) {
        if (MysticMod.isThisASpell(c)) {
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(__instance, __instance, new SpellsPlayed(__instance, 1), 1));
            if (!__instance.hasPower(SpellsPlayed.POWER_ID) && __instance instanceof MysticCharacter) {
                AbstractDungeon.actionManager.addToBottom(new VFXAction(new PowerfulActivatedEffect(MysticAnimation.swordX, MysticAnimation.swordY, 2.0f)));
            }
        }
        if (MysticMod.isThisAnArte(c)) {
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(__instance, __instance, new ArtesPlayed(__instance, 1), 1));
            if (!__instance.hasPower(ArtesPlayed.POWER_ID) && __instance instanceof MysticCharacter) {
                AbstractDungeon.actionManager.addToBottom(new VFXAction(new PoisedActivatedEffect(MysticAnimation.bookX, MysticAnimation.bookY, 2.0f)));
            }
        }
    }

    public static class Locator extends SpireInsertLocator {
        public int[] Locate(CtBehavior ctMethodToPatch) throws CannotCompileException, PatchingException {
            Matcher finalMatcher = new Matcher.MethodCallMatcher(GameActionManager.class, "addToBottom");

            return LineFinder.findInOrder(ctMethodToPatch, new ArrayList<>(), finalMatcher);
        }
    }
}
