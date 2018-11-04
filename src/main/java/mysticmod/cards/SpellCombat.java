package mysticmod.cards;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import mysticmod.actions.LoadCardImageAction;
import mysticmod.actions.SpellCombatAction;
import mysticmod.patches.AbstractCardEnum;
import mysticmod.powers.ArtesPlayed;
import mysticmod.powers.SpellsPlayed;

public class SpellCombat extends AbstractMysticCard {
    public static final String ID = "mysticmod:SpellCombat";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String[] EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
    public static final String INERT_IMG_PATH = "mysticmod/images/cards/spellcombat.png";
    public static final String ATTACK_IMG_PATH = "mysticmod/images/cards/alternate/spellcombatoffensive.png";
    public static final String DEFEND_IMG_PATH = "mysticmod/images/cards/alternate/spellcombatdefensive.png";
    public static final String BOTH_IMG_PATH = "mysticmod/images/cards/alternate/spellcombatcombined.png";
    private static final int COST = -1;
    private static final int DAMAGE_AMT = 6;
    private static final int UPGRADE_DAMAGE_AMT = 2;
    private static final int BLOCK_AMT = 5;
    private static final int UPGRADE_BLOCK_AMT = 2;
    private EffectStatus currentStatus;

    public SpellCombat() {
        super(ID, NAME, BOTH_IMG_PATH, COST, DESCRIPTION,
                AbstractCard.CardType.ATTACK, AbstractCardEnum.MYSTIC_PURPLE,
                AbstractCard.CardRarity.UNCOMMON, AbstractCard.CardTarget.ENEMY);
        this.loadCardImage(ATTACK_IMG_PATH);
        this.loadCardImage(DEFEND_IMG_PATH);
        this.loadCardImage(INERT_IMG_PATH);
        this.currentStatus = EffectStatus.INERT;
        this.damage = this.baseDamage = DAMAGE_AMT;
        this.block = this.baseBlock = BLOCK_AMT;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (this.energyOnUse < EnergyPanel.totalCount) {
            this.energyOnUse = EnergyPanel.totalCount;
        }
        AbstractDungeon.actionManager.addToBottom(new SpellCombatAction(p, m, this.damage, this.block, this.damageTypeForTurn, this.freeToPlayOnce, this.energyOnUse));
        currentStatus = EffectStatus.INERT;
        AbstractDungeon.actionManager.addToBottom(new LoadCardImageAction(this, INERT_IMG_PATH, false));
    }

    @Override
    public void applyPowers() {
        boolean artePlayed = AbstractDungeon.player.hasPower(ArtesPlayed.POWER_ID);
        boolean spellPlayed = AbstractDungeon.player.hasPower(SpellsPlayed.POWER_ID);
        if (artePlayed && spellPlayed) {
            if (currentStatus != EffectStatus.COMBINED) {
                currentStatus = EffectStatus.COMBINED;
                this.target = AbstractCard.CardTarget.ENEMY;
                AbstractDungeon.actionManager.addToBottom(new LoadCardImageAction(this, BOTH_IMG_PATH, true));
            }
        } else if (artePlayed) {
            if (currentStatus != EffectStatus.DEFENSIVE) {
                currentStatus = EffectStatus.DEFENSIVE;
                this.target = AbstractCard.CardTarget.SELF;
                AbstractDungeon.actionManager.addToBottom(new LoadCardImageAction(this, DEFEND_IMG_PATH, true));
            }
        } else if (spellPlayed) {
            if (currentStatus != EffectStatus.OFFENSIVE) {
                currentStatus = EffectStatus.OFFENSIVE;
                this.target = AbstractCard.CardTarget.ENEMY;
                AbstractDungeon.actionManager.addToBottom(new LoadCardImageAction(this, ATTACK_IMG_PATH, true));
            }
        } else {
            if (currentStatus != EffectStatus.INERT) {
                currentStatus = EffectStatus.INERT;
                this.target = AbstractCard.CardTarget.ENEMY;
                AbstractDungeon.actionManager.addToBottom(new LoadCardImageAction(this, INERT_IMG_PATH, false));
            }
        }
        super.applyPowers();
    }

    @Override
    public boolean hasEnoughEnergy() {
        if (!AbstractDungeon.player.hasPower(SpellsPlayed.POWER_ID) && !AbstractDungeon.player.hasPower(ArtesPlayed.POWER_ID)) {
            this.cantUseMessage = EXTENDED_DESCRIPTION[0];
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

    private enum EffectStatus {
        INERT,
        OFFENSIVE,
        DEFENSIVE,
        COMBINED
    }
}