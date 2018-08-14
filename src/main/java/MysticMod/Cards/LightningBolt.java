package MysticMod.Cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import MysticMod.Patches.AbstractCardEnum;
import MysticMod.Powers.TechniquesPlayed;
import MysticMod.Powers.SpellsPlayed;

import basemod.abstracts.CustomCard;

public class LightningBolt
        extends CustomCard {
    public static final String ID = "MysticMod:LightningBolt";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "MysticMod/images/cards/lightningbolt.png";
    private static final int COST = 1;
    public static final int ATTACK_DMG = 6;
    private static final int UPGRADE_PLUS_DMG = 3;
    private static final int ALTERNATE_DMG = 10;
    private static final int UPGRADE_PLUS_ALT_DMG = 4;

    public LightningBolt() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                AbstractCard.CardType.ATTACK, AbstractCardEnum.MYSTIC_PURPLE,
                AbstractCard.CardRarity.COMMON, AbstractCard.CardTarget.ALL_ENEMY);
        this.damage=this.baseDamage = ATTACK_DMG;
        this.magicNumber = this.baseMagicNumber = ALTERNATE_DMG;
    }
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int tempDamage = 0;
        if (p.hasPower(TechniquesPlayed.POWER_ID)) {
            tempDamage = this.magicNumber;
        } else {
            tempDamage = this.damage;
        }
        for (final AbstractMonster mo : AbstractDungeon.getCurrRoom().monsters.monsters) {
            AbstractDungeon.actionManager.addToBottom(
                    new com.megacrit.cardcrawl.actions.common.DamageAction(
                            mo, new DamageInfo(p, tempDamage, this.damageTypeForTurn)
                            , AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
        }
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new SpellsPlayed(p, 1), 1));
    }

    @Override
    public void applyPowers() {
        int CURRENT_MAGIC_NUMBER = this.baseMagicNumber;
        int CURRENT_DMG = this.baseDamage;
        this.baseDamage = CURRENT_MAGIC_NUMBER;
        super.applyPowers(); // takes this.baseDamage and applies things like Strength or Pen Nib to set this.damage

        this.magicNumber = this.damage; // magic number holds the first condition's modified damage, so !M! will work
        this.isMagicNumberModified = this.magicNumber != this.baseMagicNumber;

        // repeat so this.damage holds the second condition's damage
        this.baseDamage = CURRENT_DMG;
        super.applyPowers();
    }

    @Override
    public AbstractCard makeCopy() {
        return new LightningBolt();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeDamage(UPGRADE_PLUS_DMG);
            this.upgradeMagicNumber(UPGRADE_PLUS_ALT_DMG);
        }
    }
}