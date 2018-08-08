package MysticMod.Screens;

//Copy of com.megacrit.cardcrawl.screens.CardRewardScreen, except twitch integration is removed.
//Additional change: line 377 and 413, rejects cards whose descriptions do not begin with "Spell."

import com.megacrit.cardcrawl.localization.*;
import com.megacrit.cardcrawl.cards.*;
import com.megacrit.cardcrawl.rewards.*;
import com.megacrit.cardcrawl.ui.buttons.*;
import com.megacrit.cardcrawl.dungeons.*;
import com.badlogic.gdx.*;
import com.megacrit.cardcrawl.helpers.controller.*;
import com.megacrit.cardcrawl.core.*;
import com.megacrit.cardcrawl.helpers.input.*;
import com.megacrit.cardcrawl.vfx.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.*;
import com.megacrit.cardcrawl.unlock.*;
import com.megacrit.cardcrawl.helpers.*;
import com.megacrit.cardcrawl.characters.*;
import com.megacrit.cardcrawl.ui.*;
import java.util.*;
import java.util.stream.*;
import java.util.function.*;
import org.apache.logging.log4j.*;
import com.megacrit.cardcrawl.screens.CardRewardScreen;
//import de.robojumper.ststwitch.*;

public class SpellRewardScreen extends CardRewardScreen
{
    private static final Logger logger;
    private static final UIStrings uiStrings;
    public static final String[] TEXT;
    private static final float PAD_X;
    private static final float CARD_TARGET_Y;
    public ArrayList<AbstractCard> rewardGroup;
    public AbstractCard codexCard;
    public AbstractCard discoveryCard;
    public boolean onCardSelect;
    public boolean hasTakenAll;
    public boolean cardOnly;
    public RewardItem rItem;
    private boolean codex;
    private boolean draft;
    private boolean discovery;
    private String header;
    private SkipCardButton skipButton;
    private SingingBowlButton bowlButton;
    private final int SKIP_BUTTON_IDX = 0;
    private final int BOWL_BUTTON_IDX = 1;
    private int draftCount;
//    private boolean isVoting;
//    private boolean mayVote;

    public SpellRewardScreen() {
        this.codexCard = null;
        this.discoveryCard = null;
        this.onCardSelect = true;
        this.hasTakenAll = false;
        this.cardOnly = false;
        this.rItem = null;
        this.codex = false;
        this.draft = false;
        this.discovery = false;
        this.header = "";
        this.skipButton = new SkipCardButton();
        this.bowlButton = new SingingBowlButton();
        this.draftCount = 0;
//        this.isVoting = false;
//        this.mayVote = false;
    }

    public void update() {
        this.skipButton.update();
        this.bowlButton.update();
        this.updateControllerInput();
        this.cardSelectUpdate();
    }

    private void updateControllerInput() {
        if (!Settings.isControllerMode || AbstractDungeon.topPanel.selectPotionMode || !AbstractDungeon.topPanel.potionUi.isHidden) {
            return;
        }
        int index = 0;
        boolean anyHovered = false;
        for (final AbstractCard c : this.rewardGroup) {
            if (c.hb.hovered) {
                anyHovered = true;
                break;
            }
            ++index;
        }
        if (!anyHovered) {
            index = 0;
            Gdx.input.setCursorPosition((int)this.rewardGroup.get(index).hb.cX, Settings.HEIGHT - (int)this.rewardGroup.get(index).hb.cY);
        }
        else if (CInputActionSet.left.isJustPressed() || CInputActionSet.altLeft.isJustPressed()) {
            if (--index < 0) {
                index = this.rewardGroup.size() - 1;
            }
            Gdx.input.setCursorPosition((int)this.rewardGroup.get(index).hb.cX, Settings.HEIGHT - (int)this.rewardGroup.get(index).hb.cY);
        }
        else if (CInputActionSet.right.isJustPressed() || CInputActionSet.altRight.isJustPressed()) {
            if (++index > this.rewardGroup.size() - 1) {
                index = 0;
            }
            Gdx.input.setCursorPosition((int)this.rewardGroup.get(index).hb.cX, Settings.HEIGHT - (int)this.rewardGroup.get(index).hb.cY);
        }
    }

