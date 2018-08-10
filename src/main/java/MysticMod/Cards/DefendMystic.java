package MysticMod.Cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import MysticMod.Patches.AbstractCardEnum;

import basemod.abstracts.CustomCard;

public class DefendMystic
        extends CustomCard {
    public static final String ID = "MysticMod:Defend_Mystic";
    public static final String NAME = "Defend";
    public static final String DESCRIPTION = "Apply !B! block.";
    public static final String IMG_PATH = "MysticMod/images/cards/defend.png";
    private static final int COST = 1;
    private static final int BLOCK_AMT = 5;
    private static final int UPGRADE_PLUS_BLOCK = 3;

    public DefendMystic() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                AbstractCard.CardType.SKILL, AbstractCardEnum.MYSTIC_PURPLE,
                AbstractCard.CardRarity.BASIC, AbstractCard.CardTarget.SELF);
        this.block = this.baseBlock = BLOCK_AMT;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new com.megacrit.cardcrawl.actions.common.GainBlockAction(p, p, this.block));
    }

    @Override
    public boolean isDefend() { return true; }

    @Override
    public AbstractCard makeCopy() {
        return new DefendMystic();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeBlock(UPGRADE_PLUS_BLOCK);
        }
    }
}