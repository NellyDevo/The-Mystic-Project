package mysticmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;

import java.util.ArrayList;
import java.util.Random;

public class ClosingBarrageAction extends AbstractGameAction {
    private boolean freeToPlayOnce;
    private DamageInfo.DamageType damageType;
    private AbstractPlayer p;
    private int energyOnUse;
    private AbstractMonster m;
    private int inheritedDamage;


    public ClosingBarrageAction(final AbstractPlayer p, final AbstractMonster m, final int damage, final DamageInfo.DamageType damageType, final boolean isThisFreeToPlayOnce, final int energyOnUse) {
        this.freeToPlayOnce = false;
        this.energyOnUse = -1;
        this.damageType = damageType;
        this.p = p;
        this.m = m;
        this.freeToPlayOnce = isThisFreeToPlayOnce;
        this.duration = Settings.ACTION_DUR_XFAST;
        this.actionType = ActionType.SPECIAL;
        this.energyOnUse = energyOnUse;
        this.inheritedDamage = damage;
    }

    @Override
    public void update() {
        int effect = EnergyPanel.totalCount;
        if (this.energyOnUse != -1) {
            effect = this.energyOnUse;
        }
        if (this.p.hasRelic("Chemical X")) {
            effect += 2;
            this.p.getRelic("Chemical X").flash();
        }
        Random generator = new Random();
        ArrayList<AttackEffect> randomEffect = new ArrayList<>();
        randomEffect.add(AttackEffect.FIRE);
        randomEffect.add(AttackEffect.POISON);
        randomEffect.add(AttackEffect.BLUNT_LIGHT);
        randomEffect.add(AttackEffect.SLASH_DIAGONAL);
        if (effect > 0) {
            for (int i = 0; i < effect; ++i) {
                AbstractDungeon.actionManager.addToBottom(new DamageAction(this.m, new DamageInfo(this.p, this.inheritedDamage, this.damageType), randomEffect.get(generator.nextInt(randomEffect.size()))));
            }
            if (!this.freeToPlayOnce) {
                this.p.energy.use(EnergyPanel.totalCount);
            }
        }
        this.isDone = true;
    }
}