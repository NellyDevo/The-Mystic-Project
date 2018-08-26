package mysticmod.minions.foxfamiliar;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import mysticmod.cards.AbstractMysticCard;
import mysticmod.patches.AbstractCardEnum;

public class FoxSelectionCard extends AbstractMysticCard {
    public static final String ID = "mysticmod:FoxSelectionCard";
    public static final String NAME = "Familiar Move";
    public static final String ATTACK_IMG = "mysticmod/images/cards/foxminion/foxattack.png";
    public static final String FLANK_IMG = "mysticmod/images/cards/foxminion/foxflank.png";
    public static final String CHARGE_IMG = "mysticmod/images/cards/foxminion/foxcharge.png";

    public FoxSelectionCard() {
        super(ID, NAME, ATTACK_IMG, -2, "NONE", CardType.ATTACK, AbstractCardEnum.MYSTIC_PURPLE, CardRarity.SPECIAL, CardTarget.SELF_AND_ENEMY);
    }

    public void upgrade() {
    }

    public AbstractCard makeCopy() {
        return new FoxSelectionCard();
    }

    public void use(AbstractPlayer abstractPlayer, AbstractMonster abstractMonster) {
    }
}
