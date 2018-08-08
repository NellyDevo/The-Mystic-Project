package MysticMod.Patches;

import com.megacrit.cardcrawl.screens.CardRewardScreen;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import MysticMod.Patches.IsDiscoveryLookingFor;
import java.util.*;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.cards.AbstractCard;

@SpirePatch(cls="com.megacrit.cardcrawl.screens.CardRewardScreen",method="discoveryOpen",paramtypes={})
public class CardRewardScreenDiscoveryPatch {
    static AbstractCard tmp;

    @SpireInsertPatch(rloc=31)
    public static void Insert(CardRewardScreen cardRewardScreen){
        if (IsDiscoveryLookingFor.Spells.get(cardRewardScreen) == "True") {
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
                if (!dupe && tmp.rawDescription.startsWith("Spell.")) {
                    derp.add(tmp.makeCopy());
                }
            }
            cardRewardScreen.rewardGroup = derp;
        }
        if (IsDiscoveryLookingFor.Techniques.get(cardRewardScreen) == "True") {
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
            cardRewardScreen.rewardGroup = derp;
        }
    }
}