package autoplaycharactermod.patches;

import autoplaycharactermod.character.MyCharacter;
import autoplaycharactermod.relics.reworks.NecronomiconPDF;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.city.CursedTome;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.relics.Necronomicon;
import javassist.CtBehavior;

import java.util.ArrayList;

@SpirePatch(
        clz = CursedTome.class,
        method = "randomBook"
)
public class CursedTomeRandomBookPatch {
    @SpireInsertPatch(
            locator = Locator.class,
            localvars = {"possibleBooks"}
    )
    public static void Insert(CursedTome __instance, @ByRef ArrayList<AbstractRelic>[] possibleBooks) {
        if (AbstractDungeon.player instanceof MyCharacter) {
            possibleBooks[0].removeIf(r -> r instanceof Necronomicon);
            possibleBooks[0].add(new NecronomiconPDF());
        }
    }

    private static class Locator extends SpireInsertLocator {
        @Override
        public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
            Matcher matcher = new Matcher.MethodCallMatcher(ArrayList.class, "get");
            return LineFinder.findInOrder(ctMethodToPatch, matcher);
        }
    }
}
