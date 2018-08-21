package mysticmod.cards;

import basemod.abstracts.CustomCard;
import basemod.helpers.TooltipInfo;
import com.megacrit.cardcrawl.cards.AbstractCard;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractMysticCard extends CustomCard {
    public boolean isSpell = false;
    public boolean isTechnique = false;

    public AbstractMysticCard(final String id, final String name, final String img, final int cost, final String rawDescription,
                              final AbstractCard.CardType type, final AbstractCard.CardColor color,
                              final AbstractCard.CardRarity rarity, final AbstractCard.CardTarget target) {
        super(id, name, img, cost, rawDescription, type, color, rarity, target);
    }

    public boolean isSpell() {
        return this.isSpell;
    }

    @Override
    public List<TooltipInfo> getCustomTooltips() {
        List<TooltipInfo> retVal = new ArrayList<>();
        if (this.type == AbstractCard.CardType.SKILL) {
            if (this.isSpell) {
                retVal.add(new TooltipInfo("Spell.", "This Skill is considered a Spell."));
            } else if (this.isTechnique) {
                retVal.add(new TooltipInfo("Arte.", "This Skill is considered an Arte."));
            }
        } else if (this.type == AbstractCard.CardType.ATTACK) {
            if (this.isSpell) {
                retVal.add(new TooltipInfo("Spell.", "This Attack is considered a Spell."));
            } else if (this.isTechnique) {
                retVal.add(new TooltipInfo("Arte.", "This Attack is considered an Arte."));
            }
        }
        return retVal;
    }

    public boolean isTechnique() {
        return this.isTechnique;
    }

    public void upgradeToSpell() {
        this.isSpell = true;
    }

    public void upgradeToTechnique() {
        this.isTechnique = true;
    }
}
