package mysticmod.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import mysticmod.MysticMod;
import mysticmod.powers.MomentumPower;
import mysticmod.powers.SpellsPlayed;
import mysticmod.powers.ArtesPlayed;

@SpirePatch(cls="com.megacrit.cardcrawl.cards.AbstractCard",method="applyPowersToBlock")
public class ApplyPowersToBlockMomentumPatch {
    public static int baseBlockPlaceholder;

    public static void Prefix(AbstractCard __card_instance) {

        //only run code if momentum power exists
        if (AbstractDungeon.player.hasPower(MomentumPower.POWER_ID)) {

            //store instance.baseBlock into a static placeholder variable for restoration in Postfix.
            baseBlockPlaceholder = __card_instance.baseBlock;

            //Modify base block if instance is a spell or a Arte respectively.
            if (MysticMod.isThisASpell(__card_instance) && AbstractDungeon.player.hasPower(ArtesPlayed.POWER_ID)) {
                __card_instance.baseBlock += AbstractDungeon.player.getPower(ArtesPlayed.POWER_ID).amount;
            }
            if (MysticMod.isThisAnArte(__card_instance) && AbstractDungeon.player.hasPower(SpellsPlayed.POWER_ID)) {
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