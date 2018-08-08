package MysticMod.Cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import MysticMod.Patches.AbstractCardEnum;
import MysticMod.Powers.SpellsPlayed;
import MysticMod.Powers.TechniquesPlayed;

import basemod.abstracts.CustomCard;

public class ArcaneDodge
        extends CustomCard {
    public static final String ID = "MysticMod:ArcaneDodge";
    public static final String NAME = "Arcane Dodge";
    public static final String DESCRIPTION = "Technique. NL Apply !B! block. NL Energized: apply an additional !M! block.";
    public static final String IMG_PATH = "MysticMod/images/cards/arcanedodge.png";
    private static final int COST = 1;
    public static final int BLOCK_AMT = 5;
    public static final int EXTRA_BLK = 4;
    private static final int UPGRADE_EXTRA_BLOCK = 3;
    private static int blockPlaceholder;
    private static int magicNumberPlaceholder;

    public ArcaneDodge() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                AbstractCard.CardType.SKILL, AbstractCardEnum.MYSTIC_PURPLE,
                AbstractCard.CardRarity.BASIC, AbstractCard.CardTarget.SELF);
        this.block = this.baseBlock = BLOCK_AMT;
        this.magicNumber = this.baseMagicNumber = EXTRA_BLK;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new com.megacrit.cardcrawl.actions.common.GainBlockAction(p, p, this.block));
        if ((p.hasPower(SpellsPlayed.POWER_ID)) && (p.getPower(SpellsPlayed.POWER_ID).amount >= 1)) {
        AbstractDungeon.actionManager.addToBottom(new com.megacrit.cardcrawl.actions.common.GainBlockAction(p, p, this.magicNumber));
         }
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new TechniquesPlayed(p, 1), 1));
    }

    @Override
    public void applyPowers() {
        blockPlaceholder = this.baseBlock;
        magicNumberPlaceholder = this.baseMagicNumber;
        this.baseBlock = magicNumberPlaceholder;
        super.applyPowers(); // takes this.baseDamage and applies things like Strength or Pen Nib to set this.damage

        this.magicNumber = block; // magic number holds the first condition's modified damage, so !M! will work
        this.isMagicNumberModified = this.magicNumber != this.baseMagicNumber;

        // repeat so this.damage holds the second condition's damage
        this.baseBlock = blockPlaceholder;
        super.applyPowers();
    }

    @Override
    public AbstractCard makeCopy() {
        return new ArcaneDodge();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeMagicNumber(UPGRADE_EXTRA_BLOCK);
        }
    }
}