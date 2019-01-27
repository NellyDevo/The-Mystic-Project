package mysticmod.actions;

import basemod.BaseMod;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDiscardAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import mysticmod.MysticMod;

public class SpellDiscoveryAction extends AbstractGameAction {
    private boolean retrieveCard;
    private AbstractCard.CardType cardType;

    public SpellDiscoveryAction() {
        retrieveCard = false;
        cardType = null;
        actionType = ActionType.CARD_MANIPULATION;
        duration = Settings.ACTION_DUR_FAST;
    }

    @Override
    public void update() {
        if (duration == Settings.ACTION_DUR_FAST) {
            MysticMod.isDiscoveryLookingForSpells = true;
            if (cardType == null) {
                AbstractDungeon.cardRewardScreen.discoveryOpen();
            } else {
                AbstractDungeon.cardRewardScreen.discoveryOpen(cardType);
            }
            MysticMod.isDiscoveryLookingForSpells = false;
            tickDuration();
            return;
        }
        if (!retrieveCard) {
            if (AbstractDungeon.cardRewardScreen.discoveryCard != null) {
                AbstractCard disCard = AbstractDungeon.cardRewardScreen.discoveryCard.makeStatEquivalentCopy();
                disCard.current_x = -1000.0f * Settings.scale;
                if (AbstractDungeon.player.hand.size() < BaseMod.MAX_HAND_SIZE) {
                    AbstractDungeon.actionManager.addToTop(new MakeTempCardInHandAction(disCard, 1));
                } else {
                    AbstractDungeon.actionManager.addToTop(new MakeTempCardInDiscardAction(disCard, 1));
                }
                disCard.setCostForTurn(0);
                AbstractDungeon.cardRewardScreen.discoveryCard = null;
            }
            retrieveCard = true;
        }
        tickDuration();
    }
}
