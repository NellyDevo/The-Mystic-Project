package mysticmod.vfx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.BorderLongFlashEffect;

public class DisintegrateEffect extends AbstractGameEffect {
    private float x;
    private float y;
    private static final float DUR = 1.0f;
    private static TextureAtlas.AtlasRegion img;
    private boolean playedSfx;

    public DisintegrateEffect(final float x, final float y) {
        playedSfx = false;
        if (DisintegrateEffect.img == null) {
            DisintegrateEffect.img = ImageMaster.vfxAtlas.findRegion("combat/laserThick");
        }
        this.x = x;
        this.y = y;
        color = Color.SKY.cpy();
        duration = DUR;
        startingDuration = DUR;
    }

    @Override
    public void update() {
        if (!playedSfx) {
            AbstractDungeon.effectsQueue.add(new BorderLongFlashEffect(Color.SKY));
            playedSfx = true;
            CardCrawlGame.sound.play("ATTACK_MAGIC_BEAM_SHORT");
            CardCrawlGame.screenShake.rumble(2.0f);
        }
        duration -= Gdx.graphics.getDeltaTime();
        if (duration > startingDuration / 2.0f) {
            color.a = Interpolation.pow2In.apply(1.0f, 0.0f, duration - 0.5f);
        }
        else {
            color.a = Interpolation.pow2Out.apply(0.0f, 1.0f, duration);
        }
        if (duration < 0.0f) {
            isDone = true;
        }
    }

    @Override
    public void render(final SpriteBatch sb) {
        sb.setBlendFunction(770, 1);
        sb.setColor(new Color(1.0f, 0.266f, 0.0f, color.a)); //0.5f, 0.7f, 1.0f
        sb.draw(DisintegrateEffect.img, x, y - DisintegrateEffect.img.packedHeight / 2.0f, 0.0f, DisintegrateEffect.img.packedHeight / 2.0f, DisintegrateEffect.img.packedWidth, DisintegrateEffect.img.packedHeight, scale * 2.0f + MathUtils.random(-0.05f, 0.05f), scale * 1.5f + MathUtils.random(-0.1f, 0.1f), MathUtils.random(-4.0f, 4.0f));
        sb.draw(DisintegrateEffect.img, x, y - DisintegrateEffect.img.packedHeight / 2.0f, 0.0f, DisintegrateEffect.img.packedHeight / 2.0f, DisintegrateEffect.img.packedWidth, DisintegrateEffect.img.packedHeight, scale * 2.0f + MathUtils.random(-0.05f, 0.05f), scale * 1.5f + MathUtils.random(-0.1f, 0.1f), MathUtils.random(-4.0f, 4.0f));
        sb.setColor(color);
        sb.draw(DisintegrateEffect.img, x, y - DisintegrateEffect.img.packedHeight / 2.0f, 0.0f, DisintegrateEffect.img.packedHeight / 2.0f, DisintegrateEffect.img.packedWidth, DisintegrateEffect.img.packedHeight, scale * 2.0f, scale / 2.0f, MathUtils.random(-2.0f, 2.0f));
        sb.draw(DisintegrateEffect.img, x, y - DisintegrateEffect.img.packedHeight / 2.0f, 0.0f, DisintegrateEffect.img.packedHeight / 2.0f, DisintegrateEffect.img.packedWidth, DisintegrateEffect.img.packedHeight, scale * 2.0f, scale / 2.0f, MathUtils.random(-2.0f, 2.0f));
        sb.setBlendFunction(770, 771);
    }

    @Override
    public void dispose() {
    }
}
