package mysticmod.cards;

import basemod.abstracts.CustomCard;
import basemod.abstracts.DynamicVariable;
import basemod.helpers.TooltipInfo;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import mysticmod.MysticMod;
import mysticmod.patches.MysticTags;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractMysticCard extends CustomCard {
    public int baseSecondMagicNumber;
    public int secondMagicNumber;
    public boolean isSecondMagicNumberModified;
    public boolean upgradedSecondMagicNumber;
    public static String BG_ADDON_SMALL_ARTE_ATTACK = "mysticmod/images/512/bg_arte_attack_addon" + arteSpellSettings();
    public static String BG_ADDON_LARGE_ARTE_ATTACK = "mysticmod/images/1024/bg_arte_attack_addon" + arteSpellSettings();
    public static String BG_ADDON_SMALL_ARTE_SKILL = "mysticmod/images/512/bg_arte_skill_addon" + arteSpellSettings();
    public static String BG_ADDON_LARGE_ARTE_SKILL = "mysticmod/images/1024/bg_arte_skill_addon" + arteSpellSettings();
    public static String BG_ADDON_SMALL_SPELL_SKILL = "mysticmod/images/512/bg_spell_skill_addon" + arteSpellSettings();
    public static String BG_ADDON_LARGE_SPELL_SKILL = "mysticmod/images/1024/bg_spell_skill_addon" + arteSpellSettings();
    public static String BG_ADDON_SMALL_SPELL_ATTACK = "mysticmod/images/512/bg_spell_attack_addon" + arteSpellSettings();
    public static String BG_ADDON_LARGE_SPELL_ATTACK = "mysticmod/images/1024/bg_spell_attack_addon" + arteSpellSettings();
    public static String BG_ADDON_SMALL_SPERTE_ATTACK = "mysticmod/images/512/bg_sperte_attack_addon.png";
    public static String BG_ADDON_LARGE_SPERTE_ATTACK = "mysticmod/images/1024/bg_sperte_attack_addon.png";
    public static String BG_ADDON_SMALL_SPERTE_SKILL = "mysticmod/images/512/bg_sperte_skill_addon.png";
    public static String BG_ADDON_LARGE_SPERTE_SKILL = "mysticmod/images/1024/bg_sperte_skill_addon.png";
    public static CardStrings tooltip = CardCrawlGame.languagePack.getCardStrings("mysticmod:AbstractMysticCard");
    public static String[] tooltips = tooltip.EXTENDED_DESCRIPTION;

    public AbstractMysticCard(final String id, final String name, final String img, final int cost, final String rawDescription,
                              final AbstractCard.CardType type, final AbstractCard.CardColor color,
                              final AbstractCard.CardRarity rarity, final AbstractCard.CardTarget target) {
        super(id, name, img, cost, rawDescription, type, color, rarity, target);
    }

    public boolean isSpell() {
        return this.hasTag(MysticTags.IS_SPELL);
    }

    public boolean isArte() {
        return this.hasTag(MysticTags.IS_ARTE);
    }

    private static String arteSpellSettings() {
        switch(MysticMod.cardBackgroundSetting) {
            case COLOR : return "_color.png";
            case SHAPE : return "_shape.png";
            case BOTH : return "_combined.png";
            default : System.out.println("Why is there no setting set");
            return "_combined.png";
        }
    }

    public static void resetImageStrings() {
        BG_ADDON_SMALL_ARTE_ATTACK = "mysticmod/images/512/bg_arte_attack_addon" + arteSpellSettings();
        BG_ADDON_LARGE_ARTE_ATTACK = "mysticmod/images/1024/bg_arte_attack_addon" + arteSpellSettings();
        BG_ADDON_SMALL_ARTE_SKILL = "mysticmod/images/512/bg_arte_skill_addon" + arteSpellSettings();
        BG_ADDON_LARGE_ARTE_SKILL = "mysticmod/images/1024/bg_arte_skill_addon" + arteSpellSettings();
        BG_ADDON_SMALL_SPELL_SKILL = "mysticmod/images/512/bg_spell_skill_addon" + arteSpellSettings();
        BG_ADDON_LARGE_SPELL_SKILL = "mysticmod/images/1024/bg_spell_skill_addon" + arteSpellSettings();
        BG_ADDON_SMALL_SPELL_ATTACK = "mysticmod/images/512/bg_spell_attack_addon" + arteSpellSettings();
        BG_ADDON_LARGE_SPELL_ATTACK = "mysticmod/images/1024/bg_spell_attack_addon" + arteSpellSettings();
    }

    @Override
    public List<TooltipInfo> getCustomTooltips() {
        List<TooltipInfo> retVal = new ArrayList<>();
        if (this.type == AbstractCard.CardType.SKILL) {
            if (MysticMod.isThisASpell(this)) {
                retVal.add(new TooltipInfo(tooltips[0], tooltips[2]));
            }
            if (MysticMod.isThisAnArte(this)) {
                retVal.add(new TooltipInfo(tooltips[1], tooltips[3]));
            }
        } else if (this.type == AbstractCard.CardType.ATTACK) {
            if (MysticMod.isThisASpell(this)) {
                retVal.add(new TooltipInfo(tooltips[0], tooltips[4]));
            }
            if (MysticMod.isThisAnArte(this)) {
                retVal.add(new TooltipInfo(tooltips[1], tooltips[5]));
            }
        }
        return retVal;
    }

    public void upgradeToSpell() {
        this.tags.add(MysticTags.IS_SPELL);
    }

    public void upgradeToArte() {
        this.tags.add(MysticTags.IS_ARTE);
    }

    public void upgradeSecondMagicNumber(int amount) {
        this.baseSecondMagicNumber += amount;
        this.secondMagicNumber = this.baseSecondMagicNumber;
        this.upgradedSecondMagicNumber = true;
    }

    public static class SecondMagicNumber extends DynamicVariable {

        @Override
        public int baseValue(AbstractCard card) {
            return ((AbstractMysticCard)card).baseSecondMagicNumber;
        }

        @Override
        public boolean isModified(AbstractCard card) {
            return ((AbstractMysticCard)card).isSecondMagicNumberModified;
        }

        @Override
        public String key() {
            return "mysticmod:M2";
        }

        @Override
        public boolean upgraded(AbstractCard card) {
            return ((AbstractMysticCard)card).upgradedSecondMagicNumber;
        }

        @Override
        public int value(AbstractCard card) {
            return ((AbstractMysticCard)card).secondMagicNumber;
        }
    }
}
