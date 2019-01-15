package mysticmod.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import mysticmod.MysticMod;
import mysticmod.powers.*;

@SpirePatch(
        clz = AbstractCard.class,
        method = "calculateCardDamage"
)
public class CalculateCardDamagePatch {
    private static int baseDamagePlaceholder;

    public static void Prefix(AbstractCard __card_instance, AbstractMonster mo) {
        //store instance.baseDamage in a static placeholder variable for restoration in Postfix
        baseDamagePlaceholder = __card_instance.baseDamage;

        //check isSpell and isArte
        boolean isSpell = MysticMod.isThisASpell(__card_instance);
        boolean isArte = MysticMod.isThisAnArte(__card_instance);

        //apply Spellpower and Technique
        AbstractPlayer p = AbstractDungeon.player;
        if (isSpell && p.hasPower(SpellpowerPower.POWER_ID)) {
            __card_instance.baseDamage += p.getPower(SpellpowerPower.POWER_ID).amount;
        }
        if (isArte && p.hasPower(TechniquePower.POWER_ID)) {
            __card_instance.baseDamage += p.getPower(TechniquePower.POWER_ID).amount;
        }

        //apply Momentum
        if (p.hasPower(MomentumPower.POWER_ID)) {

            //Modify base damage if instance is a spell or a Arte respectively.
            if (isSpell && p.hasPower(ArtesPlayed.POWER_ID)) {
                __card_instance.baseDamage += AbstractDungeon.player.getPower(ArtesPlayed.POWER_ID).amount;
            }
            if (isArte && p.hasPower(SpellsPlayed.POWER_ID)) {
                __card_instance.baseDamage += AbstractDungeon.player.getPower(SpellsPlayed.POWER_ID).amount;
            }

            //applyPowers will now run as normal with the new baseDamage value
        }
    }

    public static void Postfix(AbstractCard __card_instance, AbstractMonster mo) {
        //restore instance.baseDamage. set modified boolean if appropriate.
        __card_instance.baseDamage = baseDamagePlaceholder;
        __card_instance.isDamageModified = __card_instance.baseDamage != __card_instance.damage;
    }
}
