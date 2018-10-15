package mysticmod.actions;

import basemod.BaseMod;
import com.badlogic.gdx.Gdx;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.EmptyDeckShuffleAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.SoulGroup;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.PlayerTurnEffect;
import mysticmod.MysticMod;

public class SparkAction extends AbstractGameAction {
    private boolean shuffleCheck;

    public SparkAction(final AbstractCreature source, final int amount, final boolean endTurnDraw) {
        this.shuffleCheck = false;
        if (endTurnDraw) {
            AbstractDungeon.topLevelEffects.add(new PlayerTurnEffect());
        }
        else if (AbstractDungeon.player.hasPower("No Draw")) {
            AbstractDungeon.player.getPower("No Draw").flash();
            this.setValues(AbstractDungeon.player, source, amount);
            this.isDone = true;
            this.duration = 0.0f;
            this.actionType = ActionType.WAIT;
            return;
        }
        this.setValues(AbstractDungeon.player, source, amount);
        this.actionType = ActionType.DRAW;
        if (Settings.FAST_MODE) {
            this.duration = Settings.ACTION_DUR_XFAST;
        }
        else {
            this.duration = Settings.ACTION_DUR_FASTER;
        }
    }

    public SparkAction(final AbstractCreature source, final int amount) {
        this(source, amount, false);
    }
    
    @Override
    public void update() {
        if (this.amount <= 0) {
            this.isDone = true;
            return;
        }
        final int deckSize = AbstractDungeon.player.drawPile.size();
        final int discardSize = AbstractDungeon.player.discardPile.size();
        if (SoulGroup.isActive()) {
            return;
        }
        if (deckSize + discardSize == 0) {
            this.isDone = true;
            return;
        }
        if (AbstractDungeon.player.hand.size() >= BaseMod.MAX_HAND_SIZE) {
            AbstractDungeon.player.createHandIsFullDialog();
            this.isDone = true;
            return;
        }
        if (!this.shuffleCheck) {
            if (this.amount > deckSize) {
                final int tmp = this.amount - deckSize;
                AbstractDungeon.actionManager.addToTop(new SparkAction(AbstractDungeon.player, tmp));
                AbstractDungeon.actionManager.addToTop(new EmptyDeckShuffleAction());
                if (deckSize != 0) {
                    AbstractDungeon.actionManager.addToTop(new SparkAction(AbstractDungeon.player, deckSize));
                }
                this.amount = 0;
                this.isDone = true;
            }
            this.shuffleCheck = true;
        }
        this.duration -= Gdx.graphics.getDeltaTime();
        if (this.amount != 0 && this.duration < 0.0f) {
            if (Settings.FAST_MODE) {
                this.duration = Settings.ACTION_DUR_XFAST;
            }
            else {
                this.duration = Settings.ACTION_DUR_FASTER;
            }
            --this.amount;
            if (!AbstractDungeon.player.drawPile.isEmpty()) {
                AbstractCard sparkedCard = AbstractDungeon.player.drawPile.getTopCard();
                AbstractDungeon.player.draw();
                AbstractDungeon.player.hand.refreshHandLayout();
                if (MysticMod.isThisAnArte(sparkedCard) || MysticMod.isThisASpell(sparkedCard)) {
                    AbstractDungeon.actionManager.addToTop(new GainEnergyAction(1));
                }
            }
            if (this.amount == 0) {
                this.isDone = true;
            }
        }
    }
}