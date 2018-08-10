package MysticMod.Cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import MysticMod.Patches.AbstractCardEnum;
import MysticMod.Powers.GeminiFormPower;

import basemod.abstracts.CustomCard;

public class GeminiForm
        extends CustomCard {
    public static final String ID = "MysticMod:GeminiForm";
    public static final String NAME = "Gemini Form";
    public static final String DESCRIPTION = "The first skill or attack you play each turn counts as both a Spell and a Technique.";
    public static final String IMG_PATH = "MysticMod/images/cards/geminiform.png";
    private static final int COST = 3;
    public static final int UPGRADE_COST = 2;

    public GeminiForm() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                AbstractCard.CardType.POWER, AbstractCardEnum.MYSTIC_PURPLE,
                AbstractCard.CardRarity.RARE, AbstractCard.CardTarget.SELF);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {

        AbstractDungeon.actionManager.addToBottom(new com.megacrit.cardcrawl.actions.common.ApplyPowerAction(p, p, new GeminiFormPower(p, 1), 1));

    }

    @Override
    public AbstractCard makeCopy() {
        return new GeminiForm();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeBaseCost(UPGRADE_COST);
        }
    }
}