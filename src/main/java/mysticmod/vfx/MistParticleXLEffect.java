package mysticmod.vfx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

public class MistParticleXLEffect extends AbstractGameEffect
{
    private float x;
    private float y;
    private float vY;
    public static TextureAtlas.AtlasRegion[] imgs = new TextureAtlas.AtlasRegion[3];
    private TextureAtlas.AtlasRegion img;
    
    public MistParticleXLEffect(final float x, final float y) {
        if (MistParticleXLEffect.imgs[0] == null) {
            MistParticleXLEffect.imgs[0] = ImageMaster.vfxAtlas.findRegion("env/fire1");
            MistParticleXLEffect.imgs[1] = ImageMaster.vfxAtlas.findRegion("env/fire2");
            MistParticleXLEffect.imgs[2] = ImageMaster.vfxAtlas.findRegion("env/fire3");
        }
        this.duration = MathUtils.random(0.5f, 1.0f);
        this.startingDuration = this.duration;
        this.img = MistParticleXLEffect.imgs[MathUtils.random(MistParticleXLEffect.imgs.length - 1)];
        this.x = x - this.img.packedWidth / 2.0f + MathUtils.random(-3.0f, 3.0f) * Settings.scale;
        this.y = y - this.img.packedHeight / 2.0f;
        this.scale = Settings.scale * MathUtils.random(1.0f, 3.0f);
        this.vY = MathUtils.random(1.0f, 10.0f) * Settings.scale;
        this.vY *= this.vY;
        this.rotation = MathUtils.random(-20.0f, 20.0f);
        this.color = new Color(MathUtils.random(0.1f, 0.3f), MathUtils.random(0.5f, 0.9f), MathUtils.random(0.4f, 0.6f), 0.01f);
        this.renderBehind = false;
    }
    
    @Override
    public void update() {
        this.duration -= Gdx.graphics.getDeltaTime();
        if (this.duration < 0.0f) {
            this.isDone = true;
        }
        this.color.a = Interpolation.fade.apply(0.0f, 1.0f, this.duration / this.startingDuration);
        this.y += this.vY * Gdx.graphics.getDeltaTime() * 2.0f;
    }
    
    @Override
    public void render(final SpriteBatch sb) {
        sb.setBlendFunction(770, 1);
        sb.setColor(this.color);
        sb.draw(this.img, this.x, this.y, this.img.packedWidth / 2.0f, this.img.packedHeight / 2.0f, this.img.packedWidth, this.img.packedHeight, this.scale, this.scale, this.rotation);
        sb.setBlendFunction(770, 771);
    }

    @Override
    public void dispose() {
    }
}
