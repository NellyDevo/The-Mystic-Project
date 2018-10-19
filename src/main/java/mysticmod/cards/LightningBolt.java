package mysticmod.cards;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import mysticmod.actions.LightningBoltAction;
import mysticmod.actions.LoadCardImageAction;
import mysticmod.patches.AbstractCardEnum;
import mysticmod.patches.MysticTags;
import mysticmod.powers.ArtesPlayed;
import mysticmod.powers.SpellsPlayed;

public class LightningBolt
        extends AbstractMysticCard {
    public static final String ID = "mysticmod:LightningBolt";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "mysticmod/images/cards/lightningbolt.png";
    public static final String ALTERNATE_IMG_PATH = "mysticmod/images/cards/alternate/lightningbolt.png";
    private static final int COST = 1;
    public static final int ATTACK_DMG = 6;
    private static final int UPGRADE_PLUS_DMG = 3;
    private static final int ALTERNATE_DMG = 10;
    private static final int UPGRADE_PLUS_ALT_DMG = 4;
    private int[] alternateMultiDamage;
    private boolean isArtAlternate = false;

    public LightningBolt() {
        super(ID, NAME, ALTERNATE_IMG_PATH, COST, DESCRIPTION,
                AbstractCard.CardType.ATTACK, AbstractCardEnum.MYSTIC_PURPLE,
                AbstractCard.CardRarity.COMMON, AbstractCard.CardTarget.ALL_ENEMY);
        this.loadCardImage(IMG_PATH);
        this.damage=this.baseDamage = ATTACK_DMG;
        this.magicNumber = this.baseMagicNumber = ALTERNATE_DMG;
        this.isMultiDamage = true;
        this.tags.add(MysticTags.IS_SPELL);
        this.setBackgroundTexture(BG_SMALL_SPELL_ATTACK_MYSTIC, BG_LARGE_SPELL_ATTACK_MYSTIC);
    }
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (p.hasPower(ArtesPlayed.POWER_ID)) {
            AbstractDungeon.actionManager.addToBottom(new LightningBoltAction(this.alternateMultiDamage, this.damageTypeForTurn, p, this));
        } else {
            AbstractDungeon.actionManager.addToBottom(new LightningBoltAction(this.multiDamage, this.damageTypeForTurn, p, this));
        }
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new SpellsPlayed(p, 1), 1));
        if (this.isArtAlternate) {
            AbstractDungeon.actionManager.addToBottom(new LoadCardImageAction(this, IMG_PATH, false));
            this.isArtAlternate = false;
        }
    }

    @Override
    public void applyPowers() {
        int CURRENT_MAGIC_NUMBER = this.baseMagicNumber;
        int CURRENT_DMG = this.baseDamage;
        this.baseDamage = CURRENT_MAGIC_NUMBER;
        super.applyPowers(); // takes this.baseDamage and applies things like Strength or Pen Nib to set this.damage

        this.alternateMultiDamage = this.multiDamage.clone();
        this.magicNumber = this.damage; // magic number holds the first condition's modified damage, so !M! will work
        this.isMagicNumberModified = this.magicNumber != this.baseMagicNumber;

        // repeat so this.damage holds the second condition's damage
        this.baseDamage = CURRENT_DMG;
        super.applyPowers();
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

    public void triggerOnEndOfPlayerTurn() {
        super.triggerOnEndOfPlayerTurn();
        if (this.isArtAlternate) {
            this.loadCardImage(IMG_PATH);
            this.isArtAlternate = false;
        }
    }

    @Override
    public void calculateCardDamage(final AbstractMonster mo) {
        int CURRENT_MAGIC_NUMBER = this.baseMagicNumber;
        int CURRENT_DMG = this.baseDamage;
        this.baseDamage = CURRENT_MAGIC_NUMBER;
        super.calculateCardDamage(mo); // takes this.baseDamage and applies things like Strength or Pen Nib to set this.damage

        this.alternateMultiDamage = this.multiDamage.clone();
        this.magicNumber = this.damage; // magic number holds the first condition's modified damage, so !M! will work
        this.isMagicNumberModified = this.magicNumber != this.baseMagicNumber;

        // repeat so this.damage holds the second condition's damage
        this.baseDamage = CURRENT_DMG;
        super.calculateCardDamage(mo);
    }

    @Override
    public AbstractCard makeCopy() {
        return new LightningBolt();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeDamage(UPGRADE_PLUS_DMG);
            this.upgradeMagicNumber(UPGRADE_PLUS_ALT_DMG);
        }
    }
}