package mysticmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.ChemicalX;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import mysticmod.powers.ArtesPlayed;
import mysticmod.powers.SpellsPlayed;

public class SpellCombatAction extends AbstractGameAction {
    private boolean freeToPlayOnce;
    private DamageInfo.DamageType damageType;
    private AbstractPlayer p;
    private int energyOnUse;
    private AbstractMonster m;
    private int damage;
    private int block;

    public SpellCombatAction(AbstractPlayer p, AbstractMonster m, int damage, int block, DamageInfo.DamageType damageType, boolean isThisFreeToPlayOnce, int energyOnUse) {
        freeToPlayOnce = isThisFreeToPlayOnce;
        duration = Settings.ACTION_DUR_XFAST;
        actionType = ActionType.SPECIAL;
        this.energyOnUse = energyOnUse;
        this.damage = damage;
        this.block = block;
        this.damageType = damageType;
        this.p = p;
        this.m = m;
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
        if (energyOnUse != -1) {
            effect = energyOnUse;
        }
        if (p.hasRelic(ChemicalX.ID)) {
            effect += 2;
            p.getRelic(ChemicalX.ID).flash();
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
                    AbstractDungeon.actionManager.addToBottom(new DamageAction(m, new DamageInfo(p, damage, damageType), alternatingEffect));
                }
                if (poised) {
                    AbstractDungeon.actionManager.addToBottom(new GainBlockAction(p, p, block));
                }
            }
            if (!freeToPlayOnce) {
                p.energy.use(EnergyPanel.totalCount);
            }
        }
        isDone = true;
    }
}
