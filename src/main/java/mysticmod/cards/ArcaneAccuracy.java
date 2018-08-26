package mysticmod.cards;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import mysticmod.patches.AbstractCardEnum;
import mysticmod.powers.ArcaneAccuracyPower;

public class ArcaneAccuracy
        extends AbstractMysticCard {
    public static final String ID = "mysticmod:ArcaneAccuracy";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "mysticmod/images/cards/arcaneaccuracy.png";
    private static final int COST = 1;

    public ArcaneAccuracy() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                AbstractCard.CardType.POWER, AbstractCardEnum.MYSTIC_PURPLE,
                AbstractCard.CardRarity.UNCOMMON, AbstractCard.CardTarget.SELF);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new ArcaneAccuracyPower(p, 1), 1));
    }

    @Override
    public AbstractCard makeCopy() {
        return new ArcaneAccuracy();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.isInnate = true;
            this.rawDescription = UPGRADE_DESCRIPTION;
            this.initializeDescription();
        }
    }
}