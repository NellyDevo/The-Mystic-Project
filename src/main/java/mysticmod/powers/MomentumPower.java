package mysticmod.powers;

import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import mysticmod.MysticMod;

public class MomentumPower extends AbstractPower implements CloneablePowerInterface {
    public static final String POWER_ID = "mysticmod:MomentumPower";
    public static final PowerStrings cardStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = cardStrings.NAME;
    public static final String[] DESCRIPTIONS = cardStrings.DESCRIPTIONS;

    public MomentumPower(AbstractCreature owner, int amount) {
        name = NAME;
        ID = POWER_ID;
        this.owner = owner;
        region128 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage("mysticmod/images/powers/momentum power 84.png"), 0, 0, 84, 84);
        region48 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage("mysticmod/images/powers/momentum power 32.png"), 0, 0, 32, 32);
        type = PowerType.BUFF;
        this.amount = amount;
        updateDescription();
    }

    @Override
    public void updateDescription() {
        if (amount == 1) {
            description = DESCRIPTIONS[0];
        } else {
            description = DESCRIPTIONS[1] + amount + DESCRIPTIONS[2];
        }
    }

    @Override
    public float atDamageGive(float damage, DamageInfo.DamageType type, AbstractCard card) {
        if (MysticMod.isThisAnArte(card) && AbstractDungeon.player.hasPower(SpellsPlayed.POWER_ID)) {
            damage += AbstractDungeon.player.getPower(SpellsPlayed.POWER_ID).amount * amount;
        }
        if (MysticMod.isThisASpell(card) && AbstractDungeon.player.hasPower(ArtesPlayed.POWER_ID)) {
            damage += AbstractDungeon.player.getPower(ArtesPlayed.POWER_ID).amount * amount;
        }
        return damage;
    }

    @Override
    public float modifyBlock(float block, AbstractCard card) {
        if (MysticMod.isThisAnArte(card) && AbstractDungeon.player.hasPower(SpellsPlayed.POWER_ID)) {
            block += AbstractDungeon.player.getPower(SpellsPlayed.POWER_ID).amount * amount;
        }
        if (MysticMod.isThisASpell(card) && AbstractDungeon.player.hasPower(ArtesPlayed.POWER_ID)) {
            block += AbstractDungeon.player.getPower(ArtesPlayed.POWER_ID).amount * amount;
        }
        return block;
    }

    @Override
    public AbstractPower makeCopy() {
        return new MomentumPower(owner, amount);
    }
}
