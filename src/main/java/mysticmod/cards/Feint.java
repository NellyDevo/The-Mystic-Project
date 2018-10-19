package mysticmod.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import mysticmod.MysticMod;
import mysticmod.patches.AbstractCardEnum;
import mysticmod.patches.MysticTags;
import mysticmod.powers.ArtesPlayed;

public class Feint
        extends AbstractMysticCard {
    public static final String ID = "mysticmod:Feint";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "mysticmod/images/cards/feint.png";
    private static final int COST = 1;
    public static final int ATTACK_DMG = 6;
    private static final int UPGRADE_PLUS_DMG = 2;

    public Feint() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                AbstractCard.CardType.ATTACK, AbstractCardEnum.MYSTIC_PURPLE,
                AbstractCard.CardRarity.UNCOMMON, AbstractCard.CardTarget.ENEMY);
        this.damage=this.baseDamage = ATTACK_DMG;
        this.tags.add(MysticTags.IS_ARTE);
        this.setBackgroundTexture(BG_SMALL_ARTE_ATTACK_MYSTIC, BG_LARGE_ARTE_ATTACK_MYSTIC);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new DamageAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn), AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
        AbstractCard randomCantrip = MysticMod.cantripsGroup.get(AbstractDungeon.cardRandomRng.random(MysticMod.cantripsGroup.size()-1)).makeCopy();
        if (this.upgraded) {
            randomCantrip.upgrade();
        }
        AbstractDungeon.actionManager.addToBottom(new MakeTempCardInDrawPileAction(randomCantrip, 1, true, true));
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new ArtesPlayed(p, 1), 1));
    }

    @Override
    public AbstractCard makeCopy() {
        return new Feint();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeDamage(UPGRADE_PLUS_DMG);
            this.rawDescription = UPGRADE_DESCRIPTION;
            this.initializeDescription();
        }
    }
}