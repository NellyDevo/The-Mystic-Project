package mysticmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import mysticmod.cards.AbstractMysticCard;
import mysticmod.powers.SpellsPlayed;
import mysticmod.relics.CrystalBall;

public class TechniqueHologram extends AbstractGameAction
{
    private AbstractPlayer p;
    private int amount;

    public TechniqueHologram(final int amount) {
        this.setValues(this.p = AbstractDungeon.player, AbstractDungeon.player, amount);
        this.actionType = ActionType.CARD_MANIPULATION;
        this.duration = Settings.ACTION_DUR_MED;
        this.amount = amount;
    }

    @Override
    public void update() {
        if (this.p.hand.size() >= 10) {
            this.isDone = true;
            return;
        }
        if (this.p.hand.size() == 9) {
            this.amount = 1;
        }
        final CardGroup tmp = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
        boolean cantripsAreSpells = (!AbstractDungeon.player.hasPower(SpellsPlayed.POWER_ID) || AbstractDungeon.player.getPower(SpellsPlayed.POWER_ID).amount == 1);
        boolean hasCrystalBall = (AbstractDungeon.player.hasRelic(CrystalBall.ID));
        for (final AbstractCard c2 : this.p.discardPile.group) {
            if ((c2 instanceof AbstractMysticCard && ((AbstractMysticCard)c2).isTechnique())
                || (hasCrystalBall && c2.type == AbstractCard.CardType.ATTACK && !(c2 instanceof AbstractMysticCard && ((AbstractMysticCard)c2).isSpell()))
                && !(c2.rawDescription.startsWith("Cantrip") && cantripsAreSpells)) {
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
            String cardOrCards = "card";
            if (this.amount == 2) {
                cardOrCards = "cards";
            }
            AbstractDungeon.gridSelectScreen.open(tmp, this.amount, "Choose " + this.amount + " Technique " + cardOrCards + " to add to your hand.", false);
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
