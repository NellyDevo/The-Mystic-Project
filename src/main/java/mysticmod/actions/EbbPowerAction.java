package mysticmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DiscardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import mysticmod.cards.AbstractMysticCard;
import mysticmod.relics.CrystalBall;

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
            if ((card instanceof AbstractMysticCard && ((AbstractMysticCard)card).isSpell()) || card.rawDescription.startsWith("Cantrip.") || (AbstractDungeon.player.hasRelic(CrystalBall.ID) && card.type == AbstractCard.CardType.SKILL && !(card instanceof AbstractMysticCard && ((AbstractMysticCard)card).isTechnique()))) {
                hasSpell = true;
            }
            if ((card instanceof AbstractMysticCard && ((AbstractMysticCard)card).isTechnique()) || (AbstractDungeon.player.hasRelic(CrystalBall.ID) && card.type == AbstractCard.CardType.ATTACK && !(card instanceof AbstractMysticCard && ((AbstractMysticCard)card).isSpell()))) {
                hasTechnique = true;
            }
            //insert relic logic here in the future
            if (hasSpell && hasTechnique) {
                this.isDone = true;
                return;
            }
        }
        AbstractDungeon.actionManager.addToBottom(new DiscardAction(AbstractDungeon.player, AbstractDungeon.player, 2, false));
        this.isDone = true;
    }
}