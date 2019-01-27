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
    private int spellPlayed;
    private int ArtePlayed;

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
            spellPlayed += 1;
        }
        if (MysticMod.isThisAnArte(c)) {
            ArtePlayed += 1;
        }
        if (spellPlayed > 1 && ArtePlayed > 1) {
            pulse = false;
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
        beginPulse();
        pulse = true;
        spellPlayed = 0;
        ArtePlayed = 0;
    }

    @Override
    public void onVictory() {
        pulse = false;
    }

    @Override
    public void onPlayerEndTurn() {
        if (!(spellPlayed > 1) || !(ArtePlayed > 1)) {
            AbstractDungeon.actionManager.addToBottom(new LoseHPAction(AbstractDungeon.player, AbstractDungeon.player, 2));
            flash();
            pulse = false;
        }
    }

    @Override
    public AbstractRelic makeCopy() {
        return new DeckOfManyThings();
    }
}
