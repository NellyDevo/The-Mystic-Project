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
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.region128 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage("mysticmod/images/powers/gemini form power 84.png"), 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage("mysticmod/images/powers/gemini form power 32.png"), 0, 0, 32, 32);
        this.type = PowerType.BUFF;
        this.amount = amount;
        this.updateDescription();
    }

    @Override
    public void updateDescription() {
        if (this.amount > 1) {
            description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[2] + DESCRIPTIONS[3];
        } else {
            description = DESCRIPTIONS[0] + DESCRIPTIONS[1] + DESCRIPTIONS[3];
        }
    }

    @Override
    public void onUseCard(AbstractCard c, UseCardAction action) {
        if (isActive) {
            if (MysticMod.isThisASpell(c)) {
                if (spellsPlayedThisTurn < this.amount) {
                    AbstractDungeon.actionManager.addToBottom(new GeminiFormAction((AbstractMonster) action.target, true, c));
                }
                this.spellsPlayedThisTurn++;
            }
            if (MysticMod.isThisAnArte(c)) {
                if (artesPlayedThisTurn < this.amount) {
                    AbstractDungeon.actionManager.addToBottom(new GeminiFormAction((AbstractMonster) action.target, false, c));
                }
                this.artesPlayedThisTurn++;
            }
        } else {
            System.out.println("cardQueue.size() = " + AbstractDungeon.actionManager.cardQueue.size());
            if (AbstractDungeon.actionManager.cardQueue.size() == 1) {
                isActive = true;
            }
        }
        System.out.println("isActive = " + isActive);
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        if (isPlayer) {
            this.artesPlayedThisTurn = 0;
            this.spellsPlayedThisTurn = 0;
        }
    }

    @Override
    public void onInitialApplication() {
        for (AbstractCard card : AbstractDungeon.actionManager.cardsPlayedThisTurn) {
            if (MysticMod.isThisASpell(card)) {
                this.spellsPlayedThisTurn++;
            }
            if (MysticMod.isThisAnArte(card)) {
                this.artesPlayedThisTurn++;
            }
        }
    }
}
