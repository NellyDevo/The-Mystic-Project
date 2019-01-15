package mysticmod.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import mysticmod.MysticMod;
import mysticmod.powers.*;

@SpirePatch(
        clz=AbstractCard.class,
        method="applyPowersToBlock")
public class ApplyPowersToBlockPatch {
    private static int baseBlockPlaceholder;

    public static void Prefix(AbstractCard __card_instance) {
        //store instance.baseBlock in a static placeholder variable for restoration in Postfix
        baseBlockPlaceholder = __card_instance.baseBlock;

        //check isSpell and isArte
        boolean isSpell = MysticMod.isThisASpell(__card_instance);
        boolean isArte = MysticMod.isThisAnArte(__card_instance);

        //apply Spellpower and Technique
        AbstractPlayer p = AbstractDungeon.player;
        if (isSpell && p.hasPower(SpellpowerPower.POWER_ID)) {
            __card_instance.baseBlock += p.getPower(SpellpowerPower.POWER_ID).amount;
        }
        if (isArte && p.hasPower(TechniquePower.POWER_ID)) {
            __card_instance.baseBlock += p.getPower(TechniquePower.POWER_ID).amount;
        }

        //apply Momentum
        if (p.hasPower(MomentumPower.POWER_ID)) {

            //Modify base Block if instance is a spell or a Arte respectively.
            if (isSpell && p.hasPower(ArtesPlayed.POWER_ID)) {
                __card_instance.baseBlock += AbstractDungeon.player.getPower(ArtesPlayed.POWER_ID).amount;
            }
            if (isArte && p.hasPower(SpellsPlayed.POWER_ID)) {
                __card_instance.baseBlock += AbstractDungeon.player.getPower(SpellsPlayed.POWER_ID).amount;
            }

            //applyPowers will now run as normal with the new baseBlock value
        }
    }

    public static void Postfix(AbstractCard __card_instance) {
        //restore instance.baseBlock. set modified boolean if appropriate.
        __card_instance.baseBlock = baseBlockPlaceholder;
        __card_instance.isBlockModified = __card_instance.baseBlock != __card_instance.block;
    }
}
