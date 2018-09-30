package mysticmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.AbstractCreature;

public class SetDurationWaitAction extends AbstractGameAction {
    public SetDurationWaitAction(float setDur) {
        this.setValues((AbstractCreature)null, (AbstractCreature)null, 0);
        this.actionType = ActionType.WAIT;
    }

    public void update() {
        this.tickDuration();
    }
}