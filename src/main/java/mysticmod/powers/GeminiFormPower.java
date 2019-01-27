package mysticmod.powers;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import mysticmod.MysticMod;
import mysticmod.actions.GeminiFormAction;

public class GeminiFormPower extends AbstractPower {
    public static final String POWER_ID = "mysticmod:GeminiFormPower";
    public static final PowerStrings cardStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = cardStrings.NAME;
    public static final String[] DESCRIPTIONS = cardStrings.DESCRIPTIONS;
    private int spellsPlayedThisTurn = 0;
    private int artesPlayedThisTurn = 0;
    public static boolean isActive = true;

    public GeminiFormPower(AbstractCreature owner, int amount) {
        name = NAME;
        ID = POWER_ID;
        this.owner = owner;
        region128 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage("mysticmod/images/powers/gemini form power 84.png"), 0, 0, 84, 84);
        region48 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage("mysticmod/images/powers/gemini form power 32.png"), 0, 0, 32, 32);
        type = PowerType.BUFF;
        this.amount = amount;
        updateDescription();
    }

    @Override
    public void updateDescription() {
        if (amount > 1) {
            description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[2] + DESCRIPTIONS[3];
        } else {
            description = DESCRIPTIONS[0] + DESCRIPTIONS[1] + DESCRIPTIONS[3];
        }
    }

    @Override
    public void onUseCard(AbstractCard c, UseCardAction action) {
        if (isActive) {
            if (MysticMod.isThisASpell(c)) {
                if (spellsPlayedThisTurn < amount) {
                    AbstractDungeon.actionManager.addToBottom(new GeminiFormAction((AbstractMonster) action.target, true, c));
                }
                spellsPlayedThisTurn++;
            }
            if (MysticMod.isThisAnArte(c)) {
                if (artesPlayedThisTurn < amount) {
                    AbstractDungeon.actionManager.addToBottom(new GeminiFormAction((AbstractMonster) action.target, false, c));
                }
                artesPlayedThisTurn++;
            }
        } else {
            System.out.println("GEMINI FORM: cardQueue.size() = " + AbstractDungeon.actionManager.cardQueue.size());
            if (AbstractDungeon.actionManager.cardQueue.size() == 1) {
                isActive = true;
            }
        }
        System.out.println("GEMINI FORM: isActive = " + isActive);
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        if (isPlayer) {
            artesPlayedThisTurn = 0;
            spellsPlayedThisTurn = 0;
        }
    }

    @Override
    public void onInitialApplication() {
        for (AbstractCard card : AbstractDungeon.actionManager.cardsPlayedThisTurn) {
            if (MysticMod.isThisASpell(card)) {
                spellsPlayedThisTurn++;
            }
            if (MysticMod.isThisAnArte(card)) {
                artesPlayedThisTurn++;
            }
        }
    }
}
