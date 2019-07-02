package mysticmod.patches;

import basemod.ReflectionHacks;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.screens.SingleCardViewPopup;
import mysticmod.MysticMod;
import mysticmod.cards.AbstractMysticCard;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;

public class renderBackgroundOverlayPatch {
    private static Method cardRenderHelperMethod;
    private static Method screenRenderHelperMethod;
    private static boolean renderReflectFailureNotified = false;
    private static boolean renderActualFailureNotified = false;

    @SpirePatch(
            clz = AbstractCard.class,
            method = "renderCardBg"
    )
    public static class AbstractCardRenderCardBgPatch {
        private static HashMap<TextureToRender, TextureAtlas.AtlasRegion> textureMap;

        public static void Postfix(AbstractCard __card_instance, SpriteBatch sb, float x, float y) {
            if (cardRenderHelperMethod == null) {
                try {
                    cardRenderHelperMethod = AbstractCard.class.getDeclaredMethod("renderHelper", SpriteBatch.class, Color.class, TextureAtlas.AtlasRegion.class, float.class, float.class);
                } catch (NoSuchMethodException e) {
                    if (!renderReflectFailureNotified) {
                        renderReflectFailureNotified = true;
                        System.out.println("ALERT: Mystic failed to reflect AbstractCard method renderHelper");
                        e.printStackTrace();
                    }
                }
            }
            if (textureMap == null) {
                textureMap = new HashMap<>();
                textureMap.put(TextureToRender.ATTACK_SPELL, new TextureAtlas.AtlasRegion(ImageMaster.loadImage(AbstractMysticCard.BG_ADDON_SMALL_SPELL_ATTACK), 0, 0, 512, 512));
                textureMap.put(TextureToRender.SKILL_SPELL, new TextureAtlas.AtlasRegion(ImageMaster.loadImage(AbstractMysticCard.BG_ADDON_SMALL_SPELL_SKILL), 0, 0, 512, 512));
                textureMap.put(TextureToRender.ATTACK_ARTE, new TextureAtlas.AtlasRegion(ImageMaster.loadImage(AbstractMysticCard.BG_ADDON_SMALL_ARTE_ATTACK), 0, 0, 512, 512));
                textureMap.put(TextureToRender.SKILL_ARTE, new TextureAtlas.AtlasRegion(ImageMaster.loadImage(AbstractMysticCard.BG_ADDON_SMALL_ARTE_SKILL), 0, 0, 512, 512));
                textureMap.put(TextureToRender.ATTACK_SPERTE, new TextureAtlas.AtlasRegion(ImageMaster.loadImage(AbstractMysticCard.BG_ADDON_SMALL_SPERTE_ATTACK), 0, 0, 512, 512));
                textureMap.put(TextureToRender.SKILL_SPERTE, new TextureAtlas.AtlasRegion(ImageMaster.loadImage(AbstractMysticCard.BG_ADDON_SMALL_SPERTE_SKILL), 0, 0, 512, 512));
            }
            boolean isSpell = MysticMod.isThisASpell(__card_instance);
            boolean isArte = MysticMod.isThisAnArte(__card_instance);
            TextureToRender textureToRender = null;
            if (isSpell && isArte) {
                switch (__card_instance.type) {
                    case ATTACK:
                        textureToRender = TextureToRender.ATTACK_SPERTE;
                        break;
                    case SKILL:
                        textureToRender = TextureToRender.SKILL_SPERTE;
                        break;
                }
            } else if (isSpell) {
                switch (__card_instance.type) {
                    case ATTACK:
                        textureToRender = TextureToRender.ATTACK_SPELL;
                        break;
                    case SKILL:
                        textureToRender = TextureToRender.SKILL_SPELL;
                        break;
                }
            } else if (isArte) {
                switch (__card_instance.type) {
                    case ATTACK:
                        textureToRender = TextureToRender.ATTACK_ARTE;
                        break;
                    case SKILL:
                        textureToRender = TextureToRender.SKILL_ARTE;
                        break;
                }
            }
            if (textureToRender != null) {
                try {
                    Color reflectedColor = (Color) ReflectionHacks.getPrivate(__card_instance, AbstractCard.class, "renderColor");
                    cardRenderHelperMethod.setAccessible(true);
                    cardRenderHelperMethod.invoke(__card_instance, sb, reflectedColor, textureMap.get(textureToRender), x, y);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    if (!renderActualFailureNotified) {
                        renderActualFailureNotified = true;
                        System.out.println("ALERT: Mystic failed to invoke AbstractCard method renderHelper");
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    @SpirePatch(
            clz = SingleCardViewPopup.class,
            method = "renderCardBack"
    )
    public static class SingleCardViewPopupRenderCardBackPatch {
        private static HashMap<TextureToRender, TextureAtlas.AtlasRegion> bigTextureMap;

        public static void Postfix(SingleCardViewPopup __instance, SpriteBatch sb) {
            AbstractCard reflectedCard = (AbstractCard) ReflectionHacks.getPrivate(__instance, SingleCardViewPopup.class, "card");
            if (screenRenderHelperMethod == null) {
                try {
                    screenRenderHelperMethod = SingleCardViewPopup.class.getDeclaredMethod("renderHelper", SpriteBatch.class, float.class, float.class, TextureAtlas.AtlasRegion.class);
                } catch (NoSuchMethodException e) {
                    if (!renderReflectFailureNotified) {
                        renderReflectFailureNotified = true;
                        System.out.println("ALERT: Mystic failed to reflect SingleCardViewPopup method renderHelper");
                        e.printStackTrace();
                    }
                }
            }
            if (bigTextureMap == null) {
                bigTextureMap = new HashMap<>();
                bigTextureMap.put(TextureToRender.ATTACK_SPELL, new TextureAtlas.AtlasRegion(ImageMaster.loadImage(AbstractMysticCard.BG_ADDON_LARGE_SPELL_ATTACK), 0, 0, 1024, 1024));
                bigTextureMap.put(TextureToRender.SKILL_SPELL, new TextureAtlas.AtlasRegion(ImageMaster.loadImage(AbstractMysticCard.BG_ADDON_LARGE_SPELL_SKILL), 0, 0, 1024, 1024));
                bigTextureMap.put(TextureToRender.ATTACK_ARTE, new TextureAtlas.AtlasRegion(ImageMaster.loadImage(AbstractMysticCard.BG_ADDON_LARGE_ARTE_ATTACK), 0, 0, 10242, 1024));
                bigTextureMap.put(TextureToRender.SKILL_ARTE, new TextureAtlas.AtlasRegion(ImageMaster.loadImage(AbstractMysticCard.BG_ADDON_LARGE_ARTE_SKILL), 0, 0, 1024, 1024));
                bigTextureMap.put(TextureToRender.ATTACK_SPERTE, new TextureAtlas.AtlasRegion(ImageMaster.loadImage(AbstractMysticCard.BG_ADDON_LARGE_SPERTE_ATTACK), 0, 0, 1024, 1024));
                bigTextureMap.put(TextureToRender.SKILL_SPERTE, new TextureAtlas.AtlasRegion(ImageMaster.loadImage(AbstractMysticCard.BG_ADDON_LARGE_SPERTE_SKILL), 0, 0, 1024, 1024));
            }
            boolean isArte = MysticMod.isThisAnArte(reflectedCard);
            boolean isSpell = MysticMod.isThisASpell(reflectedCard);
            TextureToRender textureToRender = null;
            if (isSpell && isArte) {
                switch (reflectedCard.type) {
                    case ATTACK:
                        textureToRender = TextureToRender.ATTACK_SPERTE;
                        break;
                    case SKILL:
                        textureToRender = TextureToRender.SKILL_SPERTE;
                        break;
                }
            } else if (isSpell) {
                switch (reflectedCard.type) {
                    case ATTACK:
                        textureToRender = TextureToRender.ATTACK_SPELL;
                        break;
                    case SKILL:
                        textureToRender = TextureToRender.SKILL_SPELL;
                        break;
                }
            } else if (isArte) {
                switch (reflectedCard.type) {
                    case ATTACK:
                        textureToRender = TextureToRender.ATTACK_ARTE;
                        break;
                    case SKILL:
                        textureToRender = TextureToRender.SKILL_ARTE;
                        break;
                }
            }
            if (textureToRender != null) {
                try {
                    screenRenderHelperMethod.setAccessible(true);
                    screenRenderHelperMethod.invoke(__instance, sb, Settings.WIDTH / 2.0F, Settings.HEIGHT / 2.0F, bigTextureMap.get(textureToRender));
                } catch (IllegalAccessException | InvocationTargetException e) {
                    if (!renderActualFailureNotified) {
                        renderActualFailureNotified = true;
                        System.out.println("ALERT: Mystic failed to invoke SingleCardViewPopup method renderHelper");
                        e.printStackTrace();
                    }
                }
            }
        }
    }
    
    private enum TextureToRender {
        ATTACK_SPELL,
        SKILL_SPELL,
        ATTACK_ARTE,
        SKILL_ARTE,
        ATTACK_SPERTE,
        SKILL_SPERTE
    }
}
