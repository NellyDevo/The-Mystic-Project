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

    public AbstractAltArtMysticCard(String id, String name, String img, int cost, String rawDescription,
                                    CardType type, CardColor color,
                                    CardRarity rarity, CardTarget target) {
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
            if (isGlowing) {
                reflectedGlowTimer.set(this, (float)reflectedGlowTimer.get(this) - Gdx.graphics.getDeltaTime());
                if ((float)reflectedGlowTimer.get(this) < 0.0f) {
                    ((ArrayList<CardGlowBorder>)reflectedGlowList.get(this)).add(new MysticCardGlowBorder(color,this));
                    reflectedGlowTimer.set(this, 0.15f);
                }
            }
            Iterator<CardGlowBorder> i = ((ArrayList<CardGlowBorder>)reflectedGlowList.get(this)).iterator();
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
        if (isArtAlternate) {
            loadCardImage(IMG_PATH);
            isArtAlternate = false;
        }
    }
}
