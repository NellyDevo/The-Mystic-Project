package MysticMod.Powers;

import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;

public class TechniquesPlayedNextTurn extends AbstractPower {
    public static final String POWER_ID = "MysticMod:TechniquesPlayedNextTurnPower";
    public static final String NAME = "Techniques Next Turn";
    public static final String[] DESCRIPTIONS = new String[]{ "Start next turn as if you already played ", "Technique.", "Techniques." };

    public TechniquesPlayedNextTurn(AbstractCreature owner, int amount) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;

        this.img = new Texture("MysticMod/images/powers/techniques played next turn.png");
        this.type = PowerType.BUFF;
        this.amount = amount;
        this.updateDescription();

    }

    @Override
    public void updateDescription() {
        if (amount == 1) {
            description = DESCRIPTIONS[0] + " 1 " + DESCRIPTIONS[1];
        } else {
            description = DESCRIPTIONS[0] + " "+amount+" " + DESCRIPTIONS[2];
        }

    }

    @Override
    public void atStartOfTurn() {
        AbstractDungeon.actionManager.addToBottom(new com.megacrit.cardcrawl.actions.common.ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new TechniquesPlayed(AbstractDungeon.player, this.amount), this.amount));
        AbstractDungeon.actionManager.addToBottom(
                new com.megacrit.cardcrawl.actions.common.ReducePowerAction(AbstractDungeon.player, AbstractDungeon.player, TechniquesPlayedNextTurn.POWER_ID, AbstractDungeon.player.getPower(TechniquesPlayedNextTurn.POWER_ID).amount)
        );
    }


}
