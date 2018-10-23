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
import mysticmod.patches.MysticTags;
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
    public AbstractRelic makeCopy() {
        return new CrystalBall();
    }
}