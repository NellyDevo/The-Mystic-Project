package mysticmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import mysticmod.cards.AbstractMysticCard;
import mysticmod.relics.CrystalBall;

public class SpellSeek extends AbstractGameAction
{
    private AbstractPlayer p;

    public SpellSeek(final int amount) {
        this.setValues(this.p = AbstractDungeon.player, AbstractDungeon.player, amount);
        this.actionType = ActionType.CARD_MANIPULATION;
        this.duration = Settings.ACTION_DUR_MED;
    }

    @Override
    public void update() {
        if (this.duration != Settings.ACTION_DUR_MED) {
            if (AbstractDungeon.gridSelectScreen.selectedCards.size() != 0) {
                for (final AbstractCard c : AbstractDungeon.gridSelectScreen.selectedCards) {
                    c.unhover();
                    if (this.p.hand.size() == 10) {
                        this.p.drawPile.moveToDiscardPile(c);
                        this.p.createHandIsFullDialog();
                    }
                    else {
                        this.p.drawPile.removeCard(c);
                        this.p.hand.addToTop(c);
                    }
                    this.p.hand.refreshHandLayout();
                    this.p.hand.applyPowers();
                }
                AbstractDungeon.gridSelectScreen.selectedCards.clear();
                this.p.hand.refreshHandLayout();
            }
            this.tickDuration();
            return;
        }
        final CardGroup tmp = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
        for (final AbstractCard c2 : this.p.drawPile.group) {
            if ((c2 instanceof AbstractMysticCard && ((AbstractMysticCard)c2).isSpell()) || (AbstractDungeon.player.hasRelic(CrystalBall.ID) && c2.type == AbstractCard.CardType.SKILL && !(c2 instanceof AbstractMysticCard && ((AbstractMysticCard)c2).isTechnique()))) {
                tmp.addToRandomSpot(c2);
            }
        }
        if (tmp.size() == 0) {
            this.isDone = true;
            return;
        }
        if (tmp.size() == 1) {
            final AbstractCard card = tmp.getTopCard();
            if (this.p.hand.size() == 10) {
                this.p.drawPile.moveToDiscardPile(card);
                this.p.createHandIsFullDialog();
            }
            else {
                card.unhover();
                card.lighten(true);
                card.setAngle(0.0f);
                card.drawScale = 0.12f;
                card.targetDrawScale = 0.75f;
                card.current_x = CardGroup.DRAW_PILE_X;
                card.current_y = CardGroup.DRAW_PILE_Y;
                this.p.drawPile.removeCard(card);
                AbstractDungeon.player.hand.addToTop(card);
                AbstractDungeon.player.hand.refreshHandLayout();
                AbstractDungeon.player.hand.applyPowers();
            }
            this.isDone = true;
            return;
        }
        if (tmp.size() <= this.amount) {
            for (int i = 0; i < tmp.size(); ++i) {
                final AbstractCard card2 = tmp.getNCardFromTop(i);
                if (this.p.hand.size() == 10) {
                    this.p.drawPile.moveToDiscardPile(card2);
                    this.p.createHandIsFullDialog();
                }
                else {
                    card2.unhover();
                    card2.lighten(true);
                    card2.setAngle(0.0f);
                    card2.drawScale = 0.12f;
                    card2.targetDrawScale = 0.75f;
                    card2.current_x = CardGroup.DRAW_PILE_X;
                    card2.current_y = CardGroup.DRAW_PILE_Y;
                    this.p.drawPile.removeCard(card2);
                    AbstractDungeon.player.hand.addToTop(card2);
                    AbstractDungeon.player.hand.refreshHandLayout();
                    AbstractDungeon.player.hand.applyPowers();
                }
            }
            this.isDone = true;
            return;
        }
        if (this.amount == 1) {
            AbstractDungeon.gridSelectScreen.open(tmp, this.amount, "Choose a Spell to add to your hand.", false);
        }
        else {
            AbstractDungeon.gridSelectScreen.open(tmp, this.amount, "Choose Spells to add to your hand.", false);
        }
        this.tickDuration();
    }
}
