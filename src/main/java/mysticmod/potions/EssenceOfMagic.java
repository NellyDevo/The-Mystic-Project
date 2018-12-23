package mysticmod.potions;

import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.localization.PotionStrings;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import mysticmod.MysticMod;

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
            AbstractCard randomCantrip = MysticMod.cantripsGroup.get(AbstractDungeon.cardRandomRng.random(MysticMod.cantripsGroup.size()-1)).makeCopy();
            AbstractDungeon.actionManager.addToBottom(new MakeTempCardInHandAction(randomCantrip, 1));
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
