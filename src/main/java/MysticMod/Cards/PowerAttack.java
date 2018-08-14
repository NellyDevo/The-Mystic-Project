package MysticMod.Cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.powers.LoseStrengthPower;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import MysticMod.Patches.AbstractCardEnum;
import MysticMod.Powers.TechniquesPlayed;
import MysticMod.Powers.GainDexterityPower;

import basemod.abstracts.CustomCard;

public class PowerAttack
        extends CustomCard {
    public static final String ID = "MysticMod:PowerAttack";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "MysticMod/images/cards/powerattack.png";
    private static final int COST = 0;
    private static final int DEXTERITY_LOSS = 4;
    private static final int UPGRADE_MINUS_DEX_LOSS = 2;
    private static final int STRENGTH_GAIN = 6;

    public PowerAttack() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                AbstractCard.CardType.SKILL, AbstractCardEnum.MYSTIC_PURPLE,
                AbstractCard.CardRarity.UNCOMMON, AbstractCard.CardTarget.SELF);
        this.magicNumber = this.baseMagicNumber = DEXTERITY_LOSS;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new DexterityPower(p, -this.magicNumber), -this.magicNumber, true, AbstractGameAction.AttackEffect.NONE));
        if (!p.hasPower("Artifact")) {
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new GainDexterityPower(p, this.magicNumber), this.magicNumber, true, AbstractGameAction.AttackEffect.NONE));
        }
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new StrengthPower(p, 6), 6));
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new LoseStrengthPower(p, 6), 6));
        //technique functionality
        AbstractDungeon.actionManager.addToBottom(new com.megacrit.cardcrawl.actions.common.ApplyPowerAction(p, p, new TechniquesPlayed(p, 1), 1));
    }

    @Override
    public boolean hasEnoughEnergy() {
        boolean returnValue = super.hasEnoughEnergy();
        if (AbstractDungeon.actionManager.cardsPlayedThisTurn.size() > 0) {
            returnValue = false;
            return returnValue;
        }
        return returnValue;
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
        }
    }
}