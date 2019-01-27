package mysticmod.cards.cantrips;

import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import mysticmod.cards.AbstractMysticCard;
import mysticmod.patches.MysticTags;
import mysticmod.powers.SpellsPlayed;
import mysticmod.relics.BentSpoon;

public abstract class AbstractCantrip extends AbstractMysticCard {

    public AbstractCantrip(String id, String name, String img, int cost, String rawDescription,
                           CardType type, CardColor color,
                           CardRarity rarity, CardTarget target) {
        super(id, name, img, cost, rawDescription, type, color, rarity, target);
        tags.add(MysticTags.IS_CANTRIP);
    }

    @Override
    public boolean isSpell() {
        boolean retVal = (AbstractDungeon.player == null || (!AbstractDungeon.player.hasPower(SpellsPlayed.POWER_ID)
                || AbstractDungeon.player.getPower(SpellsPlayed.POWER_ID).amount <= 1)); //controls how many much power you can have before cantrips no longer count as spells
        if (retVal) {
            if (!hasTag(MysticTags.IS_SPELL)) {
                tags.add(MysticTags.IS_SPELL);
            }
        } else {
            if (hasTag(MysticTags.IS_SPELL)) {
                tags.remove(MysticTags.IS_SPELL);
            }
        }
        return retVal;
    }

    @Override
    public void applyPowers() {
        if (AbstractDungeon.player.hasRelic(BentSpoon.ID)) {
            int baseDamagePlaceholder = baseDamage;
            int baseBlockPlaceholder = baseBlock;
            baseDamage += 1;
            baseBlock += 1;
            super.applyPowers();
            baseDamage = baseDamagePlaceholder;
            baseBlock = baseBlockPlaceholder;
            isDamageModified = damage != baseDamage;
            isBlockModified = block != baseBlock;
        } else {
            super.applyPowers();
        }
    }

    @Override
    public void calculateCardDamage(AbstractMonster mo) {
        if (AbstractDungeon.player.hasRelic(BentSpoon.ID)) {
            int baseDamagePlaceholder = baseDamage;
            int baseBlockPlaceholder = baseBlock;
            baseDamage += 1;
            baseBlock += 1;
            super.calculateCardDamage(mo);
            baseDamage = baseDamagePlaceholder;
            baseBlock = baseBlockPlaceholder;
            isDamageModified = damage != baseDamage;
            isBlockModified = block != baseBlock;
        } else {
            super.calculateCardDamage(mo);
        }
    }
}
