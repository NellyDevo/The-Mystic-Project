package mysticmod.cards;

import basemod.abstracts.CustomCard;
import basemod.helpers.TooltipInfo;
import com.megacrit.cardcrawl.cards.AbstractCard;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractMysticCard extends CustomCard {
    public boolean isSpell = false;
    public boolean isTechnique = false;
    public static final String BG_SMALL_SPELL_ATTACK_MYSTIC = "mysticmod/images/512/bg_spell_attack_mystic.png";
    public static final String BG_LARGE_SPELL_ATTACK_MYSTIC = "mysticmod/images/1024/bg_spell_attack_mystic.png";
    public static final String BG_SMALL_ARTE_ATTACK_MYSTIC = "mysticmod/images/512/bg_arte_attack_mystic.png";
    public static final String BG_LARGE_ARTE_ATTACK_MYSTIC = "mysticmod/images/1024/bg_arte_attack_mystic.png";
    public static final String BG_SMALL_SPELL_SKILL_MYSTIC = "mysticmod/images/512/bg_spell_skill_mystic.png";
    public static final String BG_LARGE_SPELL_SKILL_MYSTIC = "mysticmod/images/1024/bg_spell_skill_mystic.png";
    public static final String BG_SMALL_ARTE_SKILL_MYSTIC = "mysticmod/images/512/bg_arte_skill_mystic.png";
    public static final String BG_LARGE_ARTE_SKILL_MYSTIC = "mysticmod/images/1024/bg_arte_skill_mystic.png";
    public static final String BG_SMALL_SPELL_ATTACK_COLORLESS = "mysticmod/images/512/bg_spell_attack_colorless.png";
    public static final String BG_LARGE_SPELL_ATTACK_COLORLESS = "mysticmod/images/1024/bg_spell_attack_colorless.png";
    public static final String BG_SMALL_ARTE_ATTACK_COLORLESS = "mysticmod/images/512/bg_arte_attack_colorless.png";
    public static final String BG_LARGE_ARTE_ATTACK_COLORLESS = "mysticmod/images/1024/bg_arte_attack_colorless.png";
    public static final String BG_SMALL_DEFAULT_ATTACK_COLORLESS = "mysticmod/images/512/bg_attack_colorless.png";
    public static final String BG_LARGE_DEFAULT_ATTACK_COLORLESS = "mysticmod/images/1024/bg_attack_colorless.png";
    public static final String BG_SMALL_SPELL_SKILL_COLORLESS = "mysticmod/images/512/bg_spell_skill_colorless.png";
    public static final String BG_LARGE_SPELL_SKILL_COLORLESS = "mysticmod/images/1024/bg_spell_skill_colorless.png";
    public static final String BG_SMALL_DEFAULT_SKILL_COLORLESS = "mysticmod/images/512/bg_skill_colorless.png";
    public static final String BG_LARGE_DEFAULT_SKILL_COLORLESS = "mysticmod/images/1024/bg_skill_colorless.png";
    public static final String SMALL_ORB_COLORLESS = "mysticmod/images/512/card_colorless_orb_copy.png";
    public static final String LARGE_ORB_COLORLESS = "mysticmod/images/1024/card_colorless_orb_copy.png";

    public AbstractMysticCard(final String id, final String name, final String img, final int cost, final String rawDescription,
                              final AbstractCard.CardType type, final AbstractCard.CardColor color,
                              final AbstractCard.CardRarity rarity, final AbstractCard.CardTarget target) {
        super(id, name, img, cost, rawDescription, type, color, rarity, target);
    }

    public boolean isSpell() {
        return this.isSpell;
    }

    @Override
    public List<TooltipInfo> getCustomTooltips() {
        List<TooltipInfo> retVal = new ArrayList<>();
        if (this.type == AbstractCard.CardType.SKILL) {
            if (this.isSpell) {
                retVal.add(new TooltipInfo("Spell.", "This Skill is considered a Spell."));
            } else if (this.isTechnique) {
                retVal.add(new TooltipInfo("Arte.", "This Skill is considered an Arte."));
            }
        } else if (this.type == AbstractCard.CardType.ATTACK) {
            if (this.isSpell) {
                retVal.add(new TooltipInfo("Spell.", "This Attack is considered a Spell."));
            } else if (this.isTechnique) {
                retVal.add(new TooltipInfo("Arte.", "This Attack is considered an Arte."));
            }
        }
        return retVal;
    }

    public void changeColor(String smallBGTexture, String largeBGTexture, boolean colorlessOrb) {
        this.setBackgroundTexture(smallBGTexture, largeBGTexture);
        if (colorlessOrb) {
            this.setOrbTexture(SMALL_ORB_COLORLESS, LARGE_ORB_COLORLESS);
        }
    }

    public boolean isTechnique() {
        return this.isTechnique;
    }

    public void upgradeToSpell() {
        this.isSpell = true;
    }

    public void upgradeToTechnique() {
        this.isTechnique = true;
    }
}
