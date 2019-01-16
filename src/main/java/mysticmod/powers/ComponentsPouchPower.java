package mysticmod.powers;

import basemod.helpers.BaseModCardTags;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import mysticmod.interfaces.SpellArteLogicAffector;

public class ComponentsPouchPower extends AbstractPower implements SpellArteLogicAffector {
    public static final String POWER_ID = "mysticmod:ComponentsPouchPower";
    public static final PowerStrings cardStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = cardStrings.NAME;
    public static final String[] DESCRIPTIONS = cardStrings.DESCRIPTIONS;

    public ComponentsPouchPower(AbstractCreature owner, int amount) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.region128 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage("mysticmod/images/powers/components pouch power 84.png"), 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage("mysticmod/images/powers/components pouch power 32.png"), 0, 0, 32, 32);
        this.type = PowerType.BUFF;
        this.amount = amount;
        this.updateDescription();
    }

    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[0] + amount;
        if (amount == 1) {
            description += DESCRIPTIONS[1];
        } else {
            description += DESCRIPTIONS[2];
        }
    }

    @Override
    public boolean modifyIsSpell(AbstractCard card, boolean isSpell) {
        if (card.hasTag(BaseModCardTags.BASIC_DEFEND)) {
            return true;
        }
        return isSpell;
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        if (isPlayer) {
            amount--;
            if (amount == 0) {
                AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(AbstractDungeon.player, AbstractDungeon.player, this));
            }
            updateDescription();
        }
    }
}
