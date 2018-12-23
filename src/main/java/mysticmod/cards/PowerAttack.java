package mysticmod.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.LoseStrengthPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import mysticmod.patches.AbstractCardEnum;
import mysticmod.patches.MysticTags;
import mysticmod.powers.GainDexterityPower;

public class PowerAttack extends AbstractMysticCard {
    public static final String ID = "mysticmod:PowerAttack";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "mysticmod/images/cards/powerattack.png";
    private static final int COST = 0;
    private static final int DEXTERITY_LOSS = 2;
    private static final int UPGRADE_MINUS_DEX_LOSS = 1;
    private static final int STRENGTH_GAIN = 3;
    private static final int UPGRADE_PLUS_STR_GAIN = 1;

    public PowerAttack() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                AbstractCard.CardType.SKILL, AbstractCardEnum.MYSTIC_PURPLE,
                AbstractCard.CardRarity.UNCOMMON, AbstractCard.CardTarget.SELF);
        this.magicNumber = this.baseMagicNumber = DEXTERITY_LOSS;
        this.secondMagicNumber = this.baseSecondMagicNumber = STRENGTH_GAIN;
        this.tags.add(MysticTags.IS_ARTE);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new DexterityPower(p, -this.magicNumber), -this.magicNumber, true, AbstractGameAction.AttackEffect.NONE));
        if (!p.hasPower("Artifact")) {
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new GainDexterityPower(p, this.magicNumber), this.magicNumber, true, AbstractGameAction.AttackEffect.NONE));
        }
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new StrengthPower(p, this.secondMagicNumber), this.secondMagicNumber));
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new LoseStrengthPower(p, this.secondMagicNumber), this.secondMagicNumber));
    }

    @Override
    public boolean hasEnoughEnergy() { //feat keyword functionality
        if (AbstractDungeon.actionManager.cardsPlayedThisTurn.size() > 0) {
            return false;
        }
        return super.hasEnoughEnergy();
    }

    @Override
    public AbstractCard makeCopy() {
        return new PowerAttack();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeMagicNumber(-UPGRADE_MINUS_DEX_LOSS);
            this.upgradeSecondMagicNumber(UPGRADE_PLUS_STR_GAIN);
        }
    }
}
