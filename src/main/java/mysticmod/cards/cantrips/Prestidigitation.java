package mysticmod.cards.cantrips;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import mysticmod.cards.AbstractMysticCard;
import mysticmod.patches.MysticTags;
import mysticmod.powers.SpellsPlayed;
import mysticmod.relics.BentSpoon;

public class Prestidigitation
        extends AbstractMysticCard {
    public static final String ID = "mysticmod:Prestidigitation";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "mysticmod/images/cards/prestidigitation.png";
    private static final int COST = 0;
    private static final int BLOCK_AMT = 2;
    private static final int UPGRADE_PLUS_BLK = 2;

    public Prestidigitation() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                AbstractCard.CardType.SKILL, AbstractCard.CardColor.COLORLESS,
                AbstractCard.CardRarity.SPECIAL, AbstractCard.CardTarget.SELF);
        this.block = this.baseBlock = BLOCK_AMT;
        crystalBallToggle = false;
        this.tags.add(MysticTags.IS_CANTRIP);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new GainBlockAction(p, p, this.block));
        AbstractDungeon.actionManager.addToBottom(new DrawCardAction(p, 1));
        //cantrip functionality
        if (
                !(p.hasPower(SpellsPlayed.POWER_ID))
                        ||
                        (p.hasPower(SpellsPlayed.POWER_ID) && (p.getPower(SpellsPlayed.POWER_ID).amount <= 2))
        ) {
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new SpellsPlayed(p, 1), 1));
        }
    }

    @Override
    public boolean isSpell() {
        if (AbstractDungeon.player == null || (!AbstractDungeon.player.hasPower(SpellsPlayed.POWER_ID) || AbstractDungeon.player.getPower(SpellsPlayed.POWER_ID).amount <= 2)) {
            this.tags.add(MysticTags.IS_SPELL);
            return true;
        }
        this.tags.remove(MysticTags.IS_SPELL);
        return super.isSpell();
    }

    @Override
    public void applyPowers() {
        if (AbstractDungeon.player.hasRelic(BentSpoon.ID)) {
            int baseDamagePlaceholder = this.baseDamage;
            int baseBlockPlaceholder = this.baseBlock;
            this.baseDamage += 1;
            this.baseBlock += 1;
            super.applyPowers();
            this.baseDamage = baseDamagePlaceholder;
            this.baseBlock = baseBlockPlaceholder;
            if (this.damage != this.baseDamage) {
                this.isDamageModified = true;
            }
            if (this.block != this.baseBlock) {
                this.isBlockModified = true;
            }
        } else {
            super.applyPowers();
        }
    }

    @Override
    public void calculateCardDamage(final AbstractMonster mo) {
        if (AbstractDungeon.player.hasRelic(BentSpoon.ID)) {
            int baseDamagePlaceholder = this.baseDamage;
            int baseBlockPlaceholder = this.baseBlock;
            this.baseDamage += 1;
            this.baseBlock += 1;
            super.calculateCardDamage(mo);
            this.baseDamage = baseDamagePlaceholder;
            this.baseBlock = baseBlockPlaceholder;
            if (this.damage != this.baseDamage) {
                this.isDamageModified = true;
            }
            if (this.block != this.baseBlock) {
                this.isBlockModified = true;
            }
        } else {
            super.calculateCardDamage(mo);
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new Prestidigitation();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeBlock(UPGRADE_PLUS_BLK);
        }
    }
}