package mysticmod.actions;

import basemod.ReflectionHacks;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.combat.LightningEffect;

public class LightningBoltAction/*Rifle*/ extends AbstractGameAction {
    private int[] multiDamage;
    private DamageInfo.DamageType damageTypeForTurn;
    private AbstractPlayer p;
    private Float X_OFFSET = 1000f;
    private Float Y_OFFSET = 187f;
    private Float ROTATION = 90f;
    private Float DURATION = 0.5f;
    private AbstractCard parentCard;

    public LightningBoltAction(final int[] multiDamage, final DamageInfo.DamageType damageTypeForTurn, AbstractPlayer player, AbstractCard parentCard) {
        this.actionType = ActionType.SPECIAL;
        this.duration = Settings.ACTION_DUR_MED;
        this.multiDamage = multiDamage;
        this.damageTypeForTurn = damageTypeForTurn;
        this.p = player;
        this.parentCard = parentCard;
    }

    @Override
    public void update() {
        parentCard.target_y = Settings.HEIGHT / 2.0f + 300.0f * Settings.scale;
        Float xOffset = X_OFFSET * Settings.scale;
        Float yOffset = Y_OFFSET * Settings.scale;
        Float rotation = ROTATION;
        Float duration = DURATION;
        LightningEffect sidewaysLightning = new LightningEffect(p.drawX + xOffset, p.drawY + yOffset);
        ReflectionHacks.setPrivateInherited(sidewaysLightning, LightningEffect.class , "rotation", rotation);
        AbstractDungeon.actionManager.addToTop(
                new com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction(
                        p, this.multiDamage, this.damageTypeForTurn, AbstractGameAction.AttackEffect.NONE));
        AbstractDungeon.actionManager.addToTop(new VFXAction(sidewaysLightning, duration));
        AbstractDungeon.actionManager.addToTop(new SFXAction("ORB_LIGHTNING_EVOKE"));
        this.isDone = true;
    }
}
