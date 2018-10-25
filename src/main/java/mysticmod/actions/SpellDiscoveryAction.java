package mysticmod.actions;

import basemod.BaseMod;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDiscardAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import mysticmod.MysticMod;

public class SpellDiscoveryAction extends AbstractGameAction
{
    private boolean retrieveCard;
    private AbstractCard.CardType cardType;

    public SpellDiscoveryAction() {
        this.retrieveCard = false;
        this.cardType = null;
        this.actionType = ActionType.CARD_MANIPULATION;
        this.duration = Settings.ACTION_DUR_FAST;
    }

    @Override
    public void update() {
        if (this.duration == Settings.ACTION_DUR_FAST) {
            MysticMod.isDiscoveryLookingForSpells = true;
            if (this.cardType == null) {
                AbstractDungeon.cardRewardScreen.discoveryOpen();
            }
            else {
                AbstractDungeon.cardRewardScreen.discoveryOpen(this.cardType);
            }
            MysticMod.isDiscoveryLookingForSpells = false;
            this.tickDuration();
            return;
        }
        if (!this.retrieveCard) {
            if (AbstractDungeon.cardRewardScreen.discoveryCard != null) {
                final AbstractCard disCard = AbstractDungeon.cardRewardScreen.discoveryCard.makeStatEquivalentCopy();
                disCard.current_x = -1000.0f * Settings.scale;
                if (AbstractDungeon.player.hand.size() < BaseMod.MAX_HAND_SIZE) {
                    AbstractDungeon.actionManager.addToTop(new MakeTempCardInHandAction(disCard, 1));
                }
                else {
                    AbstractDungeon.actionManager.addToTop(new MakeTempCardInDiscardAction(disCard, 1));
                }
                disCard.setCostForTurn(0);
                AbstractDungeon.cardRewardScreen.discoveryCard = null;
            }
            this.retrieveCard = true;
        }
        this.tickDuration();
    }
}