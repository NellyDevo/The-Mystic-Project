package mysticmod.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.RemoveAllBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import mysticmod.patches.AbstractCardEnum;
import mysticmod.patches.MysticTags;
import mysticmod.vfx.DisintegrateEffect;

public class Disintegrate extends AbstractMysticCard {
    public static final String ID = "mysticmod:Disintegrate";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "mysticmod/images/cards/disintegrate.png";
    private static final int COST = 3;
    public static final int ATTACK_DMG = 30;
    private static final int UPGRADE_PLUS_DMG = 10;

    public Disintegrate() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                AbstractCard.CardType.ATTACK, AbstractCardEnum.MYSTIC_PURPLE,
                AbstractCard.CardRarity.RARE, AbstractCard.CardTarget.ENEMY);
        damage = baseDamage = ATTACK_DMG;
        tags.add(MysticTags.IS_SPELL);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // record current armor and then remove it
        int targetArmor = m.currentBlock;
        if (targetArmor > 0) {
            AbstractDungeon.actionManager.addToBottom(new RemoveAllBlockAction(m, m));
        }
        // deal the damage
        float originX = p.dialogX + 80.0f * Settings.scale;
        float originY = p.dialogY - 50.0f * Settings.scale;
        AbstractDungeon.actionManager.addToBottom(new VFXAction(new DisintegrateEffect(originX, originY)));
        AbstractDungeon.actionManager.addToBottom(new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn), AbstractGameAction.AttackEffect.NONE));
        // restore the block
        if (targetArmor > 0) {
            AbstractDungeon.actionManager.addToBottom(new GainBlockAction(m, m, targetArmor));
        }
    }
    @Override
    public AbstractCard makeCopy() {
        return new Disintegrate();
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDamage(UPGRADE_PLUS_DMG);
        }
    }
}
