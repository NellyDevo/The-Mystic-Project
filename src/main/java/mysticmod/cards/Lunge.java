package mysticmod.cards;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import mysticmod.patches.AbstractCardEnum;
import mysticmod.powers.TechniquesPlayed;
import mysticmod.powers.GainDexterityPower;

import basemod.abstracts.CustomCard;

public class Lunge
        extends CustomCard {
    public static final String ID = "mysticmod:Lunge";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "mysticmod/images/cards/lunge.png";
    private static final int COST = 0;
    private static final int ENERGY_GAIN = 2;
    private static final int PLUS_UPGRADE_ENERGY_GAIN = 1;

    public Lunge() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                AbstractCard.CardType.SKILL, AbstractCardEnum.MYSTIC_PURPLE,
                AbstractCard.CardRarity.RARE, AbstractCard.CardTarget.SELF);
        this.exhaust = true;
        this.magicNumber = this.baseMagicNumber = ENERGY_GAIN;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        //cards and energy
        AbstractDungeon.actionManager.addToBottom(new com.megacrit.cardcrawl.actions.common.GainEnergyAction(this.magicNumber));
        AbstractDungeon.actionManager.addToBottom(new com.megacrit.cardcrawl.actions.common.DrawCardAction(p, 2));
        //lose dexterity for turn
        AbstractDungeon.actionManager.addToBottom(new com.megacrit.cardcrawl.actions.common.ApplyPowerAction(p, p, new DexterityPower(p, -2), -2));
        if (!p.hasPower("Artifact")) {
            AbstractDungeon.actionManager.addToBottom(new com.megacrit.cardcrawl.actions.common.ApplyPowerAction(p, p, new GainDexterityPower(p, 2), 2));
        }
        //Technique functionality
        AbstractDungeon.actionManager.addToBottom(new com.megacrit.cardcrawl.actions.common.ApplyPowerAction(p, p, new TechniquesPlayed(p, 1), 1));
    }

    @Override
    public AbstractCard makeCopy() {
        return new Lunge();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeMagicNumber(PLUS_UPGRADE_ENERGY_GAIN);
            this.rawDescription = UPGRADE_DESCRIPTION;
            this.initializeDescription();
        }
    }
}