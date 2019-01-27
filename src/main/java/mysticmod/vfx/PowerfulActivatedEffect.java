package mysticmod.vfx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import mysticmod.character.MysticAnimation;

public class PowerfulActivatedEffect extends AbstractGameEffect {
    private float x;
    private float y;
    private static TextureAtlas.AtlasRegion[] img;
    private int animFrame = 0;
    private float endDuration;
    private Color startColor;
    private Color endColor;
    private boolean soundPlayed = false;

    public PowerfulActivatedEffect(float x, float y, float duration) {
        if (img == null) {
            img = new TextureAtlas.AtlasRegion[5];
            for (int i = 0; i < 5; i++) {
                img[i] = new TextureAtlas.AtlasRegion(ImageMaster.loadImage("mysticmod/images/vfx/electricanimation.png"), i*48, 0, 48, 48);
            }
        }
        this.x = x;
        this.y = y;
        startColor = Color.CYAN.cpy();
        endColor = Color.PURPLE.cpy();
        color = startColor.cpy();
        this.duration = 0.0f;
        endDuration = duration;
        rotation = MysticAnimation.swordAngle;
    }

    @Override
    public void update() {
        if (!soundPlayed) {
            CardCrawlGame.sound.playV("mysticmod:SPARKS", 0.3f);
            soundPlayed = true;
        }
        if (animFrame == 4) {
            animFrame = 0;
        } else {
            animFrame++;
        }
        rotation = MysticAnimation.swordAngle;
        x = MysticAnimation.swordX;
        y = MysticAnimation.swordY;
        Vector2 tmp = new Vector2(MathUtils.cosDeg(rotation), MathUtils.sinDeg(rotation));
        float distance;
        if (duration < endDuration/2) {
            distance = Interpolation.linear.apply(0.0f, 100.0f * Settings.scale, duration / (endDuration / 2));
        } else {
            distance = Interpolation.linear.apply(100.0f * Settings.scale, 0.0f, (duration - (endDuration / 2)) / (endDuration / 2));
        }
        x += tmp.x * distance;
        y += tmp.y * distance;
        duration += Gdx.graphics.getDeltaTime();
        color.r = Interpolation.linear.apply(startColor.r, endColor.r, duration / endDuration);
        color.g = Interpolation.linear.apply(startColor.g, endColor.g, duration / endDuration);
        color.b = Interpolation.linear.apply(startColor.b, endColor.b, duration / endDuration);
        if (duration >= endDuration) {
            isDone = true;
        }
    }

    @Override
    public void render(final SpriteBatch sb) {
        sb.setBlendFunction(770, 1);
        sb.setColor(color);
        sb.draw(img[animFrame], x, y, img[animFrame].packedWidth / 2.0f, img[animFrame].packedHeight/2.0f, img[animFrame].packedWidth, img[animFrame].packedHeight, MathUtils.random(0.8f, 1.2f), MathUtils.random(0.8f, 1.2f), MathUtils.random(360.0f));
        sb.setBlendFunction(770, 771);
    }

    @Override
    public void dispose() {
    }
}
