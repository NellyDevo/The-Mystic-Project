package MysticMod.Cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import MysticMod.Patches.AbstractCardEnum;
import MysticMod.Powers.TechniquesPlayed;
import MysticMod.Powers.SpellsPlayed;

import basemod.abstracts.CustomCard;

public class PureInstinct
        extends CustomCard {
    public static final String ID = "MysticMod:PureInstinct";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "MysticMod/images/cards/pureinstinct.png";
    private static final int COST = 1;
    public static final int BLOCK_AMT = 5;
    private static final int UPGRADE_PLUS_BLK = 2;

    public PureInstinct() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                AbstractCard.CardType.ATTACK, AbstractCardEnum.MYSTIC_PURPLE,
                AbstractCard.CardRarity.COMMON, AbstractCard.CardTarget.SELF);
        this.block = this.baseBlock = BLOCK_AMT;
    }
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int techniquesCount = 0;
        for (final AbstractCard card : p.hand.group) {
            //insert relic logic here in the future
            if (card.rawDescription.startsWith("Technique.")) {
                techniquesCount++;
            }
        }
        if (techniquesCount > 0) {
            for (int i = 0; i < techniquesCount; i++){
                AbstractDungeon.actionManager.addToBottom(new com.megacrit.cardcrawl.actions.common.GainBlockAction(p, p, this.block));
            }
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new PureInstinct();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeBlock(UPGRADE_PLUS_BLK);
        }
    }
}