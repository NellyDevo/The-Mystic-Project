package mysticmod.potions;

import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.localization.PotionStrings;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import mysticmod.cards.cantrips.*;

public class EssenceOfMagic extends AbstractPotion {

    public static final String POTION_ID = "mysticmod:EssenceOfMagic";
    private static final PotionStrings potionStrings = CardCrawlGame.languagePack.getPotionString(POTION_ID);
    public static final String NAME = potionStrings.NAME;
    public static final String[] DESCRIPTIONS = potionStrings.DESCRIPTIONS;
    public static final int NUMBER_OF_CANTRIPS = 2;

    public EssenceOfMagic() {
        super(NAME, POTION_ID, PotionRarity.UNCOMMON, PotionSize.CARD, PotionColor.WHITE);
        this.potency = this.getPotency();
        if (this.potency == 1) {
            this.description = DESCRIPTIONS[0] + this.potency + DESCRIPTIONS[2];
        } else {
            this.description = DESCRIPTIONS[0] + this.potency + DESCRIPTIONS[1];
        }
        this.isThrown = false;
        this.tips.add(new PowerTip(this.name, this.description));
    }

    @Override
    public void use(final AbstractCreature target) {
        for (int i = 0; i < this.potency; i++) {
            int randomlyGeneratedNumber = AbstractDungeon.cardRandomRng.random(4);
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
        }
    }

    @Override
    public AbstractPotion makeCopy() {
        return new EssenceOfMagic();
    }

    @Override
    public int getPotency(final int ascensionLevel) {
        return NUMBER_OF_CANTRIPS;
    }
}