package mysticmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class AddMyActionToBottomOfQueueLaterAction extends AbstractGameAction {
    private AbstractCard card;
    private boolean isRandom;

    public AddMyActionToBottomOfQueueLaterAction(final AbstractCard card, final boolean isRandom) {
        this.actionType = ActionType.SPECIAL;
        this.duration = Settings.ACTION_DUR_MED;
        this.isRandom = isRandom;
        this.card = card;
    }

    @Override
    public void update() {
        AbstractDungeon.actionManager.addToBottom(new SpecificCardFromDiscardToDrawPileAction(this.card, isRandom));
        this.isDone = true;
    }
}