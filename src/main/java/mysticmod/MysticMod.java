package mysticmod;

import basemod.*;
import basemod.abstracts.CustomCard;
import basemod.interfaces.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.evacipated.cardcrawl.modthespire.Loader;
import com.evacipated.cardcrawl.modthespire.lib.SpireConfig;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.audio.Sfx;
import com.megacrit.cardcrawl.audio.SoundMaster;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.*;
import com.megacrit.cardcrawl.localization.*;
import com.megacrit.cardcrawl.monsters.city.Healer;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.screens.custom.CustomMod;
import mysticmod.cards.*;
import mysticmod.cards.cantrips.*;
import mysticmod.character.MysticAnimation;
import mysticmod.character.MysticCharacter;
import mysticmod.interfaces.SpellArteLogicAffector;
import mysticmod.modifiers.CrystalClear;
import mysticmod.patches.MysticEnum;
import mysticmod.patches.MysticTags;
import mysticmod.patches.RefreshSpellArteLogicField;
import mysticmod.potions.EssenceOfMagic;
import mysticmod.powers.ArtesPlayed;
import mysticmod.powers.MysticalShieldPower;
import mysticmod.powers.MysticalShieldUpgradedPower;
import mysticmod.powers.SpellsPlayed;
import mysticmod.relics.*;
import mysticmod.vfx.PoisedActivatedEffect;
import mysticmod.vfx.PowerfulActivatedEffect;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.*;

import static mysticmod.patches.AbstractCardEnum.MYSTIC_PURPLE;

@SpireInitializer
public class MysticMod implements EditCardsSubscriber, EditCharactersSubscriber, EditKeywordsSubscriber, EditRelicsSubscriber, EditStringsSubscriber, PostBattleSubscriber, PostInitializeSubscriber, PostDungeonInitializeSubscriber, AddCustomModeModsSubscriber, OnStartBattleSubscriber, OnPlayerLoseBlockSubscriber, RelicGetSubscriber {

    private static final Color mysticPurple = CardHelper.getColor(152.0f, 34.0f, 171.0f); //152, 34, 171
    private static final String attackCard = "mysticmod/images/512/bg_attack_mystic.png";
    private static final String skillCard = "mysticmod/images/512/bg_skill_mystic.png";
    private static final String powerCard = "mysticmod/images/512/bg_power_mystic.png";
    private static final String energyOrb = "mysticmod/images/512/card_mystic_orb.png";
    private static final String attackCardPortrait = "mysticmod/images/1024/bg_attack_mystic.png";
    private static final String skillCardPortrait = "mysticmod/images/1024/bg_skill_mystic.png";
    private static final String powerCardPortrait = "mysticmod/images/1024/bg_power_mystic.png";
    private static final String energyOrbPortrait = "mysticmod/images/1024/card_mystic_orb.png";
    private static final String charButton = "mysticmod/images/charSelect/button.png";
    private static final String charPortrait = "mysticmod/images/charSelect/portrait.png";
    private static final String miniManaSymbol = "mysticmod/images/manaSymbol.png";
    public static boolean isDiscoveryLookingForSpells = false;
    public static int numberOfTimesDeckShuffledThisCombat = 0;
    public static boolean healerAccused = false;
    public static Healer storedHealer;
    public static float storedHealerDialogY;
    public static CardBackgroundConfig cardBackgroundSetting;
    private static ModLabeledToggleButton shapes;
    private static ModLabeledToggleButton colors;
    private static ModLabeledToggleButton combined;
    private static SpireConfig mysticConfig;
    private static boolean mysticFriendlyMinionsToggle;
    public static ArrayList<AbstractCard> cantripsGroup = new ArrayList<>();
    private static ArrayList<AbstractCard> spellsGroup;
    private static ArrayList<AbstractCard> artesGroup;
    private static UIStrings uiStrings;
    private static String ID = "mysticmod:MysticMod";
    private static String[] TEXT;
    private static Logger logger = LogManager.getLogger(MysticMod.class.getName());


