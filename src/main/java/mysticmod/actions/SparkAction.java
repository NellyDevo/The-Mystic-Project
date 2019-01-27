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
import com.megacrit.cardcrawl.powers.NoDrawPower;
import com.megacrit.cardcrawl.vfx.PlayerTurnEffect;
import mysticmod.MysticMod;

public class SparkAction extends AbstractGameAction {
    private boolean shuffleCheck;

    public SparkAction(AbstractCreature source, int amount, boolean endTurnDraw) {
        shuffleCheck = false;
        if (endTurnDraw) {
            AbstractDungeon.topLevelEffects.add(new PlayerTurnEffect());
        } else if (AbstractDungeon.player.hasPower(NoDrawPower.POWER_ID)) {
            AbstractDungeon.player.getPower(NoDrawPower.POWER_ID).flash();
            setValues(AbstractDungeon.player, source, amount);
            isDone = true;
            duration = 0.0f;
            actionType = ActionType.WAIT;
            return;
        }
        setValues(AbstractDungeon.player, source, amount);
        actionType = ActionType.DRAW;
        if (Settings.FAST_MODE) {
            duration = Settings.ACTION_DUR_XFAST;
        } else {
            duration = Settings.ACTION_DUR_FASTER;
        }
    }

    public SparkAction(AbstractCreature source, int amount) {
        this(source, amount, false);
    }
    
    @Override
    public void update() {
        if (amount <= 0) {
            isDone = true;
            return;
        }
        int deckSize = AbstractDungeon.player.drawPile.size();
        int discardSize = AbstractDungeon.player.discardPile.size();
        if (SoulGroup.isActive()) {
            return;
        }
        if (deckSize + discardSize == 0) {
            isDone = true;
            return;
        }
        if (AbstractDungeon.player.hand.size() >= BaseMod.MAX_HAND_SIZE) {
            AbstractDungeon.player.createHandIsFullDialog();
            isDone = true;
            return;
        }
        if (!shuffleCheck) {
            if (amount > deckSize) {
                int tmp = amount - deckSize;
                AbstractDungeon.actionManager.addToTop(new SparkAction(AbstractDungeon.player, tmp));
                AbstractDungeon.actionManager.addToTop(new EmptyDeckShuffleAction());
                if (deckSize != 0) {
                    AbstractDungeon.actionManager.addToTop(new SparkAction(AbstractDungeon.player, deckSize));
                }
                amount = 0;
                isDone = true;
            }
            shuffleCheck = true;
        }
        duration -= Gdx.graphics.getDeltaTime();
        if (amount != 0 && duration < 0.0f) {
            if (Settings.FAST_MODE) {
                duration = Settings.ACTION_DUR_XFAST;
            } else {
                duration = Settings.ACTION_DUR_FASTER;
            }
            --amount;
            if (!AbstractDungeon.player.drawPile.isEmpty()) {
                AbstractCard sparkedCard = AbstractDungeon.player.drawPile.getTopCard();
                AbstractDungeon.player.draw();
                AbstractDungeon.player.hand.refreshHandLayout();
                if (MysticMod.isThisAnArte(sparkedCard) || MysticMod.isThisASpell(sparkedCard)) {
                    AbstractDungeon.actionManager.addToTop(new GainEnergyAction(1));
                }
            }
            if (amount == 0) {
                isDone = true;
            }
        }
    }
}
