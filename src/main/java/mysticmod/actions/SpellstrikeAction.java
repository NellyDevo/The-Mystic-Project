package mysticmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DiscardSpecificCardAction;
import com.megacrit.cardcrawl.actions.common.ExhaustSpecificCardAction;
import com.megacrit.cardcrawl.actions.utility.QueueCardAction;
import com.megacrit.cardcrawl.actions.utility.UnlimboAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import mysticmod.MysticMod;

import java.util.Iterator;

public class SpellstrikeAction extends AbstractGameAction {
    private AbstractPlayer p;
    private boolean exhaustCards;
    private AbstractMonster t;
    private static final String ID = "mysticmod:SpellstrikeAction";
    private static final UIStrings ui = CardCrawlGame.languagePack.getUIString(ID);
    private static final String[] TEXT = ui.TEXT;

    public SpellstrikeAction(int amount, AbstractMonster target, boolean exhausts) {
        setValues(t = target, p = AbstractDungeon.player, amount);
        actionType = ActionType.CARD_MANIPULATION;
        duration = Settings.ACTION_DUR_MED;
        exhaustCards = exhausts;
    }

    @Override
    public void update() {
        if (duration == Settings.ACTION_DUR_MED) { //what do to open screen
            CardGroup tmp = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
            Iterator var5 = p.drawPile.group.iterator();
            AbstractCard card;
            while(var5.hasNext()) {
                card = (AbstractCard)var5.next();
                if (card.type == AbstractCard.CardType.ATTACK && MysticMod.isThisASpell(card)) {
                    tmp.addToRandomSpot(card);
                }
            }
            if (tmp.size() == 0) {
                isDone = true;
            } else if (tmp.size() == 1) { //what do if only one available target
                card = tmp.getTopCard();
                playCard(card);
                isDone = true;
            } else {
                AbstractDungeon.gridSelectScreen.open(tmp, amount, TEXT[0], false);
                tickDuration();
            }
        } else { //what do when player chooses a card
            if (AbstractDungeon.gridSelectScreen.selectedCards.size() != 0) {
                AbstractCard card = AbstractDungeon.gridSelectScreen.selectedCards.get(0);
                playCard(card);
                AbstractDungeon.gridSelectScreen.selectedCards.clear();// 106
            }
            this.tickDuration();
        }
    }

    private void playCard(AbstractCard card) {
        card.unhover();
        AbstractDungeon.player.drawPile.group.remove(card);
        AbstractDungeon.getCurrRoom().souls.remove(card);
        card.freeToPlayOnce = true;
        card.exhaustOnUseOnce = exhaustCards;
        AbstractDungeon.player.limbo.group.add(card);
        card.current_y = -200.0f * Settings.scale;
        card.target_x = Settings.WIDTH / 2.0f + 200.0f * Settings.scale;
        card.target_y = Settings.HEIGHT / 2.0f;
        card.targetAngle = 0.0f;
        card.lighten(false);
        card.drawScale = 0.12f;
        card.targetDrawScale = 0.75f;
        if (!card.canUse(AbstractDungeon.player, t)) {
            if (exhaustCards) {
                AbstractDungeon.actionManager.addToTop(new ExhaustSpecificCardAction(card, AbstractDungeon.player.limbo));
            } else {
                AbstractDungeon.actionManager.addToTop(new UnlimboAction(card));
                AbstractDungeon.actionManager.addToTop(new DiscardSpecificCardAction(card, AbstractDungeon.player.limbo));
                AbstractDungeon.actionManager.addToTop(new WaitAction(0.4f));
            }
        } else {
            card.applyPowers();
            AbstractDungeon.actionManager.addToTop(new QueueCardAction(card, t));
            AbstractDungeon.actionManager.addToTop(new UnlimboAction(card));
            if (!Settings.FAST_MODE) {
                AbstractDungeon.actionManager.addToTop(new WaitAction(Settings.ACTION_DUR_MED));
            } else {
                AbstractDungeon.actionManager.addToTop(new WaitAction(Settings.ACTION_DUR_FASTER));
            }
        }
    }
}
