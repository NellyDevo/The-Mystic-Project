package MysticMod.Patches;

import com.evacipated.cardcrawl.modthespire.lib.SpireField;
import com.megacrit.cardcrawl.screens.CardRewardScreen;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;

@SpirePatch(cls="com.megacrit.cardcrawl.screens.CardRewardScreen", method=SpirePatch.CLASS)
public class IsDiscoveryLookingFor
{
    public static SpireField<String> Spells = new SpireField<>("False");
    public static SpireField<String> Techniques = new SpireField<>("False");
}