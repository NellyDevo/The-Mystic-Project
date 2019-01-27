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

public class EnchantedMoulinetAction extends AbstractGameAction {
    public int[] damage;
    private boolean firstFrame;

    public EnchantedMoulinetAction(AbstractCreature source, int[] amount, DamageInfo.DamageType type, AttackEffect effect, boolean isFast) {
        firstFrame = true;
        setValues(null, source, amount[0]);
        damage = amount;
        actionType = ActionType.DAMAGE;
        damageType = type;
        attackEffect = effect;
        if (isFast) {
            duration = Settings.ACTION_DUR_XFAST;
        } else {
            duration = Settings.ACTION_DUR_FAST;
        }
    }

    public EnchantedMoulinetAction(AbstractCreature source, int[] amount, DamageInfo.DamageType type, AttackEffect effect) {
        this(source, amount, type, effect, false);
    }

    @Override
    public void update() {
        if (firstFrame) {
            boolean playedMusic = false;
            for (int temp = AbstractDungeon.getCurrRoom().monsters.monsters.size(), i = 0; i < temp; ++i) {
                if (!AbstractDungeon.getCurrRoom().monsters.monsters.get(i).isDying && AbstractDungeon.getCurrRoom().monsters.monsters.get(i).currentHealth > 0 && !AbstractDungeon.getCurrRoom().monsters.monsters.get(i).isEscaping) {
                    if (playedMusic) {
                        AbstractDungeon.effectList.add(new FlashAtkImgEffect(AbstractDungeon.getCurrRoom().monsters.monsters.get(i).hb.cX, AbstractDungeon.getCurrRoom().monsters.monsters.get(i).hb.cY, attackEffect, true));
                    } else {
                        playedMusic = true;
                        AbstractDungeon.effectList.add(new FlashAtkImgEffect(AbstractDungeon.getCurrRoom().monsters.monsters.get(i).hb.cX, AbstractDungeon.getCurrRoom().monsters.monsters.get(i).hb.cY, attackEffect));
                    }
                }
            }
            firstFrame = false;
        }
        tickDuration();
        if (isDone) {
            for (AbstractPower p : AbstractDungeon.player.powers) {
                p.onDamageAllEnemies(damage);
            }
            for (int temp2 = AbstractDungeon.getCurrRoom().monsters.monsters.size(), j = 0; j < temp2; ++j) {
                if (!AbstractDungeon.getCurrRoom().monsters.monsters.get(j).isDeadOrEscaped()) {
                    if (attackEffect == AttackEffect.POISON) {
                        AbstractDungeon.getCurrRoom().monsters.monsters.get(j).tint.color = Color.CHARTREUSE.cpy();
                        AbstractDungeon.getCurrRoom().monsters.monsters.get(j).tint.changeColor(Color.WHITE.cpy());
                    } else if (attackEffect == AttackEffect.FIRE) {
                        AbstractDungeon.getCurrRoom().monsters.monsters.get(j).tint.color = Color.RED.cpy();
                        AbstractDungeon.getCurrRoom().monsters.monsters.get(j).tint.changeColor(Color.WHITE.cpy());
                    }
                    AbstractDungeon.getCurrRoom().monsters.monsters.get(j).damage(new DamageInfo(source, damage[j], damageType));
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
