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
import com.badlogic.gdx.math.*;
import java.io.*;
import com.megacrit.cardcrawl.cards.red.PerfectedStrike;
import com.megacrit.cardcrawl.cards.red.HeavyBlade;
import com.megacrit.cardcrawl.powers.StrengthPower;

@SpirePatch(cls="com.megacrit.cardcrawl.cards.AbstractCard",method="calculateCardDamage")
public class CalculateCardDamageMomentumPatch {

    public static void Postfix(AbstractCard card, AbstractMonster mo) {
        //only run code if momentum power exists
        if (AbstractDungeon.player.hasPower(MomentumPower.POWER_ID)) {
            //copy entire calculateCardDamage method and re-do calculations
            //card.applyPowersToBlock(); removed; method is already called in original method
            final AbstractPlayer player = AbstractDungeon.player;
            card.isDamageModified = false;
            //BEGIN Momentum isSpell and isTechnique calcultations
            boolean isSpell = false;
            boolean isTechnique = false;
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
            //END Momentum isSpell and isTechnique calculations
            //if (!this.isMultiDamage && mo != null) boolean isMultiDamage is protected, need reflection to access
            boolean multiDamageBoolean = (boolean) ReflectionHacks.getPrivate(card, AbstractCard.class, "isMultiDamage");
            if (!multiDamageBoolean && mo != null) {
                float tmp = card.baseDamage;
                if (card instanceof PerfectedStrike) {
                    if (card.upgraded) {
                        tmp += 3 * PerfectedStrike.countCards();
                    } else {
                        tmp += 2 * PerfectedStrike.countCards();
                    }
                    if (card.baseDamage != (int) tmp) {
                        card.isDamageModified = true;
                    }
                }
                if (AbstractDungeon.player.hasRelic("WristBlade") && card.costForTurn == 0) {
                    tmp += 3.0f;
                    if (card.baseDamage != (int) tmp) {
                        card.isDamageModified = true;
                    }
                }
                for (final AbstractPower p : player.powers) {
                    if (card instanceof HeavyBlade && p instanceof StrengthPower) {
                        if (card.upgraded) {
                            tmp = p.atDamageGive(tmp, card.damageTypeForTurn);
                            tmp = p.atDamageGive(tmp, card.damageTypeForTurn);
                        }
                        tmp = p.atDamageGive(tmp, card.damageTypeForTurn);
                        tmp = p.atDamageGive(tmp, card.damageTypeForTurn);
                    }
                    tmp = p.atDamageGive(tmp, card.damageTypeForTurn);
                    if (card.baseDamage != (int) tmp) {
                        card.isDamageModified = true;
                    }
                }
                //BEGIN Momentum calculations for isMultiDamage = false
                //Add techniques played to tmp if card is a spell
                if (isSpell && AbstractDungeon.player.hasPower(TechniquesPlayed.POWER_ID)) {
                    tmp = tmp + AbstractDungeon.player.getPower(TechniquesPlayed.POWER_ID).amount;
                }
                //Add spells played to tmp if card is a technique
                if (isTechnique && AbstractDungeon.player.hasPower(SpellsPlayed.POWER_ID)) {
                    tmp = tmp + AbstractDungeon.player.getPower(SpellsPlayed.POWER_ID).amount;
                }
                //END Momentum calculations for isMultiDamage = false
                if (mo != null) {
                    for (final AbstractPower p : mo.powers) {
                        tmp = p.atDamageReceive(tmp, card.damageTypeForTurn);
                    }
                }
                for (final AbstractPower p : player.powers) {
                    tmp = p.atDamageFinalGive(tmp, card.damageTypeForTurn);
                    if (card.baseDamage != (int) tmp) {
                        card.isDamageModified = true;
                    }
                }
                if (mo != null) {
                    for (final AbstractPower p : mo.powers) {
                        tmp = p.atDamageFinalReceive(tmp, card.damageTypeForTurn);
                        if (card.baseDamage != (int) tmp) {
                            card.isDamageModified = true;
                        }
                    }
                }
                if (tmp < 0.0f) {
                    tmp = 0.0f;
                }
                card.damage = MathUtils.floor(tmp);
            } else {
                final ArrayList<AbstractMonster> m = AbstractDungeon.getCurrRoom().monsters.monsters;
                final float[] tmp2 = new float[m.size()];
                for (int i = 0; i < tmp2.length; ++i) {
                    tmp2[i] = card.baseDamage;
                }
                for (int i = 0; i < tmp2.length; ++i) {
                    if (AbstractDungeon.player.hasRelic("WristBlade") && card.cost == 0) {
                        final float[] array = tmp2;
                        final int n = i;
                        array[n] += 3.0f;
                        if (card.baseDamage != (int) tmp2[i]) {
                            card.isDamageModified = true;
                        }
                    }
                    for (final AbstractPower p2 : player.powers) {
                        tmp2[i] = p2.atDamageGive(tmp2[i], card.damageTypeForTurn);
                        if (card.baseDamage != (int) tmp2[i]) {
                            card.isDamageModified = true;
                        }
                    }
                }
                //BEGIN momentum calculations for isMultiDamage = true
                //Add techniques played to tmp if card is a spell
                if (isSpell && AbstractDungeon.player.hasPower(TechniquesPlayed.POWER_ID)) {
                    for(int i = 0; i < tmp2.length; ++i) {
                        tmp2[i] = tmp2[i] + AbstractDungeon.player.getPower(TechniquesPlayed.POWER_ID).amount;
                    }
                }
                //Add spells played to tmp if card is a technique
                if (isTechnique && AbstractDungeon.player.hasPower(SpellsPlayed.POWER_ID)) {
                    for(int i = 0; i < tmp2.length; ++i) {
                        tmp2[i] = tmp2[i] + AbstractDungeon.player.getPower(SpellsPlayed.POWER_ID).amount;
                    }
                }
                //END momentum calculations for isMultiDamage = false
                for (int i = 0; i < tmp2.length; ++i) {
                    for (final AbstractPower p2 : m.get(i).powers) {
                        if (!m.get(i).isDying && !m.get(i).isEscaping) {
                            tmp2[i] = p2.atDamageReceive(tmp2[i], card.damageTypeForTurn);
                        }
                    }
                }
                for (int i = 0; i < tmp2.length; ++i) {
                    for (final AbstractPower p2 : player.powers) {
                        tmp2[i] = p2.atDamageFinalGive(tmp2[i], card.damageTypeForTurn);
                        if (card.baseDamage != (int) tmp2[i]) {
                            card.isDamageModified = true;
                        }
                    }
                }
                for (int i = 0; i < tmp2.length; ++i) {
                    for (final AbstractPower p2 : m.get(i).powers) {
                        if (!m.get(i).isDying && !m.get(i).isEscaping) {
                            tmp2[i] = p2.atDamageFinalReceive(tmp2[i], card.damageTypeForTurn);
                        }
                    }
                }
                for (int i = 0; i < tmp2.length; ++i) {
                    if (tmp2[i] < 0.0f) {
                        tmp2[i] = 0.0f;
                    }
                }
                card.multiDamage = new int[tmp2.length];
                for (int i = 0; i < tmp2.length; ++i) {
                    card.multiDamage[i] = MathUtils.floor(tmp2[i]);
                }
                card.damage = card.multiDamage[0];
            }
        }
    }
}

//saved previous code
/*
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
*/