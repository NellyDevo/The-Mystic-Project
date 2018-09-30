package mysticmod.cards;

import basemod.helpers.CardTags;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.ThornsPower;
import mysticmod.actions.LoadCardImageAction;
import mysticmod.mystictags.MysticTags;
import mysticmod.patches.AbstractCardEnum;
import mysticmod.powers.ArtesPlayed;
import mysticmod.powers.SpellsPlayed;

public class PunishingArmor
        extends AbstractMysticCard {
    public static final String ID = "mysticmod:PunishingArmor";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "mysticmod/images/cards/punishingarmor.png";
    public static final String ALTERNATE_IMG_PATH = "mysticmod/images/cards/alternate/punishingarmor.png";
    private static final int COST = 1;
    private static final int BLOCK_AMT = 5;
    private static final int THORNS_AMT = 2;
    private static final int UPGRADE_PLUS_THORNS = 2;
    private boolean isArtAlternate = false;

    public PunishingArmor() {
        super(ID, NAME, ALTERNATE_IMG_PATH, COST, DESCRIPTION,
                AbstractCard.CardType.SKILL, AbstractCardEnum.MYSTIC_PURPLE,
                AbstractCard.CardRarity.UNCOMMON, AbstractCard.CardTarget.SELF);
        this.loadCardImage(IMG_PATH);
        this.block = this.baseBlock = BLOCK_AMT;
        this.magicNumber = this.baseMagicNumber = THORNS_AMT;
        this.exhaust = true;
        CardTags.addTags(this, MysticTags.IS_SPELL);
        this.setBackgroundTexture(BG_SMALL_SPELL_SKILL_MYSTIC, BG_LARGE_SPELL_SKILL_MYSTIC);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new GainBlockAction(p, p, this.block));
        if ((p.hasPower(ArtesPlayed.POWER_ID)) && (p.getPower(ArtesPlayed.POWER_ID).amount >= 1)) {
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new ThornsPower(p, this.magicNumber), this.magicNumber));
        }
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new SpellsPlayed(p, 1), 1));
        if (this.isArtAlternate) {
            AbstractDungeon.actionManager.addToBottom(new LoadCardImageAction(this, IMG_PATH, false));
            this.isArtAlternate = false;
        }
    }

    @Override
    public void applyPowers() {
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
    public AbstractCard makeCopy() {
        return new PunishingArmor();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeMagicNumber(UPGRADE_PLUS_THORNS);
        }
    }
}