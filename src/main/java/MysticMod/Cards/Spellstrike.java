package MysticMod.Cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import MysticMod.Patches.AbstractCardEnum;
import MysticMod.Powers.TechniquesPlayed;
import MysticMod.Actions.PlaySpellAttackFromDrawPile;
import java.io.*;

import basemod.abstracts.CustomCard;

public class Spellstrike
        extends CustomCard {
    public static final String ID = "MysticMod:Spellstrike";
    public static final String NAME = "Spellstrike";
    public static final String DESCRIPTION = "Technique. NL Deal !D! damage. NL search your draw pile for an Attack Spell and play it. NL Exhaust.";
    public static final String IMG_PATH = "MysticMod/images/cards/spellstrike.png";
    private static final int COST = 1;
    public static final int ATTACK_DMG = 11;
    private static final int UPGRADE_PLUS_DMG = 3;

    public Spellstrike() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                AbstractCard.CardType.ATTACK, AbstractCardEnum.MYSTIC_PURPLE,
                AbstractCard.CardRarity.RARE, AbstractCard.CardTarget.ENEMY);
        this.damage=this.baseDamage = ATTACK_DMG;
        this.exhaust = true;
        //if (AbstractDungeon.Player.drawPile.group.size > 0) {
        //    for (final AbstractCard AttackSpell : AbstractDungeon.Player.drawPile.group){
        //        if ((AttackSpell.CardType == AbstractCard.CardType.ATTACK) && (AttackSpell.rawDescription.startsWith("Spell."))){
        //            modal.addOption(AttackSpell);
        //        }
        //        modal.Create();
        //    }
        //}
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(
                new com.megacrit.cardcrawl.actions.common.DamageAction(
                        m, new DamageInfo(p, this.damage, this.damageTypeForTurn)
                        , AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new TechniquesPlayed(p, 1), 1));
        AbstractDungeon.actionManager.addToBottom(new PlaySpellAttackFromDrawPile(1, m, false));
    }

    @Override
    public AbstractCard makeCopy() {
        return new Spellstrike();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeDamage(UPGRADE_PLUS_DMG);
        }
    }
}