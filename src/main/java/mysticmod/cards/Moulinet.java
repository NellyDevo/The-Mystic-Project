package mysticmod.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import mysticmod.patches.AbstractCardEnum;
import mysticmod.powers.TechniquesPlayed;

import basemod.abstracts.CustomCard;

public class Moulinet
        extends CustomCard {
    public static final String ID = "mysticmod:Moulinet";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "mysticmod/images/cards/moulinet.png";
    private static final int COST = 1;
    public static final int ATTACK_DMG = 7;
    private static final int UPGRADE_PLUS_DMG = 3;

    public Moulinet() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                AbstractCard.CardType.ATTACK, AbstractCardEnum.MYSTIC_PURPLE,
                AbstractCard.CardRarity.COMMON, AbstractCard.CardTarget.ALL_ENEMY);
        this.damage=this.baseDamage = ATTACK_DMG;
        this.isMultiDamage = true;
    }
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {

            AbstractDungeon.actionManager.addToBottom(
                    new com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction(
                            p, this.multiDamage, this.damageTypeForTurn, AbstractGameAction.AttackEffect.SLASH_DIAGONAL));

        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new TechniquesPlayed(p, 1), 1));
    }

    @Override
    public AbstractCard makeCopy() {
        return new Moulinet();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeDamage(UPGRADE_PLUS_DMG);
        }
    }
}