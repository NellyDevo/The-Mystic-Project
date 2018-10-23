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
    private boolean skipWait;

    public SnowballDamageAction(final AbstractCreature target, final DamageInfo info) {
        this.skipWait = false;
        this.setValues(target, this.info = info);
        this.actionType = ActionType.DAMAGE;
        this.duration = DURATION;
    }
    
    @Override
    public void update() {
        if (this.shouldCancelAction() && this.info.type != DamageInfo.DamageType.THORNS) {
            this.isDone = true;
            return;
        }
        if (this.duration == 0.1f) {
            if (this.info.type != DamageInfo.DamageType.THORNS && (this.info.owner.isDying || this.info.owner.halfDead)) {
                this.isDone = true;
                return;
            }
            this.target.damageFlash = true;
            this.target.damageFlashFrames = 4;
            FlashAtkImgEffect blueFire = new FlashAtkImgEffect(this.target.hb.cX, this.target.hb.cY, AbstractGameAction.AttackEffect.FIRE);
            ReflectionHacks.setPrivateInherited(blueFire, FlashAtkImgEffect.class, "color", Color.CYAN.cpy());
            AbstractDungeon.effectList.add(blueFire);
        }
        this.tickDuration();
        if (this.isDone) {
            this.target.tint.color = Color.BLUE.cpy();
            this.target.tint.changeColor(Color.WHITE.cpy());
            this.target.damage(this.info);
            if (AbstractDungeon.getCurrRoom().monsters.areMonstersBasicallyDead()) {
                AbstractDungeon.actionManager.clearPostCombatActions();
            }
            if (!this.skipWait && !Settings.FAST_MODE) {
                AbstractDungeon.actionManager.addToTop(new WaitAction(POST_ATTACK_WAIT_DUR));
            }
        }
    }
}
