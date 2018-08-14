package MysticMod.Actions;

import com.megacrit.cardcrawl.actions.*;
import com.megacrit.cardcrawl.localization.*;
import com.megacrit.cardcrawl.characters.*;
import com.megacrit.cardcrawl.dungeons.*;
import com.megacrit.cardcrawl.cards.*;
import java.util.*;
import com.megacrit.cardcrawl.core.*;
import com.megacrit.cardcrawl.monsters.*;
import com.megacrit.cardcrawl.cards.CardGroup;
import MysticMod.Actions.SpecificCardFromDiscardToDrawPileAction;

public class AddMyActionToBottomOfQueueLaterAction extends AbstractGameAction {
    private AbstractGameAction actionToAdd;
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