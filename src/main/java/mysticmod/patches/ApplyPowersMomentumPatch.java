package mysticmod.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import mysticmod.MysticMod;
import mysticmod.powers.ArtesPlayed;
import mysticmod.powers.MomentumPower;
import mysticmod.powers.SpellsPlayed;

@SpirePatch(clz=AbstractCard.class, method="applyPowers")
public class ApplyPowersMomentumPatch {
    public static int baseDamagePlaceholder;

    public static void Prefix(AbstractCard __card_instance) {
        //for spell/arte logic, refresh conditional logic.
        for (AbstractCard card : AbstractDungeon.player.hand.group) {
            RefreshSpellArteLogicField.checkSpell.set(card, true);
            RefreshSpellArteLogicField.checkArte.set(card, true);
        }
        for (AbstractCard card : AbstractDungeon.player.drawPile.group) {
            RefreshSpellArteLogicField.checkSpell.set(card, true);
            RefreshSpellArteLogicField.checkArte.set(card, true);
        }
        for (AbstractCard card : AbstractDungeon.player.discardPile.group) {
            RefreshSpellArteLogicField.checkSpell.set(card, true);
            RefreshSpellArteLogicField.checkArte.set(card, true);
        }
        for (AbstractCard card : AbstractDungeon.player.exhaustPile.group) {
            RefreshSpellArteLogicField.checkSpell.set(card, true);
            RefreshSpellArteLogicField.checkArte.set(card, true);
        }
        for (AbstractCard card : AbstractDungeon.player.masterDeck.group) {
            RefreshSpellArteLogicField.checkSpell.set(card, true);
            RefreshSpellArteLogicField.checkArte.set(card, true);
        }

        //only run code if momentum power exists
        if (AbstractDungeon.player.hasPower(MomentumPower.POWER_ID)) {

            //store instance.baseDamage in a static placeholder variable for restoration in Postfix
            baseDamagePlaceholder = __card_instance.baseDamage;

            //Modify base damage if instance is a spell or a Arte respectively.
            if (MysticMod.isThisASpell(__card_instance) && AbstractDungeon.player.hasPower(ArtesPlayed.POWER_ID)) {
                __card_instance.baseDamage += AbstractDungeon.player.getPower(ArtesPlayed.POWER_ID).amount;
            }
            if (MysticMod.isThisAnArte(__card_instance) && AbstractDungeon.player.hasPower(SpellsPlayed.POWER_ID)) {
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