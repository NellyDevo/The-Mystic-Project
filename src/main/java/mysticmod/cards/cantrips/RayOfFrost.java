package mysticmod.cards.cantrips;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import mysticmod.powers.SpellsPlayed;
import mysticmod.relics.BentSpoon;

import basemod.abstracts.CustomCard;

public class RayOfFrost
        extends CustomCard {
    public static final String ID = "mysticmod:RayOfFrost";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "mysticmod/images/cards/rayoffrost.png";
    private static final int COST = 0;
    private static final int ATTACK_DMG = 2;
    private static final int UPGRADE_PLUS_DMG = 1;
    private static final int BLOCK_AMT = 1;
    private static final int UPGRADE_PLUS_BLK = 1;

    public RayOfFrost() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                AbstractCard.CardType.ATTACK, AbstractCard.CardColor.COLORLESS,
                AbstractCard.CardRarity.SPECIAL, AbstractCard.CardTarget.ENEMY);
        this.damage=this.baseDamage = ATTACK_DMG;
        this.block = this.baseBlock = BLOCK_AMT;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(
                new com.megacrit.cardcrawl.actions.common.DamageAction(
                        m, new DamageInfo(p, this.damage, this.damageTypeForTurn)
                        , AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
        AbstractDungeon.actionManager.addToBottom(new com.megacrit.cardcrawl.actions.common.GainBlockAction(p, p, this.block));
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
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new RayOfFrost();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeDamage(UPGRADE_PLUS_DMG);
            this.upgradeBlock(UPGRADE_PLUS_BLK);
        }
    }
}