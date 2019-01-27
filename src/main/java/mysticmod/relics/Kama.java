package mysticmod.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import mysticmod.MysticMod;

public class Kama extends CustomRelic {
    public static final String ID = "mysticmod:Kama";
    public static final Texture IMG = new Texture("mysticmod/images/relics/kama.png");
    public static final Texture OUTLINE = new Texture("mysticmod/images/relics/kama_p.png");

    public Kama() {
        super(ID, IMG, OUTLINE, RelicTier.RARE, LandingSound.CLINK);
        counter = 0;
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public void onPlayCard(final AbstractCard c, final AbstractMonster m) {
        ++counter;
        if (counter == 5) {
            counter = 0;
            flash();
            pulse = false;
            if (MysticMod.isThisASpell(c)) {
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new DexterityPower(AbstractDungeon.player, 1), 1));
            }
            if (MysticMod.isThisAnArte(c)) {
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new StrengthPower(AbstractDungeon.player, 1), 1));
            }
            AbstractDungeon.actionManager.addToBottom(new RelicAboveCreatureAction(AbstractDungeon.player, this));
        }
        else if (counter == 4) {
            beginPulse();
            pulse = true;
        }
    }

    @Override
    public void onVictory() {
        pulse = false;
    }

    @Override
    public void atBattleStart() {
        if (counter == 4) {
            beginPulse();
            pulse = true;
        }
    }

    @Override
    public AbstractRelic makeCopy() {
        return new Kama();
    }
}
