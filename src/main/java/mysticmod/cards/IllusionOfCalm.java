package mysticmod.cards;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.FrailPower;
import mysticmod.patches.AbstractCardEnum;
import mysticmod.patches.MysticTags;
import mysticmod.powers.ArtesPlayed;

public class IllusionOfCalm extends AbstractMysticCard {
    public static final String ID = "mysticmod:IllusionOfCalm";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "mysticmod/images/cards/illusionofcalm.png";
    private static final int COST = 1;
    private static final int BLOCK_AMT = 12;
    private static final int UPGRADE_BLOCK_AMT = 4;
    private static final int FRAIL_AMT = 3;

    public IllusionOfCalm() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                AbstractCard.CardType.SKILL, AbstractCardEnum.MYSTIC_PURPLE,
                AbstractCard.CardRarity.UNCOMMON, AbstractCard.CardTarget.SELF);
        block = baseBlock = BLOCK_AMT;
        magicNumber = baseMagicNumber = FRAIL_AMT;
        tags.add(MysticTags.IS_SPELL);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new GainBlockAction(p, p, block));
        if (magicNumber > 0) {
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new FrailPower(p, magicNumber, false), magicNumber));
        }
    }

    @Override
    public void applyPowers() {
        super.applyPowers();
        magicNumber = baseMagicNumber;
        if (AbstractDungeon.player.hasPower(ArtesPlayed.POWER_ID)) {
            magicNumber -= AbstractDungeon.player.getPower(ArtesPlayed.POWER_ID).amount;
        }
        if (magicNumber < 0) {
            magicNumber = 0;
        }
        isMagicNumberModified = baseMagicNumber != magicNumber;
    }

    @Override
    public AbstractCard makeCopy() {
        return new IllusionOfCalm();
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeBlock(UPGRADE_BLOCK_AMT);
        }
    }
}
