package mysticmod.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import mysticmod.MysticMod;
import mysticmod.powers.MomentumPower;
import mysticmod.powers.SpellsPlayed;
import mysticmod.powers.TechniquesPlayed;

@SpirePatch(cls="com.megacrit.cardcrawl.cards.AbstractCard",method="applyPowers")
public class ApplyPowersMomentumPatch {
    public static int baseDamagePlaceholder;

    public static void Prefix(AbstractCard __card_instance) {

        //only run code if momentum power exists
        if (AbstractDungeon.player.hasPower(MomentumPower.POWER_ID)) {

            //store instance.baseDamage in a static placeholder variable for restoration in Postfix
            baseDamagePlaceholder = __card_instance.baseDamage;

            //Modify base damage if instance is a spell or a technique respectively.
            if (MysticMod.isThisASpell(__card_instance) && AbstractDungeon.player.hasPower(TechniquesPlayed.POWER_ID)) {
                __card_instance.baseDamage += AbstractDungeon.player.getPower(TechniquesPlayed.POWER_ID).amount;
            }
            if (MysticMod.isThisATechnique(__card_instance) && AbstractDungeon.player.hasPower(SpellsPlayed.POWER_ID)) {
                __card_instance.baseDamage += AbstractDungeon.player.getPower(SpellsPlayed.POWER_ID).amount;
            }

            //applyPowers will now run as normal with the new baseDamage value
        }
    }

    public static void Postfix(AbstractCard __card_instance) {

        //only run code if momentum power exists
        if (AbstractDungeon.player.hasPower(MomentumPower.POWER_ID)) {

            //restore instance.baseDamage. set modified boolean if appropriate.
            __card_instance.baseDamage = baseDamagePlaceholder;
            if (__card_instance.baseDamage != __card_instance.damage) {
                __card_instance.isDamageModified = true;
            }
        }
    }
}