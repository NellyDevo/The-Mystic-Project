package mysticmod.actions;

import basemod.ReflectionHacks;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;

public class SnowballDamageAction extends AbstractGameAction {
    private DamageInfo info;
    private static final float DURATION = 0.1f;
    private static final float POST_ATTACK_WAIT_DUR = 0.1f;

    public SnowballDamageAction(final AbstractCreature target, final DamageInfo info) {
        setValues(target, this.info = info);
        actionType = ActionType.DAMAGE;
        duration = DURATION;
    }
    
    @Override
    public void update() {
        if (shouldCancelAction() && info.type != DamageInfo.DamageType.THORNS) {
            isDone = true;
            return;
        }
        if (duration == 0.1f) {
            if (info.type != DamageInfo.DamageType.THORNS && (info.owner.isDying || info.owner.halfDead)) {
                isDone = true;
                return;
            }
            FlashAtkImgEffect blueFire = new FlashAtkImgEffect(target.hb.cX, target.hb.cY, AbstractGameAction.AttackEffect.FIRE);
            ReflectionHacks.setPrivateInherited(blueFire, FlashAtkImgEffect.class, "color", Color.CYAN.cpy());
            AbstractDungeon.effectList.add(blueFire);
        }
        tickDuration();
        if (isDone) {
            target.tint.color = Color.BLUE.cpy();
            target.tint.changeColor(Color.WHITE.cpy());
            target.damage(info);
            if (AbstractDungeon.getCurrRoom().monsters.areMonstersBasicallyDead()) {
                AbstractDungeon.actionManager.clearPostCombatActions();
            }
            if (!Settings.FAST_MODE) {
                AbstractDungeon.actionManager.addToTop(new WaitAction(POST_ATTACK_WAIT_DUR));
            }
        }
    }
}
