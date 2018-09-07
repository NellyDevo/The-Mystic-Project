package mysticmod.patches;

import basemod.ReflectionHacks;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import mysticmod.MysticMod;
import mysticmod.cards.AbstractMysticCard;
import mysticmod.relics.CrystalBall;

@SpirePatch(cls="com.megacrit.cardcrawl.cards.AbstractCard",method="renderCardBg")
public class AbstractCardRenderCardBgPatch {

    public static void Postfix(AbstractCard __card_instance, SpriteBatch sb, float x, float y) {
        if (!(__card_instance instanceof AbstractMysticCard) && (AbstractDungeon.player != null && AbstractDungeon.player.hasRelic(CrystalBall.ID)) && (__card_instance.type == AbstractCard.CardType.ATTACK || __card_instance.type == AbstractCard.CardType.SKILL)) {
            Color reflectedColor = (Color)ReflectionHacks.getPrivate(__card_instance, AbstractCard.class, "renderColor");
            switch (__card_instance.type) {
                case ATTACK : Texture extraAttackBG = MysticMod.loadBgAddonTexture(AbstractMysticCard.BG_ADDON_SMALL_ARTE_ATTACK);
                sb.setColor(reflectedColor);
                sb.draw(extraAttackBG, x, y, 256f, 256f, 512f, 512f, __card_instance.drawScale * Settings.scale, __card_instance.drawScale * Settings.scale, __card_instance.angle, 0, 0, 512, 512, false, false);
                break;
                case SKILL : Texture extraSkillBG = MysticMod.loadBgAddonTexture(AbstractMysticCard.BG_ADDON_SMALL_SPELL_SKILL);
                sb.setColor(reflectedColor);
                sb.draw(extraSkillBG, x, y, 256f, 256f, 512f, 512f, __card_instance.drawScale * Settings.scale, __card_instance.drawScale * Settings.scale, __card_instance.angle, 0, 0, 512, 512, false, false);
                break;
            }
        }
        if (__card_instance instanceof AbstractMysticCard && __card_instance.color == AbstractCard.CardColor.COLORLESS) {
            Color reflectedColor = (Color)ReflectionHacks.getPrivate(__card_instance, AbstractCard.class, "renderColor");
            switch (__card_instance.type) {
                case ATTACK:
                    if (((AbstractMysticCard) __card_instance).isSpell()) {
                        Texture extraAttackBG = MysticMod.loadBgAddonTexture(AbstractMysticCard.BG_ADDON_SMALL_SPELL_ATTACK);
                        sb.setColor(reflectedColor);
                        sb.draw(extraAttackBG, x, y, 256f, 256f, 512f, 512f, __card_instance.drawScale * Settings.scale, __card_instance.drawScale * Settings.scale, __card_instance.angle, 0, 0, 512, 512, false, false);
                    } else if (((AbstractMysticCard) __card_instance).isArte() || (AbstractDungeon.player != null && AbstractDungeon.player.hasRelic(CrystalBall.ID))) {
                        Texture extraAttackBG = MysticMod.loadBgAddonTexture(AbstractMysticCard.BG_ADDON_SMALL_ARTE_ATTACK);
                        sb.setColor(reflectedColor);
                        sb.draw(extraAttackBG, x, y, 256f, 256f, 512f, 512f, __card_instance.drawScale * Settings.scale, __card_instance.drawScale * Settings.scale, __card_instance.angle, 0, 0, 512, 512, false, false);
                    }
                    break;
                case SKILL:
                    if (((AbstractMysticCard) __card_instance).isSpell() || (AbstractDungeon.player != null && AbstractDungeon.player.hasRelic(CrystalBall.ID))) {
                        Texture extraSkillBG = MysticMod.loadBgAddonTexture(AbstractMysticCard.BG_ADDON_SMALL_SPELL_SKILL);
                        sb.setColor(reflectedColor);
                        sb.draw(extraSkillBG, x, y, 256f, 256f, 512f, 512f, __card_instance.drawScale * Settings.scale, __card_instance.drawScale * Settings.scale, __card_instance.angle, 0, 0, 512, 512, false, false);
                    }
                    break;
            }
        }
    }
}