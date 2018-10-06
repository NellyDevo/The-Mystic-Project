package mysticmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import mysticmod.cards.AbstractMysticCard;
import mysticmod.patches.MysticTags;
import mysticmod.relics.CrystalBall;

public class SpellRecallAction extends AbstractGameAction
{
    private AbstractPlayer p;
    private static final String ID = "mysticmod:SpellRecallAction";
    private static final UIStrings ui = CardCrawlGame.languagePack.getUIString(ID);
    private static final String[] TEXT = ui.TEXT;

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
                if ((discardedCard instanceof AbstractMysticCard && ((AbstractMysticCard)discardedCard).isSpell()
                        || discardedCard.hasTag(MysticTags.IS_SPELL)) || (AbstractDungeon.player.hasRelic(CrystalBall.ID)
                        && discardedCard.type == AbstractCard.CardType.SKILL && !(discardedCard instanceof AbstractMysticCard
                        && ((AbstractMysticCard)discardedCard).isArte() || discardedCard.hasTag(MysticTags.IS_ARTE)))) {
                    tmp.addToRandomSpot(discardedCard);
                }
            }
            //END make card group from discard pile
            if (tmp.group.size() == 0) {
                this.isDone = true;
                return;
            }
            if (tmp.group.size() == 1) {
                final AbstractCard card = tmp.group.get(0);
                this.p.hand.addToHand(card);
                card.lighten(false);
                this.p.discardPile.removeCard(card);
                this.p.hand.refreshHandLayout();
                this.isDone = true;
                return;
            }
            AbstractDungeon.gridSelectScreen.open(tmp, this.amount, TEXT[0], false);
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
