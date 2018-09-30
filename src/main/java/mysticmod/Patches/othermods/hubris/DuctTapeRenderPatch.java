package mysticmod.patches.othermods.hubris;

import basemod.ReflectionHacks;
import basemod.helpers.CardTags;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import mysticmod.MysticMod;
import mysticmod.cards.AbstractMysticCard;
import mysticmod.mystictags.MysticTags;

import java.util.List;

public class DuctTapeRenderPatch {

    @SpirePatch(cls="com.evacipated.cardcrawl.mod.hubris.cards.DuctTapeCard",method="renderCardBg",optional=true)
    public static class DuctTapeRenderCardBgPatch {
        public static void Postfix(AbstractCard __instance, SpriteBatch sb, float x, float y) {
            try {
                Class<?> DuctTapeClass = Class.forName("com.evacipated.cardcrawl.mod.hubris.cards.DuctTapeCard");
                List<AbstractCard> reflectedList = (List<AbstractCard>)ReflectionHacks.getPrivate(__instance, DuctTapeClass, "cards");
                if (CardTags.hasTag(reflectedList.get(0), MysticTags.IS_SPELL)) {
                    switch (reflectedList.get(0).type){
                        case ATTACK:
                            sb.setColor(Color.WHITE);
                            Texture leftSpellAttackOverlay = MysticMod.loadBgAddonTexture(AbstractMysticCard.BG_ADDON_SMALL_SPELL_ATTACK);
                            sb.draw(leftSpellAttackOverlay, x, y, 256.0f, 256.0f, 256.0f, 512.0f, __instance.drawScale * Settings.scale, __instance.drawScale * Settings.scale, __instance.angle, 0, 0, 256, 512, false, false);
                            break;
                        case SKILL:
                            sb.setColor(Color.WHITE);
                            Texture leftSpellSkillOverlay = MysticMod.loadBgAddonTexture(AbstractMysticCard.BG_ADDON_SMALL_SPELL_SKILL);
                            sb.draw(leftSpellSkillOverlay, x, y, 256.0f, 256.0f, 256.0f, 512.0f, __instance.drawScale * Settings.scale, __instance.drawScale * Settings.scale, __instance.angle, 0, 0, 256, 512, false, false);
                            break;
                    }
                }
                if (CardTags.hasTag(reflectedList.get(1), MysticTags.IS_SPELL)) {
                    switch (reflectedList.get(1).type) {
                        case ATTACK:
                            sb.setColor(Color.WHITE);
                            Texture rightSpellAttackOverlay = MysticMod.loadBgAddonTexture(AbstractMysticCard.BG_ADDON_SMALL_SPELL_ATTACK);
                            sb.draw(rightSpellAttackOverlay,x + 256.0f, y, 0.0f, 256.0f, 256.0f, 512.0f, __instance.drawScale * Settings.scale, __instance.drawScale * Settings.scale, __instance.angle, 256, 0, 256, 512, false, false);
                            break;
                        case SKILL:
                            sb.setColor(Color.WHITE);
                            Texture rightSpellSkillOverlay = MysticMod.loadBgAddonTexture(AbstractMysticCard.BG_ADDON_SMALL_SPELL_SKILL);
                            sb.draw(rightSpellSkillOverlay,x + 256.0f, y, 0.0f, 256.0f, 256.0f, 512.0f, __instance.drawScale * Settings.scale, __instance.drawScale * Settings.scale, __instance.angle, 256, 0, 256, 512, false, false);
                            break;
                    }
                }
                if (CardTags.hasTag(reflectedList.get(0), MysticTags.IS_ARTE)) {
                    switch (reflectedList.get(0).type) {
                        case ATTACK:
                            sb.setColor(Color.WHITE);
                            Texture leftArteAttackOverlay = MysticMod.loadBgAddonTexture(AbstractMysticCard.BG_ADDON_SMALL_ARTE_ATTACK);
                            sb.draw(leftArteAttackOverlay, x, y, 256.0f, 256.0f, 256.0f, 512.0f, __instance.drawScale * Settings.scale, __instance.drawScale * Settings.scale, __instance.angle, 0, 0, 256, 512, false, false);
                            break;
                        case SKILL:
                            sb.setColor(Color.WHITE);
                            Texture leftArteSkillOverlay = MysticMod.loadBgAddonTexture(AbstractMysticCard.BG_ADDON_SMALL_ARTE_SKILL);
                            sb.draw(leftArteSkillOverlay, x, y, 256.0f, 256.0f, 256.0f, 512.0f, __instance.drawScale * Settings.scale, __instance.drawScale * Settings.scale, __instance.angle, 0, 0, 256, 512, false, false);
                            break;
                    }
                }
                if (CardTags.hasTag(reflectedList.get(1), MysticTags.IS_ARTE)) {
                    switch (reflectedList.get(1).type) {
                        case ATTACK:
                            sb.setColor(Color.WHITE);
                            Texture rightArteAttackOverlay = MysticMod.loadBgAddonTexture(AbstractMysticCard.BG_ADDON_SMALL_ARTE_ATTACK);
                            sb.draw(rightArteAttackOverlay,x + 256.0f, y, 0.0f, 256.0f, 256.0f, 512.0f, __instance.drawScale * Settings.scale, __instance.drawScale * Settings.scale, __instance.angle, 256, 0, 256, 512, false, false);
                            break;
                        case SKILL:
                            sb.setColor(Color.WHITE);
                            Texture rightArteSkillOverlay = MysticMod.loadBgAddonTexture(AbstractMysticCard.BG_ADDON_SMALL_ARTE_SKILL);
                            sb.draw(rightArteSkillOverlay,x + 256.0f, y, 0.0f, 256.0f, 256.0f, 512.0f, __instance.drawScale * Settings.scale, __instance.drawScale * Settings.scale, __instance.angle, 256, 0, 256, 512, false, false);
                            break;
                    }
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    @SpirePatch(cls="com.evacipated.cardcrawl.mod.hubris.cards.DuctTapeCard",method="renderDuctTapeLargeCardBg",optional=true)
    public static class DuctTapeRenderDuctTapeLargeCardBgPatch {
        public static void Postfix(AbstractCard __instance, SpriteBatch sb) {
            try {
                Class<?> DuctTapeClass = Class.forName("com.evacipated.cardcrawl.mod.hubris.cards.DuctTapeCard");
                List<AbstractCard> reflectedList = (List<AbstractCard>)ReflectionHacks.getPrivate(__instance, DuctTapeClass, "cards");
                if (CardTags.hasTag(reflectedList.get(0), MysticTags.IS_SPELL)) {
                    switch (reflectedList.get(0).type){
                        case ATTACK:
                            sb.setColor(Color.WHITE);
                            Texture leftSpellAttackOverlay = MysticMod.loadBgAddonTexture(AbstractMysticCard.BG_ADDON_LARGE_SPELL_ATTACK);
                            sb.draw(leftSpellAttackOverlay, Settings.WIDTH / 2.0f - 512.0f, Settings.HEIGHT / 2.0f - 512.0f, 512, 512, 512, 1024, Settings.scale, Settings.scale, 0, 0, 0, 512, 1024, false, false);
                            break;
                        case SKILL:
                            sb.setColor(Color.WHITE);
                            Texture leftSpellSkillOverlay = MysticMod.loadBgAddonTexture(AbstractMysticCard.BG_ADDON_LARGE_SPELL_SKILL);
                            sb.draw(leftSpellSkillOverlay, Settings.WIDTH / 2.0f - 512.0f, Settings.HEIGHT / 2.0f - 512.0f, 512, 512, 512, 1024, Settings.scale, Settings.scale, 0, 0, 0, 512, 1024, false, false);
                            break;
                    }
                }
                if (CardTags.hasTag(reflectedList.get(1), MysticTags.IS_SPELL)) {
                    switch (reflectedList.get(1).type) {
                        case ATTACK:
                            sb.setColor(Color.WHITE);
                            Texture rightSpellAttackOverlay = MysticMod.loadBgAddonTexture(AbstractMysticCard.BG_ADDON_LARGE_SPELL_ATTACK);
                            sb.draw(rightSpellAttackOverlay, Settings.WIDTH / 2.0f, Settings.HEIGHT / 2.0f - 512.0f, 0, 512, 512, 1024, Settings.scale, Settings.scale, 0, 512, 0, 512, 1024, false, false);
                            break;
                        case SKILL:
                            sb.setColor(Color.WHITE);
                            Texture rightSpellSkillOverlay = MysticMod.loadBgAddonTexture(AbstractMysticCard.BG_ADDON_LARGE_SPELL_SKILL);
                            sb.draw(rightSpellSkillOverlay, Settings.WIDTH / 2.0f, Settings.HEIGHT / 2.0f - 512.0f, 0, 512, 512, 1024, Settings.scale, Settings.scale, 0, 512, 0, 512, 1024, false, false);
                            break;
                    }
                }
                if (CardTags.hasTag(reflectedList.get(0), MysticTags.IS_ARTE)) {
                    switch (reflectedList.get(0).type) {
                        case ATTACK:
                            sb.setColor(Color.WHITE);
                            Texture leftArteAttackOverlay = MysticMod.loadBgAddonTexture(AbstractMysticCard.BG_ADDON_LARGE_ARTE_ATTACK);
                            sb.draw(leftArteAttackOverlay, Settings.WIDTH / 2.0f - 512.0f, Settings.HEIGHT / 2.0f - 512.0f, 512, 512, 512, 1024, Settings.scale, Settings.scale, 0, 0, 0, 512, 1024, false, false);
                            break;
                        case SKILL:
                            sb.setColor(Color.WHITE);
                            Texture leftArteSkillOverlay = MysticMod.loadBgAddonTexture(AbstractMysticCard.BG_ADDON_LARGE_ARTE_SKILL);
                            sb.draw(leftArteSkillOverlay, Settings.WIDTH / 2.0f - 512.0f, Settings.HEIGHT / 2.0f - 512.0f, 512, 512, 512, 1024, Settings.scale, Settings.scale, 0, 0, 0, 512, 1024, false, false);
                            break;
                    }
                }
                if (CardTags.hasTag(reflectedList.get(1), MysticTags.IS_ARTE)) {
                    switch (reflectedList.get(1).type) {
                        case ATTACK:
                            sb.setColor(Color.WHITE);
                            Texture rightArteAttackOverlay = MysticMod.loadBgAddonTexture(AbstractMysticCard.BG_ADDON_LARGE_ARTE_ATTACK);
                            sb.draw(rightArteAttackOverlay, Settings.WIDTH / 2.0f, Settings.HEIGHT / 2.0f - 512.0f, 0, 512, 512, 1024, Settings.scale, Settings.scale, 0, 512, 0, 512, 1024, false, false);
                            break;
                        case SKILL:
                            sb.setColor(Color.WHITE);
                            Texture rightArteSkillOverlay = MysticMod.loadBgAddonTexture(AbstractMysticCard.BG_ADDON_LARGE_ARTE_SKILL);
                            sb.draw(rightArteSkillOverlay, Settings.WIDTH / 2.0f, Settings.HEIGHT / 2.0f - 512.0f, 0, 512, 512, 1024, Settings.scale, Settings.scale, 0, 512, 0, 512, 1024, false, false);
                            break;
                    }
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}