    private void cardSelectUpdate() {
        AbstractCard hoveredCard = null;
        for (final AbstractCard c : this.rewardGroup) {
            c.update();
            c.updateHoverLogic();
            if (c.hb.justHovered) {
                CardCrawlGame.sound.playV("CARD_OBTAIN", 0.4f);
            }
            if (c.hb.hovered) {
                hoveredCard = c;
            }
        }
        if (hoveredCard != null && InputHelper.justClickedLeft) {
            hoveredCard.hb.clickStarted = true;
        }
        if (hoveredCard != null && (InputHelper.justClickedRight || CInputActionSet.proceed.isJustPressed())) {
            CardCrawlGame.cardPopup.open(hoveredCard);
        }
        if (hoveredCard != null && CInputActionSet.select.isJustPressed()) {
            hoveredCard.hb.clicked = true;
        }
        if (hoveredCard != null && hoveredCard.hb.clicked) {
            hoveredCard.hb.clicked = false;
            this.skipButton.hide();
            this.bowlButton.hide();
            if (this.codex) {
                this.codexCard = hoveredCard;
            }
            else if (this.discovery) {
                this.discoveryCard = hoveredCard;
            }
            else {
                this.acquireCard(hoveredCard);
            }
            this.takeReward();
            if (!this.draft || this.draftCount >= 15) {
                AbstractDungeon.closeCurrentScreen();
                this.draftCount = 0;
            }
            else {
                this.draftOpen();
            }
        }
    }

    private void acquireCard(final AbstractCard hoveredCard) {
        this.recordMetrics(hoveredCard);
        InputHelper.justClickedLeft = false;
        AbstractDungeon.effectsQueue.add(new FastCardObtainEffect(hoveredCard, hoveredCard.current_x, hoveredCard.current_y));
    }

    private void recordMetrics(final AbstractCard hoveredCard) {
        final HashMap<String, Object> choice = new HashMap<String, Object>();
        final ArrayList<String> notpicked = new ArrayList<String>();
        for (final AbstractCard card : this.rewardGroup) {
            if (card != hoveredCard) {
                notpicked.add(card.getMetricID());
            }
        }
        choice.put("picked", hoveredCard.getMetricID());
        choice.put("not_picked", notpicked);
        choice.put("floor", AbstractDungeon.floorNum);
        CardCrawlGame.metricData.card_choices.add(choice);
    }

    private void recordMetrics(final String pickText) {
        final HashMap<String, Object> choice = new HashMap<String, Object>();
        final ArrayList<String> notpicked = new ArrayList<String>();
        for (final AbstractCard card : this.rewardGroup) {
            notpicked.add(card.getMetricID());
        }
        choice.put("picked", pickText);
        choice.put("not_picked", notpicked);
        choice.put("floor", AbstractDungeon.floorNum);
        CardCrawlGame.metricData.card_choices.add(choice);
    }

