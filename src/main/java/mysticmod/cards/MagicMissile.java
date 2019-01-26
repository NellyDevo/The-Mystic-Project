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
        this.damage=this.baseDamage = ATTACK_DMG;
        this.magicNumber = this.baseMagicNumber = 1;
        this.tags.add(MysticTags.IS_SPELL);
        this.altGlowColor = Color.RED;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new SetCardTargetCoordinatesAction(this, -1.0f, Settings.HEIGHT / 2.0f + 300f * Settings.scale));
        float projectileDelay = Interpolation.linear.apply(1.0F/3.0F, 0.05F, Math.min(((float)magicNumber)/100.0F, 1.0F));
        AbstractDungeon.actionManager.addToBottom(new MagicMissileAction(m, new DamageInfo(p, this.damage), this.magicNumber, projectileDelay, isArtAlternate ? Color.RED.cpy() : Color.CYAN.cpy()));
        this.rawDescription = currentDescription;
        this.initializeDescription();
        if (this.isArtAlternate) {
            AbstractDungeon.actionManager.addToBottom(new LoadCardImageAction(this, IMG_PATH, false));
            this.isArtAlternate = false;
        }
    }

    @Override
    public void applyPowers() {
        this.magicNumber = this.baseMagicNumber;
        this.magicNumber += MysticMod.numberOfTimesDeckShuffledThisCombat;
        if (AbstractDungeon.player.hasPower(ArtesPlayed.POWER_ID) && upgraded) {
            this.magicNumber += AbstractDungeon.player.getPower(ArtesPlayed.POWER_ID).amount;
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
        isMagicNumberModified = baseMagicNumber != magicNumber;
        if (this.magicNumber > 1) {
            this.rawDescription = currentDescription + EXTENDED_DESCRIPTIONS[0];
        } else {
            this.rawDescription = currentDescription + EXTENDED_DESCRIPTIONS[1];
        }
        this.initializeDescription();
        super.applyPowers();
    }

    @Override
    public void onMoveToDiscard() {
        this.rawDescription = currentDescription;
        this.initializeDescription();
    }

    @Override
    public AbstractCard makeCopy() {
        return new MagicMissile();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.rawDescription = currentDescription = UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }
}
