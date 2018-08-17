package mysticmod.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import mysticmod.patches.AbstractCardEnum;
import mysticmod.powers.SpellsPlayed;
import mysticmod.powers.TechniquesPlayed;

import basemod.abstracts.CustomCard;

public class Flurry
        extends CustomCard {
    public static final String ID = "mysticmod:Flurry";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "mysticmod/images/cards/flurry.png";
    private static final int COST = 1;
    public static final int ATTACK_DMG = 2;
    private static final int ATTACK_COUNT = 3;
    private static final int ALTERNATIVE_ATTACK_COUNT = 5;
    private static final int UPGRADE_ATTACK_COUNT = 1;

    public Flurry() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                AbstractCard.CardType.ATTACK, AbstractCardEnum.MYSTIC_PURPLE,
                AbstractCard.CardRarity.UNCOMMON, AbstractCard.CardTarget.ENEMY);
        this.damage=this.baseDamage = ATTACK_DMG;
        this.block = this.baseBlock = ALTERNATIVE_ATTACK_COUNT;
        this.magicNumber = this.baseMagicNumber = ATTACK_COUNT;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (p.hasPower(SpellsPlayed.POWER_ID) && p.getPower(SpellsPlayed.POWER_ID).amount >= 1) {
            for (int i = 0; i < this.block; i++) {
                AbstractDungeon.actionManager.addToBottom(
                        new com.megacrit.cardcrawl.actions.common.DamageAction(
                                m, new DamageInfo(p, this.damage, this.damageTypeForTurn)
                                , AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
            }
        } else {
            for (int i = 0; i < this.magicNumber; i++) {
                AbstractDungeon.actionManager.addToBottom(
                        new com.megacrit.cardcrawl.actions.common.DamageAction(
                                m, new DamageInfo(p, this.damage, this.damageTypeForTurn)
                                , AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
            }
        }
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new TechniquesPlayed(p, 1), 1));
    }

    @Override
    public void applyPowers() {
        super.applyPowers();
        this.block = this.baseBlock;
        this.isBlockModified = false;
    }

    @Override
    public void calculateCardDamage(final AbstractMonster mo) {
        super.calculateCardDamage(mo);
        this.block = this.baseBlock;
        this.isBlockModified = false;
    }

    @Override
    public AbstractCard makeCopy() {
        return new Flurry();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeBlock(UPGRADE_ATTACK_COUNT);
            this.upgradeMagicNumber(UPGRADE_ATTACK_COUNT);
        }
    }
}