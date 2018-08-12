package MysticMod.Patches;

import com.megacrit.cardcrawl.screens.CardRewardScreen;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import MysticMod.Patches.IsDiscoveryLookingFor;
import MysticMod.MysticMod;
import java.util.*;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.cards.AbstractCard;

@SpirePatch(cls="com.megacrit.cardcrawl.screens.CardRewardScreen",method="discoveryOpen",paramtypes={})
public class CardRewardScreenDiscoveryPatch {
    static AbstractCard tmp;

    @SpireInsertPatch(rloc=31)
    public static void Insert(CardRewardScreen __cardRewardScreen_instance){
        if (IsDiscoveryLookingFor.Spells.get(__cardRewardScreen_instance) == "True") {
            final ArrayList<AbstractCard> derp = new ArrayList<AbstractCard>();
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
        if (IsDiscoveryLookingFor.Techniques.get(__cardRewardScreen_instance) == "True") { //currently unused
            final ArrayList<AbstractCard> derp = new ArrayList<AbstractCard>();
            while (derp.size() != 3) {
                boolean dupe = false;
                final AbstractCard tmp = AbstractDungeon.returnTrulyRandomCard(AbstractDungeon.cardRandomRng);
                for (final AbstractCard c : derp) {
                    if (c.cardID.equals(tmp.cardID)) {
                        dupe = true;
                        break;
                    }
                }
                if (!dupe && tmp.rawDescription.startsWith("Technique.")) {
                    derp.add(tmp.makeCopy());
                }
            }
            __cardRewardScreen_instance.rewardGroup = derp;
        }
    }
}