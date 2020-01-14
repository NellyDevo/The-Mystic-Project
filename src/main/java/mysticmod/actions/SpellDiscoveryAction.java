package mysticmod.actions;

import basemod.BaseMod;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDiscardAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.screens.CardRewardScreen;
import mysticmod.MysticMod;

import java.util.ArrayList;

public class SpellDiscoveryAction extends AbstractGameAction {
    private boolean retrieveCard;

    public SpellDiscoveryAction() {
        retrieveCard = false;
        actionType = ActionType.CARD_MANIPULATION;
        duration = Settings.ACTION_DUR_FAST;
    }

    @Override
    public void update() {
        if (duration == Settings.ACTION_DUR_FAST) {
            ArrayList<AbstractCard> choices = generateCardList();
            AbstractDungeon.cardRewardScreen.customCombatOpen(choices, CardRewardScreen.TEXT[1], true);
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

    private ArrayList<AbstractCard> generateCardList() {
        ArrayList<AbstractCard> list = new ArrayList<>();
        while (list.size() != 3) {
            boolean dupe = false;
            AbstractCard c = MysticMod.returnTrulyRandomSpell();
            for (AbstractCard card : list) {
                if (card.cardID.equals(c.cardID)) {
                    dupe = true;
                    break;
                }
            }
            if (!dupe) {
                list.add(c);
            }
        }
        return list;
    }
}
