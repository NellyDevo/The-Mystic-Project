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
import mysticmod.MysticMod;

public class ArteHologram extends AbstractGameAction {
    private AbstractPlayer p;
    private int amount;
    private static final String ID = "mysticmod:ArteHologram";
    private static final UIStrings ui = CardCrawlGame.languagePack.getUIString(ID);
    private static final String[] TEXT = ui.TEXT;

    public ArteHologram(int amount) {
        setValues(p = AbstractDungeon.player, AbstractDungeon.player, amount);
        actionType = ActionType.CARD_MANIPULATION;
        duration = Settings.ACTION_DUR_MED;
        this.amount = amount;
    }

    @Override
    public void update() {
        int handSize = this.p.hand.size();
        if (handSize >= BaseMod.MAX_HAND_SIZE) {
            isDone = true;
            return;
        }
        int minAmount = BaseMod.MAX_HAND_SIZE - handSize;
        if (minAmount < amount) {
            amount = minAmount;
        }
        final CardGroup tmp = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
        for (AbstractCard c2 : p.discardPile.group) {
            if (MysticMod.isThisAnArte(c2)) {
                tmp.addToRandomSpot(c2);
            }
        }
        if (tmp.size() == 0) {
            isDone = true;
            return;
        } else if (tmp.size() <= amount) {
            for (AbstractCard tmpCard : tmp.group) {
                p.hand.addToHand(tmpCard);
                tmpCard.lighten(false);
                p.discardPile.removeCard(tmpCard);
                p.hand.refreshHandLayout();
            }
            isDone = true;
            return;
        }
        if (duration == 0.5f) {
            AbstractDungeon.gridSelectScreen.open(tmp, amount, true, TEXT[0] + amount + TEXT[1]);
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
