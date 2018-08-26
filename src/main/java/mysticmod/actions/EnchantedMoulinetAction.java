package mysticmod.actions;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;

public class EnchantedMoulinetAction extends AbstractGameAction
{
    public int[] damage;
    private boolean firstFrame;

    public EnchantedMoulinetAction(final AbstractCreature source, final int[] amount, final DamageInfo.DamageType type, final AttackEffect effect, final boolean isFast) {
        this.firstFrame = true;
        this.setValues(null, source, amount[0]);
        this.damage = amount;
        this.actionType = ActionType.DAMAGE;
        this.damageType = type;
        this.attackEffect = effect;
        if (isFast) {
            this.duration = Settings.ACTION_DUR_XFAST;
        }
        else {
            this.duration = Settings.ACTION_DUR_FAST;
        }
    }

    public EnchantedMoulinetAction(final AbstractCreature source, final int[] amount, final DamageInfo.DamageType type, final AttackEffect effect) {
        this(source, amount, type, effect, false);
    }

    @Override
    public void update() {
        if (this.firstFrame) {
            boolean playedMusic = false;
            for (int temp = AbstractDungeon.getCurrRoom().monsters.monsters.size(), i = 0; i < temp; ++i) {
                if (!AbstractDungeon.getCurrRoom().monsters.monsters.get(i).isDying && AbstractDungeon.getCurrRoom().monsters.monsters.get(i).currentHealth > 0 && !AbstractDungeon.getCurrRoom().monsters.monsters.get(i).isEscaping) {
                    if (playedMusic) {
                        AbstractDungeon.effectList.add(new FlashAtkImgEffect(AbstractDungeon.getCurrRoom().monsters.monsters.get(i).hb.cX, AbstractDungeon.getCurrRoom().monsters.monsters.get(i).hb.cY, this.attackEffect, true));
                    }
                    else {
                        playedMusic = true;
                        AbstractDungeon.effectList.add(new FlashAtkImgEffect(AbstractDungeon.getCurrRoom().monsters.monsters.get(i).hb.cX, AbstractDungeon.getCurrRoom().monsters.monsters.get(i).hb.cY, this.attackEffect));
                    }
                }
            }
            this.firstFrame = false;
        }
        this.tickDuration();
        if (this.isDone) {
            for (final AbstractPower p : AbstractDungeon.player.powers) {
                p.onDamageAllEnemies(this.damage);
            }
            for (int temp2 = AbstractDungeon.getCurrRoom().monsters.monsters.size(), j = 0; j < temp2; ++j) {
                if (!AbstractDungeon.getCurrRoom().monsters.monsters.get(j).isDeadOrEscaped()) {
                    if (this.attackEffect == AttackEffect.POISON) {
                        AbstractDungeon.getCurrRoom().monsters.monsters.get(j).tint.color = Color.CHARTREUSE.cpy();
                        AbstractDungeon.getCurrRoom().monsters.monsters.get(j).tint.changeColor(Color.WHITE.cpy());
                    }
                    else if (this.attackEffect == AttackEffect.FIRE) {
                        AbstractDungeon.getCurrRoom().monsters.monsters.get(j).tint.color = Color.RED.cpy();
                        AbstractDungeon.getCurrRoom().monsters.monsters.get(j).tint.changeColor(Color.WHITE.cpy());
                    }
                    AbstractDungeon.getCurrRoom().monsters.monsters.get(j).damage(new DamageInfo(this.source, this.damage[j], this.damageType));
                    if (AbstractDungeon.getCurrRoom().monsters.monsters.get(j).isDying || AbstractDungeon.getCurrRoom().monsters.monsters.get(j).currentHealth <= 0) {
                        AbstractDungeon.actionManager.addToBottom(new GainEnergyAction(1));
                    }
                }
            }
            if (AbstractDungeon.getCurrRoom().monsters.areMonstersBasicallyDead()) {
                AbstractDungeon.actionManager.clearPostCombatActions();
            }
            if (!Settings.FAST_MODE) {
                AbstractDungeon.actionManager.addToTop(new WaitAction(0.1f));
            }
        }
    }
}
