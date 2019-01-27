package mysticmod.cards.cantrips;

import com.megacrit.cardcrawl.actions.common.DiscardAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class ReadMagic extends AbstractCantrip {
    public static final String ID = "mysticmod:ReadMagic";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "mysticmod/images/cards/readmagic.png";
    private static final int COST = 0;
    private static final int DRAW = 2;
    private static final int UPGRADE_EXTRA_DRAW = 1;

    public ReadMagic() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                AbstractCard.CardType.SKILL, AbstractCard.CardColor.COLORLESS,
                AbstractCard.CardRarity.SPECIAL, AbstractCard.CardTarget.SELF);
        magicNumber = baseMagicNumber = DRAW;
        exhaust = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new DrawCardAction(p, magicNumber));
        AbstractDungeon.actionManager.addToBottom(new DiscardAction(p, p, magicNumber - 1, false));
    }

    @Override
    public AbstractCard makeCopy() {
        return new ReadMagic();
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_EXTRA_DRAW);
            rawDescription = UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }
}
