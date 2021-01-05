package mysticmod.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.city.Healer;
import mysticmod.MysticMod;
import mysticmod.character.MysticCharacter;

@SpirePatch(
        clz = Healer.class,
        method = "takeTurn"
)
public class HealerTalkyPatch {

    public static void Prefix(Healer __healer_instance) {
        if (AbstractDungeon.player instanceof MysticCharacter && !MysticMod.healerAccused) {
            MysticMod.storedHealerDialogY = __healer_instance.dialogY;
            __healer_instance.dialogY += 70f * Settings.scale;
            AbstractDungeon.actionManager.addToBottom(new TalkAction(__healer_instance, MysticCharacter.TEXT[4]));
            MysticMod.healerAccused = true;
            MysticMod.storedHealer = __healer_instance;
        }
    }
}
