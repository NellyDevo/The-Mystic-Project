package mysticmod.character;

import basemod.abstracts.CustomPlayer;
import basemod.animations.SpriterAnimation;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.EnergyManager;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.localization.CharacterStrings;
import com.megacrit.cardcrawl.screens.CharSelectInfo;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import mysticmod.cards.ArcaneDodge;
import mysticmod.cards.DefendMystic;
import mysticmod.cards.ShockingGrasp;
import mysticmod.cards.StrikeMystic;
import mysticmod.patches.MysticEnum;
import mysticmod.relics.SpellBook;

import java.util.ArrayList;

public class MysticCharacter extends CustomPlayer {
    public static final int ENERGY_PER_TURN = 3; // how much energy you get every turn
    public static final String MY_CHARACTER_SHOULDER_2 = "mysticmod/images/char/shoulder2.png";
    public static final String MY_CHARACTER_SHOULDER_1 = "mysticmod/images/char/shoulder.png";
    public static final String MY_CHARACTER_CORPSE = "mysticmod/images/char/corpse.png";
    public static final String MY_CHARACTER_ANIMATION = "mysticmod/images/char/idle/Animation.scml";
    private static final String ID = "mysticmod:MysticCharacter";
    private static final CharacterStrings characterStrings = CardCrawlGame.languagePack.getCharacterString(ID);
    private static final String[] NAMES = characterStrings.NAMES;
    private static final String[] TEXT = characterStrings.TEXT;


    public static final String[] orbTextures = {
            "mysticmod/images/char/orb/layer1.png",
            "mysticmod/images/char/orb/layer2.png",
            "mysticmod/images/char/orb/layer3.png",
            "mysticmod/images/char/orb/layer4.png",
            "mysticmod/images/char/orb/layer5.png",
            "mysticmod/images/char/orb/layer6.png",
            "mysticmod/images/char/orb/layer1d.png",
            "mysticmod/images/char/orb/layer2d.png",
            "mysticmod/images/char/orb/layer3d.png",
            "mysticmod/images/char/orb/layer4d.png",
            "mysticmod/images/char/orb/layer5d.png"
    };

    public MysticCharacter (String name, PlayerClass chosenClass) {
        super(name, chosenClass, orbTextures, "mysticmod/images/char/orb/vfx.png", null, new SpriterAnimation(MY_CHARACTER_ANIMATION));

        this.dialogX = (this.drawX + 0.0F * Settings.scale);
        this.dialogY = (this.drawY + 220.0F * Settings.scale);

        initializeClass(null,
                MY_CHARACTER_SHOULDER_2,
                MY_CHARACTER_SHOULDER_1,
                MY_CHARACTER_CORPSE,
                getLoadout(), 20.0F, -10.0F, 220.0F, 290.0F, new EnergyManager(ENERGY_PER_TURN));
    }

    public static ArrayList<String> getStartingDeck() {
        ArrayList<String> retVal = new ArrayList<>();
        retVal.add(StrikeMystic.ID);
        retVal.add(StrikeMystic.ID);
        retVal.add(StrikeMystic.ID);
        retVal.add(StrikeMystic.ID);
        retVal.add(StrikeMystic.ID);
        retVal.add(DefendMystic.ID);
        retVal.add(DefendMystic.ID);
        retVal.add(DefendMystic.ID);
        retVal.add(DefendMystic.ID);
        retVal.add(ArcaneDodge.ID);
        retVal.add(ShockingGrasp.ID);
        return retVal;
    }

    public static ArrayList<String> getStartingRelics() {
        ArrayList<String> retVal = new ArrayList<>();
        retVal.add(SpellBook.ID);
        UnlockTracker.markRelicAsSeen(SpellBook.ID);
        return retVal;
    }

    public static final int STARTING_HP = 77;
    public static final int MAX_HP = 77;
    public static final int MAX_ORBS = 0;
    public static final int STARTING_GOLD = 99;
    public static final int HAND_SIZE = 5;

    public static CharSelectInfo getLoadout() {
        return new CharSelectInfo(NAMES[0], TEXT[0],
                STARTING_HP, MAX_HP, MAX_ORBS, STARTING_GOLD, HAND_SIZE,
                MysticEnum.MYSTIC_CLASS, getStartingRelics(), getStartingDeck(), false);
    }

}