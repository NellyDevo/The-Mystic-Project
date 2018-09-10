package mysticmod.vfx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.BorderLongFlashEffect;

public class DisintegrateEffect extends AbstractGameEffect
{
    private float x;
    private float y;
    private static final float DUR = 1.0f;
    private static TextureAtlas.AtlasRegion img;
    private boolean playedSfx;

    public DisintegrateEffect(final float x, final float y) {
        this.playedSfx = false;
        if (DisintegrateEffect.img == null) {
            DisintegrateEffect.img = ImageMaster.vfxAtlas.findRegion("combat/laserThick");
        }
        this.x = x + 80.0f * Settings.scale;
        this.y = y - 50.0f * Settings.scale;
        this.color = Color.SKY.cpy();
        this.duration = DUR;
        this.startingDuration = DUR;
    }

    @Override
    public void update() {
        if (!this.playedSfx) {
            AbstractDungeon.effectsQueue.add(new BorderLongFlashEffect(Color.SKY));
            this.playedSfx = true;
            CardCrawlGame.sound.play("ATTACK_MAGIC_BEAM_SHORT");
            CardCrawlGame.screenShake.rumble(2.0f);
        }
        this.duration -= Gdx.graphics.getDeltaTime();
        if (this.duration > this.startingDuration / 2.0f) {
            this.color.a = Interpolation.pow2In.apply(1.0f, 0.0f, this.duration - 0.5f);
        }
        else {
            this.color.a = Interpolation.pow2Out.apply(0.0f, 1.0f, this.duration);
        }
        if (this.duration < 0.0f) {
            this.isDone = true;
        }
    }

    @Override
    public void render(final SpriteBatch sb) {
        sb.setBlendFunction(770, 1);
        sb.setColor(new Color(1.0f, 0.266f, 0.0f, this.color.a)); //0.5f, 0.7f, 1.0f
        sb.draw(DisintegrateEffect.img, this.x, this.y - DisintegrateEffect.img.packedHeight / 2.0f, 0.0f, DisintegrateEffect.img.packedHeight / 2.0f, DisintegrateEffect.img.packedWidth, DisintegrateEffect.img.packedHeight, this.scale * 2.0f + MathUtils.random(-0.05f, 0.05f), this.scale * 1.5f + MathUtils.random(-0.1f, 0.1f), MathUtils.random(-4.0f, 4.0f));
        sb.draw(DisintegrateEffect.img, this.x, this.y - DisintegrateEffect.img.packedHeight / 2.0f, 0.0f, DisintegrateEffect.img.packedHeight / 2.0f, DisintegrateEffect.img.packedWidth, DisintegrateEffect.img.packedHeight, this.scale * 2.0f + MathUtils.random(-0.05f, 0.05f), this.scale * 1.5f + MathUtils.random(-0.1f, 0.1f), MathUtils.random(-4.0f, 4.0f));
        sb.setColor(this.color);
        sb.draw(DisintegrateEffect.img, this.x, this.y - DisintegrateEffect.img.packedHeight / 2.0f, 0.0f, DisintegrateEffect.img.packedHeight / 2.0f, DisintegrateEffect.img.packedWidth, DisintegrateEffect.img.packedHeight, this.scale * 2.0f, this.scale / 2.0f, MathUtils.random(-2.0f, 2.0f));
        sb.draw(DisintegrateEffect.img, this.x, this.y - DisintegrateEffect.img.packedHeight / 2.0f, 0.0f, DisintegrateEffect.img.packedHeight / 2.0f, DisintegrateEffect.img.packedWidth, DisintegrateEffect.img.packedHeight, this.scale * 2.0f, this.scale / 2.0f, MathUtils.random(-2.0f, 2.0f));
        sb.setBlendFunction(770, 771);
    }
}