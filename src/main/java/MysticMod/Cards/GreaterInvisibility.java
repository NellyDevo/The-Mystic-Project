package MysticMod.Cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.IntangiblePlayerPower;
import MysticMod.Patches.AbstractCardEnum;
import MysticMod.Powers.TechniquesPlayed;
import MysticMod.Powers.SpellsPlayed;

import basemod.abstracts.CustomCard;

public class GreaterInvisibility
        extends CustomCard {
    public static final String ID = "MysticMod:GreaterInvisibility";
    public static final String NAME = "Greater Invisibility";
    public static final String DESCRIPTION = "Spell. Gain 1 Intangible. NL If you did not play !M! techniques this turn, Exhaust. NL Ethereal.";
    public static final String UPGRADE_DESCRIPTION = "Spell. Gain 1 Intangible. NL If you did not play a technique this turn, Exhaust. NL Ethereal.";
    public static final String IMG_PATH = "MysticMod/images/cards/greaterinvisibility.png";
    private static final int COST = 2;
    private static final int REQUIRED_TECHNIQUES = 2;
    private static final int UPGRADE_TECHNIQUE_REDUCE = -1;

    public GreaterInvisibility() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                AbstractCard.CardType.SKILL, AbstractCardEnum.MYSTIC_PURPLE,
                AbstractCard.CardRarity.RARE, AbstractCard.CardTarget.SELF);
        this.exhaust = true;
        this.isEthereal = true;
        this.magicNumber = this.baseMagicNumber = REQUIRED_TECHNIQUES;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        //set exhaust = false if techniques played >= magicNumber
        if ((p.hasPower(TechniquesPlayed.POWER_ID)) && (p.getPower(TechniquesPlayed.POWER_ID).amount >= this.magicNumber)) {
            this.exhaust = false;
        } else {
            this.exhaust = true;
        }
        //Intangible
        AbstractDungeon.actionManager.addToBottom(new com.megacrit.cardcrawl.actions.common.ApplyPowerAction(p, p, new IntangiblePlayerPower(p, 1), 1));
        //spell functionality
        AbstractDungeon.actionManager.addToBottom(new com.megacrit.cardcrawl.actions.common.ApplyPowerAction(p, p, new SpellsPlayed(p, 1), 1));
    }

    @Override
    public AbstractCard makeCopy() {
        return new GreaterInvisibility();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeMagicNumber(UPGRADE_TECHNIQUE_REDUCE);
            this.rawDescription = UPGRADE_DESCRIPTION;
            this.initializeDescription();
        }
    }
}