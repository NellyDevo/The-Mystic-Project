package mysticmod.cards.cantrips;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import mysticmod.cards.AbstractMysticCard;
import mysticmod.patches.AbstractCardEnum;
import mysticmod.powers.SpellsPlayed;
import mysticmod.relics.BentSpoon;

public class AcidSplash
        extends AbstractMysticCard {
    public static final String ID = "mysticmod:AcidSplash";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "mysticmod/images/cards/acidsplash.png";
    private static final int COST = 0;
    private static final int ATTACK_DMG = 3;
    private static final int UPGRADE_PLUS_DMG = 2;
    public boolean bgChanged = false;

    public AcidSplash() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                AbstractCard.CardType.ATTACK, AbstractCardEnum.MYSTIC_PURPLE,
                AbstractCard.CardRarity.SPECIAL, AbstractCard.CardTarget.ENEMY);
        this.damage=this.baseDamage = ATTACK_DMG;
        this.changeColor(BG_SMALL_SPELL_ATTACK_COLORLESS, BG_LARGE_SPELL_ATTACK_COLORLESS, true);
        crystalBallToggle = false;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new DamageAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn), AbstractGameAction.AttackEffect.POISON));
        AbstractDungeon.actionManager.addToBottom(new com.megacrit.cardcrawl.actions.common.DrawCardAction(p, 1));
        //cantrip functionality
        if (
                !(p.hasPower(SpellsPlayed.POWER_ID))
                ||
                (p.hasPower(SpellsPlayed.POWER_ID) && (p.getPower(SpellsPlayed.POWER_ID).amount == 1))
        ) {
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new SpellsPlayed(p, 1), 1));
        }
    }

    @Override
    public boolean isSpell() {
        if (AbstractDungeon.player == null || (!AbstractDungeon.player.hasPower(SpellsPlayed.POWER_ID) || AbstractDungeon.player.getPower(SpellsPlayed.POWER_ID).amount == 1)) {
            if (bgChanged) {
                this.setBackgroundTexture(BG_SMALL_SPELL_ATTACK_COLORLESS, BG_LARGE_SPELL_ATTACK_COLORLESS);
                bgChanged = false;
                crystalBallToggle = false;
            }
            return true;
        }
        return super.isSpell();
    }

    @Override
    public void applyPowers() {
        if (AbstractDungeon.player.hasRelic(BentSpoon.ID)) {
            int baseDamagePlaceholder = this.baseDamage;
            int baseBlockPlaceholder = this.baseBlock;
            this.baseDamage += 1;
            this.baseBlock += 1;
            super.applyPowers();
            this.baseDamage = baseDamagePlaceholder;
            this.baseBlock = baseBlockPlaceholder;
            if (this.damage != this.baseDamage) {
                this.isDamageModified = true;
            }
            if (this.block != this.baseBlock) {
                this.isBlockModified = true;
            }
        } else {
            super.applyPowers();
        }
        if (!this.isSpell()) {
            if (!bgChanged) {
                this.setBackgroundTexture(BG_SMALL_DEFAULT_ATTACK_COLORLESS, BG_LARGE_DEFAULT_ATTACK_COLORLESS);
                crystalBallToggle = false;
                bgChanged = true;
            }
        }
    }

    @Override
    public void calculateCardDamage(final AbstractMonster mo) {
        if (AbstractDungeon.player.hasRelic(BentSpoon.ID)) {
            int baseDamagePlaceholder = this.baseDamage;
            int baseBlockPlaceholder = this.baseBlock;
            this.baseDamage += 1;
            this.baseBlock += 1;
            super.calculateCardDamage(mo);
            this.baseDamage = baseDamagePlaceholder;
            this.baseBlock = baseBlockPlaceholder;
            if (this.damage != this.baseDamage) {
                this.isDamageModified = true;
            }
            if (this.block != this.baseBlock) {
                this.isBlockModified = true;
            }
        } else {
            super.calculateCardDamage(mo);
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new AcidSplash();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeDamage(UPGRADE_PLUS_DMG);
        }
    }
}