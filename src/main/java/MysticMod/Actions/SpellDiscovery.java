/*package MysticMod.Actions;

import com.megacrit.cardcrawl.actions.*;
import com.megacrit.cardcrawl.cards.*;
import com.megacrit.cardcrawl.core.*;
import com.megacrit.cardcrawl.dungeons.*;
import com.megacrit.cardcrawl.vfx.cardManip.*;
import com.megacrit.cardcrawl.characters.*;
import java.util.*;

public class SpellDiscovery extends AbstractGameAction {
    private AbstractPlayer p;

    public SpellDiscovery(final int amount) {
        this.setValues(this.p = AbstractDungeon.player, this.p, amount);
        this.actionType = ActionType.CARD_MANIPULATION;
        this.duration = Settings.ACTION_DUR_MED;
    }

    @Override
    public void update() {
        if (this.duration != Settings.ACTION_DUR_MED) {
            if (AbstractDungeon.gridSelectScreen.selectedCards.size() != 0) {
                for (final AbstractCard chosenCard : AbstractDungeon.gridSelectScreen.selectedCards) {
                    chosenCard.unhover();
                    AbstractDungeon.player.limbo.group.add(chosenCard);
                    chosenCard.freeToPlayOnce = true;
                    if (this.p.hand.size() == 10) {
                        this.p.limbo.moveToDiscardPile(chosenCard);
                        this.p.createHandIsFullDialog();
                    } else {
                        this.p.limbo.removeCard(chosenCard);
                        this.p.hand.addToTop(chosenCard);
                    }
                    AbstractDungeon.gridSelectScreen.selectedCards.clear();
                    this.p.hand.refreshHandLayout();
                }
                this.tickDuration();
                return;
            }
            final CardGroup tmp = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
            AbstractCard nextCard;
            while (tmp.size() < 3) {
                nextCard = AbstractDungeon.returnTrulyRandomCard(AbstractDungeon.cardRandomRng);
                if (nextCard.rawDescription.startsWith("Spell.")) {
                    tmp.addToRandomSpot(nextCard.makeCopy());
                }
            }
            AbstractDungeon.gridSelectScreen.open(tmp, this.amount, "Choose a Spell to add to your hand", false);
            this.tickDuration();
        }

    }

}
*/
package MysticMod.Actions;

import com.megacrit.cardcrawl.actions.*;
import com.megacrit.cardcrawl.cards.*;
import com.megacrit.cardcrawl.core.*;
import com.megacrit.cardcrawl.dungeons.*;
import com.megacrit.cardcrawl.vfx.cardManip.*;
import MysticMod.Patches.IsDiscoveryLookingFor;

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
            IsDiscoveryLookingFor.Spells.set(AbstractDungeon.cardRewardScreen, "True");
            if (this.cardType == null) {
                AbstractDungeon.cardRewardScreen.discoveryOpen();
            }
            else {
                AbstractDungeon.cardRewardScreen.discoveryOpen(this.cardType);
            }
            IsDiscoveryLookingFor.Spells.set(AbstractDungeon.cardRewardScreen, "False");
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