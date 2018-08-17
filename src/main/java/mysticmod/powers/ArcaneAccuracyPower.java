package mysticmod.powers;

import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.powers.LoseStrengthPower;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import mysticmod.MysticMod;

public class ArcaneAccuracyPower extends AbstractPower {
    public static final String POWER_ID = "mysticmod:ArcaneAccuracyPower";
    public static final PowerStrings cardStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = cardStrings.NAME;
    public static final String[] DESCRIPTIONS = cardStrings.DESCRIPTIONS;

    public ArcaneAccuracyPower(AbstractCreature owner, int amount) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;

        this.img = new Texture("mysticmod/images/powers/arcane accuracy power.png");
        this.type = PowerType.BUFF;
        this.amount = amount;
        this.updateDescription();

    }

    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[0] + " "+amount+" " + DESCRIPTIONS[1];
    }

    @Override
    public void onUseCard(final AbstractCard card, final UseCardAction action) {
        if (MysticMod.isThisASpell(card, true)) {
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new StrengthPower(AbstractDungeon.player, this.amount), this.amount));
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new LoseStrengthPower(AbstractDungeon.player, this.amount), this.amount));
        }
    }
}