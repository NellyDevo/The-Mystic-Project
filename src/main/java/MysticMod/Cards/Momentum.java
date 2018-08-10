package MysticMod.Cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import MysticMod.Patches.AbstractCardEnum;
import MysticMod.Powers.MomentumPower;

import basemod.abstracts.CustomCard;

public class Momentum
        extends CustomCard {
    public static final String ID = "MysticMod:Momentum";
    public static final String NAME = "Momentum";
    public static final String DESCRIPTION = "Spells and techniques deal 1 more damage and apply 1 more block for each card of the other type played this turn.";
    public static final String IMG_PATH = "MysticMod/images/cards/momentum.png";
    private static final int COST = 3;
    public static final int UPGRADE_COST = 2;

    public Momentum() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                AbstractCard.CardType.POWER, AbstractCardEnum.MYSTIC_PURPLE,
                AbstractCard.CardRarity.RARE, AbstractCard.CardTarget.SELF);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {

        AbstractDungeon.actionManager.addToBottom(new com.megacrit.cardcrawl.actions.common.ApplyPowerAction(p, p, new MomentumPower(p)));

    }

    @Override
    public AbstractCard makeCopy() {
        return new Momentum();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeBaseCost(UPGRADE_COST);
        }
    }
}