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
import com.megacrit.cardcrawl.actions.common.DiscardAction;

public class EbbPowerAction extends AbstractGameAction {

    public EbbPowerAction() {
        this.actionType = ActionType.CARD_MANIPULATION;
        this.duration = Settings.ACTION_DUR_MED;
    }

    @Override
    public void update() {
        boolean hasSpell = false;
        boolean hasTechnique = false;
        for (final AbstractCard card : AbstractDungeon.player.hand.group) {
            if (card.rawDescription.startsWith("Spell.") || card.rawDescription.startsWith("Cantrip.")) {
                hasSpell = true;
            }
            if (card.rawDescription.startsWith("Technique.")) {
                hasTechnique = true;
            }
            //insert relic logic here in the future
            if (hasSpell && hasTechnique) {
                this.isDone = true;
                return;
            }
        }
        if (!hasSpell || !hasTechnique) {
            AbstractDungeon.actionManager.addToBottom(new DiscardAction(AbstractDungeon.player, AbstractDungeon.player, 2, false));
        }
        this.isDone = true;
        return;
    }
}