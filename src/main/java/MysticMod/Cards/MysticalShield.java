package MysticMod.Cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import MysticMod.Patches.AbstractCardEnum;
import MysticMod.Powers.MysticalShieldPower;
import MysticMod.Powers.MysticalShieldUpgradedPower;

import basemod.abstracts.CustomCard;

public class MysticalShield
        extends CustomCard {
    public static final String ID = "MysticMod:MysticalShield";
    public static final String NAME = "Mystical Shield";
    public static final String DESCRIPTION = "Block is reduced to 10 instead of 0 at the start of your turn.";
    public static final String UPGRADE_DESCRIPTION = "Block is set to 10 instead of 0 at the start of your turn.";
    public static final String IMG_PATH = "MysticMod/images/cards/mysticalshield.png";
    private static final int COST = 2;

    public MysticalShield() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                AbstractCard.CardType.POWER, AbstractCardEnum.MYSTIC_PURPLE,
                AbstractCard.CardRarity.RARE, AbstractCard.CardTarget.SELF);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        //to-do if card is not upgraded
        if (!this.upgraded) {
            //check if MysticalShieldUpgradedPower is not applied, then apply MysticalShieldPower
            if (!p.hasPower(MysticalShieldUpgradedPower.POWER_ID)) {
                AbstractDungeon.actionManager.addToBottom(new com.megacrit.cardcrawl.actions.common.ApplyPowerAction(p, p, new MysticalShieldPower(p)));
            }
        //to-do if card is upgraded
        } else {
            //check if MysticalShieldPower is applied, and if so, remove it.
            if (p.hasPower(MysticalShieldPower.POWER_ID)) {
                AbstractDungeon.actionManager.addToBottom(new com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction(p, p, MysticalShieldPower.POWER_ID));
            }
            //apply MysticalShieldUpgradedPower
            AbstractDungeon.actionManager.addToBottom(new com.megacrit.cardcrawl.actions.common.ApplyPowerAction(p, p, new MysticalShieldUpgradedPower(p)));
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new MysticalShield();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.rawDescription = UPGRADE_DESCRIPTION;
            this.initializeDescription();
        }
    }
}