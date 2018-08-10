//insert to: between EL:2860 and SL:2861

package MysticMod.Patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import java.util.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.actions.GameActionManager;
import MysticMod.Powers.GeminiFormPower;
import MysticMod.Powers.MomentumPower;
import MysticMod.Powers.TechniquesPlayed;
import MysticMod.Powers.SpellsPlayed;

@SpirePatch(cls="com.megacrit.cardcrawl.cards.AbstractCard",method="applyPowersToBlock")
public class ApplyPowersToBlockMomentumPatch {

    @SpireInsertPatch(rloc=12)
    public static void Insert(AbstractCard card) {
        //only run code if momentum power exists
        if (AbstractDungeon.player.hasPower(MomentumPower.POWER_ID)) {
            boolean isSpell = false;
            boolean isTechnique = false;
            int tmp = card.block;
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
            //Squeeze in additional logic for spells and techniques here in the future
            //Add techniques played to tmp if card is a spell
            if (isSpell && AbstractDungeon.player.hasPower(TechniquesPlayed.POWER_ID)) {
                tmp = tmp + AbstractDungeon.player.getPower(TechniquesPlayed.POWER_ID).amount;
            }
            //Add spells played to tmp if card is a technique
            if (isTechnique && AbstractDungeon.player.hasPower(SpellsPlayed.POWER_ID)) {
                tmp = tmp + AbstractDungeon.player.getPower(SpellsPlayed.POWER_ID).amount;
            }
            //set isBlockModified to true if tmp != card.block
            if (card.block != tmp) {
                card.isBlockModified = true;
            }
            //set card.block to tmp
            card.block = tmp;
        }
    }
}