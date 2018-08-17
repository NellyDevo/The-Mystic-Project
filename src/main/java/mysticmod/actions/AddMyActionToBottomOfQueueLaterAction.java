package mysticmod.actions;

import com.megacrit.cardcrawl.actions.*;
import com.megacrit.cardcrawl.dungeons.*;
import com.megacrit.cardcrawl.cards.*;
import com.megacrit.cardcrawl.core.*;

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