package mysticmod.cards;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import mysticmod.MysticMod;
import mysticmod.patches.AbstractCardEnum;
import mysticmod.patches.MysticTags;
import mysticmod.powers.SpellsPlayed;

public class EnergizedRift
        extends AbstractMysticCard {
    public static final String ID = "mysticmod:EnergizedRift";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "mysticmod/images/cards/energizedrift.png";
    private static final int COST = 1;

    public EnergizedRift() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                AbstractCard.CardType.SKILL, AbstractCardEnum.MYSTIC_PURPLE,
                AbstractCard.CardRarity.UNCOMMON, AbstractCard.CardTarget.SELF);
        this.exhaust = true;
        this.tags.add(MysticTags.IS_SPELL);
        this.setBackgroundTexture(BG_SMALL_SPELL_SKILL_MYSTIC, BG_LARGE_SPELL_SKILL_MYSTIC);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractCard randomCantrip = MysticMod.cantripsGroup.get(AbstractDungeon.cardRandomRng.random(MysticMod.cantripsGroup.size()-1)).makeCopy();
        if (this.upgraded) {
            randomCantrip.upgrade();
        }
        AbstractDungeon.actionManager.addToBottom(new MakeTempCardInHandAction(randomCantrip.makeStatEquivalentCopy(), 1, false));
        for (final AbstractCard potentialCantrip : p.hand.group) {
            if (potentialCantrip.hasTag(MysticTags.IS_CANTRIP)) {
                AbstractDungeon.actionManager.addToBottom(new MakeTempCardInDrawPileAction(potentialCantrip.makeStatEquivalentCopy(), 1, true, true));
            }
        }
        AbstractDungeon.actionManager.addToBottom(new MakeTempCardInDrawPileAction(randomCantrip.makeStatEquivalentCopy(), 1, true, true));
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new SpellsPlayed(p, 1), 1));
    }

    @Override
    public AbstractCard makeCopy() {
        return new EnergizedRift();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.rawDescription = UPGRADE_DESCRIPTION;
            this.initializeDescription();
        }
    }
}