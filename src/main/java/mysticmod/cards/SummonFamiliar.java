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
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import kobting.friendlyminions.helpers.BasePlayerMinionHelper;
import kobting.friendlyminions.monsters.AbstractFriendlyMonster;
import kobting.friendlyminions.monsters.MinionMove;
import mysticmod.minions.foxfamiliar.FoxEvolutionPower;
import mysticmod.minions.foxfamiliar.FoxFamiliar;
import mysticmod.patches.MysticTags;
import mysticmod.patches.AbstractCardEnum;
import mysticmod.powers.ArtesPlayedNextTurn;
import mysticmod.powers.SpellsPlayed;
import mysticmod.powers.SpellsPlayedNextTurn;

import java.util.ArrayList;
import java.util.List;

public class SummonFamiliar
        extends AbstractMysticCard {
    public static final String ID = "mysticmod:SummonFamiliar";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "mysticmod/images/cards/summonfamiliar.png";
    private static final int COST = 1;
    private static final int UPGRADE_COST = 0;

    public SummonFamiliar() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.MYSTIC_PURPLE,
                CardRarity.COMMON, CardTarget.SELF);
        this.exhaust = true;
        this.tags.add(MysticTags.IS_SPELL);
        this.setBackgroundTexture(BG_SMALL_SPELL_SKILL_MYSTIC, BG_LARGE_SPELL_SKILL_MYSTIC);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (!BasePlayerMinionHelper.hasMinions(p)) {
            BasePlayerMinionHelper.addMinion(p, new FoxFamiliar());
        } else {
            FoxFamiliar fox = (FoxFamiliar)BasePlayerMinionHelper.getMinions(p).getMonster(FoxFamiliar.ID);
            if (!fox.hasPower(FoxEvolutionPower.POWER_ID)){
                fox.addMove(new MinionMove("Fortify", fox, new Texture("mysticmod/images/minions/defend move.png"),"Gain " + fox.baseBlockAmount + " Block.", () -> {
                    AbstractDungeon.actionManager.addToBottom(new GainBlockAction(p, fox, fox.baseBlockAmount));
                }));
            } else if (fox.getPower(FoxEvolutionPower.POWER_ID).amount == 1) {
                fox.removeMove("Attack");
                fox.removeMove("Flank");
                fox.removeMove("Channel");
                fox.removeMove("Fortify");
                fox.addMove(new MinionMove("Attack", fox, new Texture("mysticmod/images/minions/area attack move.png"), "Deal " + fox.baseDamageAmount + " damage to all enemies.", () -> {
                    ArrayList<DamageInfo> multiDamageInfo = new ArrayList<>();
                    int monsterCount = AbstractDungeon.getCurrRoom().monsters.monsters.size();
                    ArrayList<AbstractMonster> monsterList = AbstractDungeon.getCurrRoom().monsters.monsters;
                    for (int i = 0; i < monsterCount; i++) {
                        multiDamageInfo.add(new DamageInfo(fox, fox.baseDamageAmount, DamageInfo.DamageType.NORMAL));
                        multiDamageInfo.get(i).applyPowers(fox, monsterList.get(i));
                        AbstractDungeon.actionManager.addToBottom(new DamageAction(monsterList.get(i), multiDamageInfo.get(i)));
                    }
                }));
                fox.addMove(new MinionMove("Flank", fox, new Texture("mysticmod/images/minions/arte move.png"),"Start next turn with " + fox.baseSpellArteAmount + " [#FF5252]Arte[] played", () -> {
                    AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, fox, new ArtesPlayedNextTurn(AbstractDungeon.player, fox.baseSpellArteAmount), fox.baseSpellArteAmount));
                }));
                fox.addMove(new MinionMove("Channel", fox, new Texture("mysticmod/images/minions/spell move.png"),"Start next turn with " + fox.baseSpellArteAmount + " [#5299DC]Spell[] played", () -> {
                    AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, fox, new SpellsPlayedNextTurn(AbstractDungeon.player, fox.baseSpellArteAmount), fox.baseSpellArteAmount));
                }));
                fox.addMove(new MinionMove("Fortify", fox, new Texture("mysticmod/images/minions/defend move.png"),"Gain " + fox.baseBlockAmount + " Block.", () -> {
                    AbstractDungeon.actionManager.addToBottom(new GainBlockAction(p, fox, fox.baseBlockAmount));
                }));
            } else if (fox.getPower(FoxEvolutionPower.POWER_ID).amount == 2) {
                fox.baseSpellArteAmount += 1;
                fox.baseDamageAmount += 2;
                fox.baseBlockAmount += 2;
                fox.removeMove("Attack");
                fox.removeMove("Flank");
                fox.removeMove("Channel");
                fox.removeMove("Fortify");
                fox.addMove(new MinionMove("Attack", fox, new Texture("mysticmod/images/minions/area attack move.png"), "Deal " + fox.baseDamageAmount + " damage to all enemies.", () -> {
                    ArrayList<DamageInfo> multiDamageInfo = new ArrayList<>();
                    int monsterCount = AbstractDungeon.getCurrRoom().monsters.monsters.size();
                    ArrayList<AbstractMonster> monsterList = AbstractDungeon.getCurrRoom().monsters.monsters;
                    for (int i = 0; i < monsterCount; i++) {
                        multiDamageInfo.add(new DamageInfo(fox, fox.baseDamageAmount, DamageInfo.DamageType.NORMAL));
                        multiDamageInfo.get(i).applyPowers(fox, monsterList.get(i));
                        AbstractDungeon.actionManager.addToBottom(new DamageAction(monsterList.get(i), multiDamageInfo.get(i)));
                    }
                }));
                fox.addMove(new MinionMove("Flank", fox, new Texture("mysticmod/images/minions/arte move.png"),"Start next turn with " + fox.baseSpellArteAmount + " [#FF5252]Artes[] played", () -> {
                    AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, fox, new ArtesPlayedNextTurn(AbstractDungeon.player, fox.baseSpellArteAmount), fox.baseSpellArteAmount));
                }));
                fox.addMove(new MinionMove("Channel", fox, new Texture("mysticmod/images/minions/spell move.png"),"Start next turn with " + fox.baseSpellArteAmount + " [#5299DC]Spells[] played", () -> {
                    AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, fox, new SpellsPlayedNextTurn(AbstractDungeon.player, fox.baseSpellArteAmount), fox.baseSpellArteAmount));
                }));
                fox.addMove(new MinionMove("Fortify", fox, new Texture("mysticmod/images/minions/defend move.png"),"Gain " + fox.baseBlockAmount + " Block.", () -> {
                    AbstractDungeon.actionManager.addToBottom(new GainBlockAction(p, fox, fox.baseBlockAmount));
                }));
            }
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(fox, p, new FoxEvolutionPower(fox, 1), 1));
            AbstractDungeon.actionManager.addToBottom(new HealAction(fox, p, 5));
        }
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new SpellsPlayed(p, 1), 1));
    }

    @Override
    public List<TooltipInfo> getCustomTooltips() {
        List<TooltipInfo> retVal = super.getCustomTooltips();
        retVal.add(new TooltipInfo("Fox Familiar", "Can attack or help set up your combos. Playing more copies of this card will upgrade your familiar, up to 3 times."));
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