package mysticmod.cards.cantrips;

import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import mysticmod.cards.AbstractMysticCard;
import mysticmod.patches.MysticTags;
import mysticmod.powers.SpellsPlayed;
import mysticmod.relics.BentSpoon;

public abstract class AbstractCantrip extends AbstractMysticCard {

    public AbstractCantrip(final String id, final String name, final String img, final int cost, final String rawDescription,
                           final CardType type, final CardColor color,
                           final CardRarity rarity, final CardTarget target) {
        super(id, name, img, cost, rawDescription, type, color, rarity, target);
        this.tags.add(MysticTags.IS_CANTRIP);
    }

    @Override
    public boolean isSpell() {
        boolean retVal = (AbstractDungeon.player == null || (!AbstractDungeon.player.hasPower(SpellsPlayed.POWER_ID)
                || AbstractDungeon.player.getPower(SpellsPlayed.POWER_ID).amount <= 2));
        if (retVal) {
            if (!this.hasTag(MysticTags.IS_SPELL)) {
                this.tags.add(MysticTags.IS_SPELL);
            }
        } else {
            if (this.hasTag(MysticTags.IS_SPELL)) {
                this.tags.remove(MysticTags.IS_SPELL);
            }
        }
        return retVal;
    }

    @Override
    public void applyPowers() {
        if (AbstractDungeon.player.hasRelic(BentSpoon.ID)) {
            int baseDamagePlaceholder = this.baseDamage;
            int baseBlockPlaceholder = this.baseBlock;
            this.baseDamage += 1;
            this.baseBlock += 1;
            super.applyPowers();
            this.baseDamage = baseDamagePlaceholder;
            this.baseBlock = baseBlockPlaceholder;
            if (this.damage != this.baseDamage) {
                this.isDamageModified = true;
            }
            if (this.block != this.baseBlock) {
                this.isBlockModified = true;
            }
        } else {
            super.applyPowers();
        }
    }

    @Override
    public void calculateCardDamage(final AbstractMonster mo) {
        if (AbstractDungeon.player.hasRelic(BentSpoon.ID)) {
            int baseDamagePlaceholder = this.baseDamage;
            int baseBlockPlaceholder = this.baseBlock;
            this.baseDamage += 1;
            this.baseBlock += 1;
            super.calculateCardDamage(mo);
            this.baseDamage = baseDamagePlaceholder;
            this.baseBlock = baseBlockPlaceholder;
            if (this.damage != this.baseDamage) {
                this.isDamageModified = true;
            }
            if (this.block != this.baseBlock) {
                this.isBlockModified = true;
            }
        } else {
            super.calculateCardDamage(mo);
        }
    }
}