    private void takeReward() {
        if (this.rItem != null) {
            AbstractDungeon.combatRewardScreen.rewards.remove(this.rItem);
            AbstractDungeon.combatRewardScreen.positionRewards();
            if (AbstractDungeon.combatRewardScreen.rewards.isEmpty()) {
                AbstractDungeon.combatRewardScreen.hasTakenAll = true;
                AbstractDungeon.overlayMenu.proceedButton.show();
            }
        }
    }

//    public void completeVoting(final int option) {
//        if (!this.isVoting) {
//            return;
//        }
//        this.isVoting = false;
//        if (this.getVoter().isPresent()) {
//            final TwitchVoter twitchVoter = this.getVoter().get();
//            AbstractDungeon.topPanel.twitch.ifPresent(twitchPanel -> twitchPanel.connection.sendMessage("Voting on card ended... chose " + twitchVoter.getOptions()[option].displayName));
//        }
//        while (AbstractDungeon.screen != AbstractDungeon.CurrentScreen.CARD_REWARD) {
//            AbstractDungeon.closeCurrentScreen();
//        }
//        if (option != 0) {
//            if (!this.bowlButton.isHidden() && option == 1) {
//                this.bowlButton.onClick();
//            }
//            else if (!this.bowlButton.isHidden()) {
//                AbstractDungeon.overlayMenu.cancelButton.hide();
//                this.acquireCard(this.rewardGroup.get(option - 2));
//            }
//            else if (option < this.rewardGroup.size() + 1) {
//                AbstractDungeon.overlayMenu.cancelButton.hide();
//                this.acquireCard(this.rewardGroup.get(option - 1));
//            }
//        }
//        this.takeReward();
//        AbstractDungeon.closeCurrentScreen();
//    }

//    private void renderTwitchVotes(final SpriteBatch sb) {
//        if (!this.isVoting) {
//            return;
//        }
//        if (this.getVoter().isPresent()) {
//            final TwitchVoter twitchVoter = this.getVoter().get();
//            final TwitchVoteOption[] options = twitchVoter.getOptions();
//            final int voteCountOffset = this.bowlButton.isHidden() ? 1 : 2;
//            final int sum = Arrays.stream(options).map(c -> c.voteCount).reduce(0, Integer::sum);
//            for (int i = 0; i < this.rewardGroup.size(); ++i) {
//                final AbstractCard c2 = this.rewardGroup.get(i);
//                final StringBuilder cardVoteText = new StringBuilder("#").append(i + voteCountOffset).append(": ").append(options[i + voteCountOffset].voteCount);
//                if (sum > 0) {
//                    cardVoteText.append(" (").append(options[i + voteCountOffset].voteCount * 100 / sum).append("%)");
//              }
//              FontHelper.renderFontCentered(sb, FontHelper.panelNameFont, cardVoteText.toString(), c2.target_x, c2.target_y - 200.0f * Settings.scale, Color.WHITE.cpy());
//            }
//            final StringBuilder skipVoteText = new StringBuilder("#0: ").append(options[0].voteCount);
//            if (sum > 0) {
//                skipVoteText.append(" (").append(options[0].voteCount * 100 / sum).append("%)");
//            }
//            if (this.bowlButton.isHidden()) {
//                FontHelper.renderFontCentered(sb, FontHelper.panelNameFont, skipVoteText.toString(), Settings.WIDTH / 2.0f, 150.0f * Settings.scale, Color.WHITE.cpy());
//            }
//            else {
//                FontHelper.renderFontCentered(sb, FontHelper.panelNameFont, skipVoteText.toString(), Settings.WIDTH / 2.0f - 100.0f, 150.0f * Settings.scale, Color.WHITE.cpy());
//                final StringBuilder bowlVoteText = new StringBuilder("#1: ").append(options[1].voteCount);
//                if (sum > 0) {
//                    bowlVoteText.append(" (").append(options[1].voteCount * 100 / sum).append("%)");
//                }
//                FontHelper.renderFontCentered(sb, FontHelper.panelNameFont, bowlVoteText.toString(), Settings.WIDTH / 2.0f + 100.0f, 150.0f * Settings.scale, Color.WHITE.cpy());
//           }
//            FontHelper.renderFontCentered(sb, FontHelper.panelNameFont, SpellRewardScreen.TEXT[3] + twitchVoter.getSecondsRemaining() + SpellRewardScreen.TEXT[4], Settings.WIDTH / 2.0f, AbstractDungeon.dynamicBanner.y - 70.0f * Settings.scale, Color.WHITE.cpy());
//        }
//    }

    public void render(final SpriteBatch sb) {
        this.skipButton.render(sb);
        this.bowlButton.render(sb);
        this.renderCardReward(sb);
//        if (!this.codex && !this.discovery) {
//            this.renderTwitchVotes(sb);
//        }
    }

    private void renderCardReward(final SpriteBatch sb) {
        for (final AbstractCard c : this.rewardGroup) {
            c.render(sb);
        }
        for (final AbstractCard c : this.rewardGroup) {
            c.renderCardTip(sb);
        }
    }

    public void reopen() {
        AbstractDungeon.screen = AbstractDungeon.CurrentScreen.CARD_REWARD;
        if (this.draft) {
            this.skipButton.hide();
            this.bowlButton.hide();
        }
        else if (this.codex) {
            this.skipButton.show();
            this.bowlButton.hide();
        }
        else if (this.discovery) {
            this.skipButton.show();
            this.bowlButton.hide();
        }
        else if (AbstractDungeon.player.hasRelic("Singing Bowl")) {
            this.skipButton.show(true);
            this.bowlButton.show(this.rItem);
        }
        else {
            this.skipButton.show();
            this.bowlButton.hide();
        }
        AbstractDungeon.topPanel.unhoverHitboxes();
        AbstractDungeon.isScreenUp = true;
        if (!this.codex && !this.discovery) {
            AbstractDungeon.dynamicBanner.appear(this.header);
        }
        else {
            AbstractDungeon.dynamicBanner.appear(SpellRewardScreen.TEXT[1]);
        }
        AbstractDungeon.overlayMenu.proceedButton.hide();
    }

