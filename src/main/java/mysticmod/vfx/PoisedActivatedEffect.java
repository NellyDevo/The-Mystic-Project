package mysticmod.vfx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Interpolation;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import mysticmod.MysticMod;
import mysticmod.character.MysticAnimation;

public class PoisedActivatedEffect extends AbstractGameEffect {
    private float x;
    private float y;
    private static TextureAtlas.AtlasRegion img;
    private float endDuration;
    private Color startColor;
    private Color endColor;
    private boolean firstSoundPlayed = false;
    private boolean secondSoundPlayed = false;
    private boolean thirdLeg = false;
    private float scale = 0.0f;

    public PoisedActivatedEffect(float x, float y, float duration) {
        if (img == null) {
            img = new TextureAtlas.AtlasRegion(ImageMaster.loadImage("mysticmod/images/vfx/bookrune.jpg"), 0, 0, 1100, 1100);
        }
        this.x = x;
        this.y = y;
        startColor = Color.RED.cpy();
        endColor = Color.PURPLE.cpy();
        color = startColor.cpy();
        this.duration = 0.0f;
        endDuration = duration;
    }

    @Override
    public void update() {
        if (!firstSoundPlayed) {
            if (MysticMod.powerPoiseSfxToggle) {
                CardCrawlGame.sound.playV("mysticmod:BOOK_RUNE_ONE", 1.0f);
            }
            firstSoundPlayed = true;
        }
        float startingScale = 0.0f;
        float targetScale = 0.09f;
        float startingRotation = 0.0f;
        float targetRotation = 90.0f;
        float startingAlpha = 1.0f;
        float targetAlpha = 0.0f;
        x = MysticAnimation.bookX;
        y = MysticAnimation.bookY;
        if (duration < endDuration / 3) {
            //first third of animation
            scale = Interpolation.linear.apply(startingScale, targetScale, duration / (endDuration / 3));
        } else if (duration < endDuration * 2 / 3) {
            //second third of animation
            if (!secondSoundPlayed) {
                if (MysticMod.powerPoiseSfxToggle) {
                    CardCrawlGame.sound.playV("mysticmod:BOOK_RUNE_TWO", 0.75f);
                }
                secondSoundPlayed = true;
                scale = targetScale;
            }
            rotation = Interpolation.linear.apply(startingRotation, targetRotation, (duration - (endDuration / 3)) / (endDuration / 3));
            color.r = Interpolation.linear.apply(startColor.r, endColor.r, (duration - (endDuration / 3)) / (endDuration / 3));
            color.g = Interpolation.linear.apply(startColor.g, endColor.g, (duration - (endDuration / 3)) / (endDuration / 3));
            color.b = Interpolation.linear.apply(startColor.b, endColor.b, (duration - (endDuration / 3)) / (endDuration / 3));
        } else {
            //final third of animation
            if (!thirdLeg) { //just to make sure values are the target value
                rotation = targetRotation;
                color.r = endColor.r;
                color.g = endColor.g;
                color.b = endColor.b;
                thirdLeg = true;
            }
            color.a = Interpolation.linear.apply(startingAlpha, targetAlpha, (duration - (endDuration * 2 / 3)) / (endDuration / 3));
        }
        duration += Gdx.graphics.getDeltaTime();
        if (duration >= endDuration) {
            isDone = true;
        }
    }

    @Override
    public void render(final SpriteBatch sb) {
        sb.setBlendFunction(770, 1);
        sb.setColor(color);
        sb.draw(img, x - img.packedWidth / 2.0f, y - img.packedHeight / 2.0f, img.packedWidth / 2.0f, img.packedHeight/2.0f, img.packedWidth, img.packedHeight, scale, scale, rotation);
        sb.setBlendFunction(770, 771);
    }

    @Override
    public void dispose() {
    }
}
