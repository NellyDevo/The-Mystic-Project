//ClosingBarrageAction(p, m, this.upgraded, this.damageTypeForTurn, this.freeToPlayOnce, this.energyOnUse));
package MysticMod.Actions;

import com.megacrit.cardcrawl.actions.*;
import com.megacrit.cardcrawl.cards.*;
import com.megacrit.cardcrawl.characters.*;
import com.megacrit.cardcrawl.ui.panels.*;
import com.megacrit.cardcrawl.dungeons.*;
import com.megacrit.cardcrawl.actions.utility.*;
import com.megacrit.cardcrawl.vfx.combat.*;
import com.megacrit.cardcrawl.actions.animations.*;
import com.megacrit.cardcrawl.core.*;
import com.megacrit.cardcrawl.vfx.*;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import MysticMod.Powers.SpellsPlayed;
import MysticMod.Powers.TechniquesPlayed;

public class ClosingBarrageAction extends AbstractGameAction
{
    public int[] multiDamage;
    private boolean freeToPlayOnce;
    private DamageInfo.DamageType damageType;
    private AbstractPlayer p;
    private int energyOnUse;
    private AbstractMonster m;
    private boolean upgraded;


    public ClosingBarrageAction(final AbstractPlayer p, final AbstractMonster m, final boolean isThisUpgraded, final DamageInfo.DamageType damageType, final boolean isThisFreeToPlayOnce, final int energyOnUse) {
        this.freeToPlayOnce = false;
        this.energyOnUse = -1;
        this.multiDamage = multiDamage;
        this.damageType = damageType;
        this.p = p;
        this.m = m;
        this.freeToPlayOnce = isThisFreeToPlayOnce;
        this.duration = Settings.ACTION_DUR_XFAST;
        this.actionType = ActionType.SPECIAL;
        this.energyOnUse = energyOnUse;
        this.upgraded = isThisUpgraded;
    }

    @Override
    public void update() {
        int damageX = 0;
        int damageY = 0;
        if (p.hasPower(TechniquesPlayed.POWER_ID)) {
            damageX = p.getPower(TechniquesPlayed.POWER_ID).amount;
        }
        if (p.hasPower(SpellsPlayed.POWER_ID)) {
            damageY = p.getPower(SpellsPlayed.POWER_ID).amount;
        }
        int damage = damageX * damageY;
        int effect = EnergyPanel.totalCount;
        if (this.energyOnUse != -1) {
            effect = this.energyOnUse;
        }
        if (this.p.hasRelic("Chemical X")) {
            effect += 2;
            this.p.getRelic("Chemical X").flash();
        }
        if (this.upgraded) {
            effect += 1;
        }
        if (effect > 0) {
            for (int i = 0; i < effect; ++i) {
                AbstractDungeon.actionManager.addToBottom(new DamageAction(this.m, new DamageInfo(this.p, damage, damageType), AttackEffect.BLUNT_LIGHT));
            }
            if (!this.freeToPlayOnce) {
                this.p.energy.use(EnergyPanel.totalCount);
            }
        }
        this.isDone = true;
    }
}