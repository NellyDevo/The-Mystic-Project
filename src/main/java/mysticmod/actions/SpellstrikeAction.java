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

    public SpellstrikeAction(final int amount, final AbstractMonster target, final boolean exhausts) {
        this.setValues(this.t = target, this.p = AbstractDungeon.player, amount);
        this.actionType = ActionType.CARD_MANIPULATION;
        this.duration = Settings.ACTION_DUR_MED;
        this.exhaustCards = exhausts;
    }

    @Override
    public void update() {
        if (this.duration == Settings.ACTION_DUR_MED) { //what do to open screen
            CardGroup tmp = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
            Iterator var5 = this.p.drawPile.group.iterator();
            AbstractCard card;
            while(var5.hasNext()) {
                card = (AbstractCard)var5.next();
                if (card.type == AbstractCard.CardType.ATTACK && MysticMod.isThisASpell(card)) {
                    tmp.addToRandomSpot(card);
                }
            }
            if (tmp.size() == 0) {
                this.isDone = true;
            } else if (tmp.size() == 1) { //what do if only one available target
                card = tmp.getTopCard();
                playCard(card);
                this.isDone = true;
            } else {
                AbstractDungeon.gridSelectScreen.open(tmp, this.amount, TEXT[0], false);
                this.tickDuration();
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
        card.exhaustOnUseOnce = this.exhaustCards;
        AbstractDungeon.player.limbo.group.add(card);
        card.current_y = -200.0f * Settings.scale;
        card.target_x = Settings.WIDTH / 2.0f + 200.0f * Settings.scale;
        card.target_y = Settings.HEIGHT / 2.0f;
        card.targetAngle = 0.0f;
        card.lighten(false);
        card.drawScale = 0.12f;
        card.targetDrawScale = 0.75f;
        if (!card.canUse(AbstractDungeon.player, this.t)) {
            if (this.exhaustCards) {
                AbstractDungeon.actionManager.addToTop(new ExhaustSpecificCardAction(card, AbstractDungeon.player.limbo));
            } else {
                AbstractDungeon.actionManager.addToTop(new UnlimboAction(card));
                AbstractDungeon.actionManager.addToTop(new DiscardSpecificCardAction(card, AbstractDungeon.player.limbo));
                AbstractDungeon.actionManager.addToTop(new WaitAction(0.4f));
            }
        } else {
            card.applyPowers();
            AbstractDungeon.actionManager.addToTop(new QueueCardAction(card, this.t));
            AbstractDungeon.actionManager.addToTop(new UnlimboAction(card));
            if (!Settings.FAST_MODE) {
                AbstractDungeon.actionManager.addToTop(new WaitAction(Settings.ACTION_DUR_MED));
            } else {
                AbstractDungeon.actionManager.addToTop(new WaitAction(Settings.ACTION_DUR_FASTER));
            }
        }
    }
}
