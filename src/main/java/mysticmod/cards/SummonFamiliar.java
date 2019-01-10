package mysticmod.cards;

import basemod.helpers.TooltipInfo;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import kobting.friendlyminions.helpers.BasePlayerMinionHelper;
import kobting.friendlyminions.monsters.AbstractFriendlyMonster;
import kobting.friendlyminions.monsters.MinionMove;
import mysticmod.minions.foxfamiliar.FoxEvolutionPower;
import mysticmod.minions.foxfamiliar.FoxFamiliar;
import mysticmod.patches.AbstractCardEnum;
import mysticmod.patches.MysticTags;
import mysticmod.powers.ArtesPlayedNextTurn;
import mysticmod.powers.SpellsPlayedNextTurn;

import java.util.ArrayList;
import java.util.List;

public class SummonFamiliar extends AbstractMysticCard {
    public static final String ID = "mysticmod:SummonFamiliar";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(FoxFamiliar.ID);
    public static final String[] monsterDialog = monsterStrings.DIALOG;
    public static final String[] monsterMoves = monsterStrings.MOVES;
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String[] EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
    public static final String IMG_PATH = "mysticmod/images/cards/summonfamiliar.png";
    private static final int COST = 1;
    private static final int UPGRADE_COST = 0;

    public SummonFamiliar() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.MYSTIC_PURPLE,
                CardRarity.COMMON, CardTarget.SELF);
        this.exhaust = true;
        this.tags.add(MysticTags.IS_SPELL);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        MonsterGroup playerMinions = BasePlayerMinionHelper.getMinions(p);
        if (!(BasePlayerMinionHelper.hasMinions(p) && playerMinions.getMonsterNames().contains(FoxFamiliar.ID))) {
            int minionCount = playerMinions.monsters.size();
            if (BasePlayerMinionHelper.getMaxMinions(p) <= minionCount) {
                BasePlayerMinionHelper.changeMaxMinionAmount(p, minionCount + 1);
            }
            float x;
            float y;
            switch (minionCount) {
                case 0:
                    x = -700.0F;
                    y = 50.0F;
                    break;
                case 1:
                    x = -1200.0F;
                    y = 50.0F;
                    break;
                case 2:
                    x = -700.0F;
                    y = 250.0F;
                    break;
                case 3:
                    x = -1200.0F;
                    y = 250.0F;
                    break;
                default:
                    x = 0;
                    y = 400.0F;
                    System.out.println("Tell @JohnnyDevo that you somehow got more than 3 minions before the fox and to make more positions");
                    break;
            }
            BasePlayerMinionHelper.addMinion(p, new FoxFamiliar(x, y));
        } else {
            FoxFamiliar fox = (FoxFamiliar)BasePlayerMinionHelper.getMinions(p).getMonster(FoxFamiliar.ID);
            if (!fox.hasPower(FoxEvolutionPower.POWER_ID)){
                fox.addMove(new MinionMove(monsterDialog[3], fox, new Texture("mysticmod/images/minions/defend move.png"),monsterMoves[5] + fox.baseBlockAmount + monsterMoves[6], () -> {
                    AbstractDungeon.actionManager.addToBottom(new GainBlockAction(p, fox, fox.baseBlockAmount));
                }));
            } else if (fox.getPower(FoxEvolutionPower.POWER_ID).amount == 1) {
                fox.removeMove(monsterDialog[0]);
                fox.removeMove(monsterDialog[1]);
                fox.removeMove(monsterDialog[2]);
                fox.removeMove(monsterDialog[3]);
                fox.addMove(new MinionMove(monsterDialog[0], fox, new Texture("mysticmod/images/minions/area attack move.png"), monsterMoves[0] + fox.baseDamageAmount + monsterMoves[7], () -> {
                    ArrayList<DamageInfo> multiDamageInfo = new ArrayList<>();
                    int monsterCount = AbstractDungeon.getCurrRoom().monsters.monsters.size();
                    ArrayList<AbstractMonster> monsterList = AbstractDungeon.getCurrRoom().monsters.monsters;
                    for (int i = 0; i < monsterCount; i++) {
                        multiDamageInfo.add(new DamageInfo(fox, fox.baseDamageAmount, DamageInfo.DamageType.NORMAL));
                        multiDamageInfo.get(i).applyPowers(fox, monsterList.get(i));
                        AbstractDungeon.actionManager.addToBottom(new DamageAction(monsterList.get(i), multiDamageInfo.get(i)));
                    }
                }));
                fox.addMove(new MinionMove(monsterDialog[1], fox, new Texture("mysticmod/images/minions/arte move.png"),monsterMoves[2] + fox.baseSpellArteAmount + monsterMoves[3], () -> {
                    AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, fox, new ArtesPlayedNextTurn(AbstractDungeon.player, fox.baseSpellArteAmount), fox.baseSpellArteAmount));
                }));
                fox.addMove(new MinionMove(monsterDialog[2], fox, new Texture("mysticmod/images/minions/spell move.png"),monsterMoves[2] + fox.baseSpellArteAmount + monsterMoves[4], () -> {
                    AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, fox, new SpellsPlayedNextTurn(AbstractDungeon.player, fox.baseSpellArteAmount), fox.baseSpellArteAmount));
                }));
                fox.addMove(new MinionMove(monsterDialog[3], fox, new Texture("mysticmod/images/minions/defend move.png"),monsterMoves[5] + fox.baseBlockAmount + monsterMoves[6], () -> {
                    AbstractDungeon.actionManager.addToBottom(new GainBlockAction(p, fox, fox.baseBlockAmount));
                }));
            } else if (fox.getPower(FoxEvolutionPower.POWER_ID).amount == 2) {
                fox.baseSpellArteAmount += 1;
                fox.baseDamageAmount += 2;
                fox.baseBlockAmount += 2;
                fox.removeMove(monsterDialog[0]);
                fox.removeMove(monsterDialog[1]);
                fox.removeMove(monsterDialog[2]);
                fox.removeMove(monsterDialog[3]);
                fox.addMove(new MinionMove(monsterDialog[0], fox, new Texture("mysticmod/images/minions/area attack move.png"), monsterMoves[0] + fox.baseDamageAmount + monsterMoves[7], () -> {
                    ArrayList<DamageInfo> multiDamageInfo = new ArrayList<>();
                    int monsterCount = AbstractDungeon.getCurrRoom().monsters.monsters.size();
                    ArrayList<AbstractMonster> monsterList = AbstractDungeon.getCurrRoom().monsters.monsters;
                    for (int i = 0; i < monsterCount; i++) {
                        multiDamageInfo.add(new DamageInfo(fox, fox.baseDamageAmount, DamageInfo.DamageType.NORMAL));
                        multiDamageInfo.get(i).applyPowers(fox, monsterList.get(i));
                        AbstractDungeon.actionManager.addToBottom(new DamageAction(monsterList.get(i), multiDamageInfo.get(i)));
                    }
                }));
                fox.addMove(new MinionMove(monsterDialog[1], fox, new Texture("mysticmod/images/minions/arte move.png"),monsterMoves[2] + fox.baseSpellArteAmount + monsterMoves[3], () -> {
                    AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, fox, new ArtesPlayedNextTurn(AbstractDungeon.player, fox.baseSpellArteAmount), fox.baseSpellArteAmount));
                }));
                fox.addMove(new MinionMove(monsterDialog[2], fox, new Texture("mysticmod/images/minions/spell move.png"),monsterMoves[2] + fox.baseSpellArteAmount + monsterMoves[4], () -> {
                    AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, fox, new SpellsPlayedNextTurn(AbstractDungeon.player, fox.baseSpellArteAmount), fox.baseSpellArteAmount));
                }));
                fox.addMove(new MinionMove(monsterDialog[3], fox, new Texture("mysticmod/images/minions/defend move.png"),monsterMoves[5] + fox.baseBlockAmount + monsterMoves[6], () -> {
                    AbstractDungeon.actionManager.addToBottom(new GainBlockAction(p, fox, fox.baseBlockAmount));
                }));
            }
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(fox, p, new FoxEvolutionPower(fox, 1), 1));
            AbstractDungeon.actionManager.addToBottom(new HealAction(fox, p, 5));
        }
    }

    @Override
    public List<TooltipInfo> getCustomTooltips() {
        List<TooltipInfo> retVal = super.getCustomTooltips();
        retVal.add(new TooltipInfo(NAME, EXTENDED_DESCRIPTION[0]));
        return retVal;
    }

    @Override
    public AbstractCard makeCopy() {
        return new SummonFamiliar();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeBaseCost(UPGRADE_COST);
        }
    }
}
