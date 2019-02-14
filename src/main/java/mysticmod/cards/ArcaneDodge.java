package mysticmod.cards;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import mysticmod.actions.LoadCardImageAction;
import mysticmod.patches.AbstractCardEnum;
import mysticmod.patches.MysticTags;
import mysticmod.powers.SpellsPlayed;

public class ArcaneDodge extends AbstractAltArtMysticCard {
    public static final String ID = "mysticmod:ArcaneDodge";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String ALTERNATE_IMG_PATH = "mysticmod/images/cards/alternate/arcanedodge.png";
    private static final int COST = 1;
    public static final int BLOCK_AMT = 5;
    private static final int EXTRA_BLK = 4;
    private static final int UPGRADE_EXTRA_BLOCK = 1;
    private static final int UPGRADE_BLOCK_AMT = 2;

    public ArcaneDodge() {
        super(ID, NAME, ALTERNATE_IMG_PATH, COST, DESCRIPTION,
                AbstractCard.CardType.SKILL, AbstractCardEnum.MYSTIC_PURPLE,
                AbstractCard.CardRarity.BASIC, AbstractCard.CardTarget.SELF);
        IMG_PATH = "mysticmod/images/cards/arcanedodge.png";
        loadCardImage(IMG_PATH);
        block = baseBlock = BLOCK_AMT;
        magicNumber = baseMagicNumber = EXTRA_BLK;
        tags.add(MysticTags.IS_ARTE);
        tags.add(MysticTags.IS_POWERFUL);
        altGlowColor = ALT_GLOW_BLUE;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new GainBlockAction(p, p, block));
        if ((p.hasPower(SpellsPlayed.POWER_ID)) && (p.getPower(SpellsPlayed.POWER_ID).amount >= 1)) {
        AbstractDungeon.actionManager.addToBottom(new GainBlockAction(p, p, magicNumber));
        }
        if (isArtAlternate) {
            AbstractDungeon.actionManager.addToBottom(new LoadCardImageAction(this, IMG_PATH, false));
            isArtAlternate = false;
        }
    }

    @Override
    public void applyPowers() {
        int blockPlaceholder = baseBlock;
        int magicNumberPlaceholder = baseMagicNumber;
        baseBlock = magicNumberPlaceholder;
        super.applyPowers(); // takes baseDamage and applies things like Strength or Pen Nib to set damage

        magicNumber = block; // magic number holds the first condition's modified damage, so !M! will work
        isMagicNumberModified = magicNumber != baseMagicNumber;

        // repeat so damage holds the second condition's damage
        baseBlock = blockPlaceholder;
        baseMagicNumber = magicNumberPlaceholder;
        super.applyPowers();
        if (AbstractDungeon.player.hasPower(SpellsPlayed.POWER_ID)) {
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
        return new ArcaneDodge();
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_EXTRA_BLOCK);
            upgradeBlock(UPGRADE_BLOCK_AMT);
        }
    }
}