    public MysticMod(){
        BaseMod.subscribe(this);

        BaseMod.addColor(MYSTIC_PURPLE,
                mysticPurple, mysticPurple, mysticPurple, mysticPurple, mysticPurple, mysticPurple, mysticPurple,   //Background color, back color, frame color, frame outline color, description box color, glow color
                attackCard, skillCard, powerCard, energyOrb,                                                        //attack background image, skill background image, power background image, energy orb image
                attackCardPortrait, skillCardPortrait, powerCardPortrait, energyOrbPortrait,                        //as above, but for card inspect view
                miniManaSymbol);                                                                                    //appears in Mystic Purple cards where you type [E]

        Color essenceOfMagicColor = CardHelper.getColor(255.0f, 255.0f, 255.0f);
        BaseMod.addPotion(EssenceOfMagic.class, essenceOfMagicColor, essenceOfMagicColor, essenceOfMagicColor, EssenceOfMagic.POTION_ID, MysticEnum.MYSTIC_CLASS);
        Properties mysticDefaults = new Properties();
        mysticDefaults.setProperty("spellArteDisplay", "BOTH");
        mysticDefaults.setProperty("Fox Minion Enabled", "TRUE");
        try {
            mysticConfig = new SpireConfig("The Mystic Mod", "MysticConfig", mysticDefaults);
        } catch (IOException e) {
            logger.error("MysticMod SpireConfig initialization failed:");
            e.printStackTrace();
        }
        logger.info("MYSTIC CONFIG OPTIONS LOADED:");
        logger.info("spellArteDisplay set to " + mysticConfig.getString("spellArteDisplay") + ".");
        logger.info("Fox Minion Enabled = " + mysticConfig.getString("Fox Minion Enabled") + ".");
        switch (mysticConfig.getString("spellArteDisplay")) {
            case "SHAPE": MysticMod.cardBackgroundSetting = CardBackgroundConfig.SHAPE;
            break;
            case "COLOR": MysticMod.cardBackgroundSetting = CardBackgroundConfig.COLOR;
            break;
            case "BOTH": MysticMod.cardBackgroundSetting = CardBackgroundConfig.BOTH;
            break;
            default: MysticMod.cardBackgroundSetting = CardBackgroundConfig.BOTH;
            logger.warn("spellArteDisplay incorrectly set; defaulting to BOTH");
            break;
        }
        mysticFriendlyMinionsToggle = (mysticConfig.getString("Fox Minion Enabled").equals("TRUE"));
        powerPoiseSfxToggle = (mysticConfig.getString("Power/Poise SFX Enabled").equals("TRUE"));
    }

    //Used by @SpireInitializer
    @SuppressWarnings("unused")
    public static void initialize(){
        MysticMod mysticMod = new MysticMod();
    }

