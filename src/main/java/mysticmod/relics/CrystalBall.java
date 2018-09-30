package mysticmod.relics;

import basemod.abstracts.CustomRelic;
import basemod.helpers.CardTags;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import mysticmod.cards.AbstractMysticCard;
import mysticmod.mystictags.MysticTags;
import mysticmod.powers.ArtesPlayed;
import mysticmod.powers.SpellsPlayed;

public class CrystalBall extends CustomRelic {
    public static final String ID = "mysticmod:CrystalBall";
    public static final Texture IMG = new Texture("mysticmod/images/relics/crystalball.png");
    public static final Texture OUTLINE = new Texture("mysticmod/images/relics/crystalball_p.png");

    public CrystalBall() {
        super(ID, IMG, OUTLINE, RelicTier.BOSS, LandingSound.MAGICAL);
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    //Most interaction effects unfortunately hardcoded... everywhere.
    @Override
    public void onPlayCard(final AbstractCard c, final AbstractMonster m) {
        if (c.type == AbstractCard.CardType.ATTACK && !(c instanceof AbstractMysticCard && (((AbstractMysticCard)c).isSpell()
                || CardTags.hasTag(c, MysticTags.IS_SPELL) || ((AbstractMysticCard)c).isArte() || CardTags.hasTag(c, MysticTags.IS_ARTE)))) {
            if (c.rawDescription.startsWith("Cantrip.") && (!AbstractDungeon.player.hasPower(SpellsPlayed.POWER_ID)
                    || AbstractDungeon.player.getPower(SpellsPlayed.POWER_ID).amount <= 2)) {
                return;
            }
            AbstractDungeon.actionManager.addToBottom(
                    new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player,
                            new ArtesPlayed(AbstractDungeon.player, 1), 1));
        }
        if (c.type == AbstractCard.CardType.SKILL && !(c instanceof AbstractMysticCard && (((AbstractMysticCard)c).isArte()
                || CardTags.hasTag(c, MysticTags.IS_ARTE) || ((AbstractMysticCard)c).isSpell() || CardTags.hasTag(c, MysticTags.IS_SPELL)))) {
            if (c.rawDescription.startsWith("Cantrip.") && (!AbstractDungeon.player.hasPower(SpellsPlayed.POWER_ID)
                    || AbstractDungeon.player.getPower(SpellsPlayed.POWER_ID).amount <= 2)) {
                return;
            }
            AbstractDungeon.actionManager.addToBottom(
                    new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player,
                            new SpellsPlayed(AbstractDungeon.player, 1), 1));
        }
    }

    @Override
    public AbstractRelic makeCopy() { // always override this method to return a new instance of your relic
        return new CrystalBall();
    }
}