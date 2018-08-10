package MysticMod.Powers;

import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;

public class HastePower extends AbstractPower {
    public static final String POWER_ID = "MysticMod:HastePower";
    public static final String NAME = "Haste";
    public static final String DESCRIPTIONS = "Draw 1 card and gain [E] each turn.";

    public HastePower(AbstractCreature owner) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;

        this.img = new Texture("MysticMod/images/powers/haste power.png");
        this.type = PowerType.BUFF;
        this.amount = -1;
        this.updateDescription();

    }

    @Override
    public void updateDescription() {
            description = DESCRIPTIONS;
    }

    @Override
    public void atStartOfTurn() {
        AbstractDungeon.actionManager.addToBottom(new com.megacrit.cardcrawl.actions.common.DrawCardAction(AbstractDungeon.player, 1));
        AbstractDungeon.actionManager.addToBottom(new com.megacrit.cardcrawl.actions.common.GainEnergyAction(1));
    }
}
