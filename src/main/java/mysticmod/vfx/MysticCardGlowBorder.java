package mysticmod.vfx;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.vfx.cardManip.CardGlowBorder;

public class MysticCardGlowBorder extends CardGlowBorder {
    public MysticCardGlowBorder(Color color, AbstractCard card) {
        super(card);
        this.color = color;
    }
}
