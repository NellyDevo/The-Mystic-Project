package mysticmod.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.screens.CardRewardScreen;
import mysticmod.MysticMod;

import java.util.ArrayList;

@SpirePatch(cls="com.megacrit.cardcrawl.screens.CardRewardScreen",method="discoveryOpen",paramtypes={})
public class CardRewardScreenDiscoveryPatch {

    @SpireInsertPatch(rloc=31)
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
}