    @Override
    public void receivePostInitialize() {
        uiStrings = CardCrawlGame.languagePack.getUIString(ID);
        TEXT = uiStrings.TEXT;
        Texture badgeImg = new Texture("mysticmod/images/badge.png");
        ModPanel settingsPanel = new ModPanel();
        settingsPanel.addUIElement(new ModLabel(TEXT[0], 350.0f, 750.0f, settingsPanel, me -> {}));
        shapes = new ModLabeledToggleButton(TEXT[1], 350.0f, 700.0f, Settings.CREAM_COLOR, FontHelper.charDescFont, MysticMod.cardBackgroundSetting == CardBackgroundConfig.SHAPE, settingsPanel, label -> {}, button -> {
            MysticMod.cardBackgroundSetting = CardBackgroundConfig.SHAPE;
            resetMysticConfigButtons();
            button.enabled = true;
            MysticMod.mysticConfig.setString("spellArteDisplay", "SHAPE");
            try {MysticMod.mysticConfig.save();} catch (IOException e) {e.printStackTrace();}
            AbstractMysticCard.resetImageStrings();
        });
        settingsPanel.addUIElement(shapes);
        colors = new ModLabeledToggleButton(TEXT[2], 350.0f, 650.0f, Settings.CREAM_COLOR, FontHelper.charDescFont, MysticMod.cardBackgroundSetting == CardBackgroundConfig.COLOR, settingsPanel, label -> {}, button -> {
            MysticMod.cardBackgroundSetting = CardBackgroundConfig.COLOR;
            resetMysticConfigButtons();
            button.enabled = true;
            MysticMod.mysticConfig.setString("spellArteDisplay", "COLOR");
            try {MysticMod.mysticConfig.save();} catch (IOException e) {e.printStackTrace();}
            AbstractMysticCard.resetImageStrings();
        });
        settingsPanel.addUIElement(colors);
        combined = new ModLabeledToggleButton(TEXT[3], 350.0f, 600.0f, Settings.CREAM_COLOR, FontHelper.charDescFont, MysticMod.cardBackgroundSetting == CardBackgroundConfig.BOTH, settingsPanel, label -> {}, button -> {
            MysticMod.cardBackgroundSetting = CardBackgroundConfig.BOTH;
            resetMysticConfigButtons();
            button.enabled = true;
            MysticMod.mysticConfig.setString("spellArteDisplay", "BOTH");
            try {MysticMod.mysticConfig.save();} catch (IOException e) {e.printStackTrace();}
            AbstractMysticCard.resetImageStrings();
        });
        settingsPanel.addUIElement(combined);
        settingsPanel.addUIElement(new ModLabel(TEXT[4], 350.0f, 450.0f, settingsPanel, me -> {}));
        ModLabeledToggleButton foxToggle = new ModLabeledToggleButton(TEXT[5], 350.0f, 500.0f, Settings.CREAM_COLOR, FontHelper.charDescFont, mysticFriendlyMinionsToggle, settingsPanel, label -> {}, button -> {
            mysticFriendlyMinionsToggle = button.enabled;
            if (mysticFriendlyMinionsToggle) {
                MysticMod.mysticConfig.setString("Fox Minion Enabled", "TRUE");
            } else {
                MysticMod.mysticConfig.setString("Fox Minion Enabled", "FALSE");
            }
            try {MysticMod.mysticConfig.save();} catch (IOException e) {e.printStackTrace();}
        });
        settingsPanel.addUIElement(foxToggle);
        BaseMod.registerModBadge(badgeImg, TEXT[6], "Johnny Devo", TEXT[7], settingsPanel);
        cantripsGroup.add(new AcidSplash());
        cantripsGroup.add(new Prestidigitation());
        cantripsGroup.add(new RayOfFrost());
        cantripsGroup.add(new ReadMagic());
        cantripsGroup.add(new Spark());

        //add sounds
        HashMap<String, Sfx> reflectedMap = (HashMap<String, Sfx>)ReflectionHacks.getPrivate(CardCrawlGame.sound, SoundMaster.class, "map");
        reflectedMap.put("mysticmod:SPARKS", new Sfx("mysticmod/sounds/sparks.ogg"));
        reflectedMap.put("mysticmod:BOOK_RUNE_ONE", new Sfx("mysticmod/sounds/bookruneone.ogg"));
        reflectedMap.put("mysticmod:BOOK_RUNE_TWO", new Sfx("mysticmod/sounds/bookrunetwo.ogg"));
    }

    private void resetMysticConfigButtons() {
        shapes.toggle.enabled = false;
        colors.toggle.enabled = false;
        combined.toggle.enabled = false;
    }

