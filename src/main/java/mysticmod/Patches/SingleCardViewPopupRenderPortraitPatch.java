package mysticmod.patches;

import basemod.ReflectionHacks;
import basemod.helpers.CardTags;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.screens.SingleCardViewPopup;
import mysticmod.MysticMod;
import mysticmod.cards.AbstractMysticCard;
import mysticmod.mystictags.MysticTags;
import mysticmod.relics.CrystalBall;

@SpirePatch(
        cls="com.megacrit.cardcrawl.screens.SingleCardViewPopup",
        method="renderCardBack"
)
public class SingleCardViewPopupRenderPortraitPatch {
    public static void Postfix(SingleCardViewPopup __instance, SpriteBatch sb) {
        AbstractCard reflectedCard = (AbstractCard) ReflectionHacks.getPrivate(__instance, SingleCardViewPopup.class, "card");
        if (!(reflectedCard instanceof AbstractMysticCard) && (AbstractDungeon.player != null && AbstractDungeon.player.hasRelic(CrystalBall.ID)) && (reflectedCard.type == AbstractCard.CardType.ATTACK || reflectedCard.type == AbstractCard.CardType.SKILL)) {
            switch (reflectedCard.type) {
                case ATTACK:
                    Texture extraAttackBG = MysticMod.loadBgAddonTexture(AbstractMysticCard.BG_ADDON_LARGE_ARTE_ATTACK);
                    sb.draw(extraAttackBG, Settings.WIDTH / 2.0f - 512.0f, Settings.HEIGHT / 2.0f - 512.0f, 512.0f, 512.0f, 1024.0f, 1024.0f, Settings.scale, Settings.scale, 0.0f, 0, 0, 1024, 1024, false, false);
                    break;
                case SKILL:
                    Texture extraSkillBG = MysticMod.loadBgAddonTexture(AbstractMysticCard.BG_ADDON_LARGE_SPELL_SKILL);
                    sb.draw(extraSkillBG, Settings.WIDTH / 2.0f - 512.0f, Settings.HEIGHT / 2.0f - 512.0f, 512.0f, 512.0f, 1024.0f, 1024.0f, Settings.scale, Settings.scale, 0.0f, 0, 0, 1024, 1024, false, false);
                    break;
            }
        } else if (reflectedCard instanceof AbstractMysticCard && !((AbstractMysticCard)reflectedCard).isSpell() && !((AbstractMysticCard)reflectedCard).isArte() && AbstractDungeon.player != null && AbstractDungeon.player.hasRelic(CrystalBall.ID)) {
            switch (reflectedCard.type) {
                case ATTACK:
                    Texture attackOverlay = MysticMod.loadBgAddonTexture(AbstractMysticCard.BG_LARGE_ARTE_ATTACK_MYSTIC);
                    sb.draw(attackOverlay, Settings.WIDTH / 2.0f - 512.0f, Settings.HEIGHT / 2.0f - 512.0f, 512.0f, 512.0f, 1024.0f, 1024.0f, Settings.scale, Settings.scale, 0.0f, 0, 0, 1024, 1024, false, false);
                    break;
                case SKILL:
                    Texture skillOverlay = MysticMod.loadBgAddonTexture(AbstractMysticCard.BG_LARGE_SPELL_SKILL_MYSTIC);
                    sb.draw(skillOverlay, Settings.WIDTH / 2.0f - 512.0f, Settings.HEIGHT / 2.0f - 512.0f, 512.0f, 512.0f, 1024.0f, 1024.0f, Settings.scale, Settings.scale, 0.0f, 0, 0, 1024, 1024, false, false);
                    break;
            }
        } else if (reflectedCard instanceof AbstractMysticCard && reflectedCard.color == AbstractCard.CardColor.COLORLESS) {
            switch (reflectedCard.type) {
                case ATTACK:
                    if (((AbstractMysticCard) reflectedCard).isSpell()) {
                        Texture extraAttackBG = MysticMod.loadBgAddonTexture(AbstractMysticCard.BG_ADDON_LARGE_SPELL_ATTACK);
                        sb.draw(extraAttackBG, Settings.WIDTH / 2.0f - 512.0f, Settings.HEIGHT / 2.0f - 512.0f, 512.0f, 512.0f, 1024.0f, 1024.0f, Settings.scale, Settings.scale, 0.0f, 0, 0, 1024, 1024, false, false);
                    } else if (((AbstractMysticCard) reflectedCard).isArte()) {
                        Texture extraAttackBG = MysticMod.loadBgAddonTexture(AbstractMysticCard.BG_ADDON_LARGE_ARTE_ATTACK);
                        sb.draw(extraAttackBG, Settings.WIDTH / 2.0f - 512.0f, Settings.HEIGHT / 2.0f - 512.0f, 512.0f, 512.0f, 1024.0f, 1024.0f, Settings.scale, Settings.scale, 0.0f, 0, 0, 1024, 1024, false, false);
                    }
                    break;
                case SKILL:
                    if (((AbstractMysticCard) reflectedCard).isSpell()) {
                        Texture extraSkillBG = MysticMod.loadBgAddonTexture(AbstractMysticCard.BG_ADDON_LARGE_SPELL_SKILL);
                        sb.draw(extraSkillBG, Settings.WIDTH / 2.0f - 512.0f, Settings.HEIGHT / 2.0f - 512.0f, 512.0f, 512.0f, 1024.0f, 1024.0f, Settings.scale, Settings.scale, 0.0f, 0, 0, 1024, 1024, false, false);
                    }
                    break;
            }
        }
        if (!(reflectedCard instanceof AbstractMysticCard)) {
            if (CardTags.hasTag(reflectedCard, MysticTags.IS_SPELL)) {
                switch (reflectedCard.type) {
                    case ATTACK:
                        Texture extraAttackBG = MysticMod.loadBgAddonTexture(AbstractMysticCard.BG_ADDON_LARGE_SPELL_ATTACK);
                        sb.draw(extraAttackBG, Settings.WIDTH / 2.0f - 512.0f, Settings.HEIGHT / 2.0f - 512.0f, 512.0f, 512.0f, 1024.0f, 1024.0f, Settings.scale, Settings.scale, 0.0f, 0, 0, 1024, 1024, false, false);
                        break;
                    case SKILL:
                        Texture extraSkillBG = MysticMod.loadBgAddonTexture(AbstractMysticCard.BG_ADDON_LARGE_SPELL_SKILL);
                        sb.draw(extraSkillBG, Settings.WIDTH / 2.0f - 512.0f, Settings.HEIGHT / 2.0f - 512.0f, 512.0f, 512.0f, 1024.0f, 1024.0f, Settings.scale, Settings.scale, 0.0f, 0, 0, 1024, 1024, false, false);
                        break;
                }
            }
            if (CardTags.hasTag(reflectedCard, MysticTags.IS_ARTE)) {
                switch (reflectedCard.type) {
                    case ATTACK:
                        Texture extraAttackBG = MysticMod.loadBgAddonTexture(AbstractMysticCard.BG_ADDON_LARGE_ARTE_ATTACK);
                        sb.draw(extraAttackBG, Settings.WIDTH / 2.0f - 512.0f, Settings.HEIGHT / 2.0f - 512.0f, 512.0f, 512.0f, 1024.0f, 1024.0f, Settings.scale, Settings.scale, 0.0f, 0, 0, 1024, 1024, false, false);
                        break;
                    case SKILL:
                        Texture extraSkillBG = MysticMod.loadBgAddonTexture(AbstractMysticCard.BG_ADDON_LARGE_ARTE_SKILL);
                        sb.draw(extraSkillBG, Settings.WIDTH / 2.0f - 512.0f, Settings.HEIGHT / 2.0f - 512.0f, 512.0f, 512.0f, 1024.0f, 1024.0f, Settings.scale, Settings.scale, 0.0f, 0, 0, 1024, 1024, false, false);
                        break;
                }
            }
        }
        if (CardTags.hasTag(reflectedCard, MysticTags.IS_SPELL) && CardTags.hasTag(reflectedCard, MysticTags.IS_ARTE)) {
            switch (reflectedCard.type) {
                case ATTACK:
                    Texture extraAttackBG = MysticMod.loadBgAddonTexture(AbstractMysticCard.BG_ADDON_LARGE_SPERTE_ATTACK);
                    sb.draw(extraAttackBG, Settings.WIDTH / 2.0f - 512.0f, Settings.HEIGHT / 2.0f - 512.0f, 512.0f, 512.0f, 1024.0f, 1024.0f, Settings.scale, Settings.scale, 0.0f, 0, 0, 1024, 1024, false, false);
                    break;
                case SKILL:
                    Texture extraSkillBG = MysticMod.loadBgAddonTexture(AbstractMysticCard.BG_ADDON_LARGE_SPERTE_SKILL);
                    sb.draw(extraSkillBG, Settings.WIDTH / 2.0f - 512.0f, Settings.HEIGHT / 2.0f - 512.0f, 512.0f, 512.0f, 1024.0f, 1024.0f, Settings.scale, Settings.scale, 0.0f, 0, 0, 1024, 1024, false, false);
                    break;
            }
        }
    }
}