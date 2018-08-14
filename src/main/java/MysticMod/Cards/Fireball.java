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
import MysticMod.Powers.SpellsPlayed;
import MysticMod.Powers.TechniquesPlayed;

import basemod.abstracts.CustomCard;

public class Fireball
        extends CustomCard {
    public static final String ID = "MysticMod:Fireball";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "MysticMod/images/cards/fireball.png";
    private static final int COST = 2;
    public static final int ATTACK_DMG = 16;
    private static final int UPGRADE_PLUS_DMG = 6;

    public Fireball() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                AbstractCard.CardType.ATTACK, AbstractCardEnum.MYSTIC_PURPLE,
                AbstractCard.CardRarity.UNCOMMON, AbstractCard.CardTarget.ENEMY);
        this.damage=this.baseDamage = ATTACK_DMG;
        this.isMultiDamage = true;
    }
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if ((p.hasPower(TechniquesPlayed.POWER_ID)) && (p.getPower(TechniquesPlayed.POWER_ID).amount >= 1)) {
            AbstractDungeon.actionManager.addToBottom(
                    new com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction(
                            p, this.multiDamage, this.damageTypeForTurn, AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
        } else {
            AbstractDungeon.actionManager.addToBottom(
                    new com.megacrit.cardcrawl.actions.common.DamageAction(
                            m, new DamageInfo(p, this.damage, this.damageTypeForTurn)
                            , AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
        }
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new SpellsPlayed(p, 1), 1));
    }

    @Override
    public void applyPowers() {
        if (AbstractDungeon.player.hasPower(TechniquesPlayed.POWER_ID)) {
            this.target = AbstractCard.CardTarget.ALL_ENEMY;
            this.isMultiDamage = true;
        } else {
            this.target = AbstractCard.CardTarget.ENEMY;
            this.isMultiDamage = false;
        }
        super.applyPowers();
    }

    @Override
    public AbstractCard makeCopy() {
        return new Fireball();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeDamage(UPGRADE_PLUS_DMG);
        }
    }
}