    @Override
    public void receiveEditCards() {
        //secondMagicNumber dynamic variable
        BaseMod.addDynamicVariable(new AbstractMysticCard.SecondMagicNumber());

        //Basic. 2 attacks, 2 skills
        BaseMod.addCard(new ArcaneDodge());
        BaseMod.addCard(new DefendMystic());
        BaseMod.addCard(new ShockingGrasp());
        BaseMod.addCard(new StrikeMystic());

        //"cantrips". 2 attacks, 3 skills
        BaseMod.addCard(new AcidSplash());
        BaseMod.addCard(new Prestidigitation());
        BaseMod.addCard(new RayOfFrost());
        BaseMod.addCard(new ReadMagic());
        BaseMod.addCard(new Spark());

        //Special
        BaseMod.addCard(new BladeBurst());

        //Commons
        //10 attacks
        BaseMod.addCard(new ComponentsPouch());
        BaseMod.addCard(new CorrosiveTouch());
        BaseMod.addCard(new DiviningBlow());
        BaseMod.addCard(new HeavyStrike());
        BaseMod.addCard(new LightningBolt());
        BaseMod.addCard(new PowerSlash());
        BaseMod.addCard(new Probe());
        BaseMod.addCard(new Riposte());
        BaseMod.addCard(new Sideswipe());
        BaseMod.addCard(new Snowball());
        //8 skills
        BaseMod.addCard(new Alacrity());
        BaseMod.addCard(new BulkUp());
        BaseMod.addCard(new Daze());
        BaseMod.addCard(new Disengage());
        BaseMod.addCard(new PureInstinct());
        BaseMod.addCard(new Shield());
        BaseMod.addCard(new SuddenClarity());
        BaseMod.addCard(new TomeOfSpells());

        //Uncommons
        //11 attacks
        BaseMod.addCard(new AllIn());
        BaseMod.addCard(new BladedDash());
        BaseMod.addCard(new Feint());
        BaseMod.addCard(new Fireball());
        BaseMod.addCard(new FiveFootStep());
        BaseMod.addCard(new Flourish());
        BaseMod.addCard(new Flurry());
        BaseMod.addCard(new MagicMissile());
        BaseMod.addCard(new MirrorStrike());
        BaseMod.addCard(new SpellCombat());
        BaseMod.addCard(new VorpalThrust());
        //18 skills
        BaseMod.addCard(new BladeMaster());
        BaseMod.addCard(new ChargedParry());
        BaseMod.addCard(new CureLightWounds());
        BaseMod.addCard(new EarthenWall());
        BaseMod.addCard(new EnergizedRift());
        BaseMod.addCard(new FloatingDisk());
        BaseMod.addCard(new Fly());
        BaseMod.addCard(new Grapple());
        BaseMod.addCard(new Grease());
        BaseMod.addCard(new HunkerDown());
        BaseMod.addCard(new IllusionOfCalm());
        BaseMod.addCard(new KeenEdge());
        BaseMod.addCard(new MagicWeapon());
        BaseMod.addCard(new PowerAttack());
        BaseMod.addCard(new PunishingArmor());
        BaseMod.addCard(new PreparedCaster());
        BaseMod.addCard(new Stoneskin());
        BaseMod.addCard(new StyleChange());
        //6 powers
        BaseMod.addCard(new Metamagic());
        BaseMod.addCard(new ComboCaster());
        BaseMod.addCard(new Dedication());
        BaseMod.addCard(new EbbAndFlow());
        BaseMod.addCard(new Bladework());
        BaseMod.addCard(new RapidCaster());

        //Rares.
        //4 attacks
        BaseMod.addCard(new ClosingBarrage());
        BaseMod.addCard(new Disintegrate());
        BaseMod.addCard(new EnchantedMoulinet());
        BaseMod.addCard(new Spellstrike());
        //8 skills
        BaseMod.addCard(new Doublecast());
        BaseMod.addCard(new GreaterInvisibility());
        BaseMod.addCard(new Haste());
        BaseMod.addCard(new KnowledgePool());
        BaseMod.addCard(new Lunge());
        BaseMod.addCard(new Natural20());
        BaseMod.addCard(new ObscuringMist());
        BaseMod.addCard(new SpellRecall());
        //6 powers
        BaseMod.addCard(new Discipline());
        BaseMod.addCard(new GeminiForm());
        BaseMod.addCard(new MirrorEntity());
        BaseMod.addCard(new Momentum());
        BaseMod.addCard(new MysticalShield());
        BaseMod.addCard(new SpontaneousCaster());

        //friendly minions only
        if (Loader.isModLoaded("Friendly_Minions_0987678") && mysticFriendlyMinionsToggle) {
            BaseMod.addCard(new SummonFamiliar());
            logger.info("Friendly_Minions_0987678 detected, Summon Familiar added");
        } else {
            BaseMod.addCard(new SummonFamiliarPlaceholder());
        }

        //testing purposes only, comment out for releases
        //BaseMod.addCard(new TestCard());
    }

    @Override
    public void receiveEditCharacters() {
        BaseMod.addCharacter(new MysticCharacter(CardCrawlGame.playerName), charButton, charPortrait, MysticEnum.MYSTIC_CLASS); //TODO
    }

    @Override
    public void receiveCustomModeMods(List<CustomMod> modsList) {
        modsList.add(new CustomMod(CrystalClear.ID, "g", true));
    }

