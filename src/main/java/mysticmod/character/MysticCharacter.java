package mysticmod.character;

import java.util.ArrayList;

import com.badlogic.gdx.math.MathUtils;
import com.esotericsoftware.spine.AnimationState;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.EnergyManager;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.screens.CharSelectInfo;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import mysticmod.patches.MysticEnum;
import mysticmod.cards.DefendMystic;
import mysticmod.cards.StrikeMystic;
import mysticmod.cards.ShockingGrasp;
import mysticmod.cards.ArcaneDodge;
import mysticmod.relics.SpellBook;

public class MysticCharacter extends AbstractPlayer {
    public static final int ENERGY_PER_TURN = 3; // how much energy you get every turn
    public static final String MY_CHARACTER_SHOULDER_2 = "mysticmod/images/char/shoulder2.png"; // campfire pose
    public static final String MY_CHARACTER_SHOULDER_1 = "mysticmod/images/char/shoulder.png"; // another campfire pose
    public static final String MY_CHARACTER_CORPSE = "mysticmod/images/char/corpse.png"; // dead corpse
    public static final String MY_CHARACTER_SKELETON_ATLAS = "mysticmod/images/char/skeleton.atlas"; // spine animation atlas
    public static final String MY_CHARACTER_SKELETON_JSON = "mysticmod/images/char/skeleton.json"; // spine animation json

    public MysticCharacter (String name, PlayerClass chosenClass) {
        super(name, chosenClass);

        this.dialogX = (this.drawX + 0.0F * Settings.scale); // set location for text bubbles
        this.dialogY = (this.drawY + 220.0F * Settings.scale); // you can just copy these values

        initializeClass(null, MY_CHARACTER_SHOULDER_2, // required call to load textures and setup energy/loadout
                MY_CHARACTER_SHOULDER_1,
                MY_CHARACTER_CORPSE,
                getLoadout(), 20.0F, -10.0F, 220.0F, 290.0F, new EnergyManager(ENERGY_PER_TURN));

        loadAnimation(MY_CHARACTER_SKELETON_ATLAS, MY_CHARACTER_SKELETON_JSON, 1.0F); // if you're using modified versions of base game animations or made animations in spine make sure to include this bit and the following lines

        AnimationState.TrackEntry e = this.state.setAnimation(0, "idle", true);
        e.setTime(e.getEndTime() * MathUtils.random());
    }

    public static ArrayList<String> getStartingDeck() { // starting deck 'nuff said
        ArrayList<String> retVal = new ArrayList<>();
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

    public static ArrayList<String> getStartingRelics() { // starting relics - also simple
        ArrayList<String> retVal = new ArrayList<>();
        retVal.add(SpellBook.ID);
        UnlockTracker.markRelicAsSeen(SpellBook.ID);
        return retVal;
    }

    public static final int STARTING_HP = 77;
    public static final int MAX_HP = 77;
    public static final int STARTING_GOLD = 99;
    public static final int HAND_SIZE = 5;

    public static CharSelectInfo getLoadout() { // the rest of the character loadout so includes your character select screen info plus hp and starting gold
        return new CharSelectInfo("The Mystic", "The Mystic is a mysterious master NL of two opposed disciplines.",
                STARTING_HP, MAX_HP, 0, STARTING_GOLD, HAND_SIZE,
                MysticEnum.MYSTIC_CLASS, getStartingRelics(), getStartingDeck(), false);
    }

}