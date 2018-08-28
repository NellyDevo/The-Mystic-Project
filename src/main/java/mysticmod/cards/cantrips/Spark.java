package mysticmod.cards.cantrips;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import mysticmod.actions.SparkAction;
import mysticmod.cards.AbstractMysticCard;
import mysticmod.patches.AbstractCardEnum;
import mysticmod.powers.SpellsPlayed;

public class Spark
        extends AbstractMysticCard {
    public static final String ID = "mysticmod:Spark";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "mysticmod/images/cards/spark.png";
    private static final int COST = 0;
    private boolean bgChanged = false;

    public Spark() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                AbstractCard.CardType.SKILL, AbstractCardEnum.MYSTIC_PURPLE,
                AbstractCard.CardRarity.SPECIAL, AbstractCard.CardTarget.SELF);
        this.exhaust = true;
        this.changeColor(BG_SMALL_SPELL_SKILL_COLORLESS, BG_LARGE_SPELL_SKILL_COLORLESS, true);
        crystalBallToggle = false;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new DrawCardAction(p, 1));
        AbstractDungeon.actionManager.addToBottom(new SparkAction());
        //cantrip functionality
        if (
                !(p.hasPower(SpellsPlayed.POWER_ID))
                        ||
                        (p.hasPower(SpellsPlayed.POWER_ID) && (p.getPower(SpellsPlayed.POWER_ID).amount <= 2))
        ) {
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new SpellsPlayed(p, 1), 1));
        }
    }

    @Override
    public boolean isSpell() {
        if (AbstractDungeon.player == null || (!AbstractDungeon.player.hasPower(SpellsPlayed.POWER_ID) || AbstractDungeon.player.getPower(SpellsPlayed.POWER_ID).amount <= 2)) {
            if (bgChanged) {
                this.setBackgroundTexture(BG_SMALL_SPELL_SKILL_COLORLESS, BG_LARGE_SPELL_SKILL_COLORLESS);
                bgChanged = false;
                crystalBallToggle = false;
            }
            return true;
        }
        return super.isSpell;
    }

    @Override
    public void applyPowers() {
        super.applyPowers();
        if (!this.isSpell) {
            if (!bgChanged) {
                this.setBackgroundTexture(BG_SMALL_DEFAULT_SKILL_COLORLESS, BG_LARGE_DEFAULT_SKILL_COLORLESS);
                bgChanged = true;
                crystalBallToggle = false;
            }
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new Spark();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.exhaust = false;
            this.rawDescription = UPGRADE_DESCRIPTION;
            this.initializeDescription();
        }
    }
}