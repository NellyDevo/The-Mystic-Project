package mysticmod.character;

import basemod.animations.AbstractAnimation;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Interpolation;
import com.brashmonkey.spriter.*;
import com.brashmonkey.spriter.LibGdx.LibGdxDrawer;
import com.brashmonkey.spriter.LibGdx.LibGdxLoader;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import mysticmod.powers.ArtesPlayed;
import mysticmod.powers.SpellsPlayed;

public class MysticAnimation extends AbstractAnimation
{
    public static boolean drawBones = false;

    private LibGdxLoader loader;
    private LibGdxDrawer drawer;
    private ShapeRenderer renderer;
    public PlayerTweener myPlayer;
    private float frameRegulator = 0;
    public int animFps = 60;
    private boolean swordGlow = false;
    private float swordGlowStartingTransparency = 0.0f;
    private float swordGlowEndingTransparency = 0.0f;
    private float swordGlowTimer = 0.0f;
    private boolean bookRunes = false;
    private float bookRunesStartingTransparency = 0.0f;
    private float bookRunesEndingTransparency = 0.0f;
    private float bookRunesTimer = 0.0f;
    private float transparencyTimeAmount = 2.0f;
    public static float bookX = 0.0f;
    public static float bookY = 0.0f;
    public static float swordX = 0.0f;
    public static float swordY = 0.0f;
    public static float swordAngle = 0.0f;


    public MysticAnimation(String filepath)
    {
        renderer = new ShapeRenderer();

        FileHandle handle = Gdx.files.internal(filepath);
        Data data = new SCMLReader(handle.read()).getData();

        loader = new LibGdxLoader(data);
        loader.load(handle.file());

        drawer = new LibGdxDrawer(loader, renderer);

        myPlayer = new PlayerTweener(data.getEntity(0));

        myPlayer.setScale(Settings.scale);
    }

    public Type type() {
        return Type.SPRITE;
    }

    @Override
    public void setFlip(boolean horizontal, boolean vertical)
    {
        if ((horizontal && myPlayer.flippedX() > 0) || (!horizontal && myPlayer.flippedX() < 0)) {
            myPlayer.flipX();
        }

        if ((vertical && myPlayer.flippedY() > 0) || (!vertical && myPlayer.flippedY() < 0)) {
            myPlayer.flipY();
        }
    }

    @Override
    public void renderSprite(SpriteBatch batch, float x, float y)
    {
        drawer.batch = batch;
        // Update animation
        float adjustedFrameRate = (1.0f / animFps);
        frameRegulator += Gdx.graphics.getDeltaTime();
        while (frameRegulator - adjustedFrameRate >= 0.0f) {
            myPlayer.update();
            frameRegulator -= adjustedFrameRate;
        }

        // Update dynamic elements
        if (swordGlowTimer < transparencyTimeAmount) {
            swordGlowTimer += Gdx.graphics.getDeltaTime();
        }
        if (bookRunesTimer < transparencyTimeAmount) {
            bookRunesTimer += Gdx.graphics.getDeltaTime();
        }
        int swordFile;
        int bookFile;
        if (AbstractDungeon.player != null && AbstractDungeon.player.hasPower(SpellsPlayed.POWER_ID)) {
            if (!swordGlow) {
                swordGlow = true;
                swordGlowStartingTransparency = 0.0f;
                swordGlowEndingTransparency = 1.0f;
                swordGlowTimer = 0.0f;
            }
            swordFile = 21;
        } else {
            if (swordGlow) {
                swordGlow = false;
                swordGlowStartingTransparency = 1.0f;
                swordGlowEndingTransparency = 0.0f;
                swordGlowTimer = 0.0f;
            }
            swordFile = 12;
        }
        if (AbstractDungeon.player != null && AbstractDungeon.player.hasPower(ArtesPlayed.POWER_ID)) {
            if (!bookRunes) {
                bookRunes = true;
                bookRunesStartingTransparency = 0.0f;
                bookRunesEndingTransparency = 1.0f;
                bookRunesTimer = 0.0f;
            }
            bookFile = 20;
        } else {
            if (bookRunes) {
                bookRunes = false;
                bookRunesStartingTransparency = 1.0f;
                bookRunesEndingTransparency = 0.0f;
                bookRunesTimer = 0.0f;
            }
            bookFile = 18;
        }
        float swordGlowTransparency = Interpolation.linear.apply(swordGlowStartingTransparency, swordGlowEndingTransparency, Math.min(1.0f, (swordGlowTimer/transparencyTimeAmount)));
        float bookRunesTransparency = Interpolation.linear.apply(bookRunesStartingTransparency, bookRunesEndingTransparency, Math.min(1.0f, (bookRunesTimer/transparencyTimeAmount)));
        myPlayer.setObject("12 right hand", 1.0F, 0, swordFile);
        myPlayer.setObject("18 left hand", 1.0F, 0, bookFile);
        myPlayer.setObject("25 blade glow", swordGlowTransparency, 0, 25);
        myPlayer.setObject("22 flame rune", bookRunesTransparency, 0, 22);
        myPlayer.setObject("23 lightning rune", bookRunesTransparency, 0, 23);
        myPlayer.setObject("24 frost rune", bookRunesTransparency, 0, 24);

        // Publish element positions
        Timeline.Key.Object swordObject = myPlayer.getObject("12 right hand");
        Timeline.Key.Object bookObject = myPlayer.getObject("18 left hand");
        swordX = swordObject.position.x + 18.0f * Settings.scale;
        swordY = swordObject.position.y - 30.0f * Settings.scale;
        swordAngle = swordObject.angle - 1074.7101f; //I don't want to talk about it
        bookX = bookObject.position.x;
        bookY = bookObject.position.y + 25.0f * Settings.scale;

        // Move to correct location on screen
        AbstractPlayer player = AbstractDungeon.player;
        if (player != null) {
            myPlayer.setPosition(new Point(x, y));

            drawer.draw(myPlayer);

            if (drawBones) {
                batch.end();
                renderer.setAutoShapeType(true);
                renderer.begin();
                drawer.drawBoxes(myPlayer);
                drawer.drawBones(myPlayer);
                renderer.end();
                batch.begin();
            }
        }
    }
}
