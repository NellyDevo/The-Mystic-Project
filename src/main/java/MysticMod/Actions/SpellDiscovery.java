package MysticMod.Actions;

import com.megacrit.cardcrawl.actions.*;
import com.megacrit.cardcrawl.cards.*;
import com.megacrit.cardcrawl.core.*;
import com.megacrit.cardcrawl.dungeons.*;
import com.megacrit.cardcrawl.vfx.cardManip.*;
import MysticMod.MysticMod;

public class SpellDiscovery extends AbstractGameAction
{
    private boolean retrieveCard;
    private AbstractCard.CardType cardType;

    public SpellDiscovery() {
        this.retrieveCard = false;
        this.cardType = null;
        this.actionType = ActionType.CARD_MANIPULATION;
        this.duration = Settings.ACTION_DUR_FAST;
    }

    public SpellDiscovery(final AbstractCard.CardType type) {
        this.retrieveCard = false;
        this.cardType = null;
        this.actionType = ActionType.CARD_MANIPULATION;
        this.duration = Settings.ACTION_DUR_FAST;
        this.cardType = type;
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
                if (AbstractDungeon.player.hand.size() < 10) {
                    AbstractDungeon.effectList.add(new ShowCardAndAddToHandEffect(disCard, Settings.WIDTH / 2.0f, Settings.HEIGHT / 2.0f));
                }
                else {
                    AbstractDungeon.effectList.add(new ShowCardAndAddToDiscardEffect(disCard, Settings.WIDTH / 2.0f, Settings.HEIGHT / 2.0f));
                }
                disCard.setCostForTurn(0);
                AbstractDungeon.cardRewardScreen.discoveryCard = null;
            }
            this.retrieveCard = true;
        }
        this.tickDuration();
    }
}