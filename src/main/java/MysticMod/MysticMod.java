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
import MysticMod.Cards.MirrorEntity;
import MysticMod.Cards.GeminiForm;
import MysticMod.Cards.Momentum;
import MysticMod.Cards.SpontaneousCaster;
import MysticMod.Cards.ObscuringMist;
import MysticMod.Cards.GreaterInvisibility;
import MysticMod.Cards.MysticalShield;
import MysticMod.Cards.Doublecast;
import MysticMod.Cards.Natural20;
import MysticMod.Cards.PreparedCaster;
import MysticMod.Cards.Lunge;
import MysticMod.Cards.SpellRecall;
import MysticMod.Cards.ClosingBarrage;
import MysticMod.Cards.Flourish;
import MysticMod.Cards.Dedication;
import MysticMod.Cards.Grapple;
import MysticMod.Cards.VorpalThrust;
import MysticMod.Cards.EarthenWall;
import MysticMod.Cards.Grease;
import MysticMod.Cards.ArcaneAccuracy;
import MysticMod.Cards.MightyMagic;
import MysticMod.Cards.RapidCaster;
import MysticMod.Cards.ChargedParry;
import MysticMod.Cards.Feint;
import MysticMod.Cards.StyleChange;
import MysticMod.Cards.Flurry;
import MysticMod.Cards.BladeMaster;
import MysticMod.Cards.EnergizedRift;
import MysticMod.Cards.IllusionOfCalm;
import MysticMod.Cards.PunishingArmor;
import MysticMod.Cards.BladedDash;
import MysticMod.Cards.MirrorStrike;
import MysticMod.Cards.Fly;
import MysticMod.Cards.BladeBurst;
import MysticMod.Cards.MagicWeapon;
import MysticMod.Cards.KeenEdge;
import MysticMod.Cards.EbbAndFlow;
import MysticMod.Cards.MagicMissile;
import MysticMod.Cards.PowerAttack;
import MysticMod.Cards.Stoneskin;
import MysticMod.Cards.CureLightWounds;
import MysticMod.Cards.HunkerDown;
import MysticMod.Cards.AllIn;
import MysticMod.Cards.FiveFootStep;
import MysticMod.Cards.SpellCombat;
import MysticMod.Cards.Alacrity;
import MysticMod.Cards.DiviningBlow;
import MysticMod.Cards.Riposte;
import MysticMod.Cards.Moulinet;
import MysticMod.Cards.LightningBolt;
import MysticMod.Cards.Snowball;
import MysticMod.Cards.Shield;
import MysticMod.Cards.Disengage;
import MysticMod.Cards.ComponentsPouch;
import MysticMod.Cards.PureInstinct;
import MysticMod.Cards.TomeOfSpells;
import MysticMod.Cards.Sideswipe;
import MysticMod.Cards.Probe;
import MysticMod.Cards.Daze;
import MysticMod.Cards.CorrosiveTouch;
import MysticMod.Character.MysticCharacter;
import MysticMod.Patches.MysticEnum;
import MysticMod.Relics.MysticSpellBook;
import MysticMod.Powers.GeminiFormPower;
import MysticMod.Powers.SpellsPlayed;
import MysticMod.Powers.TechniquesPlayed;
import com.evacipated.cardcrawl.modthespire.lib.SpireEnum;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.localization.RelicStrings;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.helpers.RelicLibrary;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import basemod.interfaces.EditCardsSubscriber;
import basemod.interfaces.EditCharactersSubscriber;
import basemod.interfaces.EditKeywordsSubscriber;
import basemod.interfaces.EditStringsSubscriber;
import basemod.interfaces.EditRelicsSubscriber;
import basemod.interfaces.PostBattleSubscriber;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.helpers.CardHelper;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.Gdx;
import java.util.*;

