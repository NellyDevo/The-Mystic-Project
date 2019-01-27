package mysticmod.cards;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.BlurPower;
import mysticmod.actions.LoadCardImageAction;
import mysticmod.patches.AbstractCardEnum;
import mysticmod.patches.MysticTags;
import mysticmod.powers.ArtesPlayed;

public class Fly extends AbstractAltArtMysticCard {
    public static final String ID = "mysticmod:Fly";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String ALTERNATE_IMG_PATH = "mysticmod/images/cards/alternate/fly.png";
    private static final int COST = 1;
    private static final int BLOCK_AMT = 6;
    private static final int UPGRADE_PLUS_BLK = 2;

    public Fly() {
        super(ID, NAME, ALTERNATE_IMG_PATH, COST, DESCRIPTION,
                AbstractCard.CardType.SKILL, AbstractCardEnum.MYSTIC_PURPLE,
                AbstractCard.CardRarity.UNCOMMON, AbstractCard.CardTarget.SELF);
        IMG_PATH = "mysticmod/images/cards/fly.png";
        loadCardImage(IMG_PATH);
        block = baseBlock = BLOCK_AMT;
        tags.add(MysticTags.IS_SPELL);
        altGlowColor = Color.RED;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new GainBlockAction(p, p, block));
        if (AbstractDungeon.player.hasPower(ArtesPlayed.POWER_ID)) {
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new BlurPower(p, 1), 1));
        }
        if (isArtAlternate) {
            AbstractDungeon.actionManager.addToBottom(new LoadCardImageAction(this, IMG_PATH, false));
            isArtAlternate = false;
        }
    }

    @Override
    public void applyPowers() {
        super.applyPowers();
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
    public AbstractCard makeCopy() {
        return new Fly();
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeBlock(UPGRADE_PLUS_BLK);
        }
    }
}
