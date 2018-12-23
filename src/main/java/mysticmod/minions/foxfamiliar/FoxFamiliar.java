package mysticmod.minions.foxfamiliar;

import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import kobting.friendlyminions.monsters.AbstractFriendlyMonster;
import kobting.friendlyminions.monsters.MinionMove;
import mysticmod.powers.ArtesPlayedNextTurn;
import mysticmod.powers.SpellsPlayedNextTurn;

public class FoxFamiliar extends AbstractFriendlyMonster {
    public static String NAME = "Fox Familiar";
    public static String ID = "mysticmod:FoxFamiliar";
    private AbstractMonster target;
    public int baseDamageAmount = 5;
    public int baseBlockAmount = 4;
    public int baseSpellArteAmount = 1;

    public FoxFamiliar() {
        super(NAME, ID, 20, -8.0F, 10.0F, 230.0F, 240.0F, "mysticmod/images/minions/foxfamiliar.png", -700.0F, 50.0F);
        addMoves();
    }

    private void addMoves(){
        this.moves.addMove(new MinionMove("Attack", this, new Texture("mysticmod/images/minions/attack move.png"), "Deal " + baseDamageAmount + " damage.", () -> {
            target = AbstractDungeon.getRandomMonster();
            DamageInfo info = new DamageInfo(this, baseDamageAmount, DamageInfo.DamageType.NORMAL);
            info.applyPowers(this, target); // <--- This lets powers effect minions attacks
            AbstractDungeon.actionManager.addToBottom(new DamageAction(target, info));
        }));
        this.moves.addMove(new MinionMove("Flank", this, new Texture("mysticmod/images/minions/arte move.png"),"Start next turn with " + baseSpellArteAmount + " [#FF5252]Arte[] played", () -> {
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new ArtesPlayedNextTurn(AbstractDungeon.player, baseSpellArteAmount), baseSpellArteAmount));
        }));
        this.moves.addMove(new MinionMove("Channel", this, new Texture("mysticmod/images/minions/spell move.png"),"Start next turn with " + baseSpellArteAmount + " [#5299DC]Spell[] played", () -> {
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new SpellsPlayedNextTurn(AbstractDungeon.player, baseSpellArteAmount), baseSpellArteAmount));
        }));
    }
}
