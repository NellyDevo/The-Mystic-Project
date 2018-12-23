package mysticmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import mysticmod.powers.ArtesPlayed;
import mysticmod.powers.SpellsPlayed;

public class SpellCombatAction extends AbstractGameAction
{
    private boolean freeToPlayOnce;
    private DamageInfo.DamageType damageType;
    private AbstractPlayer p;
    private int energyOnUse;
    private AbstractMonster m;
    private int damage;
    private int block;

    public SpellCombatAction(final AbstractPlayer p, final AbstractMonster m, final int damage, final int block, final DamageInfo.DamageType damageType, final boolean isThisFreeToPlayOnce, final int energyOnUse) {
        this.freeToPlayOnce = false;
        this.energyOnUse = -1;
        this.damageType = damageType;
        this.p = p;
        this.m = m;
        this.freeToPlayOnce = isThisFreeToPlayOnce;
        this.duration = Settings.ACTION_DUR_XFAST;
        this.actionType = ActionType.SPECIAL;
        this.energyOnUse = energyOnUse;
        this.damage = damage;
        this.block = block;
    }

    @Override
    public void update() {
        boolean poised = false;
        boolean powerful = false;
        if (p.hasPower(ArtesPlayed.POWER_ID)) {
            poised = true;
        }
        if (p.hasPower(SpellsPlayed.POWER_ID)) {
            powerful = true;
        }
        int effect = EnergyPanel.totalCount;
        if (this.energyOnUse != -1) {
            effect = this.energyOnUse;
        }
        if (this.p.hasRelic("Chemical X")) {
            effect += 2;
            this.p.getRelic("Chemical X").flash();
        }
        if (effect > 0) {
            for (int i = 0; i < effect; ++i) {
                if (powerful) {
                    AttackEffect alternatingEffect;
                    if (i % 2 == 0) {
                        alternatingEffect = AttackEffect.SLASH_HORIZONTAL;
                    } else {
                        alternatingEffect = AttackEffect.SLASH_VERTICAL;
                    }
                    AbstractDungeon.actionManager.addToBottom(new DamageAction(this.m, new DamageInfo(this.p, this.damage, damageType), alternatingEffect));
                }
                if (poised) {
                    AbstractDungeon.actionManager.addToBottom(new com.megacrit.cardcrawl.actions.common.GainBlockAction(p, p, this.block));
                }
            }
            if (!this.freeToPlayOnce) {
                this.p.energy.use(EnergyPanel.totalCount);
            }
        }
        this.isDone = true;
    }
}