package mysticmod.cards;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import mysticmod.actions.LoadCardImageAction;
import mysticmod.patches.AbstractCardEnum;
import mysticmod.powers.ArtesPlayed;
import mysticmod.powers.SpellsPlayed;

public class ArcaneDodge
        extends AbstractMysticCard {
    public static final String ID = "mysticmod:ArcaneDodge";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "mysticmod/images/cards/arcanedodge.png";
    public static final String ALTERNATE_IMG_PATH = "mysticmod/images/cards/alternate/arcanedodge.png";
    private static final int COST = 1;
    public static final int BLOCK_AMT = 5;
    private static final int EXTRA_BLK = 4;
    private static final int UPGRADE_EXTRA_BLOCK = 3;
    private boolean isArtAlternate = false;

    public ArcaneDodge() {
        super(ID, NAME, ALTERNATE_IMG_PATH, COST, DESCRIPTION,
                AbstractCard.CardType.SKILL, AbstractCardEnum.MYSTIC_PURPLE,
                AbstractCard.CardRarity.BASIC, AbstractCard.CardTarget.SELF);
        this.loadCardImage(IMG_PATH);
        this.block = this.baseBlock = BLOCK_AMT;
        this.magicNumber = this.baseMagicNumber = EXTRA_BLK;
        this.isArte = true;
        this.setBackgroundTexture(BG_SMALL_ARTE_SKILL_MYSTIC, BG_LARGE_ARTE_SKILL_MYSTIC);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new GainBlockAction(p, p, this.block));
        if ((p.hasPower(SpellsPlayed.POWER_ID)) && (p.getPower(SpellsPlayed.POWER_ID).amount >= 1)) {
        AbstractDungeon.actionManager.addToBottom(new GainBlockAction(p, p, this.magicNumber));
         }
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new ArtesPlayed(p, 1), 1));
        if (this.isArtAlternate) {
            AbstractDungeon.actionManager.addToBottom(new LoadCardImageAction(this, IMG_PATH, false));
            this.isArtAlternate = false;
        }
    }

    public void triggerOnEndOfPlayerTurn() {
        super.triggerOnEndOfPlayerTurn();
        if (this.isArtAlternate) {
            this.loadCardImage(IMG_PATH);
            this.isArtAlternate = false;
        }
    }

    @Override
    public void applyPowers() {
        int blockPlaceholder = this.baseBlock;
        int magicNumberPlaceholder = this.baseMagicNumber;
        this.baseBlock = magicNumberPlaceholder;
        super.applyPowers(); // takes this.baseDamage and applies things like Strength or Pen Nib to set this.damage

        this.magicNumber = block; // magic number holds the first condition's modified damage, so !M! will work
        this.isMagicNumberModified = this.magicNumber != this.baseMagicNumber;

        // repeat so this.damage holds the second condition's damage
        this.baseBlock = blockPlaceholder;
        this.baseMagicNumber = magicNumberPlaceholder;
        super.applyPowers();
        if (AbstractDungeon.player.hasPower(SpellsPlayed.POWER_ID)) {
            if (!this.isArtAlternate) {
                AbstractDungeon.actionManager.addToBottom(new LoadCardImageAction(this, ALTERNATE_IMG_PATH, true));
                this.isArtAlternate = true;
            }
        } else {
            if (this.isArtAlternate) {
                this.loadCardImage(IMG_PATH);
                this.isArtAlternate = false;
            }
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new ArcaneDodge();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeMagicNumber(UPGRADE_EXTRA_BLOCK);
        }
    }
}