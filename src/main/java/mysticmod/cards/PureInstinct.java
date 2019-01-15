package mysticmod.cards;

import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.BiteEffect;
import mysticmod.MysticMod;
import mysticmod.patches.AbstractCardEnum;
import mysticmod.powers.PureInstinctPower;

public class PureInstinct extends AbstractMysticCard {
    public static final String ID = "mysticmod:PureInstinct";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String[] EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
    public static final String IMG_PATH = "mysticmod/images/cards/pureinstinct.png";
    private static final int COST = 1;
    public static final int BLOCK_AMT = 5;
    private static final int UPGRADE_PLUS_BLK = 2;
    private static final int POWER_AMT = 2;
    private static final int UPGRADE_POWER_AMT = 1;

    public PureInstinct() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                AbstractCard.CardType.SKILL, AbstractCardEnum.MYSTIC_PURPLE,
                AbstractCard.CardRarity.COMMON, AbstractCard.CardTarget.SELF);
        this.block = this.baseBlock = BLOCK_AMT;
        this.magicNumber=this.baseMagicNumber = POWER_AMT;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new VFXAction(new BiteEffect(p.hb.cX, p.hb.cY - 40.0f * Settings.scale, Settings.GREEN_TEXT_COLOR.cpy()), 0.3f));
        int ArtesCount = 0;
        for (final AbstractCard card : p.hand.group) {
            if (MysticMod.isThisAnArte(card)) {
                ArtesCount++;
            }
        }
        for (int i = 0; i <= ArtesCount; i++){
            AbstractDungeon.actionManager.addToBottom(new GainBlockAction(p, p, this.block));
        }
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new PureInstinctPower(p, this.magicNumber), this.magicNumber));
        this.rawDescription = DESCRIPTION;
        this.initializeDescription();
    }

    @Override
    public void applyPowers() {
        int ArtesCount = 0;
        for (final AbstractCard card : AbstractDungeon.player.hand.group) {
            if (MysticMod.isThisAnArte(card)){
                ArtesCount++;
            }
        }
        this.rawDescription = DESCRIPTION + EXTENDED_DESCRIPTION[0] + ArtesCount;
        if (ArtesCount == 1) {
            this.rawDescription += EXTENDED_DESCRIPTION[1];
        } else {
            this.rawDescription += EXTENDED_DESCRIPTION[2];
        }
        this.initializeDescription();
        super.applyPowers();
    }

    @Override
    public void onMoveToDiscard() {
        this.rawDescription = DESCRIPTION;
        this.initializeDescription();
    }

    @Override
    public AbstractCard makeCopy() {
        return new PureInstinct();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeBlock(UPGRADE_PLUS_BLK);
            this.upgradeMagicNumber(UPGRADE_POWER_AMT);
        }
    }
}
