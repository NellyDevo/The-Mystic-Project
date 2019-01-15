package mysticmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.ChemicalX;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import mysticmod.powers.HastePower;

import java.util.ArrayList;
import java.util.Random;

public class HasteAction extends AbstractGameAction {
    private boolean freeToPlayOnce;
    private int energyOnUse;
    private boolean upgraded;

    public HasteAction(boolean isThisFreeToPlayOnce, int energyOnUse, boolean upgraded) {
        this.freeToPlayOnce = false;
        this.energyOnUse = -1;
        this.freeToPlayOnce = isThisFreeToPlayOnce;
        this.duration = Settings.ACTION_DUR_XFAST;
        this.actionType = ActionType.SPECIAL;
        this.energyOnUse = energyOnUse;
        this.upgraded = upgraded;
    }

    @Override
    public void update() {
        AbstractPlayer p = AbstractDungeon.player;
        int effect = EnergyPanel.totalCount;
        if (this.energyOnUse != -1) {
            effect = this.energyOnUse;
        }
        if (p.hasRelic(ChemicalX.ID)) {
            effect += 2;
            p.getRelic(ChemicalX.ID).flash();
        }
        if (this.upgraded) {
            effect += 1;
        }
        if (effect > 0) {
            AbstractDungeon.actionManager.addToTop(new ApplyPowerAction(p, p, new HastePower(p, effect), effect));
        }
        if (!this.freeToPlayOnce) {
            p.energy.use(EnergyPanel.totalCount);
        }
        this.isDone = true;
    }
}
