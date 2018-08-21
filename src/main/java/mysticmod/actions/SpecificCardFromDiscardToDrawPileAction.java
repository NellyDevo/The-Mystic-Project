package mysticmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class SpecificCardFromDiscardToDrawPileAction extends AbstractGameAction {
    private boolean isRandom;
    private AbstractCard card;

    public SpecificCardFromDiscardToDrawPileAction(final AbstractCard card, final boolean isRandom) {
        this.actionType = ActionType.CARD_MANIPULATION;
        this.duration = Settings.ACTION_DUR_MED;
        this.card = card;
        this.isRandom = isRandom;
    }

    @Override
    public void update() {
        AbstractDungeon.player.discardPile.moveToDeck(this.card, this.isRandom);
        AbstractDungeon.player.discardPile.removeCard(this.card);
        this.isDone = true;
    }
}