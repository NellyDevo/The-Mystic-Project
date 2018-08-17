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
import com.megacrit.cardcrawl.powers.WeakPower;
import mysticmod.patches.AbstractCardEnum;
import mysticmod.powers.SpellsPlayed;
import mysticmod.powers.TechniquesPlayed;

import basemod.abstracts.CustomCard;

public class Sideswipe
        extends CustomCard {
    public static final String ID = "mysticmod:Sideswipe";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "mysticmod/images/cards/sideswipe.png";
    private static final int COST = 0;
    public static final int ATTACK_DMG = 5;
    private static final int WEAK_AMT = 1;
    private static final int UPGRADE_WEAK_AMT = 1;

    public Sideswipe() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                AbstractCard.CardType.ATTACK, AbstractCardEnum.MYSTIC_PURPLE,
                AbstractCard.CardRarity.COMMON, AbstractCard.CardTarget.ENEMY);
        this.damage=this.baseDamage = ATTACK_DMG;
        this.magicNumber = this.baseMagicNumber = WEAK_AMT;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(
                new com.megacrit.cardcrawl.actions.common.DamageAction(
                        m, new DamageInfo(p, this.damage, this.damageTypeForTurn)
                        , AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
        if (p.hasPower(SpellsPlayed.POWER_ID)) {
            AbstractDungeon.actionManager.addToBottom(
                    new com.megacrit.cardcrawl.actions.common.ApplyPowerAction(m, p, new WeakPower(m, this.magicNumber, false), this.magicNumber));
        }
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new TechniquesPlayed(p, 1), 1));
    }

    @Override
    public AbstractCard makeCopy() {
        return new Sideswipe();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeMagicNumber(UPGRADE_WEAK_AMT);
        }
    }
}