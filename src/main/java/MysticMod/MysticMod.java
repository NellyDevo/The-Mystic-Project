package MysticMod;

import java.nio.charset.StandardCharsets;
import basemod.BaseMod;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import MysticMod.Patches.AbstractCardEnum;
import MysticMod.Patches.LibraryTypeEnum;
import MysticMod.Cards.DefendMystic;
import MysticMod.Cards.StrikeMystic;
import MysticMod.Cards.ShockingGrasp;
import MysticMod.Cards.ArcaneDodge;
import MysticMod.Cards.Cantrips.*;
import MysticMod.Cards.Disintegrate;
import MysticMod.Cards.Haste;
import MysticMod.Cards.Discipline;
import MysticMod.Cards.FloatingDisk;
import MysticMod.Cards.Fireball;
import MysticMod.Cards.ComboCaster;
import MysticMod.Cards.HeavyStrike;
import MysticMod.Cards.PowerSlash;
import MysticMod.Cards.SuddenClarity;
import MysticMod.Cards.Spellstrike;
import MysticMod.Cards.KnowledgePool;
import MysticMod.Character.MysticCharacter;
import MysticMod.Patches.MysticEnum;
import MysticMod.Relics.MysticSpellBook;
import com.evacipated.cardcrawl.modthespire.lib.SpireEnum;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.localization.RelicStrings;
import com.megacrit.cardcrawl.helpers.RelicLibrary;
import basemod.interfaces.EditCardsSubscriber;
import basemod.interfaces.EditCharactersSubscriber;
import basemod.interfaces.EditKeywordsSubscriber;
import basemod.interfaces.EditStringsSubscriber;
import basemod.interfaces.EditRelicsSubscriber;
import com.megacrit.cardcrawl.helpers.CardHelper;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.Gdx;

@SpireInitializer
public class MysticMod implements EditCardsSubscriber, EditCharactersSubscriber, EditKeywordsSubscriber, EditRelicsSubscriber, EditStringsSubscriber {

    private static final String modName = "TheMysticMod";
    private static final String author = "Johnny Devo";
    private static final String description = "v0.1\n Adds The Mystic custom character";
    private static final Color mysticPurple = CardHelper.getColor(152.0f, 34.0f, 171.0f); //152, 34, 171
    private static final String attackCard = "MysticMod/images/512/bg_attack_mystic.png";
    private static final String skillCard = "MysticMod/images/512/bg_skill_mystic.png";
    private static final String powerCard = "MysticMod/images/512/bg_power_mystic.png";
    private static final String energyOrb = "MysticMod/images/512/card_mystic_orb.png";
    private static final String attackCardPortrait = "MysticMod/images/1024/bg_attack_mystic.png";
    private static final String skillCardPortrait = "MysticMod/images/1024/bg_skill_mystic.png";
    private static final String powerCardPortrait = "MysticMod/images/1024/bg_power_mystic.png";
    private static final String energyOrbPortrait = "MysticMod/images/1024/card_mystic_orb.png";
    private static final String charButton = "MysticMod/images/charSelect/button.png";
    private static final String charPortrait = "MysticMod/images/charSelect/portrait.png";
    public static final String badgeImg = "MysticMod/images/badge.png";

    public MysticMod(){
       BaseMod.subscribe(this);

        BaseMod.addColor(AbstractCardEnum.MYSTIC_PURPLE.toString(),
                mysticPurple, mysticPurple, mysticPurple, mysticPurple, mysticPurple, mysticPurple, mysticPurple,   //Background color, back color, frame color, frame outline color, description box color, glow color
                attackCard, skillCard, powerCard, energyOrb,                                                        //attack background image, skill background image, power background image, energy orb image
                attackCardPortrait, skillCardPortrait, powerCardPortrait, energyOrbPortrait);                       //as above, but for card inspect view
    }
    //Used by @SpireInitializer
    public static void initialize(){

        //This creates an instance of our classes and gets our code going after BaseMod and ModTheSpire initialize.
        MysticMod mysticMod = new MysticMod();

        //Look at the BaseMod wiki for getting started. (Skip the decompiling part. It's not needed right now)

    }
    @Override
    public void receiveEditCards() {
        //Basic. 2 attacks, 2 skills
        BaseMod.addCard(new StrikeMystic());
        BaseMod.addCard(new DefendMystic());
        BaseMod.addCard(new ShockingGrasp());
        BaseMod.addCard(new ArcaneDodge());

        //Cantrips. 2 attacks, 3 skills
        BaseMod.addCard(new AcidSplash());
        BaseMod.addCard(new Prestidigitation());
        BaseMod.addCard(new RayOfFrost());
        BaseMod.addCard(new Spark());
        BaseMod.addCard(new ReadMagic());

        //Commons. 2 attacks, 1 skill
        BaseMod.addCard(new HeavyStrike());
        BaseMod.addCard(new SuddenClarity());
        BaseMod.addCard(new PowerSlash());

        //Uncommons. 1 attack, 1 skill, 1 power
        BaseMod.addCard(new Fireball());
        BaseMod.addCard(new FloatingDisk());
        BaseMod.addCard(new ComboCaster());

        //Rares. 2 attacks, 2 skills, 1 power
        BaseMod.addCard(new Disintegrate());
        BaseMod.addCard(new Haste());
        BaseMod.addCard(new Discipline());
        BaseMod.addCard(new Spellstrike());
        BaseMod.addCard(new KnowledgePool());
    }

    @Override
    public void receiveEditCharacters() {
        BaseMod.addCharacter(MysticCharacter.class, "The Mystic", "MysticCharacter",
                AbstractCardEnum.MYSTIC_PURPLE.toString(), "The Mystic", charButton, charPortrait,
                MysticEnum.MYSTIC_CLASS.toString());
    }

    @Override
    public void receiveEditKeywords() {
        String[] keywordCantrips = {"cantrip", "cantrips"};
        String[] keywordSpells = {"spell", "spells"};
        String[] keywordTechniques = {"technique", "techniques"};
        String[] keywordEnergized = {"energized"};
        String[] keywordTechnical = {"technical"};
        BaseMod.addKeyword(keywordCantrips, "Considered a spell so long as you've played fewer than 2 spells this turn");
        BaseMod.addKeyword(keywordSpells, "Advance the \"spells played\" counter for the turn");
        BaseMod.addKeyword(keywordTechniques, "Advance the \"Techniques played\" counter for the turn");
        BaseMod.addKeyword(keywordEnergized, "Has a special effect if you played a spell this turn");
        BaseMod.addKeyword(keywordTechnical, "Has a special effect if you played a Technique this turn");
    }

    @Override
    public void receiveEditStrings() {
        String relicStrings = Gdx.files.internal("MysticMod/strings/relics.json").readString(String.valueOf(StandardCharsets.UTF_8));
        BaseMod.loadCustomStrings(RelicStrings.class, relicStrings);
    }

    @Override
    public void receiveEditRelics() {
        BaseMod.addRelicToCustomPool(new MysticSpellBook(), AbstractCardEnum.MYSTIC_PURPLE.toString());
    }
}
