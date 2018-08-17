package mysticmod.relics;

import com.megacrit.cardcrawl.relics.AbstractRelic;
import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import mysticmod.powers.SpellsPlayed;
import mysticmod.powers.TechniquesPlayed;

public class CrystalBall extends CustomRelic {
    public static final String ID = "mysticmod:CrystalBall";
    public static final Texture IMG = new Texture("mysticmod/images/relics/crystalball.png");

    public CrystalBall() {
        super(ID, IMG, RelicTier.BOSS, LandingSound.MAGICAL);
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    //Most interaction effects unfortunately hardcoded... everywhere.
    @Override
    public void onPlayCard(final AbstractCard c, final AbstractMonster m) {
        if (c.type == AbstractCard.CardType.ATTACK && !c.rawDescription.startsWith("Spell.")) {
            if (c.rawDescription.startsWith("Cantrip") && (!AbstractDungeon.player.hasPower(SpellsPlayed.POWER_ID) || AbstractDungeon.player.getPower(SpellsPlayed.POWER_ID).amount == 1)) {
                return;
            }
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new TechniquesPlayed(AbstractDungeon.player, 1), 1));
        }
        if (c.type == AbstractCard.CardType.SKILL && !c.rawDescription.startsWith("Technique.")) {
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new SpellsPlayed(AbstractDungeon.player, 1), 1));
        }
    }

    @Override
    public AbstractRelic makeCopy() { // always override this method to return a new instance of your relic
        return new CrystalBall();
    }
}