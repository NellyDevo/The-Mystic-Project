package mysticmod.vfx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

public class MagicMissileTrailEffect extends AbstractGameEffect {
    private static final float EFFECT_DUR = 0.4f;
    private float x;
    private float y;
    private static TextureAtlas.AtlasRegion img;
    private Color effectColor;

    public MagicMissileTrailEffect(float x, float y, Color effectColor) {
        if (MagicMissileTrailEffect.img == null) {
            MagicMissileTrailEffect.img = ImageMaster.vfxAtlas.findRegion("combat/blurDot");
        }
        renderBehind = false;
        duration = EFFECT_DUR;
        startingDuration = EFFECT_DUR;
        this.x = x - MagicMissileTrailEffect.img.packedWidth / 2.0f;
        this.y = y - MagicMissileTrailEffect.img.packedHeight / 2.0f;
        rotation = 0.0f;
        scale = 0.01f;
        this.effectColor = effectColor;
    }

    @Override
    public void update() {
        duration -= Gdx.graphics.getDeltaTime();
        if (duration < 0.2f) {
            scale = duration * Settings.scale * 11.0f;
        }
        else {
            scale = (duration - 0.2f) * Settings.scale * 11.0f;
        }
        if (duration < 0.0f) {
            isDone = true;
        }
    }

    @Override
    public void render(SpriteBatch sb) {
        Color tmp = effectColor.cpy();
        tmp.set(tmp.r, tmp.g, tmp.b, 1.0F - (0.8f - (duration * 2)));
        sb.setColor(tmp);
        sb.draw(MagicMissileTrailEffect.img, x, y, MagicMissileTrailEffect.img.packedWidth / 2.0f, MagicMissileTrailEffect.img.packedHeight / 2.0f, MagicMissileTrailEffect.img.packedWidth, MagicMissileTrailEffect.img.packedHeight, scale, scale, rotation);
        sb.setBlendFunction(770, 1);
        sb.setColor(new Color(0.0F, 0.0F, 1.0F, 1.0F - (0.8f - (duration * 2))));
        sb.draw(MagicMissileTrailEffect.img, x, y, MagicMissileTrailEffect.img.packedWidth / 2.0f, MagicMissileTrailEffect.img.packedHeight / 2.0f, MagicMissileTrailEffect.img.packedWidth, MagicMissileTrailEffect.img.packedHeight, scale * 1.7f, scale * 1.7f, rotation);
        sb.setBlendFunction(770, 771);
    }

    static {
        MagicMissileTrailEffect.img = null;
    }

    @Override
    public void dispose() {
    }
}
