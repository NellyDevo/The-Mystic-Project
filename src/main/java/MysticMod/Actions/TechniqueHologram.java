package MysticMod.Actions;

import com.megacrit.cardcrawl.actions.*;
import com.megacrit.cardcrawl.localization.*;
import com.megacrit.cardcrawl.characters.*;
import com.megacrit.cardcrawl.dungeons.*;
import com.megacrit.cardcrawl.cards.*;
import java.util.*;
import com.megacrit.cardcrawl.core.*;

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
        for (final AbstractCard c2 : this.p.discardPile.group) {
            if (c2.rawDescription.startsWith("Technique.")) {
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
