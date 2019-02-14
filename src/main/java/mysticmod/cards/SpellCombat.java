package mysticmod.cards;

import com.badlogic.gdx.graphics.Color;
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
import mysticmod.patches.MysticTags;
import mysticmod.powers.ArtesPlayed;
import mysticmod.powers.SpellsPlayed;

public class SpellCombat extends AbstractAltArtMysticCard {
    public static final String ID = "mysticmod:SpellCombat";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String[] EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
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
        loadCardImage(ATTACK_IMG_PATH);
        loadCardImage(DEFEND_IMG_PATH);
        IMG_PATH = "mysticmod/images/cards/spellcombat.png";
        loadCardImage(IMG_PATH);
        currentStatus = EffectStatus.INERT;
        damage = baseDamage = DAMAGE_AMT;
        block = baseBlock = BLOCK_AMT;
        tags.add(MysticTags.IS_POWERFUL);
        tags.add(MysticTags.IS_POISED);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (energyOnUse < EnergyPanel.totalCount) {
            energyOnUse = EnergyPanel.totalCount;
        }
        AbstractDungeon.actionManager.addToBottom(new SpellCombatAction(p, m, damage, block, damageTypeForTurn, freeToPlayOnce, energyOnUse));
        currentStatus = EffectStatus.INERT;
        AbstractDungeon.actionManager.addToBottom(new LoadCardImageAction(this, IMG_PATH, false));
    }

    @Override
    public void applyPowers() {
        boolean artePlayed = AbstractDungeon.player.hasPower(ArtesPlayed.POWER_ID);
        boolean spellPlayed = AbstractDungeon.player.hasPower(SpellsPlayed.POWER_ID);
        if (artePlayed && spellPlayed) {
            if (currentStatus != EffectStatus.COMBINED) {
                currentStatus = EffectStatus.COMBINED;
                target = AbstractCard.CardTarget.ENEMY;
                AbstractDungeon.actionManager.addToBottom(new LoadCardImageAction(this, BOTH_IMG_PATH, true));
            }
        } else if (artePlayed) {
            if (currentStatus != EffectStatus.DEFENSIVE) {
                currentStatus = EffectStatus.DEFENSIVE;
                target = AbstractCard.CardTarget.SELF;
                AbstractDungeon.actionManager.addToBottom(new LoadCardImageAction(this, DEFEND_IMG_PATH, true));
            }
        } else if (spellPlayed) {
            if (currentStatus != EffectStatus.OFFENSIVE) {
                currentStatus = EffectStatus.OFFENSIVE;
                target = AbstractCard.CardTarget.ENEMY;
                AbstractDungeon.actionManager.addToBottom(new LoadCardImageAction(this, ATTACK_IMG_PATH, true));
            }
        } else {
            if (currentStatus != EffectStatus.INERT) {
                currentStatus = EffectStatus.INERT;
                target = AbstractCard.CardTarget.ENEMY;
                AbstractDungeon.actionManager.addToBottom(new LoadCardImageAction(this, IMG_PATH, false));
            }
        }
        super.applyPowers();
    }

    @Override
    public boolean hasEnoughEnergy() {
        if (!AbstractDungeon.player.hasPower(SpellsPlayed.POWER_ID) && !AbstractDungeon.player.hasPower(ArtesPlayed.POWER_ID)) {
            cantUseMessage = EXTENDED_DESCRIPTION[0];
            return false;
        }
        return super.hasEnoughEnergy();
    }

    @Override
    protected void updateGlow() {
        if (AbstractDungeon.player != null) {
            switch (currentStatus) {
                case COMBINED:
                    updateGlowWithColor(Color.valueOf("5233FF").cpy());
                    break;
                case OFFENSIVE:
                    updateGlowWithColor(ALT_GLOW_BLUE.cpy());
                    break;
                case DEFENSIVE:
                    updateGlowWithColor(ALT_GLOW_RED.cpy());
                    break;
                default:
                    super.updateGlow();
                    break;
            }
        } else {
            super.updateGlow();
        }
    }

    public void triggerOnEndOfPlayerTurn() {
        super.triggerOnEndOfPlayerTurn();
        if (currentStatus != EffectStatus.INERT) {
            loadCardImage(IMG_PATH);
            currentStatus = EffectStatus.INERT;
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new SpellCombat();
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDamage(UPGRADE_DAMAGE_AMT);
            upgradeBlock(UPGRADE_BLOCK_AMT);
        }
    }

    private enum EffectStatus {
        INERT,
        OFFENSIVE,
        DEFENSIVE,
        COMBINED
    }
}
