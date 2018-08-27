package mysticmod.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.EnergyManager;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import mysticmod.MysticMod;

public class DeckOfManyThings extends CustomRelic {
    public static final String ID = "mysticmod:DeckOfManyThings";
    public static final Texture IMG = new Texture("mysticmod/images/relics/deckofmanythings.png");
    public static final Texture OUTLINE = new Texture("mysticmod/images/relics/deckofmanythings_p.png");
    private boolean spellPlayed;
    private boolean techniquePlayed;

    public DeckOfManyThings() {
        super(ID, IMG, OUTLINE, RelicTier.BOSS, LandingSound.FLAT);
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public void onPlayCard(final AbstractCard c, final AbstractMonster m) {
        if (MysticMod.isThisASpell(c)) {
            this.spellPlayed = true;

        }
        if (MysticMod.isThisATechnique(c)) {
            this.techniquePlayed = true;
        }
        if (this.spellPlayed && this.techniquePlayed) {
            this.pulse = false;
        }
    }

    @Override
    public void onEquip() {
        final EnergyManager energy = AbstractDungeon.player.energy;
        ++energy.energyMaster;
    }

    @Override
    public void onUnequip() {
        final EnergyManager energy = AbstractDungeon.player.energy;
        --energy.energyMaster;
    }

    @Override
    public void atTurnStart() {
        this.beginPulse();
        this.pulse = true;
    }

    @Override
    public void onPlayerEndTurn() {
        if (!this.spellPlayed || !this.techniquePlayed) {
            AbstractDungeon.actionManager.addToBottom(new LoseHPAction(AbstractDungeon.player, AbstractDungeon.player, 2));
            this.flash();
            this.pulse = false;
        }
    }

    @Override
    public AbstractRelic makeCopy() { // always override this method to return a new instance of your relic
        return new DeckOfManyThings();
    }
}