package mysticmod.powers;

import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
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

        this.img = new Texture("mysticmod/images/powers/spontaneous caster power.png");
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
        //if card is a spell, set exhaust = true, generate a random spell, add a copy of random spell to discard pile
        if (MysticMod.isThisASpell(card)) {
            this.flash();
            action.exhaustCard = true;
            AbstractCard newCard = MysticMod.returnTrulyRandomSpell();
            UnlockTracker.markCardAsSeen(newCard.cardID);
            AbstractDungeon.actionManager.addToBottom(new ReplaceCardAction(card, newCard.makeStatEquivalentCopy()));
        }
    }
}