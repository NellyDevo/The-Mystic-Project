package mysticmod.cards;

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
import com.megacrit.cardcrawl.powers.WeakPower;
import mysticmod.patches.AbstractCardEnum;
import mysticmod.patches.MysticTags;
import mysticmod.powers.SpellsPlayed;

public class AllIn extends AbstractMysticCard {
    public static final String ID = "mysticmod:AllIn";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "mysticmod/images/cards/allin.png";
    private static final int COST = 0;
    private static final int DAMAGE_AMT = 10;
    private static final int UPGRADE_DAMAGE_AMT = 3;
    private static final int WEAK_AMT = 3;

    public AllIn() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                AbstractCard.CardType.ATTACK, AbstractCardEnum.MYSTIC_PURPLE,
                AbstractCard.CardRarity.UNCOMMON, AbstractCard.CardTarget.ENEMY);
        damage = baseDamage = DAMAGE_AMT;
        magicNumber = baseMagicNumber = WEAK_AMT;
        tags.add(MysticTags.IS_ARTE);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn), AbstractGameAction.AttackEffect.BLUNT_HEAVY));
        if (magicNumber > 0) {
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new WeakPower(p, magicNumber, false), magicNumber));
        }
    }

    @Override
    public void applyPowers() {
        super.applyPowers();
        magicNumber = baseMagicNumber;
        if (AbstractDungeon.player.hasPower(SpellsPlayed.POWER_ID)) {
            magicNumber -= AbstractDungeon.player.getPower(SpellsPlayed.POWER_ID).amount;
        }
        if (magicNumber < 0) {
            magicNumber = 0;
        }
        isMagicNumberModified = baseMagicNumber != magicNumber;
    }

    @Override
    public AbstractCard makeCopy() {
        return new AllIn();
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDamage(UPGRADE_DAMAGE_AMT);
        }
    }
}
