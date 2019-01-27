package mysticmod.cards;

import com.badlogic.gdx.graphics.Color;
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

public class EnergizedRift extends AbstractAltArtMysticCard {
    public static final String ID = "mysticmod:EnergizedRift";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String[] EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
    public static final String ALTERNATE_IMG_PATH = "mysticmod/images/cards/alternate/energizedrift.png";
    private static final int COST = 1;
    private int cardAmount = 0;
    private String currentDescription = DESCRIPTION;

    public EnergizedRift() {
        super(ID, NAME, ALTERNATE_IMG_PATH, COST, DESCRIPTION,
                AbstractCard.CardType.SKILL, AbstractCardEnum.MYSTIC_PURPLE,
                AbstractCard.CardRarity.UNCOMMON, AbstractCard.CardTarget.SELF);
        exhaust = true;
        tags.add(MysticTags.IS_SPELL);
        IMG_PATH = "mysticmod/images/cards/energizedrift.png";
        loadCardImage(IMG_PATH);
        altGlowColor = ALT_GLOW_RED;
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
        isArtAlternate = false;
        rawDescription = currentDescription;
        initializeDescription();
    }

    @Override
    public void applyPowers() {
        cardAmount = 0;
        if (AbstractDungeon.player.hasPower(ArtesPlayed.POWER_ID)) {
            cardAmount = AbstractDungeon.player.getPower(ArtesPlayed.POWER_ID).amount;
        }
        if (cardAmount > 0) {
            rawDescription = currentDescription + EXTENDED_DESCRIPTION[0] + cardAmount + (cardAmount == 1 ? EXTENDED_DESCRIPTION[1] : EXTENDED_DESCRIPTION[2]);
            initializeDescription();
        }
        if (AbstractDungeon.player.hasPower(ArtesPlayed.POWER_ID)) {
            if (!isArtAlternate) {
                AbstractDungeon.actionManager.addToBottom(new LoadCardImageAction(this, ALTERNATE_IMG_PATH, true));
                isArtAlternate = true;
            }
        } else {
            if (isArtAlternate) {
                loadCardImage(IMG_PATH);
                isArtAlternate = false;
            }
        }
    }

    @Override
    public void onMoveToDiscard() {
        rawDescription = currentDescription;
        initializeDescription();
    }

    @Override
    public boolean hasEnoughEnergy() {
        if (!AbstractDungeon.player.hasPower(ArtesPlayed.POWER_ID)) {
            cantUseMessage = EXTENDED_DESCRIPTION[3];
            return false;
        }
        return super.hasEnoughEnergy();
    }

    @Override
    public AbstractCard makeCopy() {
        return new EnergizedRift();
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            rawDescription = currentDescription = UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }
}
