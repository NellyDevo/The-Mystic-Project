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
import com.megacrit.cardcrawl.powers.LoseStrengthPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import mysticmod.actions.LoadCardImageAction;
import mysticmod.patches.AbstractCardEnum;
import mysticmod.patches.MysticTags;
import mysticmod.powers.SpellsPlayed;

public class ChargedParry extends AbstractAltArtMysticCard {
    public static final String ID = "mysticmod:ChargedParry";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String ALTERNATE_IMG_PATH = "mysticmod/images/cards/alternate/chargedparry.png";
    private static final int COST = 1;
    public static final int BLOCK_AMT = 6;
    public static final int STRENGTH_GAIN = 3;
    private static final int UPGRADE_STRENGTH_GAIN = 2;

    public ChargedParry() {
        super(ID, NAME, ALTERNATE_IMG_PATH, COST, DESCRIPTION,
                AbstractCard.CardType.SKILL, AbstractCardEnum.MYSTIC_PURPLE,
                AbstractCard.CardRarity.UNCOMMON, AbstractCard.CardTarget.SELF);
        IMG_PATH = "mysticmod/images/cards/chargedparry.png";
        loadCardImage(IMG_PATH);
        block = baseBlock = BLOCK_AMT;
        magicNumber = baseMagicNumber = STRENGTH_GAIN;
        tags.add(MysticTags.IS_ARTE);
        altGlowColor = ALT_GLOW_BLUE;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new GainBlockAction(p, p, block));
        if ((p.hasPower(SpellsPlayed.POWER_ID)) && (p.getPower(SpellsPlayed.POWER_ID).amount >= 1)) {
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, AbstractDungeon.player, new StrengthPower(p, magicNumber), magicNumber));
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, AbstractDungeon.player, new LoseStrengthPower(p, magicNumber), magicNumber));
         }
        if (isArtAlternate) {
            AbstractDungeon.actionManager.addToBottom(new LoadCardImageAction(this, IMG_PATH, false));
            isArtAlternate = false;
        }
    }

    @Override
    public void applyPowers() {
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
        return new ChargedParry();
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_STRENGTH_GAIN);
        }
    }
}