    public void open(final ArrayList<AbstractCard> cards, final RewardItem rItem, final String header) {
        this.codex = false;
        this.discovery = false;
        this.draft = false;
        this.rItem = rItem;
        if (AbstractDungeon.player.hasRelic("Singing Bowl")) {
            this.skipButton.show(true);
            this.bowlButton.show(rItem);
        }
        else {
            this.skipButton.show();
            this.bowlButton.hide();
        }
        this.onCardSelect = true;
        AbstractDungeon.topPanel.unhoverHitboxes();
        this.rewardGroup = cards;
        AbstractDungeon.isScreenUp = true;
        AbstractDungeon.screen = AbstractDungeon.CurrentScreen.CARD_REWARD;
        this.header = header;
        AbstractDungeon.dynamicBanner.appear(header);
        AbstractDungeon.overlayMenu.proceedButton.hide();
        AbstractDungeon.overlayMenu.showBlackScreen();
        this.placeCards(Settings.WIDTH / 2.0f, SpellRewardScreen.CARD_TARGET_Y);
        for (final AbstractCard c : cards) {
            UnlockTracker.markCardAsSeen(c.cardID);
        }
        for (final AbstractCard c : cards) {
            if (c.type == AbstractCard.CardType.POWER && !TipTracker.tips.get("POWER_TIP")) {
                AbstractDungeon.ftue = new FtueTip(AbstractPlayer.LABEL[0], AbstractPlayer.MSG[0], Settings.WIDTH / 2.0f - 500.0f * Settings.scale, Settings.HEIGHT / 2.0f, c);
                AbstractDungeon.ftue.type = FtueTip.TipType.POWER;
                TipTracker.neverShowAgain("POWER_TIP");
                this.skipButton.hide();
                this.bowlButton.hide();
                break;
            }
        }
//        this.mayVote = true;
//        if (AbstractDungeon.topPanel.twitch.isPresent()) {
//            this.updateVote();
//        }
    }

    @Override
    public void discoveryOpen() {
        this.rItem = null;
        this.codex = false;
        this.discovery = true;
        this.discoveryCard = null;
        this.draft = false;
        this.codexCard = null;
        this.bowlButton.hide();
        this.skipButton.show();
        this.onCardSelect = true;
        AbstractDungeon.topPanel.unhoverHitboxes();
        final ArrayList<AbstractCard> derp = new ArrayList<AbstractCard>();
        while (derp.size() != 3) {
            boolean dupe = false;
            final AbstractCard tmp = AbstractDungeon.returnTrulyRandomCard(AbstractDungeon.cardRandomRng);
            for (final AbstractCard c : derp) {
                if (c.cardID.equals(tmp.cardID)) {
                    dupe = true;
                    break;
                }
            }
            if (!dupe && tmp.rawDescription.startsWith("Spell")) {
                derp.add(tmp.makeCopy());
            }
        }
        this.rewardGroup = derp;
        AbstractDungeon.isScreenUp = true;
        AbstractDungeon.screen = AbstractDungeon.CurrentScreen.CARD_REWARD;
        AbstractDungeon.dynamicBanner.appear(SpellRewardScreen.TEXT[1]);
        AbstractDungeon.overlayMenu.showBlackScreen();
        this.placeCards(Settings.WIDTH / 2.0f, SpellRewardScreen.CARD_TARGET_Y);
        for (final AbstractCard c2 : this.rewardGroup) {
            UnlockTracker.markCardAsSeen(c2.cardID);
        }
    }

    @Override
    public void discoveryOpen(final AbstractCard.CardType type) {
        this.rItem = null;
        this.codex = false;
        this.discovery = true;
        this.discoveryCard = null;
        this.draft = false;
        this.codexCard = null;
        this.bowlButton.hide();
        this.skipButton.show();
        this.onCardSelect = true;
        AbstractDungeon.topPanel.unhoverHitboxes();
        final ArrayList<AbstractCard> derp = new ArrayList<AbstractCard>();
        while (derp.size() != 3) {
            boolean dupe = false;
            final AbstractCard tmp = AbstractDungeon.returnTrulyRandomCard(type, AbstractDungeon.cardRandomRng);
            for (final AbstractCard c : derp) {
                if (c.cardID.equals(tmp.cardID)) {
                    dupe = true;
                    break;
                }
            }
            if (!dupe && tmp.rawDescription.startsWith("Spell")) {
                derp.add(tmp.makeCopy());
            }
        }
        this.rewardGroup = derp;
        AbstractDungeon.isScreenUp = true;
        AbstractDungeon.screen = AbstractDungeon.CurrentScreen.CARD_REWARD;
        AbstractDungeon.dynamicBanner.appear(SpellRewardScreen.TEXT[1]);
        AbstractDungeon.overlayMenu.showBlackScreen();
        this.placeCards(Settings.WIDTH / 2.0f, SpellRewardScreen.CARD_TARGET_Y);
        for (final AbstractCard c2 : this.rewardGroup) {
            UnlockTracker.markCardAsSeen(c2.cardID);
        }
    }