    @Override
    public void receivePostDungeonInitialize() {
        if (CardCrawlGame.trial != null && CardCrawlGame.trial.dailyModIDs().contains(CrystalClear.ID)) {
            RelicLibrary.getRelic(CrystalBall.ID).makeCopy().instantObtain();
            for (AbstractRelic relicInBossPool : RelicLibrary.bossList) {
                if (relicInBossPool instanceof CrystalBall) {
                    RelicLibrary.bossList.remove(relicInBossPool);
                    break;
                }
            }
        }
    }

    @Override
    public int receiveOnPlayerLoseBlock(int amount) {
        if (AbstractDungeon.player.hasPower(MysticalShieldPower.POWER_ID)) {
            amount = ((MysticalShieldPower)AbstractDungeon.player.getPower(MysticalShieldPower.POWER_ID)).onMoreSpecificTrigger(amount);
        } else if (AbstractDungeon.player.hasPower(MysticalShieldUpgradedPower.POWER_ID)) {
            amount = ((MysticalShieldUpgradedPower)AbstractDungeon.player.getPower(MysticalShieldUpgradedPower.POWER_ID)).onMoreSpecificTrigger(amount);
        }
        return amount;
    }

    @Override
    public void receiveEditKeywords() {
        Gson gson = new Gson();

        String keywordStrings = Gdx.files.internal("mysticmod/strings/keywords.json").readString(String.valueOf(StandardCharsets.UTF_8));
        Type typeToken = new TypeToken<Map<String, Keyword>>() {}.getType();

        Map<String, Keyword> keywords = (Map)gson.fromJson(keywordStrings, typeToken);

        keywords.forEach((k,v)->{
            // Keyword word = (Keyword)v;
            logger.info("MysticMod: adding Keyword - " + v.NAMES[0]);
            BaseMod.addKeyword(v.NAMES, v.DESCRIPTION);
        });
    }

    @Override
    public void receiveEditStrings() {
        String cardStrings = Gdx.files.internal("mysticmod/strings/cards.json").readString(String.valueOf(StandardCharsets.UTF_8));
        BaseMod.loadCustomStrings(CardStrings.class, cardStrings);
        String characterStrings = Gdx.files.internal("mysticmod/strings/character.json").readString(String.valueOf(StandardCharsets.UTF_8));
        BaseMod.loadCustomStrings(CharacterStrings.class, characterStrings);
        String potionStrings = Gdx.files.internal("mysticmod/strings/potions.json").readString(String.valueOf(StandardCharsets.UTF_8));
        BaseMod.loadCustomStrings(PotionStrings.class, potionStrings);
        String powerStrings = Gdx.files.internal("mysticmod/strings/powers.json").readString(String.valueOf(StandardCharsets.UTF_8));
        BaseMod.loadCustomStrings(PowerStrings.class, powerStrings);
        String relicStrings = Gdx.files.internal("mysticmod/strings/relics.json").readString(String.valueOf(StandardCharsets.UTF_8));
        BaseMod.loadCustomStrings(RelicStrings.class, relicStrings);
        String runModStrings = Gdx.files.internal("mysticmod/strings/run_mods.json").readString(String.valueOf(StandardCharsets.UTF_8));
        BaseMod.loadCustomStrings(RunModStrings.class, runModStrings);
        String uiStrings = Gdx.files.internal("mysticmod/strings/ui.json").readString(String.valueOf(StandardCharsets.UTF_8));
        BaseMod.loadCustomStrings(UIStrings.class, uiStrings);
        String monsterStrings = Gdx.files.internal("mysticmod/strings/monsters.json").readString(String.valueOf(StandardCharsets.UTF_8));
        BaseMod.loadCustomStrings(MonsterStrings.class, monsterStrings);
        String eventStrings = Gdx.files.internal("mysticmod/strings/events.json").readString(String.valueOf(StandardCharsets.UTF_8));
        BaseMod.loadCustomStrings(EventStrings.class, eventStrings);
    }

