package mysticmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndAddToDiscardEffect;

public class ReplaceCardAction extends AbstractGameAction {
    private AbstractCard originalCard;
    private AbstractCard newCard;

    public ReplaceCardAction(AbstractCard originalCard, AbstractCard newCard) {
        actionType = ActionType.SPECIAL;
        this.originalCard = originalCard;
        this.newCard = newCard;
    }

    @Override
    public void update() {
        originalCard.target_x = Settings.WIDTH / 2.0f - 150.0f * Settings.scale;
        AbstractDungeon.effectList.add(new ShowCardAndAddToDiscardEffect(newCard, Settings.WIDTH / 2.0f + 150.0f * Settings.scale, Settings.HEIGHT / 2.0f));
        isDone = true;
    }
}
