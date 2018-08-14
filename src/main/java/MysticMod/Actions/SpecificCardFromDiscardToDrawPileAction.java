package MysticMod.Actions;

import com.megacrit.cardcrawl.actions.*;
import com.megacrit.cardcrawl.localization.*;
import com.megacrit.cardcrawl.characters.*;
import com.megacrit.cardcrawl.dungeons.*;
import com.megacrit.cardcrawl.cards.*;
import java.util.*;
import com.megacrit.cardcrawl.core.*;
import com.megacrit.cardcrawl.monsters.*;
import com.megacrit.cardcrawl.cards.CardGroup;

public class SpecificCardFromDiscardToDrawPileAction extends AbstractGameAction {
    private boolean isRandom;
    private AbstractCard card;

    public SpecificCardFromDiscardToDrawPileAction(final AbstractCard card, final boolean isRandom) {
        this.actionType = ActionType.CARD_MANIPULATION;
        this.duration = Settings.ACTION_DUR_MED;
        this.card = card;
        this.isRandom = isRandom;
    }

    @Override
    public void update() {
        AbstractDungeon.player.discardPile.moveToDeck(this.card, this.isRandom);
        AbstractDungeon.player.discardPile.removeCard(this.card);
        this.isDone = true;
    }
}