package mysticmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DiscardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import mysticmod.MysticMod;

public class EbbPowerAction extends AbstractGameAction {

    public EbbPowerAction() {
        actionType = ActionType.CARD_MANIPULATION;
        duration = Settings.ACTION_DUR_MED;
    }

    @Override
    public void update() {
        boolean hasSpell = false;
        boolean hasArte = false;
        int discardAmount = 2;
        for (AbstractCard card : AbstractDungeon.player.hand.group) {
            if (MysticMod.isThisASpell(card)) {
                hasSpell = true;
            }
            if (MysticMod.isThisAnArte(card)) {
                hasArte = true;
            }
            if (hasSpell && hasArte) {
                isDone = true;
                return;
            }
        }
        if (hasSpell || hasArte) {
            discardAmount = 1;
        }
        AbstractDungeon.actionManager.addToBottom(new DiscardAction(AbstractDungeon.player, AbstractDungeon.player, discardAmount, false));
        isDone = true;
    }
}
