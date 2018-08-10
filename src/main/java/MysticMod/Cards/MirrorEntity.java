package MysticMod.Cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import MysticMod.Patches.AbstractCardEnum;
import MysticMod.Powers.MirrorEntityPower;

import basemod.abstracts.CustomCard;

public class MirrorEntity
        extends CustomCard {
    public static final String ID = "MysticMod:MirrorEntity";
    public static final String NAME = "Mirror Entity";
    public static final String DESCRIPTION = "Spells and Techniques also advance next turn's Spells and Techniques counter.";
    public static final String IMG_PATH = "MysticMod/images/cards/mirrorentity.png";
    private static final int COST = 3;
    public static final int UPGRADE_COST = 2;

    public MirrorEntity() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                AbstractCard.CardType.POWER, AbstractCardEnum.MYSTIC_PURPLE,
                AbstractCard.CardRarity.RARE, AbstractCard.CardTarget.SELF);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {

        AbstractDungeon.actionManager.addToBottom(new com.megacrit.cardcrawl.actions.common.ApplyPowerAction(p, p, new MirrorEntityPower(p)));

    }

    @Override
    public AbstractCard makeCopy() {
        return new MirrorEntity();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeBaseCost(UPGRADE_COST);
        }
    }
}