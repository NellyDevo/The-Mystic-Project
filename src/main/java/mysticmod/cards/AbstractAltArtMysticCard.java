package mysticmod.cards;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.cards.AbstractCard;

public abstract class AbstractAltArtMysticCard extends AbstractMysticCard {
    public Color altGlowColor;
    public boolean isArtAlternate;
    public String IMG_PATH;
    public static final Color ALT_GLOW_BLUE = Color.valueOf("005599");
    public static final Color ALT_GLOW_RED = Color.valueOf("FF5252");

    public AbstractAltArtMysticCard(String id, String name, String img, int cost, String rawDescription,
                                    CardType type, CardColor color,
                                    CardRarity rarity, CardTarget target) {
        super(id, name, img, cost, rawDescription, type, color, rarity, target);
    }

    @Override
    public void triggerOnGlowCheck() {
        if (isArtAlternate) {
            glowColor = altGlowColor.cpy();
        } else {
            glowColor = AbstractCard.BLUE_BORDER_GLOW_COLOR.cpy();
        }
    }

    @Override
    public void triggerOnEndOfPlayerTurn() {
        super.triggerOnEndOfPlayerTurn();
        if (isArtAlternate) {
            loadCardImage(IMG_PATH);
            isArtAlternate = false;
        }
    }
}
