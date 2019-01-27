package mysticmod.minions.foxfamiliar;

import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import kobting.friendlyminions.monsters.AbstractFriendlyMonster;
import kobting.friendlyminions.monsters.MinionMove;
import mysticmod.powers.ArtesPlayedNextTurn;
import mysticmod.powers.SpellsPlayedNextTurn;

public class FoxFamiliar extends AbstractFriendlyMonster {
    public static String ID = "mysticmod:FoxFamiliar";
    public static MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
    public static String[] MOVES = monsterStrings.MOVES;
    public static String[] DIALOG = monsterStrings.DIALOG;
    public static String NAME = monsterStrings.NAME;
    private AbstractMonster target;
    public int baseDamageAmount = 5;
    public int baseBlockAmount = 4;
    public int baseSpellArteAmount = 1;

    public FoxFamiliar(float x, float y) {
        super(NAME, ID, 20, -8.0F, 10.0F, 230.0F, 240.0F, "mysticmod/images/minions/foxfamiliar.png", x, y);
        addMoves();
    }

    private void addMoves(){
        moves.addMove(new MinionMove(DIALOG[0], this, new Texture("mysticmod/images/minions/attack move.png"), MOVES[0] + baseDamageAmount + MOVES[1], () -> {
            target = AbstractDungeon.getRandomMonster();
            DamageInfo info = new DamageInfo(this, baseDamageAmount, DamageInfo.DamageType.NORMAL);
            info.applyPowers(this, target); // <--- This lets powers effect minions attacks
            AbstractDungeon.actionManager.addToBottom(new DamageAction(target, info));
        }));
        moves.addMove(new MinionMove(DIALOG[1], this, new Texture("mysticmod/images/minions/arte move.png"),MOVES[2] + baseSpellArteAmount + MOVES[3], () -> {
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new ArtesPlayedNextTurn(AbstractDungeon.player, baseSpellArteAmount), baseSpellArteAmount));
        }));
        moves.addMove(new MinionMove(DIALOG[2], this, new Texture("mysticmod/images/minions/spell move.png"),MOVES[2] + baseSpellArteAmount + MOVES[4], () -> {
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new SpellsPlayedNextTurn(AbstractDungeon.player, baseSpellArteAmount), baseSpellArteAmount));
        }));
    }
}
