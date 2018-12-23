package mysticmod.cards;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.DexterityPower;
import mysticmod.patches.AbstractCardEnum;
import mysticmod.patches.MysticTags;
import mysticmod.powers.GainDexterityPower;

public class Lunge extends AbstractMysticCard {
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
        this.tags.add(MysticTags.IS_ARTE);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        //cards and energy
        AbstractDungeon.actionManager.addToBottom(new GainEnergyAction(this.magicNumber));
        AbstractDungeon.actionManager.addToBottom(new DrawCardAction(p, 2));
        //lose dexterity for turn
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new DexterityPower(p, -4), -4));
        if (!p.hasPower("Artifact")) {
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new GainDexterityPower(p, 4), 4));
        }
    }

    @Override
    public boolean hasEnoughEnergy() { //feat keyword functionality
        boolean returnValue = super.hasEnoughEnergy();
        if (AbstractDungeon.actionManager.cardsPlayedThisTurn.size() > 0) {
            returnValue = false;
        }
        return returnValue;
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
