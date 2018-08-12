//insert to: between EL:2860 and SL:2861

package MysticMod.Patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.actions.GameActionManager;
import MysticMod.Powers.MomentumPower;
import MysticMod.Powers.TechniquesPlayed;
import MysticMod.Powers.SpellsPlayed;
import com.badlogic.gdx.math.*;
import MysticMod.MysticMod;

@SpirePatch(cls="com.megacrit.cardcrawl.cards.AbstractCard",method="applyPowersToBlock")
public class ApplyPowersToBlockMomentumPatch {
    public static int baseBlockPlaceholder;

    public static void Prefix(AbstractCard __card_instance) {

        //only run code if momentum power exists
        if (AbstractDungeon.player.hasPower(MomentumPower.POWER_ID)) {

            //store instance.baseBlock into a static placeholder variable for restoration in Postfix.
            baseBlockPlaceholder = __card_instance.baseBlock;

            //Modify base block if instance is a spell or a technique respectively.
            if (MysticMod.isThisASpell(__card_instance, false) && AbstractDungeon.player.hasPower(TechniquesPlayed.POWER_ID)) {
                __card_instance.baseBlock += AbstractDungeon.player.getPower(TechniquesPlayed.POWER_ID).amount;
            }
            if (MysticMod.isThisATechnique(__card_instance, false) && AbstractDungeon.player.hasPower(SpellsPlayed.POWER_ID)) {
                __card_instance.baseBlock += AbstractDungeon.player.getPower(SpellsPlayed.POWER_ID).amount;
            }

            //applyPowersToBlock will run as normal with the new baseBlock value
        }
    }

    public static void Postfix(AbstractCard __card_instance) {

        //only run code if momentum power exists
        if (AbstractDungeon.player.hasPower(MomentumPower.POWER_ID)) {

            //restore instance.baseBlock. set modified boolean if appropriate.
            __card_instance.baseBlock = baseBlockPlaceholder;
            if (__card_instance.baseBlock != __card_instance.block) {
                __card_instance.isBlockModified = true;
            }
        }
    }
}