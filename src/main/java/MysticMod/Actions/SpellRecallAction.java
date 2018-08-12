package MysticMod.Actions;

import com.megacrit.cardcrawl.actions.*;
import com.megacrit.cardcrawl.characters.*;
import com.megacrit.cardcrawl.dungeons.*;
import com.megacrit.cardcrawl.cards.*;
import java.util.*;
import com.megacrit.cardcrawl.core.*;

public class SpellRecallAction extends AbstractGameAction
{
    private AbstractPlayer p;

    public SpellRecallAction(final int amount) {
        this.setValues(this.p = AbstractDungeon.player, AbstractDungeon.player, amount);
        this.actionType = ActionType.CARD_MANIPULATION;
    }

    @Override
    public void update() {
        if (this.p.hand.size() >= 10) {
            this.isDone = true;
            return;
        }
        if (this.duration == 0.5f) {
            //BEGIN make card group from discard pile
            final CardGroup tmp = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
            for (final AbstractCard discardedCard : this.p.discardPile.group) {
                if (discardedCard.rawDescription.startsWith("Spell.")) {
                    tmp.addToRandomSpot(discardedCard);
                }
            }
            //END make card group from discard pile
            if (tmp.group.size() == 1) {
                final AbstractCard card = tmp.group.get(0);
                this.p.hand.addToHand(card);
                card.lighten(false);
                this.p.discardPile.removeCard(card);
                this.p.hand.refreshHandLayout();
                this.isDone = true;
                return;
            }
            AbstractDungeon.gridSelectScreen.open(tmp, this.amount, "Choose a Spell to add to your hand.", false);
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
