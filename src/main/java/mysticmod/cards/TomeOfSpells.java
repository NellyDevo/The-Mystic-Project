package mysticmod.cards;

import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import mysticmod.MysticMod;
import mysticmod.patches.AbstractCardEnum;

public class TomeOfSpells extends AbstractMysticCard {
    public static final String ID = "mysticmod:TomeOfSpells";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "mysticmod/images/cards/tomeofspells.png";
    private static final int COST = 1;
    public static final int CANTRIP_AMT = 2;
    private static final int UPGRADE_PLUS_CANTRIP = 1;

    public TomeOfSpells() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                AbstractCard.CardType.SKILL, AbstractCardEnum.MYSTIC_PURPLE,
                AbstractCard.CardRarity.COMMON, AbstractCard.CardTarget.SELF);
        magicNumber = baseMagicNumber = CANTRIP_AMT;
        exhaust = true;
    }
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        for (int i = 0; i < magicNumber; i++) {
            AbstractCard randomCantrip = MysticMod.cantripsGroup.get(AbstractDungeon.cardRandomRng.random(MysticMod.cantripsGroup.size()-1)).makeCopy();
            AbstractDungeon.actionManager.addToBottom(new MakeTempCardInDrawPileAction(randomCantrip, 1, true, true));
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new TomeOfSpells();
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_PLUS_CANTRIP);
        }
    }
}
