package mysticmod.powers;

import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import mysticmod.MysticMod;
import mysticmod.actions.ReplaceCardAction;

public class SpontaneousCasterPower extends AbstractPower implements CloneablePowerInterface {
    public static final String POWER_ID = "mysticmod:SpontaneousCasterPower";
    public static final PowerStrings cardStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = cardStrings.NAME;
    public static final String[] DESCRIPTIONS = cardStrings.DESCRIPTIONS;

    public SpontaneousCasterPower(AbstractCreature owner) {
        name = NAME;
        ID = POWER_ID;
        this.owner = owner;
        region128 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage("mysticmod/images/powers/spontaneous caster power 84.png"), 0, 0, 84, 84);
        region48 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage("mysticmod/images/powers/spontaneous caster power 32.png"), 0, 0, 32, 32);
        type = PowerType.BUFF;
        amount = -1;
        updateDescription();
    }

    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[0];
    }

    @Override
    public void onUseCard(AbstractCard card, UseCardAction action) {
        if (MysticMod.isThisASpell(card)) {
            flash();
            action.exhaustCard = true;
            AbstractCard newCard = MysticMod.returnTrulyRandomSpell();
            UnlockTracker.markCardAsSeen(newCard.cardID);
            AbstractDungeon.actionManager.addToBottom(new ReplaceCardAction(card, newCard.makeStatEquivalentCopy()));
        }
    }

    @Override
    public void onDrawOrDiscard() {
        setSpellCosts();
    }

    @Override
    public void onInitialApplication() {
        setSpellCosts();
    }

    private void setSpellCosts() {
        for (AbstractCard card : AbstractDungeon.player.hand.group) {
            if (card.costForTurn > 1 && MysticMod.isThisASpell(card)) {
                card.costForTurn = 1;
                card.isCostModifiedForTurn = true;
                card.cost = 1;
                card.isCostModified = true;
            }
        }
        for (AbstractCard card : AbstractDungeon.player.drawPile.group) {
            if (card.costForTurn > 1 && MysticMod.isThisASpell(card)) {
                card.costForTurn = 1;
                card.isCostModifiedForTurn = true;
                card.cost = 1;
                card.isCostModified = true;
            }
        }
        for (AbstractCard card : AbstractDungeon.player.discardPile.group) {
            if (card.costForTurn > 1 && MysticMod.isThisASpell(card)) {
                card.costForTurn = 1;
                card.isCostModifiedForTurn = true;
                card.cost = 1;
                card.isCostModified = true;
            }
        }
        for (AbstractCard card : AbstractDungeon.player.exhaustPile.group) {
            if (card.costForTurn > 1 && MysticMod.isThisASpell(card)) {
                card.costForTurn = 1;
                card.isCostModifiedForTurn = true;
                card.cost = 1;
                card.isCostModified = true;
            }
        }
    }

    @Override
    public AbstractPower makeCopy() {
        return new SpontaneousCasterPower(owner);
    }
}
