package mysticmod.cards;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Interpolation;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import mysticmod.MysticMod;
import mysticmod.actions.LoadCardImageAction;
import mysticmod.actions.MagicMissileAction;
import mysticmod.actions.SetCardTargetCoordinatesAction;
import mysticmod.patches.AbstractCardEnum;
import mysticmod.patches.MysticTags;
import mysticmod.powers.ArtesPlayed;

public class MagicMissile extends AbstractAltArtMysticCard {
    public static final String ID = "mysticmod:MagicMissile";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String[] EXTENDED_DESCRIPTIONS = cardStrings.EXTENDED_DESCRIPTION;
    public static final String ALTERNATE_IMG_PATH = "mysticmod/images/cards/alternate/magicmissile.png";
    private static final int COST = 0;
    public static final int ATTACK_DMG = 3;
    private String currentDescription = DESCRIPTION;

    public MagicMissile() {
        super(ID, NAME, "mysticmod/images/cards/magicmissile.png", COST, DESCRIPTION,
                AbstractCard.CardType.ATTACK, AbstractCardEnum.MYSTIC_PURPLE,
                AbstractCard.CardRarity.UNCOMMON, AbstractCard.CardTarget.ENEMY);
        IMG_PATH = "mysticmod/images/cards/magicmissile.png";
        damage = baseDamage = ATTACK_DMG;
        magicNumber = baseMagicNumber = 1;
        tags.add(MysticTags.IS_SPELL);
        altGlowColor = ALT_GLOW_RED;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new SetCardTargetCoordinatesAction(this, -1.0f, Settings.HEIGHT / 2.0f + 300f * Settings.scale));
        float projectileDelay = Interpolation.linear.apply(1.0F/3.0F, 0.05F, Math.min(((float)magicNumber)/100.0F, 1.0F));
        AbstractDungeon.actionManager.addToBottom(new MagicMissileAction(m, new DamageInfo(p, damage), magicNumber, projectileDelay, isArtAlternate ? Color.RED.cpy() : Color.CYAN.cpy()));
        rawDescription = currentDescription;
        initializeDescription();
        if (isArtAlternate) {
            AbstractDungeon.actionManager.addToBottom(new LoadCardImageAction(this, IMG_PATH, false));
            isArtAlternate = false;
        }
    }

    @Override
    public void applyPowers() {
        magicNumber = baseMagicNumber;
        magicNumber += MysticMod.numberOfTimesDeckShuffledThisCombat;
        if (AbstractDungeon.player.hasPower(ArtesPlayed.POWER_ID) && upgraded) {
            magicNumber += AbstractDungeon.player.getPower(ArtesPlayed.POWER_ID).amount;
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
        isMagicNumberModified = baseMagicNumber != magicNumber;
        if (magicNumber > 1) {
            rawDescription = currentDescription + EXTENDED_DESCRIPTIONS[0];
        } else {
            rawDescription = currentDescription + EXTENDED_DESCRIPTIONS[1];
        }
        initializeDescription();
        super.applyPowers();
    }

    @Override
    public void onMoveToDiscard() {
        rawDescription = currentDescription;
        initializeDescription();
    }

    @Override
    public AbstractCard makeCopy() {
        return new MagicMissile();
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
