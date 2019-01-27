package mysticmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.ChemicalX;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import mysticmod.powers.HastePower;

public class HasteAction extends AbstractGameAction {
    private boolean freeToPlayOnce;
    private int energyOnUse;
    private boolean upgraded;

    public HasteAction(boolean isThisFreeToPlayOnce, int energyOnUse, boolean upgraded) {
        freeToPlayOnce = isThisFreeToPlayOnce;
        duration = Settings.ACTION_DUR_XFAST;
        actionType = ActionType.SPECIAL;
        this.energyOnUse = energyOnUse;
        this.upgraded = upgraded;
    }

    @Override
    public void update() {
        AbstractPlayer p = AbstractDungeon.player;
        int effect = EnergyPanel.totalCount;
        if (energyOnUse != -1) {
            effect = energyOnUse;
        }
        if (p.hasRelic(ChemicalX.ID)) {
            effect += 2;
            p.getRelic(ChemicalX.ID).flash();
        }
        if (upgraded) {
            effect += 1;
        }
        if (effect > 0) {
            boolean dontDecay = !AbstractDungeon.player.hasPower(HastePower.POWER_ID);
            AbstractDungeon.actionManager.addToTop(new ApplyPowerAction(p, p, new HastePower(p, dontDecay, effect), effect));
        }
        if (!freeToPlayOnce) {
            p.energy.use(EnergyPanel.totalCount);
        }
        isDone = true;
    }
}
