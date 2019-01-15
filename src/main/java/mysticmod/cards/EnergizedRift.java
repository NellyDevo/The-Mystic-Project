package mysticmod.cards;

import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import mysticmod.MysticMod;
import mysticmod.actions.LoadCardImageAction;
import mysticmod.patches.AbstractCardEnum;
import mysticmod.patches.MysticTags;
import mysticmod.powers.ArtesPlayed;

public class EnergizedRift extends AbstractMysticCard {
    public static final String ID = "mysticmod:EnergizedRift";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String[] EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
    public static final String IMG_PATH = "mysticmod/images/cards/energizedrift.png";
    public static final String ALTERNATE_IMG_PATH = "mysticmod/images/cards/alternate/energizedrift.png";
    private static final int COST = 1;
    private int cardAmount = 0;
    private boolean isArtAlternate = false;
    private String currentDescription = DESCRIPTION;

    public EnergizedRift() {
        super(ID, NAME, ALTERNATE_IMG_PATH, COST, DESCRIPTION,
                AbstractCard.CardType.SKILL, AbstractCardEnum.MYSTIC_PURPLE,
                AbstractCard.CardRarity.UNCOMMON, AbstractCard.CardTarget.SELF);
        this.exhaust = true;
        this.tags.add(MysticTags.IS_SPELL);
        this.loadCardImage(IMG_PATH);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        for (int i = 0; i < cardAmount; i++) {
            AbstractCard randomCantrip = MysticMod.cantripsGroup.get(AbstractDungeon.cardRandomRng.random(MysticMod.cantripsGroup.size()-1)).makeCopy();
            if (upgraded) {
                randomCantrip.upgrade();
            }
            AbstractDungeon.actionManager.addToBottom(new MakeTempCardInDrawPileAction(randomCantrip, 1, true, true));
        }
        AbstractDungeon.actionManager.addToBottom(new LoadCardImageAction(this, IMG_PATH, false));
        this.isArtAlternate = false;
        this.rawDescription = currentDescription;
        this.initializeDescription();
    }

    @Override
    public void applyPowers() {
        cardAmount = 0;
        if (AbstractDungeon.player.hasPower(ArtesPlayed.POWER_ID)) {
            cardAmount = AbstractDungeon.player.getPower(ArtesPlayed.POWER_ID).amount;
        }
        if (cardAmount > 0) {
            this.rawDescription = currentDescription + EXTENDED_DESCRIPTION[0] + cardAmount + (cardAmount == 1 ? EXTENDED_DESCRIPTION[1] : EXTENDED_DESCRIPTION[2]);
        }
        if (AbstractDungeon.player.hasPower(ArtesPlayed.POWER_ID)) {
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
    public void onMoveToDiscard() {
        this.rawDescription = currentDescription;
        this.initializeDescription();
    }

    @Override
    public boolean hasEnoughEnergy() {
        if (!AbstractDungeon.player.hasPower(ArtesPlayed.POWER_ID)) {
            return false;
        }
        return super.hasEnoughEnergy();
    }

    public void triggerOnEndOfPlayerTurn() {
        super.triggerOnEndOfPlayerTurn();
        if (this.isArtAlternate) {
            this.loadCardImage(IMG_PATH);
            this.isArtAlternate = false;
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new EnergizedRift();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.rawDescription = currentDescription = UPGRADE_DESCRIPTION;
            this.initializeDescription();
        }
    }
}
