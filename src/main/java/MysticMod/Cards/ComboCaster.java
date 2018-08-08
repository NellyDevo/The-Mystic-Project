package MysticMod.Cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import MysticMod.Patches.AbstractCardEnum;
import MysticMod.Powers.ComboCasterPower;

import basemod.abstracts.CustomCard;

public class ComboCaster
        extends CustomCard {
    public static final String ID = "MysticMod:ComboCaster";
    public static final String NAME = "Combo Caster";
    public static final String DESCRIPTION = "At the start of each turn, add a random Cantrip to your hand.";
    public static final String IMG_PATH = "MysticMod/images/cards/combocaster.png";
    private static final int COST = 2;
    public static final int UPGRADE_COST = 1;

    public ComboCaster() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                AbstractCard.CardType.POWER, AbstractCardEnum.MYSTIC_PURPLE,
                AbstractCard.CardRarity.UNCOMMON, AbstractCard.CardTarget.SELF);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {

        AbstractDungeon.actionManager.addToBottom(new com.megacrit.cardcrawl.actions.common.ApplyPowerAction(p, p, new ComboCasterPower(p, 1), 1));

    }

    @Override
    public AbstractCard makeCopy() {
        return new ComboCaster();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeBaseCost(UPGRADE_COST);
        }
    }
}