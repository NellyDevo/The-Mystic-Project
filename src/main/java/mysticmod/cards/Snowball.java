package mysticmod.cards;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import mysticmod.MysticMod;
import mysticmod.actions.ApplyPowerfulAction;
import mysticmod.actions.SnowballDamageAction;
import mysticmod.patches.AbstractCardEnum;
import mysticmod.patches.MysticTags;

public class Snowball extends AbstractMysticCard {
    public static final String ID = "mysticmod:Snowball";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "mysticmod/images/cards/snowball.png";
    private static final int COST = 1;
    public static final int ATTACK_DMG = 11;
    private static final int UPGRADE_PLUS_DMG = 3;

    public Snowball() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                AbstractCard.CardType.ATTACK, AbstractCardEnum.MYSTIC_PURPLE,
                AbstractCard.CardRarity.COMMON, AbstractCard.CardTarget.ENEMY);
        damage = baseDamage = ATTACK_DMG;
        tags.add(MysticTags.IS_SPELL);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new SnowballDamageAction(m, new DamageInfo(p, damage, damageTypeForTurn)));
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerfulAction(p, 1));
    }

    @Override
    public AbstractCard makeCopy() {
        return new Snowball();
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDamage(UPGRADE_PLUS_DMG);
        }
    }
}
