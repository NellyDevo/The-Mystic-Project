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
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import mysticmod.actions.MagicMissileAction;

public class MagicMissileEffect extends AbstractGameEffect {
    private static final float EFFECT_DUR = 1.0f;
    private float x;
    private float y;
    private float targetX;
    private float targetY;
    private float straightStartX;
    private float straightStartY;
    private float speed;
    private float initialRotation;
    private TextureAtlas.AtlasRegion img;
    private boolean soundPlayed = false;
    private MagicMissileAction parentAction;
    private Color effectColor;

    public MagicMissileEffect(float originX, float originY, float targetX, float targetY, Color effectColor) {
        this(originX, originY, targetX, targetY, null, effectColor);
    }

    public MagicMissileEffect(float originX, float originY, float targetX, float targetY, MagicMissileAction parentAction, Color effectColor) {
        img = ImageMaster.WOBBLY_LINE;
        rotation = MathUtils.random(180.0f) - 90.0f;
        initialRotation = rotation;
        x = originX - img.packedWidth / 2.0f;
        y = originY - img.packedHeight / 2.0f;
        this.targetX = targetX - img.packedWidth / 2.0f;
        this.targetY = targetY - img.packedHeight / 2.0f;
        duration = EFFECT_DUR;
        speed = 900.0f * Settings.scale;
        this.parentAction = parentAction;
        this.effectColor = effectColor;
    }

    @Override
    public void update() {
        if (!soundPlayed) {
            CardCrawlGame.sound.playV("ATTACK_WHIFF_1", 2.0f);
            soundPlayed = true;
        }
        duration -= Gdx.graphics.getDeltaTime();
        Vector2 currentPosition = new Vector2(x, y);
        Vector2 targetPosition = new Vector2(targetX, targetY);
        float targetDirection = targetPosition.sub(currentPosition).angle();
        if (duration > 0.5f) {
            if (targetDirection - initialRotation > 180f) {
                rotation = Interpolation.circle.apply(initialRotation + 360f, targetDirection, 1.0f - ((duration - 0.5f) * 2.0f));
            } else {
                rotation = Interpolation.circle.apply(initialRotation, targetDirection, 1.0f - ((duration - 0.5f) * 2.0f));
            }
            Vector2 tmp = new Vector2(MathUtils.cosDeg(rotation), MathUtils.sinDeg(rotation));
            tmp.x *= speed * Gdx.graphics.getDeltaTime();
            tmp.y *= speed * Gdx.graphics.getDeltaTime();
            x += tmp.x;
            y += tmp.y;
            straightStartX = x;
            straightStartY = y;
            AbstractDungeon.effectsQueue.add(new MagicMissileTrailEffect(x + img.packedWidth / 2.0f, y + img.packedHeight / 2.0f, effectColor.cpy()));
        } else if (duration < 0.0f) {
            isDone = true;
        } else {
            if (rotation != targetDirection) {
                rotation = targetDirection;
            }
            x = Interpolation.linear.apply(straightStartX, targetX, 1.0f - (duration) * 2);
            y = Interpolation.linear.apply(straightStartY, targetY, 1.0f - (duration) * 2);
            AbstractDungeon.effectsQueue.add(new MagicMissileTrailEffect(x + img.packedWidth / 2.0f, y + img.packedHeight / 2.0f, effectColor.cpy()));
        }
        if (isDone && parentAction != null) {
            parentAction.doDamage = true;
        }
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.setColor(effectColor);
        sb.draw(img, x, y, img.packedWidth / 2.0f, img.packedHeight / 2.0f, img.packedWidth, img.packedHeight, 0.5f, 0.5f, rotation + 90f);
    }

    @Override
    public void dispose() {
    }
}
