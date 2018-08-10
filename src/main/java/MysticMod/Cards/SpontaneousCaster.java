package MysticMod.Cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import MysticMod.Patches.AbstractCardEnum;
import MysticMod.Powers.SpontaneousCasterPower;

import basemod.abstracts.CustomCard;

public class SpontaneousCaster
        extends CustomCard {
    public static final String ID = "MysticMod:SpontaneousCaster";
    public static final String NAME = "Spontaneous Caster";
    public static final String DESCRIPTION = "Spells cost [E] less to play, Exhaust when played, and then add a random Spell to your discard pile.";
    public static final String UPGRADE_DESCRIPTION = "Spells cost [E] less to play, Exhaust when played, and then add a random Spell to your discard pile. NL Innate.";
    public static final String IMG_PATH = "MysticMod/images/cards/spontaneouscaster.png";
    private static final int COST = 3;

    public SpontaneousCaster() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                AbstractCard.CardType.POWER, AbstractCardEnum.MYSTIC_PURPLE,
                AbstractCard.CardRarity.RARE, AbstractCard.CardTarget.SELF);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {

        AbstractDungeon.actionManager.addToBottom(new com.megacrit.cardcrawl.actions.common.ApplyPowerAction(p, p, new SpontaneousCasterPower(p)));

    }

    @Override
    public AbstractCard makeCopy() {
        return new SpontaneousCaster();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.isInnate = true;
            this.rawDescription = UPGRADE_DESCRIPTION;
            this.initializeDescription();
        }
    }
}