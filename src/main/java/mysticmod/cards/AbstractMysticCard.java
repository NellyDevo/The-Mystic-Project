package mysticmod.cards;

import basemod.abstracts.CustomCard;
import basemod.abstracts.DynamicVariable;
import basemod.helpers.TooltipInfo;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import mysticmod.MysticMod;
import mysticmod.relics.CrystalBall;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractMysticCard extends CustomCard {
    public boolean isSpell = false;
    public boolean isArte = false;
    public boolean crystalBallToggle = false;
    public int baseSecondMagicNumber;
    public int secondMagicNumber;
    public boolean isSecondMagicNumberModified;
    public boolean upgradedSecondMagicNumber;
    public static String BG_SMALL_SPELL_ATTACK_MYSTIC = "mysticmod/images/512/bg_spell_attack_mystic" + arteSpellSettings();
    public static String BG_LARGE_SPELL_ATTACK_MYSTIC = "mysticmod/images/1024/bg_spell_attack_mystic" + arteSpellSettings();
    public static String BG_SMALL_ARTE_ATTACK_MYSTIC = "mysticmod/images/512/bg_arte_attack_mystic" + arteSpellSettings();
    public static String BG_LARGE_ARTE_ATTACK_MYSTIC = "mysticmod/images/1024/bg_arte_attack_mystic" + arteSpellSettings();
    public static String BG_SMALL_SPELL_SKILL_MYSTIC = "mysticmod/images/512/bg_spell_skill_mystic" + arteSpellSettings();
    public static String BG_LARGE_SPELL_SKILL_MYSTIC = "mysticmod/images/1024/bg_spell_skill_mystic" + arteSpellSettings();
    public static String BG_SMALL_ARTE_SKILL_MYSTIC = "mysticmod/images/512/bg_arte_skill_mystic" + arteSpellSettings();
    public static String BG_LARGE_ARTE_SKILL_MYSTIC = "mysticmod/images/1024/bg_arte_skill_mystic" + arteSpellSettings();
    public static String BG_SMALL_DEFAULT_SKILL_MYSTIC = "mysticmod/images/512/bg_skill_mystic.png";
    public static String BG_LARGE_DEFAULT_SKILL_MYSTIC = "mysticmod/images/1024/bg_skill_mystic.png";
    public static String BG_SMALL_SPELL_ATTACK_COLORLESS = "mysticmod/images/512/bg_spell_attack_colorless" + arteSpellSettings();
    public static String BG_LARGE_SPELL_ATTACK_COLORLESS = "mysticmod/images/1024/bg_spell_attack_colorless" + arteSpellSettings();
    public static String BG_SMALL_ARTE_ATTACK_COLORLESS = "mysticmod/images/512/bg_arte_attack_colorless" + arteSpellSettings();
    public static String BG_LARGE_ARTE_ATTACK_COLORLESS = "mysticmod/images/1024/bg_arte_attack_colorless" + arteSpellSettings();
    public static String BG_SMALL_DEFAULT_ATTACK_COLORLESS = "mysticmod/images/512/bg_attack_colorless.png";
    public static String BG_LARGE_DEFAULT_ATTACK_COLORLESS = "mysticmod/images/1024/bg_attack_colorless.png";
    public static String BG_SMALL_SPELL_SKILL_COLORLESS = "mysticmod/images/512/bg_spell_skill_colorless" + arteSpellSettings();
    public static String BG_LARGE_SPELL_SKILL_COLORLESS = "mysticmod/images/1024/bg_spell_skill_colorless" + arteSpellSettings();
    public static String BG_SMALL_DEFAULT_SKILL_COLORLESS = "mysticmod/images/512/bg_skill_colorless.png";
    public static String BG_LARGE_DEFAULT_SKILL_COLORLESS = "mysticmod/images/1024/bg_skill_colorless.png";
    public static String SMALL_ORB_COLORLESS = "mysticmod/images/512/card_colorless_orb_copy.png";
    public static String LARGE_ORB_COLORLESS = "mysticmod/images/1024/card_colorless_orb_copy.png";
    public static String BG_ADDON_SMALL_ARTE_ATTACK = "mysticmod/images/512/bg_arte_attack_addon" + arteSpellSettings();
    public static String BG_ADDON_LARGE_ARTE_ATTACK = "mysticmod/images/1024/bg_arte_attack_addon" + arteSpellSettings();
    public static String BG_ADDON_SMALL_SPELL_SKILL = "mysticmod/images/512/bg_spell_skill_addon" + arteSpellSettings();
    public static String BG_ADDON_LARGE_SPELL_SKILL = "mysticmod/images/1024/bg_spell_skill_addon" + arteSpellSettings();
    public static String BG_ADDON_SMALL_SPELL_ATTACK = "mysticmod/images/512/bg_spell_attack_addon" + arteSpellSettings();
    public static String BG_ADDON_LARGE_SPELL_ATTACK = "mysticmod/images/1024/bg_spell_attack_addon" + arteSpellSettings();

    public AbstractMysticCard(final String id, final String name, final String img, final int cost, final String rawDescription,
                              final AbstractCard.CardType type, final AbstractCard.CardColor color,
                              final AbstractCard.CardRarity rarity, final AbstractCard.CardTarget target) {
        super(id, name, img, cost, rawDescription, type, color, rarity, target);
    }

    public boolean isSpell() {
        if (this.type == AbstractCard.CardType.SKILL && (AbstractDungeon.player != null && AbstractDungeon.player.hasRelic(CrystalBall.ID)) && !this.crystalBallToggle && !this.isArte && !this.isSpell) {
            this.setBackgroundTexture(BG_SMALL_SPELL_SKILL_MYSTIC, BG_LARGE_SPELL_SKILL_MYSTIC);
            this.crystalBallToggle = true;
        }
        return this.isSpell;
    }

    public boolean isArte() {
        if (this.type == AbstractCard.CardType.ATTACK && (AbstractDungeon.player != null && AbstractDungeon.player.hasRelic(CrystalBall.ID)) && !this.crystalBallToggle && !this.isArte && !this.isSpell) {
            this.setBackgroundTexture(BG_SMALL_ARTE_ATTACK_MYSTIC, BG_LARGE_ARTE_ATTACK_MYSTIC);
            this.crystalBallToggle = true;
        }
        return this.isArte;
    }

    public static String arteSpellSettings() {
        switch(MysticMod.cardBackgroundSetting) {
            case COLOR : return "_color.png";
            case SHAPE : return "_shape.png";
            case BOTH : return "_combined.png";
            default : System.out.println("Why is there no setting set");
            return "_combined.png";
        }
    }

    @Override
    public void applyPowers() {
        if (this.type == AbstractCard.CardType.SKILL && (AbstractDungeon.player != null && AbstractDungeon.player.hasRelic(CrystalBall.ID)) && !this.crystalBallToggle && !this.isArte && !this.isSpell) {
            this.setBackgroundTexture(BG_SMALL_SPELL_SKILL_MYSTIC, BG_LARGE_SPELL_SKILL_MYSTIC);
            this.crystalBallToggle = true;
        } else if (this.type == AbstractCard.CardType.ATTACK && (AbstractDungeon.player != null && AbstractDungeon.player.hasRelic(CrystalBall.ID)) && !this.crystalBallToggle && !this.isArte && !this.isSpell) {
            this.setBackgroundTexture(BG_SMALL_ARTE_ATTACK_MYSTIC, BG_LARGE_ARTE_ATTACK_MYSTIC);
            this.crystalBallToggle = true;
        }
        super.applyPowers();
    }

    public static void resetImageStrings() {
        BG_SMALL_SPELL_ATTACK_MYSTIC = "mysticmod/images/512/bg_spell_attack_mystic" + arteSpellSettings();
        BG_LARGE_SPELL_ATTACK_MYSTIC = "mysticmod/images/1024/bg_spell_attack_mystic" + arteSpellSettings();
        BG_SMALL_ARTE_ATTACK_MYSTIC = "mysticmod/images/512/bg_arte_attack_mystic" + arteSpellSettings();
        BG_LARGE_ARTE_ATTACK_MYSTIC = "mysticmod/images/1024/bg_arte_attack_mystic" + arteSpellSettings();
        BG_SMALL_SPELL_SKILL_MYSTIC = "mysticmod/images/512/bg_spell_skill_mystic" + arteSpellSettings();
        BG_LARGE_SPELL_SKILL_MYSTIC = "mysticmod/images/1024/bg_spell_skill_mystic" + arteSpellSettings();
        BG_SMALL_ARTE_SKILL_MYSTIC = "mysticmod/images/512/bg_arte_skill_mystic" + arteSpellSettings();
        BG_LARGE_ARTE_SKILL_MYSTIC = "mysticmod/images/1024/bg_arte_skill_mystic" + arteSpellSettings();
        BG_SMALL_SPELL_ATTACK_COLORLESS = "mysticmod/images/512/bg_spell_attack_colorless" + arteSpellSettings();
        BG_LARGE_SPELL_ATTACK_COLORLESS = "mysticmod/images/1024/bg_spell_attack_colorless" + arteSpellSettings();
        BG_SMALL_ARTE_ATTACK_COLORLESS = "mysticmod/images/512/bg_arte_attack_colorless" + arteSpellSettings();
        BG_LARGE_ARTE_ATTACK_COLORLESS = "mysticmod/images/1024/bg_arte_attack_colorless" + arteSpellSettings();
        BG_SMALL_DEFAULT_ATTACK_COLORLESS = "mysticmod/images/512/bg_attack_colorless.png";
        BG_LARGE_DEFAULT_ATTACK_COLORLESS = "mysticmod/images/1024/bg_attack_colorless.png";
        BG_SMALL_SPELL_SKILL_COLORLESS = "mysticmod/images/512/bg_spell_skill_colorless" + arteSpellSettings();
        BG_LARGE_SPELL_SKILL_COLORLESS = "mysticmod/images/1024/bg_spell_skill_colorless" + arteSpellSettings();
        BG_SMALL_DEFAULT_SKILL_COLORLESS = "mysticmod/images/512/bg_skill_colorless.png";
        BG_LARGE_DEFAULT_SKILL_COLORLESS = "mysticmod/images/1024/bg_skill_colorless.png";
        SMALL_ORB_COLORLESS = "mysticmod/images/512/card_colorless_orb_copy.png";
        LARGE_ORB_COLORLESS = "mysticmod/images/1024/card_colorless_orb_copy.png";
        BG_ADDON_SMALL_ARTE_ATTACK = "mysticmod/images/512/bg_arte_attack_addon" + arteSpellSettings();
        BG_ADDON_LARGE_ARTE_ATTACK = "mysticmod/images/1024/bg_arte_attack_addon" + arteSpellSettings();
        BG_ADDON_SMALL_SPELL_SKILL = "mysticmod/images/512/bg_spell_skill_addon" + arteSpellSettings();
        BG_ADDON_LARGE_SPELL_SKILL = "mysticmod/images/1024/bg_spell_skill_addon" + arteSpellSettings();
    }

    @Override
    public List<TooltipInfo> getCustomTooltips() {
        List<TooltipInfo> retVal = new ArrayList<>();
        if (this.type == AbstractCard.CardType.SKILL) {
            if (this.isSpell) {
                retVal.add(new TooltipInfo("Spell.", "This Skill is considered a [#5299DC]Spell[]."));
            } else if (this.isArte) {
                retVal.add(new TooltipInfo("Arte.", "This Skill is considered an [#FF5252]Arte[]."));
            }
        } else if (this.type == AbstractCard.CardType.ATTACK) {
            if (this.isSpell) {
                retVal.add(new TooltipInfo("Spell.", "This Attack is considered a [#5299DC]Spell[]."));
            } else if (this.isArte) {
                retVal.add(new TooltipInfo("Arte.", "This Attack is considered an [#FF5252]Arte[]."));
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

    public void upgradeToSpell() {
        this.isSpell = true;
    }

    public void upgradeToArte() {
        this.isArte = true;
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
            return "M2";
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