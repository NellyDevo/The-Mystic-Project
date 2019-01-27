package mysticmod.cards;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.LightningEffect;
import mysticmod.actions.LoadCardImageAction;
import mysticmod.patches.AbstractCardEnum;
import mysticmod.patches.MysticTags;
import mysticmod.powers.ArtesPlayed;

public class ShockingGrasp extends AbstractAltArtMysticCard {
    public static final String ID = "mysticmod:ShockingGrasp";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String ALTERNATE_IMG_PATH = "mysticmod/images/cards/alternate/shockinggrasp.png";
    private static final int COST = 1;
    public static final int ATTACK_DMG = 8;
    private static final int UPGRADE_PLUS_DMG = 2;
    public static final int BOOSTED_DMG = 14;
    private static final int UPGRADE_PLUS_BOOST_DMG = 4;

    public ShockingGrasp() {
        super(ID, NAME, ALTERNATE_IMG_PATH, COST, DESCRIPTION,
                AbstractCard.CardType.ATTACK, AbstractCardEnum.MYSTIC_PURPLE,
                AbstractCard.CardRarity.BASIC, AbstractCard.CardTarget.ENEMY);
        IMG_PATH = "mysticmod/images/cards/shockinggrasp.png";
        loadCardImage(IMG_PATH);
        damage = baseDamage = ATTACK_DMG;
        magicNumber = baseMagicNumber = BOOSTED_DMG;
        tags.add(MysticTags.IS_SPELL);
        altGlowColor = ALT_GLOW_RED;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if ((p.hasPower(ArtesPlayed.POWER_ID)) && (p.getPower(ArtesPlayed.POWER_ID).amount >= 1)) {
        AbstractDungeon.actionManager.addToBottom(new VFXAction(new LightningEffect(m.hb.cX, m.hb.cY)));
        AbstractDungeon.actionManager.addToBottom(new SFXAction("ORB_LIGHTNING_EVOKE"));
        AbstractDungeon.actionManager.addToBottom(new DamageAction(m, new DamageInfo(p, magicNumber, damageTypeForTurn), AbstractGameAction.AttackEffect.FIRE));
    } else {
        AbstractDungeon.actionManager.addToBottom(new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn), AbstractGameAction.AttackEffect.SMASH));
    }
        if (isArtAlternate) {
            AbstractDungeon.actionManager.addToBottom(new LoadCardImageAction(this, IMG_PATH, false));
            isArtAlternate = false;
        }
    }

    @Override
    public void applyPowers() {
        int CURRENT_MAGIC_NUMBER = baseMagicNumber;
        int CURRENT_DMG = baseDamage;
        baseDamage = CURRENT_MAGIC_NUMBER;
        super.applyPowers(); // takes baseDamage and applies things like Strength or Pen Nib to set damage

        magicNumber = damage; // magic number holds the first condition's modified damage, so !M! will work
        isMagicNumberModified = magicNumber != baseMagicNumber;

        // repeat so damage holds the second condition's damage
        baseDamage = CURRENT_DMG;
        super.applyPowers();
        if (AbstractDungeon.player.hasPower(ArtesPlayed.POWER_ID)) {
            if (!isArtAlternate) {
                AbstractDungeon.actionManager.addToBottom(new LoadCardImageAction(this, ALTERNATE_IMG_PATH, true));
                isArtAlternate = true;
            }
        } else {
            if (isArtAlternate) {
                loadCardImage(IMG_PATH);
                isArtAlternate = false;
            }
        }
    }

    @Override
    public void calculateCardDamage(final AbstractMonster mo) {
        int CURRENT_MAGIC_NUMBER = baseMagicNumber;
        int CURRENT_DMG = baseDamage;
        baseDamage = CURRENT_MAGIC_NUMBER;
        super.calculateCardDamage(mo); // takes baseDamage and applies things like Strength or Pen Nib to set damage

        magicNumber = damage; // magic number holds the first condition's modified damage, so !M! will work
        isMagicNumberModified = magicNumber != baseMagicNumber;

        // repeat so damage holds the second condition's damage
        baseDamage = CURRENT_DMG;
        super.calculateCardDamage(mo);
    }

    @Override
    public AbstractCard makeCopy() {
        return new ShockingGrasp();
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDamage(UPGRADE_PLUS_DMG);
            upgradeMagicNumber(UPGRADE_PLUS_BOOST_DMG);
        }
    }
}
