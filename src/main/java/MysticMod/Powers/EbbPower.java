package MysticMod.Powers;

import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.actions.common.DiscardAction;

public class EbbPower extends AbstractPower {
    public static final String POWER_ID = "MysticMod:EbbPower";
    public static final PowerStrings cardStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = cardStrings.NAME;
    public static final String[] DESCRIPTIONS = cardStrings.DESCRIPTIONS;

    public EbbPower(AbstractCreature owner) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;

        this.img = new Texture("MysticMod/images/powers/ebb power.png");
        this.type = PowerType.DEBUFF;
        this.amount = -1;
        this.updateDescription();

    }

    @Override
    public void updateDescription() {
            description = DESCRIPTIONS[0];
    }

    @Override
    public void atStartOfTurnPostDraw() {
        boolean hasSpell = false;
        boolean hasTechnique = false;
        for (final AbstractCard card : AbstractDungeon.player.hand.group) {
            if (card.rawDescription.startsWith("Spell") || card.rawDescription.startsWith("Cantrip")) {
                hasSpell = true;
            }
            if (card.rawDescription.startsWith("Technique")) {
                hasTechnique = true;
            }
            if (hasSpell && hasTechnique) {
                return;
            }
        }
        if (!hasSpell || !hasTechnique) {
            AbstractDungeon.actionManager.addToBottom(new DiscardAction(AbstractDungeon.player, AbstractDungeon.player, 2, false));
        }
    }
}
