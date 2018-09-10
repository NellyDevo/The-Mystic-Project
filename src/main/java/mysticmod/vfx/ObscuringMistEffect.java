package mysticmod.vfx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.BorderFlashEffect;
import com.megacrit.cardcrawl.vfx.BorderLongFlashEffect;
import com.megacrit.cardcrawl.vfx.ExhaustEmberEffect;

public class ObscuringMistEffect extends AbstractGameEffect
{
    private float x;
    private float y;
    private static final float X_RADIUS = 200.0f * Settings.scale;
    private static final float Y_RADIUS = 250.0f * Settings.scale;
    private boolean flashedBorder;
    private Vector2 v;

    public ObscuringMistEffect(final float x, final float y) {
        this.flashedBorder = true;
        this.v = new Vector2(0.0f, 0.0f);
        this.duration = 0.5f;
        this.x = x;
        this.y = y;
        this.renderBehind = false;
    }
    
    @Override
    public void update() {
        if (this.flashedBorder) {
            CardCrawlGame.sound.play("ATTACK_FLAME_BARRIER", 0.05f);
            this.flashedBorder = false;
            AbstractDungeon.effectsQueue.add(new BorderFlashEffect(new Color(0.5f, 1.0f, 0.5f, 1.0f)));
            AbstractDungeon.effectsQueue.add(new BorderLongFlashEffect(new Color(0.5f, 0.4f, 0.1f, 1.0f)));
        }
        final float tmp = Interpolation.fade.apply(-209.0f, 30.0f, this.duration / 0.5f) * 0.017453292f;
        this.v.x = MathUtils.cos(tmp) * this.X_RADIUS;
        this.v.y = -MathUtils.sin(tmp) * this.Y_RADIUS;
        AbstractDungeon.effectsQueue.add(new MistParticleXLEffect(this.x + this.v.x + MathUtils.random(-30.0f, 30.0f) * Settings.scale, this.y + this.v.y + MathUtils.random(-10.0f, 10.0f) * Settings.scale));
        AbstractDungeon.effectsQueue.add(new MistParticleXLEffect(this.x + this.v.x + MathUtils.random(-30.0f, 30.0f) * Settings.scale, this.y + this.v.y + MathUtils.random(-10.0f, 10.0f) * Settings.scale));
        AbstractDungeon.effectsQueue.add(new MistParticleXLEffect(this.x + this.v.x + MathUtils.random(-30.0f, 30.0f) * Settings.scale, this.y + this.v.y + MathUtils.random(-10.0f, 10.0f) * Settings.scale));
        AbstractDungeon.effectsQueue.add(new ExhaustEmberEffect(this.x + this.v.x, this.y + this.v.y));
        this.duration -= Gdx.graphics.getDeltaTime();
        if (this.duration < 0.0f) {
            this.isDone = true;
        }
    }
    
    @Override
    public void render(final SpriteBatch sb) {
    }
}
