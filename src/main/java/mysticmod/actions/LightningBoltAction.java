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
    private AbstractCard parentCard;

    public LightningBoltAction(int[] multiDamage, DamageInfo.DamageType damageTypeForTurn, AbstractPlayer player, AbstractCard parentCard) {
        actionType = ActionType.SPECIAL;
        duration = Settings.ACTION_DUR_MED;
        p = player;
        this.multiDamage = multiDamage;
        this.damageTypeForTurn = damageTypeForTurn;
        this.parentCard = parentCard;
    }

    @Override
    public void update() {
        parentCard.target_y = Settings.HEIGHT / 2.0f + 300.0f * Settings.scale;
        float xOffset = 1000f * Settings.scale;
        float yOffset = 187f * Settings.scale;
        float rotation = 90f;
        float duration = 0.5f;
        LightningEffect sidewaysLightning = new LightningEffect(p.drawX + xOffset, p.drawY + yOffset);
        ReflectionHacks.setPrivateInherited(sidewaysLightning, LightningEffect.class , "rotation", rotation);
        AbstractDungeon.actionManager.addToTop(
                new com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction(
                        p, multiDamage, damageTypeForTurn, AbstractGameAction.AttackEffect.NONE));
        AbstractDungeon.actionManager.addToTop(new VFXAction(sidewaysLightning, duration));
        AbstractDungeon.actionManager.addToTop(new SFXAction("ORB_LIGHTNING_EVOKE"));
        isDone = true;
    }
}
