package mysticmod.relics;

import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.core.EnergyManager;
import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import mysticmod.MysticMod;

public class DeckOfManyThings extends CustomRelic {
    public static final String ID = "mysticmod:DeckOfManyThings";
    public static final Texture IMG = new Texture("mysticmod/images/relics/deckofmanythings.png");

    public DeckOfManyThings() {
        super(ID, IMG, RelicTier.BOSS, LandingSound.MAGICAL);
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public void onPlayCard(final AbstractCard c, final AbstractMonster m) {
        if (!MysticMod.isThisASpell(c, true) && !MysticMod.isThisATechnique(c, true) && c.type != AbstractCard.CardType.POWER) {
            this.flash();
            AbstractDungeon.actionManager.addToBottom(new LoseHPAction(AbstractDungeon.player, AbstractDungeon.player, 1));
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
    public AbstractRelic makeCopy() { // always override this method to return a new instance of your relic
        return new DeckOfManyThings();
    }
}