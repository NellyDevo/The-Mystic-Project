package mysticmod.cards;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import mysticmod.actions.ClosingBarrageAction;
import mysticmod.patches.AbstractCardEnum;
import mysticmod.powers.ArtesPlayed;
import mysticmod.powers.SpellsPlayed;

public class ClosingBarrage
        extends AbstractMysticCard {
    public static final String ID = "mysticmod:ClosingBarrage";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION[0];
    public static final String IMG_PATH = "mysticmod/images/cards/closingbarrage.png";
    private static final int COST = -1;
    private static final int MULTIPLIER = 3;

    public ClosingBarrage() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                AbstractCard.CardType.ATTACK, AbstractCardEnum.MYSTIC_PURPLE,
                AbstractCard.CardRarity.RARE, AbstractCard.CardTarget.ENEMY);
            this.magicNumber = this.baseMagicNumber = MULTIPLIER;
            this.damage = this.baseDamage = 0;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (this.energyOnUse < EnergyPanel.totalCount) {
            this.energyOnUse = EnergyPanel.totalCount;
        }
        AbstractDungeon.actionManager.addToBottom(new ClosingBarrageAction(p, m, this.damage, this.damageTypeForTurn, this.freeToPlayOnce, this.energyOnUse, this.upgraded));
        this.rawDescription = DESCRIPTION;
        this.initializeDescription();
    }

    @Override
    public void applyPowers() {
        int baseDamagePlaceholder = this.baseDamage;
        int damageX = 0;
        int damageY = 0;
        if (AbstractDungeon.player.hasPower(ArtesPlayed.POWER_ID)) {
            damageX = AbstractDungeon.player.getPower(ArtesPlayed.POWER_ID).amount;
        }
        if (AbstractDungeon.player.hasPower(SpellsPlayed.POWER_ID)) {
            damageY = AbstractDungeon.player.getPower(SpellsPlayed.POWER_ID).amount;
        }
        this.baseDamage = this.magicNumber * (damageX + damageY);
        super.applyPowers();
        this.baseDamage = baseDamagePlaceholder;
        if (this.damage != this.baseDamage) {
            this.isDamageModified = true;
        }
        this.rawDescription = DESCRIPTION + EXTENDED_DESCRIPTION;
        this.initializeDescription();
    }

    public void calculateCardDamage(final AbstractMonster mo) {
        int baseDamagePlaceholder = this.baseDamage;
        int damageX = 0;
        int damageY = 0;
        if (AbstractDungeon.player.hasPower(ArtesPlayed.POWER_ID)) {
            damageX = AbstractDungeon.player.getPower(ArtesPlayed.POWER_ID).amount;
        }
        if (AbstractDungeon.player.hasPower(SpellsPlayed.POWER_ID)) {
            damageY = AbstractDungeon.player.getPower(SpellsPlayed.POWER_ID).amount;
        }
        this.baseDamage = this.magicNumber * (damageX + damageY);
        super.calculateCardDamage(mo);
        this.baseDamage = baseDamagePlaceholder;
        if (this.damage != this.baseDamage) {
            this.isDamageModified = true;
        }
        this.rawDescription = DESCRIPTION + EXTENDED_DESCRIPTION;
        this.initializeDescription();
    }

    @Override
    public void onMoveToDiscard() {
        this.rawDescription = DESCRIPTION;
        this.initializeDescription();
    }


    @Override
    public AbstractCard makeCopy() {
        return new ClosingBarrage();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
        }
    }
}