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

public class VorpalThrust
        extends CustomCard {
    public static final String ID = "mysticmod:VorpalThrust";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "mysticmod/images/cards/vorpalthrust.png";
    private static final int COST = 2;
    private static final int BASE_DMG = 12;
    public static final int ATTACK_DMG = 24;
    private static final int UPGRADE_PLUS_DMG = 6;

    public VorpalThrust() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                AbstractCard.CardType.ATTACK, AbstractCardEnum.MYSTIC_PURPLE,
                AbstractCard.CardRarity.UNCOMMON, AbstractCard.CardTarget.ENEMY);
        this.damage=this.baseDamage = BASE_DMG;
        this.magicNumber = this.baseMagicNumber = ATTACK_DMG;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if ((p.hasPower(SpellsPlayed.POWER_ID)) && (p.getPower(SpellsPlayed.POWER_ID).amount >= 2)) {
            AbstractDungeon.actionManager.addToBottom(
                    new com.megacrit.cardcrawl.actions.common.DamageAction(
                            m, new DamageInfo(p, this.magicNumber, this.damageTypeForTurn)
                            , AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
        } else {
            AbstractDungeon.actionManager.addToBottom(
                    new com.megacrit.cardcrawl.actions.common.DamageAction(
                            m, new DamageInfo(p, this.damage, this.damageTypeForTurn)
                            , AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
        }
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new TechniquesPlayed(p, 1), 1));
    }

    @Override
    public void applyPowers() {
        int CURRENT_MAGIC_NUMBER = this.baseMagicNumber;
        int CURRENT_DMG = this.baseDamage;
        this.baseDamage = CURRENT_MAGIC_NUMBER;
        super.applyPowers(); // takes this.baseDamage and applies things like Strength or Pen Nib to set this.damage

        this.magicNumber = this.damage; // magic number holds the first condition's modified damage, so !M! will work
        this.isMagicNumberModified = this.magicNumber != this.baseMagicNumber;

        // repeat so this.damage holds the second condition's damage
        this.baseDamage = CURRENT_DMG;
        super.applyPowers();
    }

    @Override
    public void calculateCardDamage(final AbstractMonster mo) {
        int CURRENT_MAGIC_NUMBER = this.baseMagicNumber;
        int CURRENT_DMG = this.baseDamage;
        this.baseDamage = CURRENT_MAGIC_NUMBER;
        super.calculateCardDamage(mo); // takes this.baseDamage and applies things like Strength or Pen Nib to set this.damage

        this.magicNumber = this.damage; // magic number holds the first condition's modified damage, so !M! will work
        this.isMagicNumberModified = this.magicNumber != this.baseMagicNumber;

        // repeat so this.damage holds the second condition's damage
        this.baseDamage = CURRENT_DMG;
        super.calculateCardDamage(mo);
    }

    @Override
    public AbstractCard makeCopy() {
        return new VorpalThrust();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeMagicNumber(UPGRADE_PLUS_DMG);
        }
    }
}