package mysticmod.cards;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import mysticmod.patches.AbstractCardEnum;
import mysticmod.powers.TechniquesPlayed;

public class Disengage
        extends AbstractMysticCard {
    public static final String ID = "mysticmod:Disengage";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "mysticmod/images/cards/disengage.png";
    private static final int COST = 1;
    private static final int BLOCK_AMT = 7;
    private static final int UPGRADE_EXTRA_BLK = 3;

    public Disengage() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                AbstractCard.CardType.SKILL, AbstractCardEnum.MYSTIC_PURPLE,
                AbstractCard.CardRarity.COMMON, AbstractCard.CardTarget.SELF);
        this.block = this.baseBlock = BLOCK_AMT;
        this.isTechnique = true;
        this.setBackgroundTexture(BG_SMALL_ARTE_SKILL_MYSTIC, BG_LARGE_ARTE_SKILL_MYSTIC);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new GainBlockAction(p, p, this.block));
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new TechniquesPlayed(p, 1), 1));
    }

    @Override
    public AbstractCard makeCopy() {
        return new Disengage();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeBlock(UPGRADE_EXTRA_BLK);
        }
    }
}