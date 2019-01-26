package mysticmod.cards;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import mysticmod.patches.AbstractCardEnum;

public class TestCard extends AbstractMysticCard {
    public static final String ID = "mysticmod:TestCard";
    public static final String NAME = "Test Card";
    public static final String DESCRIPTION = "For Testing Things";
    public static final String UPGRADE_DESCRIPTION = "Still for Testing Things";
    public static final String IMG_PATH = "mysticmod/images/cards/defend.png";
    private static final int COST = 1;

    public TestCard() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.MYSTIC_PURPLE,
                CardRarity.BASIC, CardTarget.SELF);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
    }

    @Override
    public AbstractCard makeCopy() {
        return new TestCard();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
        }
    }
}
