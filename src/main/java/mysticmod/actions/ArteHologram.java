package mysticmod.actions;

import basemod.BaseMod;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import mysticmod.cards.AbstractMysticCard;
import mysticmod.patches.MysticTags;
import mysticmod.powers.SpellsPlayed;
import mysticmod.relics.CrystalBall;

public class ArteHologram extends AbstractGameAction
{
    private AbstractPlayer p;
    private int amount;
    private static final String ID = "mysticmod:ArteHologram";
    private static final UIStrings ui = CardCrawlGame.languagePack.getUIString(ID);
    private static final String[] TEXT = ui.TEXT;

    public ArteHologram(final int amount) {
        this.setValues(this.p = AbstractDungeon.player, AbstractDungeon.player, amount);
        this.actionType = ActionType.CARD_MANIPULATION;
        this.duration = Settings.ACTION_DUR_MED;
        this.amount = amount;
    }

    @Override
    public void update() {
        int handSize = this.p.hand.size();
        if (handSize >= BaseMod.MAX_HAND_SIZE) {
            this.isDone = true;
            return;
        }
        int minAmount = BaseMod.MAX_HAND_SIZE - handSize;
        if (minAmount < this.amount) {
            this.amount = minAmount;
        }
        final CardGroup tmp = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
        boolean cantripsAreSpells = (!AbstractDungeon.player.hasPower(SpellsPlayed.POWER_ID) || AbstractDungeon.player.getPower(SpellsPlayed.POWER_ID).amount <= 2);
        boolean hasCrystalBall = (AbstractDungeon.player.hasRelic(CrystalBall.ID));
        for (final AbstractCard c2 : this.p.discardPile.group) {
            if ((c2 instanceof AbstractMysticCard && ((AbstractMysticCard)c2).isArte() || c2.hasTag(MysticTags.IS_ARTE))
                || (hasCrystalBall && c2.type == AbstractCard.CardType.ATTACK && !(c2 instanceof AbstractMysticCard && ((AbstractMysticCard)c2).isSpell() || c2.hasTag(MysticTags.IS_SPELL)))
                && !(c2.hasTag(MysticTags.IS_CANTRIP) && cantripsAreSpells)) {
                tmp.addToRandomSpot(c2);
            }
        }
        if (tmp.size() == 0) {
            this.isDone = true;
            return;
        } else if (tmp.size() <= this.amount) {
            for (final AbstractCard tmpCard : tmp.group) {
                this.p.hand.addToHand(tmpCard);
                tmpCard.lighten(false);
                this.p.discardPile.removeCard(tmpCard);
                this.p.hand.refreshHandLayout();
            }
            this.isDone = true;
            return;
        }
        if (this.duration == 0.5f) {
            AbstractDungeon.gridSelectScreen.open(tmp, this.amount, true, TEXT[0] + this.amount + TEXT[1]);
            this.tickDuration();
            return;
        }
        if (AbstractDungeon.gridSelectScreen.selectedCards.size() != 0) {
            for (final AbstractCard c : AbstractDungeon.gridSelectScreen.selectedCards) {
                this.p.hand.addToHand(c);
                this.p.discardPile.removeCard(c);
                c.lighten(false);
                c.unhover();
            }
            AbstractDungeon.gridSelectScreen.selectedCards.clear();
            this.p.hand.refreshHandLayout();
            for (final AbstractCard c : this.p.discardPile.group) {
                c.unhover();
                c.target_x = CardGroup.DISCARD_PILE_X;
                c.target_y = 0.0f;
            }
            this.isDone = true;
        }
        this.tickDuration();
    }
}
