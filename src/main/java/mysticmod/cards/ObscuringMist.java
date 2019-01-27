package mysticmod.cards;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.ArtifactPower;
import mysticmod.actions.LoadCardImageAction;
import mysticmod.patches.AbstractCardEnum;
import mysticmod.patches.MysticTags;
import mysticmod.powers.ArtesPlayed;
import mysticmod.vfx.ObscuringMistEffect;

public class ObscuringMist extends AbstractAltArtMysticCard {
    public static final String ID = "mysticmod:ObscuringMist";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String ALTERNATE_IMG_PATH = "mysticmod/images/cards/alternate/obscuringmist.png";
    private static final int COST = 2;
    private static final int BLOCK_AMT = 15;
    private static final int UPGRADE_ARTIFACT_PLUS = 1;
    private static final int ARTIFACT_AMT = 1;

    public ObscuringMist() {
        super(ID, NAME, ALTERNATE_IMG_PATH, COST, DESCRIPTION,
                AbstractCard.CardType.SKILL, AbstractCardEnum.MYSTIC_PURPLE,
                AbstractCard.CardRarity.RARE, AbstractCard.CardTarget.SELF);
        IMG_PATH = "mysticmod/images/cards/obscuringmist.png";
        loadCardImage(IMG_PATH);
        exhaust = true;
        block = baseBlock = BLOCK_AMT;
        magicNumber = baseMagicNumber = ARTIFACT_AMT;
        tags.add(MysticTags.IS_SPELL);
        altGlowColor = ALT_GLOW_RED;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new VFXAction(p, new ObscuringMistEffect(p.hb.cX, p.hb.cY), 0.5f));
        //block
        AbstractDungeon.actionManager.addToBottom(new GainBlockAction(p, p, block));
        //Technical: artifact
        if ((p.hasPower(ArtesPlayed.POWER_ID)) && (p.getPower(ArtesPlayed.POWER_ID).amount >= 1)) {
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new ArtifactPower(p, magicNumber), magicNumber));
        }
        if (isArtAlternate) {
            AbstractDungeon.actionManager.addToBottom(new LoadCardImageAction(this, IMG_PATH, false));
            isArtAlternate = false;
        }
    }

    @Override
    public void applyPowers() {
        super.applyPowers();
        if (AbstractDungeon.player.hasPower(ArtesPlayed.POWER_ID)) {
            if (!isArtAlternate) {
                AbstractDungeon.actionManager.addToBottom(new LoadCardImageAction(this, ALTERNATE_IMG_PATH, true));
                isArtAlternate = true;
            }
        } else {
            if (isArtAlternate) {
                loadCardImage(IMG_PATH);
                isArtAlternate = false;
            }
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new ObscuringMist();
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_ARTIFACT_PLUS);
        }
    }
}
