package mysticmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import mysticmod.cards.AbstractMysticCard;
import mysticmod.relics.CrystalBall;

public class SparkAction extends AbstractGameAction {

    public SparkAction() {
        this.actionType = ActionType.SPECIAL;
        this.duration = Settings.ACTION_DUR_MED;
    }

    @Override
    public void update() {
        AbstractCard drawnCard = AbstractDungeon.player.hand.group.get(AbstractDungeon.player.hand.group.size()-1);
        if ((drawnCard instanceof AbstractMysticCard && (((AbstractMysticCard)drawnCard).isSpell() || ((AbstractMysticCard)drawnCard).isTechnique())) || ((AbstractDungeon.player.hasRelic(CrystalBall.ID)) && (drawnCard.type == AbstractCard.CardType.SKILL || drawnCard.type == AbstractCard.CardType.ATTACK))) {
            AbstractDungeon.actionManager.addToTop(new GainEnergyAction(1));
        }
        this.isDone = true;
    }
}