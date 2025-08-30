package autoplaycharactermod.patches;

import autoplaycharactermod.cards.optionSelection.evolutionCards.*;
import autoplaycharactermod.relics.RunicEngine;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.audio.SoundMaster;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.ObtainKeyEffect;
import javassist.CtBehavior;

import java.util.ArrayList;

@SpirePatch(
        clz = ObtainKeyEffect.class,
        method = "update"
)
public class ObtainKeyEffectUpdatePatch {
    public static ArrayList<AbstractCard> getLvl1Cards() {
        ArrayList<AbstractCard> list = new ArrayList<>();
        list.add(new EvolutionOneA());
        list.add(new EvolutionOneB());
        list.add(new EvolutionOneC());
        return list;
    }
    public static ArrayList<AbstractCard> getLvl2Cards() {
        ArrayList<AbstractCard> list = new ArrayList<>();
        list.add(new EvolutionTwoA());
        list.add(new EvolutionTwoB());
        list.add(new EvolutionTwoC());
        return list;
    }
    public static ArrayList<AbstractCard> getLvl3Cards() {
        ArrayList<AbstractCard> list = new ArrayList<>();
        list.add(new EvolutionThreeA());
        list.add(new EvolutionThreeB());
        list.add(new EvolutionThreeC());
        return list;
    }

    @SpireInsertPatch(
            locator = Locator.class
    )
    public static void Insert(ObtainKeyEffect __instance) {
        if (AbstractDungeon.player.hasRelic(RunicEngine.ID)) {
            ((RunicEngine) AbstractDungeon.player.getRelic(RunicEngine.ID)).onObtainKey();
        }
    }

    private static class Locator extends SpireInsertLocator {
        @Override
        public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
            Matcher matcher = new Matcher.MethodCallMatcher(SoundMaster.class, "playA");
            return LineFinder.findInOrder(ctMethodToPatch, matcher);
        }
    }
}