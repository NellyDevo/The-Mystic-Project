package mysticmod.actions;

import com.megacrit.cardcrawl.actions.*;
import com.megacrit.cardcrawl.dungeons.*;
import com.megacrit.cardcrawl.cards.*;
import mysticmod.cards.HeavyStrike;

import java.util.*;

public class IncreaseMiscDamageAction extends AbstractGameAction
{
    private int miscVal;
    private int miscIncrease;
    private String id;

    public IncreaseMiscDamageAction(final String id, final int miscValue, final int miscIncrease) {
        this.miscIncrease = miscIncrease;
        this.miscVal = miscValue;
        this.id = id;
    }

    @Override
    public void update() {
        boolean success = false;
        for (final AbstractCard c : AbstractDungeon.player.masterDeck.group) {
            if (c.cardID.equals(this.id) && c.misc == this.miscVal) {
                c.misc += this.miscIncrease;
                c.baseDamage = c.misc;
                c.damage = c.baseDamage;
                c.isDamageModified = false;
                c.initializeDescription();
                success = true;
                break;
            }
        }
        if (success) {
            System.out.println("Success!");
        }
        this.isDone = true;
    }
}
