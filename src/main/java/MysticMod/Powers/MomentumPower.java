package MysticMod.Powers;

import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import MysticMod.Powers.GeminiFormPower;

public class MomentumPower extends AbstractPower {
    public static final String POWER_ID = "MysticMod:MomentumPower";
    public static final String NAME = "Momentum";
    public static final String DESCRIPTIONS = "Spells and Techniques deal extra damage and apply extra block for each card of the other type played this turn";

    public MomentumPower(AbstractCreature owner) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;

        this.img = new Texture("MysticMod/images/powers/momentum power.png");
        this.type = PowerType.BUFF;
        this.amount = -1;
        this.updateDescription();

    }

    @Override
    public void updateDescription() {
        description = DESCRIPTIONS;
    }

    @Override
    public void onUseCard(final AbstractCard card, final UseCardAction action) {
        //Refer to ApplyPowersToBlockMomentumPatch.java, CalculateCardDamageMomentumPatch.java, and ApplyPowersMomentumPatch.java for full effects
    }
}
