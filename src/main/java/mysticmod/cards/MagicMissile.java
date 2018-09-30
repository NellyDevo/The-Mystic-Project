package mysticmod.cards;

import basemod.helpers.CardTags;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import mysticmod.MysticMod;
import mysticmod.actions.SetCardTargetCoordinatesAction;
import mysticmod.mystictags.MysticTags;
import mysticmod.patches.AbstractCardEnum;
import mysticmod.powers.SpellsPlayed;
import mysticmod.vfx.MagicMissileEffect;

public class MagicMissile
        extends AbstractMysticCard {
    public static final String ID = "mysticmod:MagicMissile";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String[] EXTENDED_DESCRIPTIONS = cardStrings.EXTENDED_DESCRIPTION;
    public static final String IMG_PATH = "mysticmod/images/cards/magicmissile.png";
    private static final int COST = 0;
    public static final int ATTACK_DMG = 3;
    private static final int UPGRADE_PLUS_DMG = 1;

    public MagicMissile() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                AbstractCard.CardType.ATTACK, AbstractCardEnum.MYSTIC_PURPLE,
                AbstractCard.CardRarity.UNCOMMON, AbstractCard.CardTarget.ENEMY);
        this.damage=this.baseDamage = ATTACK_DMG;
        this.magicNumber = this.baseMagicNumber = 1;
        CardTags.addTags(this, MysticTags.IS_SPELL);
        this.setBackgroundTexture(BG_SMALL_SPELL_ATTACK_MYSTIC, BG_LARGE_SPELL_ATTACK_MYSTIC);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new SetCardTargetCoordinatesAction(this, -1.0f, Settings.HEIGHT / 2.0f + 300f * Settings.scale));
        int effectCount = 0;
        int damageCount = 0;
        int maximumCount = (int)Math.ceil(((float)m.currentHealth + (float)m.currentBlock) / (float)this.damage);
        if (this.magicNumber > maximumCount) {
            this.magicNumber = maximumCount;
        }
        float elapsedTime = 0.0f;
        while (effectCount < this.magicNumber || damageCount < this.magicNumber) {
            if (effectCount < this.magicNumber) {
                AbstractDungeon.actionManager.addToBottom(new VFXAction(new MagicMissileEffect(p.dialogX + 80.0f * Settings.scale, p.dialogY - 50.0f * Settings.scale, m.hb.cX, m.hb.cY)));
                effectCount++;
            }
            if (damageCount < this.magicNumber && elapsedTime >= 1.0f) {
                AbstractDungeon.actionManager.addToBottom(new DamageAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn), AbstractGameAction.AttackEffect.POISON));
                damageCount++;
            }
            AbstractDungeon.actionManager.addToBottom(new WaitAction(0.25f));
            elapsedTime += 0.25f;
        }
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new SpellsPlayed(p, 1), 1));
        this.rawDescription = DESCRIPTION;
        this.initializeDescription();
    }

    @Override
    public void applyPowers() {
        this.magicNumber = this.baseMagicNumber = MysticMod.numberOfTimesDeckShuffledThisCombat + 1;
        if (this.magicNumber > 1) {
            this.rawDescription = DESCRIPTION + EXTENDED_DESCRIPTIONS[0];
            this.isMagicNumberModified = true;
        } else {
            this.rawDescription = DESCRIPTION + EXTENDED_DESCRIPTIONS[1];
        }
        this.initializeDescription();
        super.applyPowers();
    }

    @Override
    public void onMoveToDiscard() {
        this.rawDescription = DESCRIPTION;
        this.initializeDescription();
    }

    @Override
    public AbstractCard makeCopy() {
        return new MagicMissile();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeDamage(UPGRADE_PLUS_DMG);
        }
    }
}