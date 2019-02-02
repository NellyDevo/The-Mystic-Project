package mysticmod.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import mysticmod.actions.LoadCardImageAction;
import mysticmod.patches.AbstractCardEnum;
import mysticmod.patches.MysticTags;
import mysticmod.powers.ArtesPlayed;

public class Fireball extends AbstractAltArtMysticCard {
    public static final String ID = "mysticmod:Fireball";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String ALTERNATE_IMG_PATH = "mysticmod/images/cards/alternate/fireball.png";
    private static final int COST = 2;
    public static final int ATTACK_DMG = 14;
    private static final int UPGRADE_PLUS_DMG = 6;

    public Fireball() {
        super(ID, NAME, ALTERNATE_IMG_PATH, COST, DESCRIPTION,
                AbstractCard.CardType.ATTACK, AbstractCardEnum.MYSTIC_PURPLE,
                AbstractCard.CardRarity.UNCOMMON, AbstractCard.CardTarget.ENEMY);
        IMG_PATH = "mysticmod/images/cards/fireball.png";
        loadCardImage(IMG_PATH);
        damage = baseDamage = ATTACK_DMG;
        isMultiDamage = true;
        tags.add(MysticTags.IS_SPELL);
        altGlowColor = ALT_GLOW_RED;
    }
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if ((p.hasPower(ArtesPlayed.POWER_ID)) && (p.getPower(ArtesPlayed.POWER_ID).amount >= 1)) {
            AbstractDungeon.actionManager.addToBottom(new DamageAllEnemiesAction(p, multiDamage, damageTypeForTurn, AbstractGameAction.AttackEffect.FIRE));
        } else {
            AbstractDungeon.actionManager.addToBottom(new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn), AbstractGameAction.AttackEffect.FIRE));
        }
        if (isArtAlternate) {
            AbstractDungeon.actionManager.addToBottom(new LoadCardImageAction(this, IMG_PATH, false));
            isArtAlternate = false;
        }
    }

    @Override
    public void applyPowers() {
        if (AbstractDungeon.player.hasPower(ArtesPlayed.POWER_ID)) {
            target = AbstractCard.CardTarget.ALL_ENEMY;
            isMultiDamage = true;
            if (!isArtAlternate) {
                AbstractDungeon.actionManager.addToBottom(new LoadCardImageAction(this, ALTERNATE_IMG_PATH, true));
                isArtAlternate = true;
            }
        } else {
            target = AbstractCard.CardTarget.ENEMY;
            isMultiDamage = false;
            if (isArtAlternate) {
                loadCardImage(IMG_PATH);
                isArtAlternate = false;
            }
        }
        super.applyPowers();
    }

    @Override
    public void calculateCardDamage(AbstractMonster mo) {
        if (AbstractDungeon.player.hasPower(ArtesPlayed.POWER_ID)) {
            target = AbstractCard.CardTarget.ALL_ENEMY;
            isMultiDamage = true;
            if (!isArtAlternate) {
                AbstractDungeon.actionManager.addToBottom(new LoadCardImageAction(this, ALTERNATE_IMG_PATH, true));
                isArtAlternate = true;
            }
        } else {
            target = AbstractCard.CardTarget.ENEMY;
            isMultiDamage = false;
            if (isArtAlternate) {
                loadCardImage(IMG_PATH);
                isArtAlternate = false;
            }
        }
        super.calculateCardDamage(mo);
    }

    @Override
    public AbstractCard makeCopy() {
        return new Fireball();
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDamage(UPGRADE_PLUS_DMG);
        }
    }
}
