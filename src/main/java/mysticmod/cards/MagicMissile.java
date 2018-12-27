package mysticmod.cards;

import com.badlogic.gdx.math.Interpolation;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import mysticmod.MysticMod;
import mysticmod.actions.MagicMissileAction;
import mysticmod.actions.SetCardTargetCoordinatesAction;
import mysticmod.patches.AbstractCardEnum;
import mysticmod.patches.MysticTags;

public class MagicMissile extends AbstractMysticCard {
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
        this.tags.add(MysticTags.IS_SPELL);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new SetCardTargetCoordinatesAction(this, -1.0f, Settings.HEIGHT / 2.0f + 300f * Settings.scale));
        float projectileDelay = Interpolation.linear.apply(1.0F/3.0F, 0.05F, Math.min(((float)magicNumber)/100.0F, 1.0F));
        AbstractDungeon.actionManager.addToBottom(new MagicMissileAction(m, new DamageInfo(p, this.damage), this.magicNumber, projectileDelay));
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
