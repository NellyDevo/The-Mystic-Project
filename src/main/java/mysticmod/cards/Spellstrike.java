package mysticmod.cards;

import basemod.helpers.BaseModTags;
import basemod.helpers.CardTags;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import mysticmod.actions.PlaySpellAttackFromDrawPile;
import mysticmod.patches.AbstractCardEnum;
import mysticmod.powers.TechniquesPlayed;

public class Spellstrike
        extends AbstractMysticCard {
    public static final String ID = "mysticmod:Spellstrike";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "mysticmod/images/cards/spellstrike.png";
    private static final int COST = 1;
    public static final int ATTACK_DMG = 8;
    private static final int UPGRADE_PLUS_DMG = 3;

    public Spellstrike() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                AbstractCard.CardType.ATTACK, AbstractCardEnum.MYSTIC_PURPLE,
                AbstractCard.CardRarity.RARE, AbstractCard.CardTarget.ENEMY);
        this.damage=this.baseDamage = ATTACK_DMG;
        this.exhaust = true;
        this.isTechnique = true;
        this.setBackgroundTexture(BG_SMALL_ARTE_ATTACK_MYSTIC, BG_LARGE_ARTE_ATTACK_MYSTIC);
        CardTags.addTags(this, BaseModTags.STRIKE);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new DamageAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn), AbstractGameAction.AttackEffect.SLASH_VERTICAL));
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new TechniquesPlayed(p, 1), 1));
        AbstractDungeon.actionManager.addToBottom(new PlaySpellAttackFromDrawPile(1, m, false));
    }

    @Override
    public AbstractCard makeCopy() {
        return new Spellstrike();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeDamage(UPGRADE_PLUS_DMG);
        }
    }
}