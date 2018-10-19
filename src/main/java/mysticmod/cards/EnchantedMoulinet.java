package mysticmod.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.CleaveEffect;
import mysticmod.actions.EnchantedMoulinetAction;
import mysticmod.patches.AbstractCardEnum;
import mysticmod.patches.MysticTags;
import mysticmod.powers.ArtesPlayed;

public class EnchantedMoulinet
        extends AbstractMysticCard {
    public static final String ID = "mysticmod:EnchantedMoulinet";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "mysticmod/images/cards/enchantedmoulinet.png";
    private static final int COST = 2;
    public static final int ATTACK_DMG = 15;
    private static final int UPGRADE_PLUS_DMG = 3;

    public EnchantedMoulinet() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                AbstractCard.CardType.ATTACK, AbstractCardEnum.MYSTIC_PURPLE,
                AbstractCard.CardRarity.RARE, AbstractCard.CardTarget.ALL_ENEMY);
        this.damage=this.baseDamage = ATTACK_DMG;
        this.isMultiDamage = true;
        this.tags.add(MysticTags.IS_ARTE);
        this.setBackgroundTexture(BG_SMALL_ARTE_ATTACK_MYSTIC, BG_LARGE_ARTE_ATTACK_MYSTIC);
    }
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new SFXAction("ATTACK_HEAVY"));
        AbstractDungeon.actionManager.addToBottom(new VFXAction(p, new CleaveEffect(), 0.25f));
        AbstractDungeon.actionManager.addToBottom(new EnchantedMoulinetAction(p, this.multiDamage, this.damageTypeForTurn, AbstractGameAction.AttackEffect.FIRE));
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new ArtesPlayed(p, 1), 1));
    }

    @Override
    public AbstractCard makeCopy() {
        return new EnchantedMoulinet();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeDamage(UPGRADE_PLUS_DMG);
        }
    }
}