    @Override
    public void receiveEditRelics() {
        //starter
        BaseMod.addRelicToCustomPool(new SpellBook(), MYSTIC_PURPLE);

        //Common
        BaseMod.addRelicToCustomPool(new TrainingManual(), MYSTIC_PURPLE);

        //Uncommon
        BaseMod.addRelicToCustomPool(new RabbitsFoot(), MYSTIC_PURPLE);
        BaseMod.addRelicToCustomPool(new RunicPrism(), MYSTIC_PURPLE);

        //Rare
        BaseMod.addRelicToCustomPool(new Kama(), MYSTIC_PURPLE);

        //Shop
        BaseMod.addRelicToCustomPool(new BentSpoon(), MYSTIC_PURPLE);

        //Boss
        BaseMod.addRelicToCustomPool(new BlessedBook(), MYSTIC_PURPLE); //replaces starting relic
        BaseMod.addRelicToCustomPool(new CrystalBall(), MYSTIC_PURPLE);
        BaseMod.addRelicToCustomPool(new DeckOfManyThings(), MYSTIC_PURPLE);
    }

    @Override
    public void receiveOnBattleStart(AbstractRoom room) {
        numberOfTimesDeckShuffledThisCombat = 0;
    }

    @Override
    public void receivePostBattle(final AbstractRoom p0) {
        numberOfTimesDeckShuffledThisCombat = 0;
        healerAccused = false;
        if (storedHealer != null) {
            storedHealer.dialogY = storedHealerDialogY;
            storedHealer = null;
        }
    }

    public static boolean isThisASpell(AbstractCard card) {
        boolean retVal = false;
        if (card instanceof AbstractMysticCard) {
            if (((AbstractMysticCard)card).isSpell()) { //methods simply call against hasTag, but exist to offer hooks into conditional spell/arte logic within specific cards
                retVal = true;
            }
        } else if (card.hasTag(MysticTags.IS_SPELL)) { //First, determine if card is naturally a spell (has the tag)
            retVal = true;
        }
        if (RefreshSpellArteLogicField.checkSpell.get(card)) {
            if (AbstractDungeon.player != null) { //Then, apply any conditional relic/power logic with provided hooks.
                ArrayList<SpellArteLogicAffector> listByPriority = new ArrayList<>();
                Iterator iter;
                iter = AbstractDungeon.player.relics.iterator();
                AbstractRelic r;
                while (iter.hasNext()) {
                    r = (AbstractRelic) iter.next();
                    if (r instanceof SpellArteLogicAffector) {
                        listByPriority.add((SpellArteLogicAffector)r);
                    }
                }
                iter = AbstractDungeon.player.powers.iterator();
                AbstractPower p;
                while (iter.hasNext()) {
                    p = (AbstractPower) iter.next();
                    if (p instanceof SpellArteLogicAffector) {
                        listByPriority.add((SpellArteLogicAffector)p);
                    }
                }
                listByPriority.sort(new SpellArteLogicAffector.SortByPriority());
                iter = listByPriority.iterator();
                while (iter.hasNext()) {
                    retVal = ((SpellArteLogicAffector)iter.next()).modifyIsSpell(card, retVal);
                }
            }
            RefreshSpellArteLogicField.isConditionalSpell.set(card, retVal);
            RefreshSpellArteLogicField.checkSpell.set(card, false);
        } else {
            retVal = RefreshSpellArteLogicField.isConditionalSpell.get(card);
        }
        return retVal;
    }

    public static boolean isThisAnArte(AbstractCard card) {
        boolean retVal = false;
        if (card instanceof AbstractMysticCard) {
            if (((AbstractMysticCard)card).isArte()) { //if card is Mystic card, test if has Arte tag
                retVal = true;
            }
        } else if (card.hasTag(MysticTags.IS_ARTE)) { //if card is not a mystic card, repeat tests.
            retVal = true;
        }
        if (RefreshSpellArteLogicField.checkArte.get(card)) {
            if (AbstractDungeon.player != null) { //Then, apply any conditional relic/power logic with provided hooks.
                ArrayList<SpellArteLogicAffector> listByPriority = new ArrayList<>();
                Iterator iter;
                iter = AbstractDungeon.player.relics.iterator();
                AbstractRelic r;
                while (iter.hasNext()) {
                    r = (AbstractRelic) iter.next();
                    if (r instanceof SpellArteLogicAffector) {
                        listByPriority.add((SpellArteLogicAffector)r);
                    }
                }
                iter = AbstractDungeon.player.powers.iterator();
                AbstractPower p;
                while (iter.hasNext()) {
                    p = (AbstractPower) iter.next();
                    if (p instanceof SpellArteLogicAffector) {
                        listByPriority.add((SpellArteLogicAffector)p);
                    }
                }
                listByPriority.sort(new SpellArteLogicAffector.SortByPriority());
                iter = listByPriority.iterator();
                while (iter.hasNext()) {
                    retVal = ((SpellArteLogicAffector)iter.next()).modifyIsArte(card, retVal);
                }
            }
            RefreshSpellArteLogicField.isConditionalArte.set(card, retVal);
            RefreshSpellArteLogicField.checkArte.set(card, false);
        } else {
            retVal = RefreshSpellArteLogicField.isConditionalArte.get(card);
        }
        return retVal;
    }

