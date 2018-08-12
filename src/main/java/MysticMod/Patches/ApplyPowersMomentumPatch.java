package MysticMod.Patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import java.util.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import MysticMod.Powers.GeminiFormPower;
import MysticMod.Powers.MomentumPower;
import MysticMod.Powers.TechniquesPlayed;
import MysticMod.Powers.SpellsPlayed;
import MysticMod.MysticMod;
import basemod.ReflectionHacks;

@SpirePatch(cls="com.megacrit.cardcrawl.cards.AbstractCard",method="applyPowers")
public class ApplyPowersMomentumPatch {

    public static void Postfix(AbstractCard __card_instance) {
        //only run code if momentum power exists
        if (AbstractDungeon.player.hasPower(MomentumPower.POWER_ID)) {
            boolean isSpell = false;
            boolean isTechnique = false;
            //isMultiDamage is protected, but I need the value
            boolean multiDamageBoolean = (boolean)ReflectionHacks.getPrivate(__card_instance, AbstractCard.class, "isMultiDamage");
            final ArrayList<AbstractMonster> m = AbstractDungeon.getCurrRoom().monsters.monsters;
            final int[] tmp = new int[m.size()];
            for (int i = 0; i < tmp.length; ++i) {
                tmp[i] = __card_instance.damage;
            }
            int tmp2 = __card_instance.damage;
            //Determine if card is a spell
            if (MysticMod.isThisASpell(__card_instance, false)) {
                isSpell = true;
            }
            //Determine if card is a technique
            if (MysticMod.isThisATechnique(__card_instance, false)) {
                isTechnique = true;
            }
            //Add techniques played to tmp if card is a spell
            if (isSpell && AbstractDungeon.player.hasPower(TechniquesPlayed.POWER_ID)) {
                if (!multiDamageBoolean) {
                    tmp2 = tmp2 + AbstractDungeon.player.getPower(TechniquesPlayed.POWER_ID).amount;
                } else {
                    for(int i = 0; i < tmp.length; ++i) {
                        tmp[i] = tmp[i] + AbstractDungeon.player.getPower(TechniquesPlayed.POWER_ID).amount;
                    }
                }
            }
            //Add spells played to tmp if card is a technique
            if (isTechnique && AbstractDungeon.player.hasPower(SpellsPlayed.POWER_ID)) {
                if (!multiDamageBoolean) {
                    tmp2 = tmp2 + AbstractDungeon.player.getPower(SpellsPlayed.POWER_ID).amount;
                } else {
                    for(int i = 0; i < tmp.length; ++i) {
                        tmp[i] = tmp[i] + AbstractDungeon.player.getPower(SpellsPlayed.POWER_ID).amount;
                    }
                }
            }
            //set isDamageModified to true if tmp != card.damage
            if (!multiDamageBoolean) {
                if (__card_instance.damage != tmp2) {
                    __card_instance.isDamageModified = true;
                }
            } else {
                for (int i = 0; i < tmp.length; ++i) {
                    if (__card_instance.damage != tmp[i]) {
                        __card_instance.isDamageModified = true;
                    }
                }
            }
            //set card.damage to tmp
            if (!multiDamageBoolean) {
                __card_instance.damage = tmp2;
            } else {
                for (int i = 0; i < tmp.length; ++i) {
                    __card_instance.multiDamage[i] = tmp[i];
                    }
                __card_instance.damage = __card_instance.multiDamage[0];
            }
        }
    }
}