package mysticmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DiscardSpecificCardAction;
import com.megacrit.cardcrawl.actions.utility.QueueCardAction;
import com.megacrit.cardcrawl.actions.utility.UnlimboAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import mysticmod.cards.AbstractMysticCard;
import mysticmod.powers.GeminiFormPower;
import mysticmod.relics.CrystalBall;

public class PlayRandomSpellArteFromDrawPile extends AbstractGameAction {
    private AbstractPlayer p;
    private boolean lookingForArte;
    private AbstractMonster t;

    public PlayRandomSpellArteFromDrawPile(final AbstractMonster target, final boolean lookingForArte, final AbstractCard triggerCard) {
        this.setValues(this.t = target, this.p = AbstractDungeon.player, amount);
        this.actionType = ActionType.CARD_MANIPULATION;
        this.duration = Settings.ACTION_DUR_MED;
        this.lookingForArte = lookingForArte;
    }

    @Override
    public void update() {
        CardGroup tmp = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
        for (AbstractCard drawPileCard : p.drawPile.group) {
            if (lookingForArte) {
                if ((drawPileCard instanceof AbstractMysticCard && ((AbstractMysticCard)drawPileCard).isTechnique()) || (AbstractDungeon.player.hasRelic(CrystalBall.ID) && drawPileCard.type == AbstractCard.CardType.ATTACK)) {
                    tmp.addToRandomSpot(drawPileCard);
                }
            } else {
                if ((drawPileCard instanceof AbstractMysticCard && ((AbstractMysticCard)drawPileCard).isSpell()) || (AbstractDungeon.player.hasRelic(CrystalBall.ID) && drawPileCard.type == AbstractCard.CardType.SKILL)) {
                    tmp.addToRandomSpot(drawPileCard);
                }
            }
        }
        if (tmp.size() > 0) {
            AbstractCard randomlyChosenCard = tmp.getRandomCard(AbstractDungeon.cardRandomRng);
            if (randomlyChosenCard.target == AbstractCard.CardTarget.ENEMY || randomlyChosenCard.target == AbstractCard.CardTarget.SELF_AND_ENEMY) {
                if (this.t == null) {
                    this.t = AbstractDungeon.getCurrRoom().monsters.getRandomMonster(true);
                }
            } else {
                this.t = null;
            }
            GeminiFormPower.isActive = false;
            AbstractDungeon.player.drawPile.group.remove(randomlyChosenCard);
            AbstractDungeon.getCurrRoom().souls.remove(randomlyChosenCard);
            randomlyChosenCard.freeToPlayOnce = true;
            AbstractDungeon.player.limbo.group.add(randomlyChosenCard);
            randomlyChosenCard.current_y = -200.0f * Settings.scale;
            randomlyChosenCard.target_x = Settings.WIDTH / 2.0f - 200.0f * Settings.scale;
            randomlyChosenCard.target_y = Settings.HEIGHT / 2.0f;
            randomlyChosenCard.targetAngle = 0.0f;
            randomlyChosenCard.lighten(false);
            randomlyChosenCard.drawScale = 0.12f;
            randomlyChosenCard.targetDrawScale = 0.75f;
            if (!randomlyChosenCard.canUse(AbstractDungeon.player, this.t)) {
                AbstractDungeon.actionManager.addToTop(new UnlimboAction(randomlyChosenCard));
                AbstractDungeon.actionManager.addToTop(new DiscardSpecificCardAction(randomlyChosenCard, AbstractDungeon.player.limbo));
                AbstractDungeon.actionManager.addToTop(new WaitAction(0.4f));
            } else {
                randomlyChosenCard.applyPowers();
                AbstractDungeon.actionManager.addToTop(new QueueCardAction(randomlyChosenCard, this.t));
                AbstractDungeon.actionManager.addToTop(new UnlimboAction(randomlyChosenCard));
                if (!Settings.FAST_MODE) {
                    AbstractDungeon.actionManager.addToTop(new WaitAction(Settings.ACTION_DUR_MED));
                } else {
                    AbstractDungeon.actionManager.addToTop(new WaitAction(Settings.ACTION_DUR_FASTER));
                }
            }
        }
        this.isDone = true;
    }
}