@SpireInitializer
public class MysticMod implements EditCardsSubscriber, EditCharactersSubscriber, EditKeywordsSubscriber, EditRelicsSubscriber, EditStringsSubscriber, PostBattleSubscriber {

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
    public static boolean isDiscoveryLookingForSpells = false;
    public static boolean isDiscoveryLookingForTechniques = false;
    public static int numberOfTimesDeckShuffledThisCombat = 0;

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
    }
    @Override
    public void receiveEditCards() {
        //Basic. 2 attacks, 2 skills
        BaseMod.addCard(new StrikeMystic());
        BaseMod.addCard(new DefendMystic());
        BaseMod.addCard(new ShockingGrasp());
        BaseMod.addCard(new ArcaneDodge());

        //"Cantrips". 2 attacks, 3 skills
        BaseMod.addCard(new AcidSplash());
        BaseMod.addCard(new Prestidigitation());
        BaseMod.addCard(new RayOfFrost());
        BaseMod.addCard(new Spark());
        BaseMod.addCard(new ReadMagic());

        //Commons. 10 attacks, 7 skills
        BaseMod.addCard(new HeavyStrike());
        BaseMod.addCard(new SuddenClarity());
        BaseMod.addCard(new PowerSlash());
        BaseMod.addCard(new Alacrity());
        BaseMod.addCard(new DiviningBlow());
        BaseMod.addCard(new Riposte());
        BaseMod.addCard(new Moulinet());
        BaseMod.addCard(new LightningBolt());
        BaseMod.addCard(new Snowball());
        BaseMod.addCard(new Shield());
        BaseMod.addCard(new Disengage());
        BaseMod.addCard(new ComponentsPouch());
        BaseMod.addCard(new PureInstinct());
        BaseMod.addCard(new TomeOfSpells());
        BaseMod.addCard(new Sideswipe());
        BaseMod.addCard(new Probe());
        BaseMod.addCard(new Daze());
        BaseMod.addCard(new CorrosiveTouch());

        //Uncommons. 12 attacks, 17 skills, 6 powers
        BaseMod.addCard(new Fireball());
        BaseMod.addCard(new FloatingDisk());
        BaseMod.addCard(new ComboCaster());
        BaseMod.addCard(new Flourish());
        BaseMod.addCard(new Dedication());
        BaseMod.addCard(new Grapple());
        BaseMod.addCard(new VorpalThrust());
        BaseMod.addCard(new EarthenWall());
        BaseMod.addCard(new Grease());
        BaseMod.addCard(new ArcaneAccuracy());
        BaseMod.addCard(new MightyMagic());
        BaseMod.addCard(new RapidCaster());
        BaseMod.addCard(new ChargedParry());
        BaseMod.addCard(new Feint());
        BaseMod.addCard(new StyleChange());
        BaseMod.addCard(new Flurry());
        BaseMod.addCard(new BladeMaster());
        BaseMod.addCard(new EnergizedRift());
        BaseMod.addCard(new IllusionOfCalm());
        BaseMod.addCard(new PunishingArmor());
        BaseMod.addCard(new BladedDash());
        BaseMod.addCard(new MirrorStrike());
        BaseMod.addCard(new Fly());
        BaseMod.addCard(new MagicWeapon());
        BaseMod.addCard(new BladeBurst());
        BaseMod.addCard(new KeenEdge());
        BaseMod.addCard(new EbbAndFlow());
        BaseMod.addCard(new MagicMissile());
        BaseMod.addCard(new PowerAttack());
        BaseMod.addCard(new Stoneskin());
        BaseMod.addCard(new CureLightWounds());
        BaseMod.addCard(new HunkerDown());
        BaseMod.addCard(new AllIn());
        BaseMod.addCard(new FiveFootStep());
        BaseMod.addCard(new SpellCombat());

        //Rares. 3 attacks, 9 skills, 6 powers
        BaseMod.addCard(new Disintegrate());
        BaseMod.addCard(new Haste());
        BaseMod.addCard(new Discipline());
        BaseMod.addCard(new Spellstrike());
        BaseMod.addCard(new KnowledgePool());
        BaseMod.addCard(new MirrorEntity());
        BaseMod.addCard(new GeminiForm());
        BaseMod.addCard(new Momentum());
        BaseMod.addCard(new SpontaneousCaster());
        BaseMod.addCard(new ObscuringMist());
        BaseMod.addCard(new GreaterInvisibility());
        BaseMod.addCard(new MysticalShield());
        BaseMod.addCard(new Doublecast());
        BaseMod.addCard(new Natural20());
        BaseMod.addCard(new PreparedCaster());
        BaseMod.addCard(new Lunge());
        BaseMod.addCard(new SpellRecall());
        BaseMod.addCard(new ClosingBarrage());
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
        String[] keywordArcane = {"arcane"};
        String[] keywordTechnical = {"technical"};
        BaseMod.addKeyword(keywordCantrips, "Considered a spell so long as you've played fewer than 2 spells this turn");
        BaseMod.addKeyword(keywordSpells, "Advance the \"spells played\" counter for the turn");
        BaseMod.addKeyword(keywordTechniques, "Advance the \"Techniques played\" counter for the turn");
        BaseMod.addKeyword(keywordArcane, "Has a special effect if you played a spell this turn");
        BaseMod.addKeyword(keywordTechnical, "Has a special effect if you played a Technique this turn");
    }

    @Override
    public void receiveEditStrings() {
        String relicStrings = Gdx.files.internal("MysticMod/strings/relics.json").readString(String.valueOf(StandardCharsets.UTF_8));
        BaseMod.loadCustomStrings(RelicStrings.class, relicStrings);
        String cardStrings = Gdx.files.internal("MysticMod/strings/cards.json").readString(String.valueOf(StandardCharsets.UTF_8));
        BaseMod.loadCustomStrings(CardStrings.class, cardStrings);
        String powerStrings = Gdx.files.internal("MysticMod/strings/powers.json").readString(String.valueOf(StandardCharsets.UTF_8));
        BaseMod.loadCustomStrings(PowerStrings.class, powerStrings);
    }

    @Override
    public void receiveEditRelics() {
        BaseMod.addRelicToCustomPool(new MysticSpellBook(), AbstractCardEnum.MYSTIC_PURPLE.toString());
    }

    @Override
    public void receivePostBattle(final AbstractRoom p0) { //for Magic Missile
        numberOfTimesDeckShuffledThisCombat = 0;
        for (final AbstractCard card : AbstractDungeon.player.masterDeck.group) {
            if (card instanceof MagicMissile) {
                card.rawDescription = MagicMissile.DESCRIPTION;
                card.initializeDescription();
            }
            if (card instanceof ClosingBarrage) {
                card.rawDescription = ClosingBarrage.DESCRIPTION;
                card.initializeDescription();
            }
        }
    }

    public static boolean isThisASpell(final AbstractCard card, final boolean onUseCard) { //Is this a pigeon?
        if (card.type == AbstractCard.CardType.SKILL || card.type == AbstractCard.CardType.ATTACK) {
            if (AbstractDungeon.player.hasPower(GeminiFormPower.POWER_ID)) {
                int attackOrSkillNumber = GeminiFormPower.attacksAndSkillsPlayedThisTurn;
                if (onUseCard) {
                    attackOrSkillNumber--;
                }
                if (attackOrSkillNumber < AbstractDungeon.player.getPower(GeminiFormPower.POWER_ID).amount) {
                    return true;
                }
            } else if (card.rawDescription.startsWith("Spell.")) {
                return true;
            } else if (card.rawDescription.startsWith("Cantrip.")) {
                if (!AbstractDungeon.player.hasPower(SpellsPlayed.POWER_ID) || (AbstractDungeon.player.getPower(SpellsPlayed.POWER_ID).amount < 2)) {
                    return true;
                }
            } else { return false; }
        } else { return false; }
        return false; //how did you get here
    }

    public static boolean isThisATechnique(final AbstractCard card, final boolean onUseCard) {
        if (card.type == AbstractCard.CardType.SKILL || card.type == AbstractCard.CardType.ATTACK) {
            if (AbstractDungeon.player.hasPower(GeminiFormPower.POWER_ID)) {
                int attackOrSkillNumber = GeminiFormPower.attacksAndSkillsPlayedThisTurn;
                if (onUseCard) {
                    attackOrSkillNumber--;
                }
                if (attackOrSkillNumber < AbstractDungeon.player.getPower(GeminiFormPower.POWER_ID).amount) {
                    return true;
                }
            } else if (card.rawDescription.startsWith("Technique.")) {
                return true;
            } else { return false; }
        } else { return false; }
        return false; //how did you get here
    }

    public static AbstractCard returnTrulyRandomSpell() {
        final ArrayList<AbstractCard> list = new ArrayList<AbstractCard>();
        for (final Map.Entry<String, AbstractCard> potentialSpell : CardLibrary.cards.entrySet()) {
            final AbstractCard card = potentialSpell.getValue();
            if (card.rarity != AbstractCard.CardRarity.BASIC && card.rawDescription.startsWith("Spell.")) {
                list.add(card);
            }
        }
        return list.get(AbstractDungeon.cardRandomRng.random(list.size() - 1));
    }

    public static AbstractCard returnTrulyRandomTechnique() {
        final ArrayList<AbstractCard> list = new ArrayList<AbstractCard>();
        for (final Map.Entry<String, AbstractCard> potentialTechnique : CardLibrary.cards.entrySet()) {
            final AbstractCard card = potentialTechnique.getValue();
            if (card.rarity != AbstractCard.CardRarity.BASIC && card.rawDescription.startsWith("Technique.")) {
                list.add(card);
            }
        }
        return list.get(AbstractDungeon.cardRandomRng.random(list.size() - 1));
    }
}
