package mysticmod.minions.foxfamiliar;

import basemod.ReflectionHacks;
import basemod.helpers.CardTags;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import kobting.friendlyminions.actions.ChooseAction;
import kobting.friendlyminions.actions.ChooseActionInfo;
import kobting.friendlyminions.monsters.AbstractFriendlyMonster;
import mysticmod.cards.AbstractMysticCard;
import mysticmod.mystictags.MysticTags;
import mysticmod.powers.ArtesPlayedNextTurn;
import mysticmod.powers.SpellsPlayedNextTurn;

import java.util.ArrayList;

import static com.badlogic.gdx.math.MathUtils.floor;

public class FoxFamiliar extends AbstractFriendlyMonster {

    public static String NAME = "Fox Familiar";
    public static String ID = "mysticmod:FoxFamiliar";
    private ArrayList<ChooseActionInfo> moveInfo;
    private boolean hasAttacked = false;
    private AbstractMonster target;

    public FoxFamiliar() {
        super(NAME, ID, 20, null, -8.0F, 10.0F, 230.0F, 240.0F, "mysticmod/images/minions/foxfamiliar.png", -700.0F, 0);

    }

    @Override
    public void takeTurn() {
        if(!hasAttacked){
            moveInfo = makeMoves();
            ChooseAction pickAction = new ChooseAction(new FoxSelectionCard(), target, "Choose your familiar's next action");
            this.moveInfo.forEach( move -> {
                pickAction.add(move.getName(), move.getDescription(), move.getAction());
            });
            CardGroup reflectedChoiceGroup = (CardGroup)ReflectionHacks.getPrivate(pickAction, ChooseAction.class, "choices");
            System.out.println("reflectedChoiceGroup = " + reflectedChoiceGroup);
            ((AbstractMysticCard)reflectedChoiceGroup.group.get(0)).loadCardImage(FoxSelectionCard.ATTACK_IMG);
            ((AbstractMysticCard)reflectedChoiceGroup.group.get(1)).loadCardImage(FoxSelectionCard.FLANK_IMG);
            ((AbstractMysticCard)reflectedChoiceGroup.group.get(1)).setBackgroundTexture(AbstractMysticCard.BG_SMALL_ARTE_ATTACK_MYSTIC, AbstractMysticCard.BG_LARGE_ARTE_ATTACK_MYSTIC);
            CardTags.addTags(reflectedChoiceGroup.group.get(1), MysticTags.IS_ARTE);
            ((AbstractMysticCard)reflectedChoiceGroup.group.get(2)).loadCardImage(FoxSelectionCard.CHARGE_IMG);
            ((AbstractMysticCard)reflectedChoiceGroup.group.get(2)).setBackgroundTexture(AbstractMysticCard.BG_SMALL_SPELL_ATTACK_MYSTIC, AbstractMysticCard.BG_LARGE_SPELL_ATTACK_MYSTIC);
            CardTags.addTags(reflectedChoiceGroup.group.get(2), MysticTags.IS_SPELL);
            if (this.hasPower(FoxEvolutionPower.POWER_ID) && this.getPower(FoxEvolutionPower.POWER_ID).amount >= 1) {
                ((AbstractMysticCard)reflectedChoiceGroup.group.get(3)).type = AbstractCard.CardType.SKILL;
                ((AbstractMysticCard)reflectedChoiceGroup.group.get(3)).loadCardImage(FoxSelectionCard.REINFORCE_IMG);
                ((AbstractMysticCard)reflectedChoiceGroup.group.get(3)).setBackgroundTexture(AbstractMysticCard.BG_SMALL_DEFAULT_SKILL_MYSTIC, AbstractMysticCard.BG_LARGE_DEFAULT_SKILL_MYSTIC);
            }
            if (this.hasPower(FoxEvolutionPower.POWER_ID) && this.getPower(FoxEvolutionPower.POWER_ID).amount >= 3) {
                reflectedChoiceGroup.group.get(0).upgrade();
                reflectedChoiceGroup.group.get(1).upgrade();
                reflectedChoiceGroup.group.get(2).upgrade();
                reflectedChoiceGroup.group.get(3).upgrade();
            }
            AbstractDungeon.actionManager.addToBottom(pickAction);
        }
    }

    @Override
    public void applyEndOfTurnTriggers() {
        super.applyEndOfTurnTriggers();
        this.hasAttacked = false;
    }

