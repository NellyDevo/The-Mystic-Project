package MysticMod.Cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
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
import MysticMod.Cards.Cantrips.*;

import basemod.abstracts.CustomCard;

public class TomeOfSpells
        extends CustomCard {
    public static final String ID = "MysticMod:TomeOfSpells";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "MysticMod/images/cards/tomeofspells.png";
    private static final int COST = 1;
    public static final int CANTRIP_AMT = 2;
    private static final int UPGRADE_PLUS_CANTRIP = 1;

    public TomeOfSpells() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                AbstractCard.CardType.SKILL, AbstractCardEnum.MYSTIC_PURPLE,
                AbstractCard.CardRarity.COMMON, AbstractCard.CardTarget.SELF);
        this.magicNumber = this.baseMagicNumber = CANTRIP_AMT;
        this.exhaust = true;
    }
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        for (int i = 0; i < this.magicNumber; i++) {
            int randomlyGeneratedNumber = AbstractDungeon.cardRandomRng.random(4);
            switch (randomlyGeneratedNumber) {
                case 0:
                    AbstractDungeon.actionManager.addToBottom(new MakeTempCardInHandAction(new AcidSplash(), 1, false));
                    break;
                case 1:
                    AbstractDungeon.actionManager.addToBottom(new MakeTempCardInHandAction(new Prestidigitation(), 1, false));
                    break;
                case 2:
                    AbstractDungeon.actionManager.addToBottom(new MakeTempCardInHandAction(new RayOfFrost(), 1, false));
                    break;
                case 3:
                    AbstractDungeon.actionManager.addToBottom(new MakeTempCardInHandAction(new Spark(), 1, false));
                    break;
                case 4:
                    AbstractDungeon.actionManager.addToBottom(new MakeTempCardInHandAction(new ReadMagic(), 1, false));
                    break;
            }
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new TomeOfSpells();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeMagicNumber(UPGRADE_PLUS_CANTRIP);
        }
    }
}