package mysticmod.actions;

import basemod.ReflectionHacks;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ShuffleAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class ReboundCardAction extends AbstractGameAction {
    private AbstractCard card;
    private boolean shuffle;

    public ReboundCardAction(final AbstractCard card, boolean shuffle) {
        this.actionType = ActionType.SPECIAL;
        this.duration = Settings.ACTION_DUR_MED;
        this.card = card;
        this.shuffle = shuffle;
    }

    @Override
    public void update() {
        for (AbstractGameAction action : AbstractDungeon.actionManager.actions) {
            if (action instanceof UseCardAction) {
                AbstractCard reflectedCard = (AbstractCard)ReflectionHacks.getPrivate(action, UseCardAction.class, "targetCard");
                if (reflectedCard == this.card) {
                    ((UseCardAction)action).reboundCard = true;
                }
            }
        }
        if (this.shuffle) {
            AbstractDungeon.actionManager.addToBottom(new ShuffleAction(AbstractDungeon.player.drawPile, true));
        }
        this.isDone = true;
    }
}