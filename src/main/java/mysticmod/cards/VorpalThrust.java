package mysticmod.cards;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
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
import mysticmod.powers.SpellsPlayed;

public class VorpalThrust extends AbstractAltArtMysticCard {
    public static final String ID = "mysticmod:VorpalThrust";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String ALTERNATE_IMG_PATH = "mysticmod/images/cards/alternate/vorpalthrust.png";
    private static final int COST = 2;
    private static final int BASE_DMG = 12;
    private static final int EXTRA_ATTACK_DMG = 6;
    private static final int UPGRADE_PLUS_EXTRA_DMG = 2;
    private static final int UPGRADE_PLUS_BASE_DMG = 4;

    public VorpalThrust() {
        super(ID, NAME, ALTERNATE_IMG_PATH, COST, DESCRIPTION,
                AbstractCard.CardType.ATTACK, AbstractCardEnum.MYSTIC_PURPLE,
                AbstractCard.CardRarity.UNCOMMON, AbstractCard.CardTarget.ENEMY);
        IMG_PATH = "mysticmod/images/cards/vorpalthrust.png";
        this.loadCardImage(IMG_PATH);
        this.damage = this.baseDamage = BASE_DMG;
        this.magicNumber = this.baseMagicNumber = EXTRA_ATTACK_DMG;
        this.tags.add(MysticTags.IS_ARTE);
        this.altGlowColor = Color.BLUE;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if ((p.hasPower(SpellsPlayed.POWER_ID)) && (p.getPower(SpellsPlayed.POWER_ID).amount >= 2)) {
            AbstractDungeon.actionManager.addToBottom(new DamageAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn), AbstractGameAction.AttackEffect.BLUNT_HEAVY));
        } else {
            AbstractDungeon.actionManager.addToBottom(new DamageAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn), AbstractGameAction.AttackEffect.BLUNT_LIGHT));
        }
        if (this.isArtAlternate) {
            AbstractDungeon.actionManager.addToBottom(new LoadCardImageAction(this, IMG_PATH, false));
            this.isArtAlternate = false;
        }
    }

    @Override
    public void applyPowers() {
        int baseDamagePlaceholder = this.baseDamage;
        if (AbstractDungeon.player.hasPower(SpellsPlayed.POWER_ID)) {
            baseDamage += (AbstractDungeon.player.getPower(SpellsPlayed.POWER_ID).amount * magicNumber);
            if (!this.isArtAlternate) {
                AbstractDungeon.actionManager.addToBottom(new LoadCardImageAction(this, ALTERNATE_IMG_PATH, true));
                this.isArtAlternate = true;
            }
        } else {
            if (this.isArtAlternate) {
                this.loadCardImage(IMG_PATH);
                this.isArtAlternate = false;
            }
        }
        super.applyPowers();
        this.baseDamage = baseDamagePlaceholder;
        this.isDamageModified = this.baseDamage != this.damage;
    }

    @Override
    public void calculateCardDamage(final AbstractMonster mo) {
        int baseDamagePlaceholder = this.baseDamage;
        if (AbstractDungeon.player.hasPower(SpellsPlayed.POWER_ID)) {
            baseDamage += (AbstractDungeon.player.getPower(SpellsPlayed.POWER_ID).amount * magicNumber);
        }
        super.calculateCardDamage(mo);
        this.baseDamage = baseDamagePlaceholder;
        this.isDamageModified = this.baseDamage != this.damage;
    }

    @Override
    public AbstractCard makeCopy() {
        return new VorpalThrust();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeMagicNumber(UPGRADE_PLUS_EXTRA_DMG);
            this.upgradeDamage(UPGRADE_PLUS_BASE_DMG);
        }
    }
}
