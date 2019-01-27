package mysticmod.actions;

import basemod.BaseMod;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import mysticmod.MysticMod;

public class SpellRecallAction extends AbstractGameAction {
    private AbstractPlayer p;
    private static final String ID = "mysticmod:SpellRecallAction";
    private static final UIStrings ui = CardCrawlGame.languagePack.getUIString(ID);
    private static final String[] TEXT = ui.TEXT;

    public SpellRecallAction(int amount) {
        setValues(p = AbstractDungeon.player, AbstractDungeon.player, amount);
        actionType = ActionType.CARD_MANIPULATION;
    }

    @Override
    public void update() {
        if (p.hand.size() >= BaseMod.MAX_HAND_SIZE) {
            isDone = true;
            return;
        }
        if (duration == 0.5f) {
            //BEGIN make card group from discard pile
            CardGroup tmp = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
            for (AbstractCard discardedCard : p.discardPile.group) {
                if (MysticMod.isThisASpell(discardedCard)) {
                    tmp.addToRandomSpot(discardedCard);
                }
            }
            //END make card group from discard pile
            if (tmp.group.size() == 0) {
                isDone = true;
                return;
            }
            if (tmp.group.size() == 1) {
                AbstractCard card = tmp.group.get(0);
                p.hand.addToHand(card);
                card.lighten(false);
                p.discardPile.removeCard(card);
                p.hand.refreshHandLayout();
                isDone = true;
                return;
            }
            AbstractDungeon.gridSelectScreen.open(tmp, amount, TEXT[0], false);
            tickDuration();
            return;
        }
        if (AbstractDungeon.gridSelectScreen.selectedCards.size() != 0) {
            for (AbstractCard c : AbstractDungeon.gridSelectScreen.selectedCards) {
                p.hand.addToHand(c);
                p.discardPile.removeCard(c);
                c.lighten(false);
                c.unhover();
            }
            AbstractDungeon.gridSelectScreen.selectedCards.clear();
            p.hand.refreshHandLayout();
            for (AbstractCard c : p.discardPile.group) {
                c.unhover();
                c.target_x = CardGroup.DISCARD_PILE_X;
                c.target_y = 0.0f;
            }
            isDone = true;
        }
        tickDuration();
    }
}
