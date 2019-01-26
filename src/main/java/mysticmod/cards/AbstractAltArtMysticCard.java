package mysticmod.cards;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.evacipated.cardcrawl.modthespire.lib.SpireOverride;
import com.evacipated.cardcrawl.modthespire.lib.SpireSuper;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.cardManip.CardGlowBorder;
import mysticmod.vfx.MysticCardGlowBorder;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;

public abstract class AbstractAltArtMysticCard extends AbstractMysticCard {
    public Color altGlowColor;
    public boolean isArtAlternate;
    public String IMG_PATH;

    public AbstractAltArtMysticCard(final String id, final String name, final String img, final int cost, final String rawDescription,
                                    final CardType type, final CardColor color,
                                    final CardRarity rarity, final CardTarget target) {
        super(id, name, img, cost, rawDescription, type, color, rarity, target);
    }

    @SpireOverride
    protected void updateGlow() {
        if (isArtAlternate && AbstractDungeon.player != null) {
            updateGlowWithColor(altGlowColor.cpy());
        } else {
            SpireSuper.call();
        }
    }

    public void updateGlowWithColor(Color color) {
        try {
            Field reflectedGlowTimer = AbstractCard.class.getDeclaredField("glowTimer");
            Field reflectedGlowList = AbstractCard.class.getDeclaredField("glowList");
            reflectedGlowTimer.setAccessible(true);
            reflectedGlowList.setAccessible(true);
            if (this.isGlowing) {
                reflectedGlowTimer.set(this, (float)reflectedGlowTimer.get(this) - Gdx.graphics.getDeltaTime());
                if ((float)reflectedGlowTimer.get(this) < 0.0f) {
                    ((ArrayList<CardGlowBorder>)reflectedGlowList.get(this)).add(new MysticCardGlowBorder(color,this));
                    reflectedGlowTimer.set(this, 0.15f);
                }
            }
            final Iterator<CardGlowBorder> i = ((ArrayList<CardGlowBorder>)reflectedGlowList.get(this)).iterator();
            while (i.hasNext()) {
                CardGlowBorder e = i.next();
                e.update();
                if (e.isDone) {
                    e.dispose();
                    i.remove();
                }
            }
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void triggerOnEndOfPlayerTurn() {
        super.triggerOnEndOfPlayerTurn();
        if (this.isArtAlternate) {
            this.loadCardImage(IMG_PATH);
            this.isArtAlternate = false;
        }
    }
}
