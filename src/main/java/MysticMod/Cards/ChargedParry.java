package MysticMod.Cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.powers.LoseStrengthPower;
import MysticMod.Patches.AbstractCardEnum;
import MysticMod.Powers.SpellsPlayed;
import MysticMod.Powers.TechniquesPlayed;

import basemod.abstracts.CustomCard;

public class ChargedParry
        extends CustomCard {
    public static final String ID = "MysticMod:ChargedParry";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "MysticMod/images/cards/chargedparry.png";
    private static final int COST = 1;
    public static final int BLOCK_AMT = 6;
    public static final int STRENGTH_GAIN = 4;
    private static final int UPGRADE_STRENGTH_GAIN = 2;

    public ChargedParry() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                AbstractCard.CardType.SKILL, AbstractCardEnum.MYSTIC_PURPLE,
                AbstractCard.CardRarity.UNCOMMON, AbstractCard.CardTarget.SELF);
        this.block = this.baseBlock = BLOCK_AMT;
        this.magicNumber = this.baseMagicNumber = STRENGTH_GAIN;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new com.megacrit.cardcrawl.actions.common.GainBlockAction(p, p, this.block));
        if ((p.hasPower(SpellsPlayed.POWER_ID)) && (p.getPower(SpellsPlayed.POWER_ID).amount >= 1)) {
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, AbstractDungeon.player, new StrengthPower(p, this.magicNumber), this.magicNumber));
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, AbstractDungeon.player, new LoseStrengthPower(p, this.magicNumber), this.magicNumber));
         }
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new TechniquesPlayed(p, 1), 1));
    }

    @Override
    public AbstractCard makeCopy() {
        return new ChargedParry();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeMagicNumber(UPGRADE_STRENGTH_GAIN);
        }
    }
}