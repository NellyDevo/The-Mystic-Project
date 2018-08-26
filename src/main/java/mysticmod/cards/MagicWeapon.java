package mysticmod.cards;

import basemod.helpers.TooltipInfo;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import mysticmod.actions.ReplaceCardAction;
import mysticmod.patches.AbstractCardEnum;
import mysticmod.powers.SpellsPlayed;

import java.util.ArrayList;
import java.util.List;

public class MagicWeapon
        extends AbstractMysticCard {
    public static final String ID = "mysticmod:MagicWeapon";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "mysticmod/images/cards/magicweapon.png";
    private static final int COST = 1;
    private static final int UPGRADED_STR_GAIN = 1;
    private static final int STRENGTH_GAIN = 2;

    public MagicWeapon() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                AbstractCard.CardType.SKILL, AbstractCardEnum.MYSTIC_PURPLE,
                AbstractCard.CardRarity.UNCOMMON, AbstractCard.CardTarget.SELF);
        this.magicNumber = this.baseMagicNumber = STRENGTH_GAIN;
        this.exhaust = true;
        this.isSpell = true;
        this.setBackgroundTexture(BG_SMALL_SPELL_SKILL_MYSTIC, BG_LARGE_SPELL_SKILL_MYSTIC);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new StrengthPower(p, this.magicNumber), this.magicNumber));
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new SpellsPlayed(p, 1), 1));
        AbstractCard newBladeBurst = new BladeBurst();
        if (this.upgraded) {
            newBladeBurst.upgrade();
        }
        UnlockTracker.markCardAsSeen(newBladeBurst.cardID);
        AbstractDungeon.actionManager.addToBottom(new ReplaceCardAction(this, newBladeBurst));
    }

    @Override
    public AbstractCard makeCopy() {
        return new MagicWeapon();
    }

    @Override
    public List<TooltipInfo> getCustomTooltips() {
        List<TooltipInfo> retVal = super.getCustomTooltips();
        retVal.add(new TooltipInfo("Blade Burst", "an Arte that exhausts and deals High damage for 1 energy, loses 2 Strength, and adds this card back to your discard pile."));
        return retVal;
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeMagicNumber(UPGRADED_STR_GAIN);
            this.rawDescription = UPGRADE_DESCRIPTION;
            this.initializeDescription();
        }
    }
}