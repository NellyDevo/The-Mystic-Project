package MysticMod.Cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.ArtifactPower;
import MysticMod.Patches.AbstractCardEnum;
import MysticMod.Powers.SpellsPlayed;
import MysticMod.Powers.TechniquesPlayed;

import basemod.abstracts.CustomCard;

public class ObscuringMist
        extends CustomCard {
    public static final String ID = "MysticMod:ObscuringMist";
    public static final String NAME = "Obscuring Mist";
    public static final String DESCRIPTION = "Spell. Apply !B! Block. NL Technical: gain 1 Artifact. NL Exhaust.";
    public static final String IMG_PATH = "MysticMod/images/cards/obscuringmist.png";
    private static final int COST = 2;
    private static final int BLOCK_AMT = 15;
    private static final int UPGRADE_BLOCK_PLUS = 5;

    public ObscuringMist() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                AbstractCard.CardType.SKILL, AbstractCardEnum.MYSTIC_PURPLE,
                AbstractCard.CardRarity.RARE, AbstractCard.CardTarget.SELF);
        this.exhaust = true;
        this.block = this.baseBlock = BLOCK_AMT;

    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        //block
        AbstractDungeon.actionManager.addToBottom(new com.megacrit.cardcrawl.actions.common.GainBlockAction(p, p, this.block));
        //Technical: artifact
        if ((p.hasPower(TechniquesPlayed.POWER_ID)) && (p.getPower(TechniquesPlayed.POWER_ID).amount >= 1)) {
            AbstractDungeon.actionManager.addToBottom(new com.megacrit.cardcrawl.actions.common.ApplyPowerAction(p, p, new ArtifactPower(p, 1), 1));
        }
        //spell functionality
        AbstractDungeon.actionManager.addToBottom(new com.megacrit.cardcrawl.actions.common.ApplyPowerAction(p, p, new SpellsPlayed(p, 1), 1));
    }

    @Override
    public AbstractCard makeCopy() {
        return new ObscuringMist();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeBlock(UPGRADE_BLOCK_PLUS);
        }
    }
}