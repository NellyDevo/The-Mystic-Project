package mysticmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.ChemicalX;
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
    private boolean upgraded;

    public ClosingBarrageAction(AbstractPlayer p, AbstractMonster m, int damage, DamageInfo.DamageType damageType, boolean isThisFreeToPlayOnce, int energyOnUse, boolean upgraded) {
        this.damageType = damageType;
        this.p = p;
        this.m = m;
        this.energyOnUse = energyOnUse;
        this.upgraded = upgraded;
        freeToPlayOnce = isThisFreeToPlayOnce;
        duration = Settings.ACTION_DUR_XFAST;
        actionType = ActionType.SPECIAL;
        inheritedDamage = damage;
    }

    @Override
    public void update() {
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
        Random generator = new Random();
        ArrayList<AttackEffect> randomEffect = new ArrayList<>();
        randomEffect.add(AttackEffect.FIRE);
        randomEffect.add(AttackEffect.POISON);
        randomEffect.add(AttackEffect.BLUNT_LIGHT);
        randomEffect.add(AttackEffect.SLASH_DIAGONAL);
        if (effect > 0) {
            for (int i = 0; i < effect; ++i) {
                AbstractDungeon.actionManager.addToBottom(
                        new DamageAction(m, new DamageInfo(p, inheritedDamage, damageType),
                                randomEffect.get(generator.nextInt(randomEffect.size()))));
            }
            if (!freeToPlayOnce) {
                p.energy.use(EnergyPanel.totalCount);
            }
        }
        isDone = true;
    }
}
