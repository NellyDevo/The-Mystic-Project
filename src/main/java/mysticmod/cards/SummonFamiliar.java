package mysticmod.cards;

import basemod.helpers.TooltipInfo;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import kobting.friendlyminions.helpers.BasePlayerMinionHelper;
import mysticmod.minions.foxfamiliar.FoxEvolutionPower;
import mysticmod.minions.foxfamiliar.FoxFamiliar;
import mysticmod.patches.MysticTags;
import mysticmod.patches.AbstractCardEnum;
import mysticmod.powers.SpellsPlayed;

import java.util.List;

public class SummonFamiliar
        extends AbstractMysticCard {
    public static final String ID = "mysticmod:SummonFamiliar";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "mysticmod/images/cards/summonfamiliar.png";
    private static final int COST = 1;
    private static final int UPGRADE_COST = 0;

    public SummonFamiliar() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.MYSTIC_PURPLE,
                CardRarity.COMMON, CardTarget.SELF);
        this.exhaust = true;
        this.tags.add(MysticTags.IS_SPELL);
        this.setBackgroundTexture(BG_SMALL_SPELL_SKILL_MYSTIC, BG_LARGE_SPELL_SKILL_MYSTIC);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (!BasePlayerMinionHelper.hasMinions(p)) {
            BasePlayerMinionHelper.addMinion(p, new FoxFamiliar());
        } else {
            AbstractDungeon.actionManager.addToBottom(new
                    ApplyPowerAction(BasePlayerMinionHelper.getMinions(p).getMonster(FoxFamiliar.ID), p,
                    new FoxEvolutionPower((BasePlayerMinionHelper.getMinions(p).getMonster(FoxFamiliar.ID)), 1), 1));
            AbstractDungeon.actionManager.addToBottom(new HealAction(BasePlayerMinionHelper.getMinions(p).getMonster(FoxFamiliar.ID), p, 5));
        }
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new SpellsPlayed(p, 1), 1));
    }

    @Override
    public List<TooltipInfo> getCustomTooltips() {
        List<TooltipInfo> retVal = super.getCustomTooltips();
        retVal.add(new TooltipInfo("Fox Familiar", "Can attack or help set up your combos. Playing more copies of this card will upgrade your familiar, up to 3 times."));
        return retVal;
    }

    @Override
    public AbstractCard makeCopy() {
        return new SummonFamiliar();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeBaseCost(UPGRADE_COST);
        }
    }
}