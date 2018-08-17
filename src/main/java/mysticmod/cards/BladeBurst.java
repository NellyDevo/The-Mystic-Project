package mysticmod.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDiscardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.powers.StrengthPower;
import mysticmod.patches.AbstractCardEnum;
import mysticmod.powers.TechniquesPlayed;

import basemod.abstracts.CustomCard;

public class BladeBurst
        extends CustomCard {
    public static final String ID = "mysticmod:BladeBurst";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "mysticmod/images/cards/bladeburst.png";
    private static final int COST = 1;
    public static final int ATTACK_DMG = 12;
    private static final int UPGRADE_PLUS_DMG = 4;
    private static final int STRENGTH_LOSS = 1;

    public BladeBurst() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                AbstractCard.CardType.ATTACK, AbstractCardEnum.MYSTIC_PURPLE,
                AbstractCard.CardRarity.UNCOMMON, AbstractCard.CardTarget.ENEMY);
        this.damage=this.baseDamage = ATTACK_DMG;
        this.magicNumber = this.baseMagicNumber = STRENGTH_LOSS;
        this.exhaust = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(
                new com.megacrit.cardcrawl.actions.common.DamageAction(
                        m, new DamageInfo(p, this.damage, this.damageTypeForTurn)
                        , AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new StrengthPower(p, -this.magicNumber), -this.magicNumber));
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new TechniquesPlayed(p, 1), 1));
        AbstractCard newMagicWeapon = new MagicWeapon();
        if (this.upgraded) {
            newMagicWeapon.upgrade();
        }
        AbstractDungeon.actionManager.addToBottom(new MakeTempCardInDiscardAction(newMagicWeapon.makeStatEquivalentCopy(), 1));
    }

    @Override
    public AbstractCard makeCopy() {
        return new BladeBurst();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeDamage(UPGRADE_PLUS_DMG);
            this.rawDescription = UPGRADE_DESCRIPTION;
            this.initializeDescription();
        }
    }
}