    public void codexOpen() {
        this.rItem = null;
        this.codex = true;
        this.discovery = false;
        this.discoveryCard = null;
        this.draft = false;
        this.codexCard = null;
        this.bowlButton.hide();
        this.skipButton.show();
        this.onCardSelect = true;
        AbstractDungeon.topPanel.unhoverHitboxes();
        final ArrayList<AbstractCard> derp = new ArrayList<AbstractCard>();
        while (derp.size() != 3) {
            boolean dupe = false;
            final AbstractCard tmp = AbstractDungeon.returnTrulyRandomCard();
            for (final AbstractCard c : derp) {
                if (c.cardID.equals(tmp.cardID)) {
                    dupe = true;
                    break;
                }
            }
            if (!dupe) {
                derp.add(tmp.makeCopy());
            }
        }
        this.rewardGroup = derp;
        AbstractDungeon.isScreenUp = true;
        AbstractDungeon.screen = AbstractDungeon.CurrentScreen.CARD_REWARD;
        AbstractDungeon.dynamicBanner.appear(SpellRewardScreen.TEXT[1]);
        AbstractDungeon.overlayMenu.showBlackScreen();
        this.placeCards(Settings.WIDTH / 2.0f, SpellRewardScreen.CARD_TARGET_Y);
        for (final AbstractCard c2 : this.rewardGroup) {
            UnlockTracker.markCardAsSeen(c2.cardID);
        }
    }

    public void draftOpen() {
        ++this.draftCount;
        this.rItem = null;
        this.codex = false;
        this.discovery = false;
        this.discoveryCard = null;
        this.draft = true;
        this.codexCard = null;
        this.header = SpellRewardScreen.TEXT[1];
        this.bowlButton.hide();
        this.skipButton.hide();
        this.onCardSelect = true;
        AbstractDungeon.topPanel.unhoverHitboxes();
        final ArrayList<AbstractCard> derp = new ArrayList<AbstractCard>();
        while (derp.size() != 3) {
            boolean dupe = false;
            final AbstractCard tmp = AbstractDungeon.returnRandomCard();
            for (final AbstractCard c : derp) {
                if (c.cardID.equals(tmp.cardID)) {
                    dupe = true;
                    break;
                }
            }
            if (!dupe) {
                derp.add(tmp.makeCopy());
            }
        }
        this.rewardGroup = derp;
        AbstractDungeon.isScreenUp = true;
        AbstractDungeon.screen = AbstractDungeon.CurrentScreen.CARD_REWARD;
        AbstractDungeon.dynamicBanner.appear(SpellRewardScreen.TEXT[1]);
        AbstractDungeon.overlayMenu.showBlackScreen();
        this.placeCards(Settings.WIDTH / 2.0f, SpellRewardScreen.CARD_TARGET_Y);
        for (final AbstractCard c2 : this.rewardGroup) {
            UnlockTracker.markCardAsSeen(c2.cardID);
        }
    }

