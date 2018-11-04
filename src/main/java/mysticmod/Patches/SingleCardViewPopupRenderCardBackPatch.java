package mysticmod.patches;

import basemod.ReflectionHacks;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.screens.SingleCardViewPopup;
import mysticmod.MysticMod;
import mysticmod.cards.AbstractMysticCard;

@SpirePatch(
        cls="com.megacrit.cardcrawl.screens.SingleCardViewPopup",
        method="renderCardBack"
)
public class SingleCardViewPopupRenderCardBackPatch {
    public static void Postfix(SingleCardViewPopup __instance, SpriteBatch sb) {
        AbstractCard reflectedCard = (AbstractCard) ReflectionHacks.getPrivate(__instance, SingleCardViewPopup.class, "card");
        boolean isArte = MysticMod.isThisAnArte(reflectedCard);
        boolean isSpell = MysticMod.isThisASpell(reflectedCard);
        if (isArte && isSpell) {
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
        } else if (isSpell) {
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
        } else if (isArte) {
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
}