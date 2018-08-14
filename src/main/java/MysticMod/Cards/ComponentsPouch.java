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

public class ComponentsPouch
        extends CustomCard {
    public static final String ID = "MysticMod:ComponentsPouch";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "MysticMod/images/cards/componentspouch.png";
    private static final int COST = 1;
    public static final int ATTACK_DMG = 6;
    private static final int UPGRADE_PLUS_DMG = 2;

    public ComponentsPouch() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                AbstractCard.CardType.ATTACK, AbstractCardEnum.MYSTIC_PURPLE,
                AbstractCard.CardRarity.COMMON, AbstractCard.CardTarget.ENEMY);
        this.damage=this.baseDamage = ATTACK_DMG;
    }
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int spellsCount = 0;
        for (final AbstractCard card : p.hand.group) {
            //insert relic logic here in the future
            if (card.rawDescription.startsWith("Spell.")) {
                spellsCount++;
            }
            if (card.rawDescription.startsWith("Cantrip.")) {
                if (!p.hasPower(SpellsPlayed.POWER_ID) || p.getPower(SpellsPlayed.POWER_ID).amount == 1) {
                    spellsCount++;
                }
            }
        }
        if (spellsCount > 0) {
            for (int i = 0; i < spellsCount; i++){
                AbstractDungeon.actionManager.addToBottom(
                        new com.megacrit.cardcrawl.actions.common.DamageAction(
                                m, new DamageInfo(p, this.damage, this.damageTypeForTurn)
                                , AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
            }
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new ComponentsPouch();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeDamage(UPGRADE_PLUS_DMG);
        }
    }
}