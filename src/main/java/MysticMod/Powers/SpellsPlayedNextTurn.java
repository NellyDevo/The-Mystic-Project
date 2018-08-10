package MysticMod.Powers;

import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;

public class SpellsPlayedNextTurn extends AbstractPower {
    public static final String POWER_ID = "MysticMod:SpellsPlayedNextTurnPower";
    public static final String NAME = "Spells Next Turn";
    public static final String[] DESCRIPTIONS = new String[]{ "Start next turn as if you already played ", "Spell.", "Spells." };

    public SpellsPlayedNextTurn(AbstractCreature owner, int amount) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;

        this.img = new Texture("MysticMod/images/powers/spells played next turn.png");
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
        AbstractDungeon.actionManager.addToBottom(new com.megacrit.cardcrawl.actions.common.ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new SpellsPlayed(AbstractDungeon.player, this.amount), this.amount));
        AbstractDungeon.actionManager.addToBottom(
                new com.megacrit.cardcrawl.actions.common.ReducePowerAction(AbstractDungeon.player, AbstractDungeon.player, SpellsPlayedNextTurn.POWER_ID, AbstractDungeon.player.getPower(SpellsPlayedNextTurn.POWER_ID).amount)
        );
    }


}