    public static AbstractCard returnTrulyRandomSpell() {
        if (spellsGroup == null) {
            spellsGroup = new ArrayList<>();
            for (Map.Entry<String, AbstractCard> potentialSpell : CardLibrary.cards.entrySet()) {
                AbstractCard card = potentialSpell.getValue();
                if (card.rarity != AbstractCard.CardRarity.BASIC && card.rarity != AbstractCard.CardRarity.SPECIAL
                        && card.hasTag(MysticTags.IS_SPELL) && !card.hasTag(AbstractCard.CardTags.HEALING)) {
                    spellsGroup.add(card.makeCopy());
                }
            }
        }
        return spellsGroup.get(AbstractDungeon.cardRandomRng.random(spellsGroup.size() - 1));
    }

    public static AbstractCard returnTrulyRandomArte() {
        if (artesGroup == null) {
            artesGroup = new ArrayList<>();
            for (Map.Entry<String, AbstractCard> potentialArte : CardLibrary.cards.entrySet()) {
                AbstractCard card = potentialArte.getValue();
                if (card.rarity != AbstractCard.CardRarity.BASIC && card.rarity != AbstractCard.CardRarity.SPECIAL
                        && card.hasTag(MysticTags.IS_ARTE) && !card.hasTag(AbstractCard.CardTags.HEALING)) {
                    artesGroup.add(card.makeCopy());
                }
            }
        }
        return artesGroup.get(AbstractDungeon.cardRandomRng.random(artesGroup.size() - 1));
    }

    public static Texture loadBgAddonTexture(String imgPath) {
        Texture extraTexture;
        if (CustomCard.imgMap.containsKey(imgPath)) {
            extraTexture = CustomCard.imgMap.get(imgPath);
        } else {
            extraTexture = ImageMaster.loadImage(imgPath);
            CustomCard.imgMap.put(imgPath, extraTexture);
        }
        extraTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        return extraTexture;
    }

    public static void refreshSpellArteLogicChecks() {
        //for spell/arte logic, refresh conditional logic.
        for (AbstractCard card : AbstractDungeon.player.hand.group) {
            RefreshSpellArteLogicField.checkSpell.set(card, true);
            RefreshSpellArteLogicField.checkArte.set(card, true);
        }
        for (AbstractCard card : AbstractDungeon.player.drawPile.group) {
            RefreshSpellArteLogicField.checkSpell.set(card, true);
            RefreshSpellArteLogicField.checkArte.set(card, true);
        }
        for (AbstractCard card : AbstractDungeon.player.discardPile.group) {
            RefreshSpellArteLogicField.checkSpell.set(card, true);
            RefreshSpellArteLogicField.checkArte.set(card, true);
        }
        for (AbstractCard card : AbstractDungeon.player.exhaustPile.group) {
            RefreshSpellArteLogicField.checkSpell.set(card, true);
            RefreshSpellArteLogicField.checkArte.set(card, true);
        }
        for (AbstractCard card : AbstractDungeon.player.masterDeck.group) {
            RefreshSpellArteLogicField.checkSpell.set(card, true);
            RefreshSpellArteLogicField.checkArte.set(card, true);
        }
    }

    @Override
    public void receiveRelicGet(AbstractRelic r) {
        if (r instanceof SpellArteLogicAffector) {
            refreshSpellArteLogicChecks();
        }
    }

    public enum CardBackgroundConfig {
        SHAPE,
        COLOR,
        BOTH
    }
}
