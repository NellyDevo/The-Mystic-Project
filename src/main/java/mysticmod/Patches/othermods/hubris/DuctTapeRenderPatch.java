package mysticmod.patches.othermods.hubris;

import basemod.ReflectionHacks;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import mysticmod.MysticMod;
import mysticmod.cards.AbstractMysticCard;
import mysticmod.patches.RefreshSpellArteLogicField;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class DuctTapeRenderPatch {

    @SpirePatch(
            cls = "com.evacipated.cardcrawl.mod.hubris.cards.DuctTapeCard",
            method = "renderCardBg",
            optional = true
    )
    public static class DuctTapeRenderCardBgPatch {
        private static HashMap<String,String> smallTextures = new HashMap<>();
        private static HashMap<List<Boolean>,String> spellArteStatusToString = new HashMap<>();

        public static void Postfix(AbstractCard __instance, SpriteBatch sb, float x, float y) {
            if (smallTextures.isEmpty()) {
                smallTextures.put("SPELL" + AbstractCard.CardType.ATTACK.toString(), AbstractMysticCard.BG_ADDON_SMALL_SPELL_ATTACK);
                smallTextures.put("ARTE" + AbstractCard.CardType.ATTACK.toString(), AbstractMysticCard.BG_ADDON_SMALL_ARTE_ATTACK);
                smallTextures.put("SPERTE" + AbstractCard.CardType.ATTACK.toString(), AbstractMysticCard.BG_ADDON_SMALL_SPERTE_ATTACK);
                smallTextures.put("SPELL" + AbstractCard.CardType.SKILL.toString(), AbstractMysticCard.BG_ADDON_SMALL_SPELL_SKILL);
                smallTextures.put("ARTE" + AbstractCard.CardType.SKILL.toString(), AbstractMysticCard.BG_ADDON_SMALL_ARTE_SKILL);
                smallTextures.put("SPERTE" + AbstractCard.CardType.SKILL.toString(), AbstractMysticCard.BG_ADDON_SMALL_SPERTE_SKILL);
            }
            if (spellArteStatusToString.isEmpty()) {
                spellArteStatusToString.put(new ArrayList<>(Arrays.asList(true, false)), "SPELL");
                spellArteStatusToString.put(new ArrayList<>(Arrays.asList(false, true)), "ARTE");
                spellArteStatusToString.put(new ArrayList<>(Arrays.asList(true, true)), "SPERTE");
            }
            try {
                Class<?> DuctTapeClass = Class.forName("com.evacipated.cardcrawl.mod.hubris.cards.DuctTapeCard");
                List<AbstractCard> reflectedList = (List<AbstractCard>)ReflectionHacks.getPrivate(__instance, DuctTapeClass, "cards");
                AbstractCard leftCard = reflectedList.get(0);
                AbstractCard rightCard = reflectedList.get(1);
                if (RefreshSpellArteLogicField.checkSpell.get(__instance)) {
                    RefreshSpellArteLogicField.checkSpell.set(leftCard, true);
                    RefreshSpellArteLogicField.checkSpell.set(rightCard, true);
                }
                if (RefreshSpellArteLogicField.checkArte.get(__instance)) {
                    RefreshSpellArteLogicField.checkArte.set(leftCard, true);
                    RefreshSpellArteLogicField.checkArte.set(rightCard, true);
                }
                boolean leftSpell = MysticMod.isThisASpell(leftCard);
                boolean leftArte = MysticMod.isThisAnArte(leftCard);
                boolean rightSpell = MysticMod.isThisASpell(rightCard);
                boolean rightArte = MysticMod.isThisAnArte(rightCard);
                sb.setColor(Color.WHITE);
                if (leftSpell || leftArte) {
                    Texture leftCardOverlay = MysticMod.loadBgAddonTexture(smallTextures.get(spellArteStatusToString.get(new ArrayList<>(Arrays.asList(leftSpell, leftArte))) + leftCard.type.toString()));
                    sb.draw(leftCardOverlay, x, y, 256.0f, 256.0f, 256.0f, 512.0f, __instance.drawScale * Settings.scale, __instance.drawScale * Settings.scale, __instance.angle, 0, 0, 256, 512, false, false);
                }
                if (rightSpell || rightArte) {
                    Texture rightCardOverlay = MysticMod.loadBgAddonTexture(smallTextures.get(spellArteStatusToString.get(new ArrayList<>(Arrays.asList(rightSpell, rightArte))) + rightCard.type.toString()));
                    sb.draw(rightCardOverlay,x + 256.0f, y, 0.0f, 256.0f, 256.0f, 512.0f, __instance.drawScale * Settings.scale, __instance.drawScale * Settings.scale, __instance.angle, 256, 0, 256, 512, false, false);
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    @SpirePatch(
            cls = "com.evacipated.cardcrawl.mod.hubris.cards.DuctTapeCard",
            method = "renderDuctTapeLargeCardBg",
            optional = true
    )
    public static class DuctTapeRenderDuctTapeLargeCardBgPatch {
        private static HashMap<String,String> largeTextures = new HashMap<>();
        private static HashMap<List<Boolean>,String> spellArteStatusToString = new HashMap<>();

        public static void Postfix(AbstractCard __instance, SpriteBatch sb) {
            if (largeTextures.isEmpty()) {
                largeTextures.put("SPELL" + AbstractCard.CardType.ATTACK.toString(), AbstractMysticCard.BG_ADDON_LARGE_SPELL_ATTACK);
                largeTextures.put("ARTE" + AbstractCard.CardType.ATTACK.toString(), AbstractMysticCard.BG_ADDON_LARGE_ARTE_ATTACK);
                largeTextures.put("SPERTE" + AbstractCard.CardType.ATTACK.toString(), AbstractMysticCard.BG_ADDON_LARGE_SPERTE_ATTACK);
                largeTextures.put("SPELL" + AbstractCard.CardType.SKILL.toString(), AbstractMysticCard.BG_ADDON_LARGE_SPELL_SKILL);
                largeTextures.put("ARTE" + AbstractCard.CardType.SKILL.toString(), AbstractMysticCard.BG_ADDON_LARGE_ARTE_SKILL);
                largeTextures.put("SPERTE" + AbstractCard.CardType.SKILL.toString(), AbstractMysticCard.BG_ADDON_LARGE_SPERTE_SKILL);
            }
            if (spellArteStatusToString.isEmpty()) {
                spellArteStatusToString.put(new ArrayList<>(Arrays.asList(true, false)), "SPELL");
                spellArteStatusToString.put(new ArrayList<>(Arrays.asList(false, true)), "ARTE");
                spellArteStatusToString.put(new ArrayList<>(Arrays.asList(true, true)), "SPERTE");
            }
            try {
                Class<?> DuctTapeClass = Class.forName("com.evacipated.cardcrawl.mod.hubris.cards.DuctTapeCard");
                List<AbstractCard> reflectedList = (List<AbstractCard>)ReflectionHacks.getPrivate(__instance, DuctTapeClass, "cards");
                AbstractCard leftCard = reflectedList.get(0);
                AbstractCard rightCard = reflectedList.get(1);
                if (RefreshSpellArteLogicField.checkSpell.get(__instance)) {
                    RefreshSpellArteLogicField.checkSpell.set(leftCard, true);
                    RefreshSpellArteLogicField.checkSpell.set(rightCard, true);
                }
                if (RefreshSpellArteLogicField.checkArte.get(__instance)) {
                    RefreshSpellArteLogicField.checkArte.set(leftCard, true);
                    RefreshSpellArteLogicField.checkArte.set(rightCard, true);
                }
                boolean leftSpell = MysticMod.isThisASpell(leftCard);
                boolean leftArte = MysticMod.isThisAnArte(leftCard);
                boolean rightSpell = MysticMod.isThisASpell(rightCard);
                boolean rightArte = MysticMod.isThisAnArte(rightCard);
                sb.setColor(Color.WHITE);
                if (leftSpell || leftArte) {
                    Texture leftCardOverlay = MysticMod.loadBgAddonTexture(largeTextures.get(spellArteStatusToString.get(new ArrayList<>(Arrays.asList(leftSpell, leftArte))) + leftCard.type.toString()));
                    sb.draw(leftCardOverlay, Settings.WIDTH / 2.0f - 512.0f, Settings.HEIGHT / 2.0f - 512.0f, 512, 512, 512, 1024, Settings.scale, Settings.scale, 0, 0, 0, 512, 1024, false, false);
                }
                if (rightSpell || rightArte) {
                    Texture rightCardOverlay = MysticMod.loadBgAddonTexture(largeTextures.get(spellArteStatusToString.get(new ArrayList<>(Arrays.asList(rightSpell, rightArte))) + rightCard.type.toString()));
                    sb.draw(rightCardOverlay, Settings.WIDTH / 2.0f, Settings.HEIGHT / 2.0f - 512.0f, 0, 512, 512, 1024, Settings.scale, Settings.scale, 0, 512, 0, 512, 1024, false, false);
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}
