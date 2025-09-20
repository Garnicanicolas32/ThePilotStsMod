package autoplaycharactermod.patches;

import autoplaycharactermod.cards.equipment.TacticalVisor;
import autoplaycharactermod.character.PilotCharacter;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import javassist.CtBehavior;

import java.util.ArrayList;

@SpirePatch(clz = CardGroup.class, method = "initializeDeck")
public class CardGroupInitializeDeckPatch {
    @SpireInsertPatch(locator = LocatorInnate.class, localvars = {"placeOnTop"})
    public static SpireReturn<Void> InsertInnate(CardGroup __instance, CardGroup masterDeck, @ByRef ArrayList<AbstractCard>[] placeOnTop) {
        if (AbstractDungeon.player instanceof PilotCharacter) {
            ((PilotCharacter) AbstractDungeon.player).innateAmount = placeOnTop[0].size();
            for (AbstractCard c : placeOnTop[0]){
                if (c instanceof TacticalVisor){
                    ((TacticalVisor)c).unused = true;
                }
            }
            placeOnTop[0].clear();
            return SpireReturn.Return();
        }
        return SpireReturn.Continue();
    }

    private static class LocatorInnate extends SpireInsertLocator {
        public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
            Matcher.MethodCallMatcher methodCallMatcher = new Matcher.MethodCallMatcher(ArrayList.class, "size");
            return LineFinder.findInOrder(ctMethodToPatch, methodCallMatcher);
        }
    }
}