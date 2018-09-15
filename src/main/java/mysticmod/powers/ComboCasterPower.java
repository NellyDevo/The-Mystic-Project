package mysticmod.powers;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import mysticmod.cards.cantrips.*;

public class ComboCasterPower extends AbstractPower {
    public static final String POWER_ID = "mysticmod:ComboCasterPower";
    public static final PowerStrings cardStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = cardStrings.NAME;
    public static final String[] DESCRIPTIONS = cardStrings.DESCRIPTIONS;

    public ComboCasterPower(AbstractCreature owner, int amount) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.region128 = new TextureAtlas.AtlasRegion(new Texture("mysticmod/images/powers/combo caster power 84.png"), 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(new Texture("mysticmod/images/powers/combo caster power 32.png"), 0, 0, 32, 32);
        this.type = PowerType.BUFF;
        this.amount = amount;
        this.updateDescription();

    }

    @Override
    public void updateDescription() {
        if (amount == 1) {
            description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1] + DESCRIPTIONS[3] + amount + DESCRIPTIONS[4];
        } else {
            description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[2] + DESCRIPTIONS[3] + amount + DESCRIPTIONS[5];
        }

    }

    @Override
    public void atStartOfTurn() {
        int randomlyGeneratedNumber;
        for (int i = 0; i < this.amount; i++){
            randomlyGeneratedNumber = AbstractDungeon.cardRandomRng.random(4);
            switch (randomlyGeneratedNumber) {
                case 0:
                    AbstractDungeon.actionManager.addToBottom(new MakeTempCardInHandAction(new AcidSplash(), 1, false));
                    break;
                case 1:
                    AbstractDungeon.actionManager.addToBottom(new MakeTempCardInHandAction(new Prestidigitation(), 1, false));
                    break;
                case 2:
                    AbstractDungeon.actionManager.addToBottom(new MakeTempCardInHandAction(new RayOfFrost(), 1, false));
                    break;
                case 3:
                    AbstractDungeon.actionManager.addToBottom(new MakeTempCardInHandAction(new Spark(), 1, false));
                    break;
                case 4:
                    AbstractDungeon.actionManager.addToBottom(new MakeTempCardInHandAction(new ReadMagic(), 1, false));
                    break;
            }
            AbstractDungeon.player.gameHandSize--;
        }
    }

    @Override
    public void atStartOfTurnPostDraw() {
        for (int i = 0; i < this.amount; i++){
            AbstractDungeon.player.gameHandSize++;
        }
    }
}