    //Create possible moves for the monster
    private ArrayList<ChooseActionInfo> makeMoves(){
        int baseValue = 0;
        if (this.hasPower(FoxEvolutionPower.POWER_ID) && this.getPower(FoxEvolutionPower.POWER_ID).amount >= 3) {
            baseValue = 1;
        }
        int attackValue = baseValue * 2 + 5;
        int modifiedAttackValue = 0;
        int defendValue = baseValue * 2 + 4;
        int spellArteValue = baseValue + 1;
        String attackDescription = String.format("Deal %d damage to a random enemy.", attackValue);
        String attackAllDescription = String.format("Deal %d damage to all enemies.", attackValue);
        String defendDescription = String.format("Give %d Block to The Mystic", defendValue);
        String spellDescription = String.format("Begin next turn as if you had already played %d Spell", spellArteValue);
        String arteDescription = String.format("Begin next turn as if you had already played %d Arte", spellArteValue);
        if (baseValue > 0) {
            spellDescription += "s.";
            arteDescription += "s.";
        } else {
            spellDescription += ".";
            arteDescription += ".";
        }
        ArrayList<ChooseActionInfo> tempInfo = new ArrayList<>();
        if (this.hasPower(FoxEvolutionPower.POWER_ID) && this.getPower(FoxEvolutionPower.POWER_ID).amount >= 2) {
            final ArrayList<AbstractMonster> m = AbstractDungeon.getCurrRoom().monsters.monsters;
            int[] multiDamage = new int[m.size()];
            for (int i = 0; i < multiDamage.length; i++) {
                float modifiedAttack = applyPowersProxy(m.get(i) , (float)attackValue, this);
                multiDamage[i] = floor(modifiedAttack);
            }
            tempInfo.add(new ChooseActionInfo("Attack", attackAllDescription, () -> {
                AbstractDungeon.actionManager.addToBottom(
                        new DamageAllEnemiesAction(this, multiDamage, DamageInfo.DamageType.NORMAL, AbstractGameAction.AttackEffect.SMASH));
            }));
        } else {
            ArrayList<AbstractMonster> viableTargets = new ArrayList<>();
            for (AbstractMonster monster : AbstractDungeon.getCurrRoom().monsters.monsters) {
                if (monster.currentHealth > 0) {
                    viableTargets.add(monster);
                }
            }
            if (viableTargets.size() > 0) {
                target = viableTargets.get(AbstractDungeon.aiRng.random(viableTargets.size()-1));
                modifiedAttackValue = floor(applyPowersProxy(target , (float)attackValue, this));
            }
            final int finalAttackValue = modifiedAttackValue;
            tempInfo.add(new ChooseActionInfo("Attack", attackDescription, () -> {
                if (target != null) {
                    AbstractDungeon.actionManager.addToBottom(new DamageAction(target,
                            new DamageInfo(AbstractDungeon.player, finalAttackValue, DamageInfo.DamageType.NORMAL), AbstractGameAction.AttackEffect.SMASH));
                }
            }));
        }
        tempInfo.add(new ChooseActionInfo("Flank", arteDescription, () -> {
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player,AbstractDungeon.player,
                    new ArtesPlayedNextTurn(AbstractDungeon.player, spellArteValue), spellArteValue));
        }));

        tempInfo.add(new ChooseActionInfo("Channel", spellDescription, () -> {
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player,AbstractDungeon.player,
                    new SpellsPlayedNextTurn(AbstractDungeon.player, spellArteValue), spellArteValue));
        }));
        if (this.hasPower(FoxEvolutionPower.POWER_ID) && this.getPower(FoxEvolutionPower.POWER_ID).amount >= 1) {
            tempInfo.add(new ChooseActionInfo("Reinforce", defendDescription, () -> {
                AbstractDungeon.actionManager.addToBottom(new GainBlockAction(AbstractDungeon.player, AbstractDungeon.player, defendValue));
            }));
        }
        return tempInfo;
    }

    private static float applyPowersProxy(AbstractMonster m, float input, AbstractMonster minionInstance) {
        float retVal = input;
        for (final AbstractPower p : minionInstance.powers) {
            retVal = p.atDamageGive(retVal, DamageInfo.DamageType.NORMAL);
        }
        for (final AbstractPower p : m.powers) {
            retVal = p.atDamageReceive(retVal, DamageInfo.DamageType.NORMAL);
        }
        for (final AbstractPower p : minionInstance.powers) {
            retVal = p.atDamageFinalGive(retVal, DamageInfo.DamageType.NORMAL);
        }
        for (final AbstractPower p : m.powers) {
            retVal = p.atDamageFinalReceive(retVal, DamageInfo.DamageType.NORMAL);
        }
        return retVal;
    }

    //Not needed unless doing some kind of random move like normal Monsters
    @Override
    protected void getMove(int i) {

    }
}