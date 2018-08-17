package mysticmod.patches;

import mysticmod.MysticMod;
import com.megacrit.cardcrawl.actions.common.EmptyDeckShuffleAction;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;

@SpirePatch(cls="com.megacrit.cardcrawl.actions.common.EmptyDeckShuffleAction",method=SpirePatch.CONSTRUCTOR)
public class ShuffleDeckTrackingPatch {

    public static void Prefix(EmptyDeckShuffleAction __instance) {
        MysticMod.numberOfTimesDeckShuffledThisCombat++;
    }
}