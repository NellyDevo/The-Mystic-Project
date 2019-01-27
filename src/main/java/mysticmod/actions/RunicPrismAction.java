package mysticmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.unique.SwordBoomerangAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class RunicPrismAction extends AbstractGameAction {
    private AbstractCreature target;
    private DamageInfo info;

    public RunicPrismAction(AbstractCreature target, DamageInfo info) {
        actionType = ActionType.SPECIAL;
        this.info = info;
        this.target = target;
    }

    @Override
    public void update() {
        AbstractDungeon.actionManager.addToBottom(new SwordBoomerangAction(target, info, 1));
        isDone = true;
    }
}