    private void placeCards(final float x, final float y) {
        switch (this.rewardGroup.size()) {
            case 1: {
                this.rewardGroup.get(0).target_x = Settings.WIDTH / 2.0f;
                this.rewardGroup.get(0).target_y = y;
                break;
            }
            case 2: {
                this.rewardGroup.get(0).target_x = Settings.WIDTH / 2.0f - AbstractCard.IMG_WIDTH / 2.0f;
                this.rewardGroup.get(1).target_x = Settings.WIDTH / 2.0f + AbstractCard.IMG_WIDTH / 2.0f;
                this.rewardGroup.get(0).target_y = y;
                this.rewardGroup.get(1).target_y = y;
                break;
            }
            case 3: {
                this.rewardGroup.get(0).target_x = Settings.WIDTH / 2.0f - AbstractCard.IMG_WIDTH - SpellRewardScreen.PAD_X;
                this.rewardGroup.get(1).target_x = Settings.WIDTH / 2.0f;
                this.rewardGroup.get(2).target_x = Settings.WIDTH / 2.0f + AbstractCard.IMG_WIDTH + SpellRewardScreen.PAD_X;
                this.rewardGroup.get(0).target_y = y;
                this.rewardGroup.get(1).target_y = y;
                this.rewardGroup.get(2).target_y = y;
                break;
            }
            case 4: {
                this.rewardGroup.get(0).target_x = Settings.WIDTH / 2.0f - AbstractCard.IMG_WIDTH * 1.5f - SpellRewardScreen.PAD_X * 1.5f;
                this.rewardGroup.get(1).target_x = Settings.WIDTH / 2.0f - AbstractCard.IMG_WIDTH / 2.0f - SpellRewardScreen.PAD_X / 2.0f;
                this.rewardGroup.get(2).target_x = Settings.WIDTH / 2.0f + AbstractCard.IMG_WIDTH / 2.0f + SpellRewardScreen.PAD_X / 2.0f;
                this.rewardGroup.get(3).target_x = Settings.WIDTH / 2.0f + AbstractCard.IMG_WIDTH * 1.5f + SpellRewardScreen.PAD_X * 1.5f;
                this.rewardGroup.get(0).target_y = y;
                this.rewardGroup.get(1).target_y = y;
                this.rewardGroup.get(2).target_y = y;
                this.rewardGroup.get(3).target_y = y;
                break;
            }
        }
        for (final AbstractCard c : this.rewardGroup) {
            c.drawScale = 0.75f;
            c.targetDrawScale = 0.75f;
            c.current_x = x;
            c.current_y = y;
        }
    }

    public void onClose() {
//        if (AbstractDungeon.topPanel.twitch.isPresent()) {
//            this.mayVote = false;
//            this.updateVote();
//        }
    }

    public void reset() {
        this.draftCount = 0;
        this.codex = false;
        this.discovery = false;
        this.draft = false;
    }

    public void skippedCards() {
        this.recordMetrics("SKIP");
    }

    public void closeFromBowlButton() {
        this.recordMetrics("Singing Bowl");
    }

//    private Optional<TwitchVoter> getVoter() {
//        return TwitchPanel.getDefaultVoter();
//    }

//    private void updateVote() {
//        if (this.getVoter().isPresent()) {
//            final TwitchVoter twitchVoter = this.getVoter().get();
//            if (this.mayVote && twitchVoter.isVotingConnected() && !this.isVoting) {
//                SpellRewardScreen.logger.info("Publishing Card Reward Vote");
//                if (this.bowlButton.isHidden()) {
//                    this.isVoting = twitchVoter.initiateSimpleNumberVote(Stream.concat((Stream<?>)Stream.of((T)SpellRewardScreen.TEXT[0]), this.rewardGroup.stream().map((Function<? super Object, ?>)AbstractCard::toString)).toArray(String[]::new), this::completeVoting);
//                }
//                else {
//                    this.isVoting = twitchVoter.initiateSimpleNumberVote(Stream.concat((Stream<?>)Stream.builder().add(SpellRewardScreen.TEXT[0]).add(SpellRewardScreen.TEXT[2]).build(), this.rewardGroup.stream().map((Function<? super Object, ?>)AbstractCard::toString)).toArray(String[]::new), this::completeVoting);
//                }
//            }
//            else if (this.isVoting && (!this.mayVote || !twitchVoter.isVotingConnected())) {
//                twitchVoter.endVoting(true);
//                this.isVoting = false;
//            }
//        }
//    }

    static {
        logger = LogManager.getLogger(SpellRewardScreen.class.getName());
        uiStrings = CardCrawlGame.languagePack.getUIString("CardRewardScreen");
        TEXT = SpellRewardScreen.uiStrings.TEXT;
        PAD_X = 40.0f * Settings.scale;
        CARD_TARGET_Y = Settings.HEIGHT * 0.45f;
//        TwitchVoter.registerListener(new TwitchVoteListener() {
//            @Override
//            public void onTwitchAvailable() {
//                AbstractDungeon.spellRewardScreen.updateVote();
//            }
//
//            @Override
//            public void onTwitchUnavailable() {
//                AbstractDungeon.spellRewardScreen.updateVote();
//            }
//        });
    }
}