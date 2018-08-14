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
import com.megacrit.cardcrawl.ui.panels.*;
import MysticMod.Patches.AbstractCardEnum;
import MysticMod.Actions.SpellCombatAction;
import MysticMod.Powers.TechniquesPlayed;
import MysticMod.Powers.SpellsPlayed;
import java.io.*;

import basemod.abstracts.CustomCard;

public class SpellCombat
        extends CustomCard {
    public static final String ID = "MysticMod:SpellCombat";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "MysticMod/images/cards/closingbarrage.png";
    private static final int COST = -1;
    private static final int DAMAGE_AMT = 6;
    private static final int UPGRADE_DAMAGE_AMT = 3;
    private static final int BLOCK_AMT = 5;
    private static final int UPGRADE_BLOCK_AMT = 3;

    public SpellCombat() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                AbstractCard.CardType.ATTACK, AbstractCardEnum.MYSTIC_PURPLE,
                AbstractCard.CardRarity.RARE, AbstractCard.CardTarget.ENEMY);
            this.damage = this.baseDamage = DAMAGE_AMT;
            this.block = this.baseBlock = BLOCK_AMT;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (this.energyOnUse < EnergyPanel.totalCount) {
            this.energyOnUse = EnergyPanel.totalCount;
        }
        AbstractDungeon.actionManager.addToBottom(new SpellCombatAction(p, m, this.damage, this.block, this.damageTypeForTurn, this.freeToPlayOnce, this.energyOnUse));
    }

    @Override
    public void applyPowers() {
        if (AbstractDungeon.player.hasPower(TechniquesPlayed.POWER_ID) && !AbstractDungeon.player.hasPower(SpellsPlayed.POWER_ID)) {
            this.target = AbstractCard.CardTarget.SELF;
        } else {
            this.target = AbstractCard.CardTarget.ENEMY;
        }
        super.applyPowers();
    }

    @Override
    public boolean hasEnoughEnergy() {
        if (!AbstractDungeon.player.hasPower(SpellsPlayed.POWER_ID) && !AbstractDungeon.player.hasPower(TechniquesPlayed.POWER_ID)) {
            return false;
        }
        return super.hasEnoughEnergy();
    }

    @Override
    public AbstractCard makeCopy() {
        return new SpellCombat();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeDamage(UPGRADE_DAMAGE_AMT);
            this.upgradeBlock(UPGRADE_BLOCK_AMT);
        }
    }
}