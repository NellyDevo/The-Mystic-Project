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
import java.util.*;

public class MirrorEntityPower extends AbstractPower {
    public static final String POWER_ID = "MysticMod:MirrorEntityPower";
    public static final String NAME = "Mirror Entity";
    public static final String DESCRIPTIONS = "Spells and Techniques played carry over to the next turn";

    public MirrorEntityPower(AbstractCreature owner) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;

        this.img = new Texture("MysticMod/images/powers/mirror entity power.png");
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
        if (card.rawDescription.startsWith("Spell.")) {
            AbstractDungeon.actionManager.addToBottom(new com.megacrit.cardcrawl.actions.common.ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new SpellsPlayedNextTurn(AbstractDungeon.player, 1), 1));
        }
        if (card.rawDescription.startsWith("Technique.")) {
            AbstractDungeon.actionManager.addToBottom(new com.megacrit.cardcrawl.actions.common.ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new TechniquesPlayedNextTurn(AbstractDungeon.player, 1), 1));
        }
        if (card.rawDescription.startsWith("Cantrip.")) {
            if (
                    !(AbstractDungeon.player.hasPower(SpellsPlayed.POWER_ID))
                            ||
                            (AbstractDungeon.player.hasPower(SpellsPlayed.POWER_ID) && (AbstractDungeon.player.getPower(SpellsPlayed.POWER_ID).amount == 1))
            ) {
                AbstractDungeon.actionManager.addToBottom(new com.megacrit.cardcrawl.actions.common.ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new SpellsPlayedNextTurn(AbstractDungeon.player, 1), 1));
            }
        }
    }
}
