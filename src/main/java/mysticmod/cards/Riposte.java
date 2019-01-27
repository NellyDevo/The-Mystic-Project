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
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.powers.WeakPower;
import mysticmod.patches.AbstractCardEnum;
import mysticmod.patches.MysticTags;

public class Riposte extends AbstractMysticCard {
    public static final String ID = "mysticmod:Riposte";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "mysticmod/images/cards/riposte.png";
    private static final int COST = 1;
    public static final int ATTACK_DMG = 6;
    public static final int UPGRADE_DMG = 3;
    public static final int WEAK_VULN_AMT = 1;

    public Riposte() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                AbstractCard.CardType.ATTACK, AbstractCardEnum.MYSTIC_PURPLE,
                AbstractCard.CardRarity.COMMON, AbstractCard.CardTarget.ENEMY);
        damage = baseDamage = ATTACK_DMG;
        magicNumber = baseMagicNumber = WEAK_VULN_AMT;
        tags.add(MysticTags.IS_ARTE);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn), AbstractGameAction.AttackEffect.SLASH_VERTICAL));
        if ((m.intent == AbstractMonster.Intent.ATTACK || m.intent == AbstractMonster.Intent.ATTACK_BUFF || m.intent == AbstractMonster.Intent.ATTACK_DEBUFF || m.intent == AbstractMonster.Intent.ATTACK_DEFEND)) {
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(m, p, new WeakPower(m, magicNumber, false), magicNumber));
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(m, p, new VulnerablePower(m, magicNumber, false), magicNumber));
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new Riposte();
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDamage(UPGRADE_DMG);
        }
    }
}
