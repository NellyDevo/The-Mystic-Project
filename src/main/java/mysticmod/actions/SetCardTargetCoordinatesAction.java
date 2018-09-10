package mysticmod.actions;

import basemod.ReflectionHacks;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.combat.LightningEffect;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SetCardTargetCoordinatesAction extends AbstractGameAction {
    private AbstractCard card;
    private float destinationX;
    private float destinationY;

    public SetCardTargetCoordinatesAction(AbstractCard card, float destinationX, float destinationY) { //set a coordinate to -1 to use original coordinate
        this.actionType = ActionType.SPECIAL;
        this.duration = Settings.ACTION_DUR_MED;
        this.card = card;
        this.destinationX = destinationX;
        this.destinationY = destinationY;
    }

    @Override
    public void update() {
        if (destinationX != -1.0f) {
            card.target_x = destinationX;
        }
        if (destinationY != -1.0f) {
            card.target_y = destinationY;
        }
        this.isDone = true;
    }
}