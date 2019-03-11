package mysticmod.powers;

import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class StoneskinPower extends AbstractPower implements CloneablePowerInterface {
    public static final String POWER_ID = "mysticmod:StoneskinPower";
    public static final PowerStrings cardStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = cardStrings.NAME;
    public static final String[] DESCRIPTIONS = cardStrings.DESCRIPTIONS;
    private int baseBlock;
    private int block;

    public StoneskinPower(AbstractCreature owner, int amount) {
        name = NAME;
        ID = POWER_ID;
        this.owner = owner;
        region128 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage("mysticmod/images/powers/stoneskin power 84.png"), 0, 0, 84, 84);
        region48 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage("mysticmod/images/powers/stoneskin power 32.png"), 0, 0, 32, 32);
        type = PowerType.BUFF;
        this.amount = amount;
        baseBlock = block = 2;
        applyPowersToBlock();
        updateDescription();
        priority = -199;
    }

    @Override
    public void updateDescription() {
        if (amount == 1) {
            description = DESCRIPTIONS[0] + DESCRIPTIONS[1] + block + DESCRIPTIONS[3];
        } else {
            description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[2] + block + DESCRIPTIONS[3];
        }
    }

    @Override
    public void atStartOfTurn() {
        applyPowersToBlock();
        if (amount == 1) {
            AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(AbstractDungeon.player, AbstractDungeon.player, POWER_ID));
        } else {
            amount--;
            updateDescription();
        }
        AbstractDungeon.actionManager.addToBottom(new GainBlockAction(AbstractDungeon.player, AbstractDungeon.player, block));
    }

    @Override
    public void onAfterUseCard(AbstractCard card, UseCardAction action) {
        applyPowersToBlock();
        updateDescription();
    }

    private void applyPowersToBlock() {
        float tmp = baseBlock;
        for (final AbstractPower p : AbstractDungeon.player.powers) {
            tmp = p.modifyBlock(tmp);
        }
        if (tmp < 0.0f) {
            tmp = 0.0f;
        }
        block = MathUtils.floor(tmp);
    }

    @Override
    public AbstractPower makeCopy() {
        return new StoneskinPower(owner, amount);
    }
}
