package mysticmod.actions;

import basemod.BaseMod;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import mysticmod.MysticMod;

public class SpellSeekAction extends AbstractGameAction {
    private AbstractPlayer p;
    private static final String ID = "mysticmod:SpellSeekAction";
    private static final UIStrings ui = CardCrawlGame.languagePack.getUIString(ID);
    private static final String[] TEXT = ui.TEXT;

    public SpellSeekAction(int amount) {
        setValues(p = AbstractDungeon.player, AbstractDungeon.player, amount);
        actionType = ActionType.CARD_MANIPULATION;
        duration = Settings.ACTION_DUR_MED;
    }

    @Override
    public void update() {
        if (duration != Settings.ACTION_DUR_MED) {
            if (AbstractDungeon.gridSelectScreen.selectedCards.size() != 0) {
                for (AbstractCard c : AbstractDungeon.gridSelectScreen.selectedCards) {
                    c.unhover();
                    if (p.hand.size() >= BaseMod.MAX_HAND_SIZE) {
                        p.drawPile.moveToDiscardPile(c);
                        p.createHandIsFullDialog();
                    } else {
                        p.drawPile.removeCard(c);
                        p.hand.addToTop(c);
                    }
                    p.hand.refreshHandLayout();
                    p.hand.applyPowers();
                }
                AbstractDungeon.gridSelectScreen.selectedCards.clear();
                p.hand.refreshHandLayout();
            }
            tickDuration();
            return;
        }
        CardGroup tmp = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
        for (AbstractCard c2 : p.drawPile.group) {
            if (MysticMod.isThisASpell(c2)) {
                tmp.addToRandomSpot(c2);
            }
        }
        if (tmp.size() == 0) {
            isDone = true;
            return;
        }
        if (tmp.size() == 1) {
            AbstractCard card = tmp.getTopCard();
            if (p.hand.size() >= BaseMod.MAX_HAND_SIZE) {
                p.drawPile.moveToDiscardPile(card);
                p.createHandIsFullDialog();
            } else {
                card.unhover();
                card.lighten(true);
                card.setAngle(0.0f);
                card.drawScale = 0.12f;
                card.targetDrawScale = 0.75f;
                card.current_x = CardGroup.DRAW_PILE_X;
                card.current_y = CardGroup.DRAW_PILE_Y;
                p.drawPile.removeCard(card);
                AbstractDungeon.player.hand.addToTop(card);
                AbstractDungeon.player.hand.refreshHandLayout();
                AbstractDungeon.player.hand.applyPowers();
            }
            isDone = true;
            return;
        }
        if (tmp.size() <= amount) {
            for (int i = 0; i < tmp.size(); ++i) {
                AbstractCard card2 = tmp.getNCardFromTop(i);
                if (p.hand.size() >= BaseMod.MAX_HAND_SIZE) {
                    p.drawPile.moveToDiscardPile(card2);
                    p.createHandIsFullDialog();
                } else {
                    card2.unhover();
                    card2.lighten(true);
                    card2.setAngle(0.0f);
                    card2.drawScale = 0.12f;
                    card2.targetDrawScale = 0.75f;
                    card2.current_x = CardGroup.DRAW_PILE_X;
                    card2.current_y = CardGroup.DRAW_PILE_Y;
                    p.drawPile.removeCard(card2);
                    AbstractDungeon.player.hand.addToTop(card2);
                    AbstractDungeon.player.hand.refreshHandLayout();
                    AbstractDungeon.player.hand.applyPowers();
                }
            }
            isDone = true;
            return;
        }
        String uiText = TEXT[0];
        if (amount == 1) {
            uiText += TEXT[1];
        } else {
            uiText += amount + TEXT[2];
        }
        uiText += TEXT[3];
        AbstractDungeon.gridSelectScreen.open(tmp, amount, uiText, false);
        tickDuration();
    }
}
