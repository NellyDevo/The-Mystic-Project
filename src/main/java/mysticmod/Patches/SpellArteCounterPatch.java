package mysticmod.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import mysticmod.MysticMod;
import mysticmod.powers.ArtesPlayed;
import mysticmod.powers.SpellsPlayed;

@SpirePatch(clz=AbstractPlayer.class, method="useCard")
public class SpellArteCounterPatch {

    public static void Postfix(AbstractPlayer __instance, AbstractCard c, AbstractMonster monster, int energyOnUse) {
        if (MysticMod.isThisASpell(c)) {
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(__instance, __instance, new SpellsPlayed(__instance, 1), 1));
        }
        if (MysticMod.isThisAnArte(c)) {
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(__instance, __instance, new ArtesPlayed(__instance, 1), 1));
        }
    }
}