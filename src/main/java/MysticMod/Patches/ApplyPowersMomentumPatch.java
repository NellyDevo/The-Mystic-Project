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
import basemod.ReflectionHacks;

@SpirePatch(cls="com.megacrit.cardcrawl.cards.AbstractCard",method="applyPowers")
public class ApplyPowersMomentumPatch {

    public static void Postfix(AbstractCard card) {
        //only run code if momentum power exists
        if (AbstractDungeon.player.hasPower(MomentumPower.POWER_ID)) {
            boolean isSpell = false;
            boolean isTechnique = false;
            //isMultiDamage is protected, but I need the value
            boolean multiDamageBoolean = (boolean)ReflectionHacks.getPrivate(card, AbstractCard.class, "isMultiDamage");
            final ArrayList<AbstractMonster> m = AbstractDungeon.getCurrRoom().monsters.monsters;
            final int[] tmp = new int[m.size()];
            for (int i = 0; i < tmp.length; ++i) {
                tmp[i] = card.damage;
            }
            int tmp2 = card.damage;
            //Determine if card is a spell
            if (card.rawDescription.startsWith("Spell.")) {
                isSpell = true;
            }
            //Determine if card is a technique
            if (card.rawDescription.startsWith("Technique.")) {
                isTechnique = true;
            }
            //Determine if card is affected by Gemini Form
            int attacksAndSkillsPlayedThisTurn = 0;
            for (AbstractCard playedCard : AbstractDungeon.actionManager.cardsPlayedThisTurn) {
                if (playedCard.type == AbstractCard.CardType.ATTACK || playedCard.type == AbstractCard.CardType.SKILL) {
                    attacksAndSkillsPlayedThisTurn++;
                }
            }
            if (AbstractDungeon.player.hasPower(GeminiFormPower.POWER_ID) && attacksAndSkillsPlayedThisTurn < AbstractDungeon.player.getPower(GeminiFormPower.POWER_ID).amount) {
                isSpell = true;
                isTechnique = true;
            }
            attacksAndSkillsPlayedThisTurn = 0;
            //Squeeze in additional logic for spells and techniques here in the future
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
                if (card.damage != tmp2) {
                    card.isDamageModified = true;
                }
            } else {
                for (int i = 0; i < tmp.length; ++i) {
                    if (card.damage != tmp[i]) {
                        card.isDamageModified = true;
                    }
                }
            }
            //set card.damage to tmp
            if (!multiDamageBoolean) {
                card.damage = tmp2;
            } else {
                for (int i = 0; i < tmp.length; ++i) {
                    card.multiDamage[i] = tmp[i];
                    }
                card.damage = card.multiDamage[0];
            }
        }
    }
}