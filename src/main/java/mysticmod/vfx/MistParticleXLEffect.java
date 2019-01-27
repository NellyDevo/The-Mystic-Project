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

public class MistParticleXLEffect extends AbstractGameEffect {
    private float x;
    private float y;
    private float vY;
    private static TextureAtlas.AtlasRegion[] imgs = new TextureAtlas.AtlasRegion[3];
    private TextureAtlas.AtlasRegion img;
    
    public MistParticleXLEffect(float x, float y) {
        if (MistParticleXLEffect.imgs[0] == null) {
            MistParticleXLEffect.imgs[0] = ImageMaster.vfxAtlas.findRegion("env/fire1");
            MistParticleXLEffect.imgs[1] = ImageMaster.vfxAtlas.findRegion("env/fire2");
            MistParticleXLEffect.imgs[2] = ImageMaster.vfxAtlas.findRegion("env/fire3");
        }
        duration = MathUtils.random(0.5f, 1.0f);
        startingDuration = duration;
        img = MistParticleXLEffect.imgs[MathUtils.random(MistParticleXLEffect.imgs.length - 1)];
        this.x = x - img.packedWidth / 2.0f + MathUtils.random(-3.0f, 3.0f) * Settings.scale;
        this.y = y - img.packedHeight / 2.0f;
        scale = Settings.scale * MathUtils.random(1.0f, 3.0f);
        vY = MathUtils.random(1.0f, 10.0f) * Settings.scale;
        vY *= vY;
        rotation = MathUtils.random(-20.0f, 20.0f);
        color = new Color(MathUtils.random(0.1f, 0.3f), MathUtils.random(0.5f, 0.9f), MathUtils.random(0.4f, 0.6f), 0.01f);
        renderBehind = false;
    }
    
    @Override
    public void update() {
        duration -= Gdx.graphics.getDeltaTime();
        if (duration < 0.0f) {
            isDone = true;
        }
        color.a = Interpolation.fade.apply(0.0f, 1.0f, duration / startingDuration);
        y += vY * Gdx.graphics.getDeltaTime() * 2.0f;
    }
    
    @Override
    public void render(SpriteBatch sb) {
        sb.setBlendFunction(770, 1);
        sb.setColor(color);
        sb.draw(img, x, y, img.packedWidth / 2.0f, img.packedHeight / 2.0f, img.packedWidth, img.packedHeight, scale, scale, rotation);
        sb.setBlendFunction(770, 771);
    }

    @Override
    public void dispose() {
    }
}
