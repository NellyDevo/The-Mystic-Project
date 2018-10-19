package mysticmod.cards;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import mysticmod.patches.AbstractCardEnum;
import mysticmod.patches.MysticTags;
import mysticmod.powers.HastePower;
import mysticmod.powers.SpellsPlayed;

public class Haste
        extends AbstractMysticCard {
    public static final String ID = "mysticmod:Haste";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "mysticmod/images/cards/haste.png";
    private static final int COST = 3;

    public Haste() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                AbstractCard.CardType.SKILL, AbstractCardEnum.MYSTIC_PURPLE,
                AbstractCard.CardRarity.RARE, AbstractCard.CardTarget.SELF);
        this.exhaust = true;
        this.isEthereal = true;
        this.tags.add(MysticTags.IS_SPELL);
        this.setBackgroundTexture(BG_SMALL_SPELL_SKILL_MYSTIC, BG_LARGE_SPELL_SKILL_MYSTIC);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        //apply haste power if no haste power applied
        if (!(p.hasPower(HastePower.POWER_ID))) {
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new HastePower(p)));
        }
        //str and dex
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new DexterityPower(p, 1), 1));
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new StrengthPower(p, 1), 1));
        //spell functionality
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new SpellsPlayed(p, 1), 1));
    }

    @Override
    public AbstractCard makeCopy() {
        return new Haste();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.isEthereal = false;
            this.rawDescription = UPGRADE_DESCRIPTION;
            this.initializeDescription();
        }
    }
}