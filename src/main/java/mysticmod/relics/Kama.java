package mysticmod.relics;

import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import mysticmod.MysticMod;

public class Kama extends CustomRelic {
    public static final String ID = "mysticmod:Kama";
    public static final Texture IMG = new Texture("mysticmod/images/relics/kama.png");

    public Kama() {
        super(ID, IMG, RelicTier.RARE, LandingSound.MAGICAL);
        this.counter = 0;
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public void onPlayCard(final AbstractCard c, final AbstractMonster m) {
        ++this.counter;
        if (this.counter == 5) {
            this.counter = 0;
            this.flash();
            this.pulse = false;
            if (MysticMod.isThisASpell(c,true)) {
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new DexterityPower(AbstractDungeon.player, 1), 1));
            }
            if (MysticMod.isThisATechnique(c,true)) {
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new StrengthPower(AbstractDungeon.player, 1), 1));
            }
            AbstractDungeon.actionManager.addToBottom(new RelicAboveCreatureAction(AbstractDungeon.player, this));
        }
        else if (this.counter == 4) {
            this.beginPulse();
            this.pulse = true;
        }
    }

    @Override
    public void atBattleStart() {
        if (this.counter == 4) {
            this.beginPulse();
            this.pulse = true;
        }
    }

    @Override
    public AbstractRelic makeCopy() { // always override this method to return a new instance of your relic
        return new Kama();
    }
}