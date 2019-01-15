package mysticmod.powers;

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

public class SpontaneousCasterPower extends AbstractPower {
    public static final String POWER_ID = "mysticmod:SpontaneousCasterPower";
    public static final PowerStrings cardStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = cardStrings.NAME;
    public static final String[] DESCRIPTIONS = cardStrings.DESCRIPTIONS;

    public SpontaneousCasterPower(AbstractCreature owner) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.region128 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage("mysticmod/images/powers/spontaneous caster power 84.png"), 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage("mysticmod/images/powers/spontaneous caster power 32.png"), 0, 0, 32, 32);
        this.type = PowerType.BUFF;
        this.amount = -1;
        this.updateDescription();
    }

    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[0];
    }

    @Override
    public void onUseCard(final AbstractCard card, final UseCardAction action) {
        if (MysticMod.isThisASpell(card)) {
            this.flash();
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
}
