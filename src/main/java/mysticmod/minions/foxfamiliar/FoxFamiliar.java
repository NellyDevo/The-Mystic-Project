package mysticmod.minions.foxfamiliar;

import basemod.ReflectionHacks;
import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import kobting.friendlyminions.actions.ChooseAction;
import kobting.friendlyminions.actions.ChooseActionInfo;
import kobting.friendlyminions.cards.MonsterCard;
import kobting.friendlyminions.monsters.AbstractFriendlyMonster;
import mysticmod.cards.AbstractMysticCard;
import mysticmod.powers.SpellsPlayedNextTurn;
import mysticmod.powers.TechniquesPlayedNextTurn;

import java.util.ArrayList;

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
            ((AbstractMysticCard)reflectedChoiceGroup.group.get(1)).isTechnique = true;
            ((AbstractMysticCard)reflectedChoiceGroup.group.get(2)).loadCardImage(FoxSelectionCard.CHARGE_IMG);
            ((AbstractMysticCard)reflectedChoiceGroup.group.get(2)).setBackgroundTexture(AbstractMysticCard.BG_SMALL_SPELL_ATTACK_MYSTIC, AbstractMysticCard.BG_LARGE_SPELL_ATTACK_MYSTIC);
            ((AbstractMysticCard)reflectedChoiceGroup.group.get(2)).isSpell = true;
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
        ArrayList<ChooseActionInfo> tempInfo = new ArrayList<>();

        target = AbstractDungeon.getRandomMonster();

        tempInfo.add(new ChooseActionInfo("Attack", "Deal 5 damage.", () -> {
            AbstractDungeon.actionManager.addToBottom(new DamageAction(target,
                    new DamageInfo(AbstractDungeon.player, 5, DamageInfo.DamageType.NORMAL), AbstractGameAction.AttackEffect.SMASH));
        }));

        tempInfo.add(new ChooseActionInfo("Flank", "Begin next turn as if you had played 1 Arte", () -> {
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player,AbstractDungeon.player,
                    new TechniquesPlayedNextTurn(AbstractDungeon.player, 1), 1));
        }));

        tempInfo.add(new ChooseActionInfo("Channel", "Begin next turn as if you had played 1 Spell", () -> {
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player,AbstractDungeon.player,
                    new SpellsPlayedNextTurn(AbstractDungeon.player, 1), 1));
        }));

        return tempInfo;
    }


    //Not needed unless doing some kind of random move like normal Monsters
    @Override
    protected void getMove(int i) {

    }
}