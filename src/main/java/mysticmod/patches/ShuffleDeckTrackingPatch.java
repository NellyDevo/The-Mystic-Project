package mysticmod.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.actions.common.EmptyDeckShuffleAction;
import com.megacrit.cardcrawl.actions.common.ShuffleAction;
import com.megacrit.cardcrawl.cards.CardGroup;
import mysticmod.MysticMod;

@SpirePatch(cls="com.megacrit.cardcrawl.actions.common.EmptyDeckShuffleAction",method=SpirePatch.CONSTRUCTOR)
public class ShuffleDeckTrackingPatch {

    public static void Prefix(EmptyDeckShuffleAction __instance) {
        MysticMod.numberOfTimesDeckShuffledThisCombat++;
    }
}