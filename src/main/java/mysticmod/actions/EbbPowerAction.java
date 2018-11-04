package mysticmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DiscardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import mysticmod.MysticMod;

public class EbbPowerAction extends AbstractGameAction {

    public EbbPowerAction() {
        this.actionType = ActionType.CARD_MANIPULATION;
        this.duration = Settings.ACTION_DUR_MED;
    }

    @Override
    public void update() {
        boolean hasSpell = false;
        boolean hasArte = false;
        int discardAmount = 2;
        for (final AbstractCard card : AbstractDungeon.player.hand.group) {
            if (MysticMod.isThisASpell(card)) {
                hasSpell = true;
            }
            if (MysticMod.isThisAnArte(card)) {
                hasArte = true;
            }
            if (hasSpell && hasArte) {
                this.isDone = true;
                return;
            }
        }
        if (hasSpell || hasArte) {
            discardAmount = 1;
        }
        AbstractDungeon.actionManager.addToBottom(new DiscardAction(AbstractDungeon.player, AbstractDungeon.player, discardAmount, false));
        this.isDone = true;
    }
}