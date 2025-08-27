package autoplaycharactermod.patches;

import autoplaycharactermod.cards.EquipmentCard;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.common.EmptyDeckShuffleAction;
import com.megacrit.cardcrawl.actions.common.ShuffleAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import javassist.CtBehavior;

import static basemod.BaseMod.logger;

public class TriggerOnShufflePatches {
    @SpirePatch(
            clz = ShuffleAction.class,
            method = "update"
    )
    public static class OnShuffleTriggerPatch {
        @SpireInsertPatch(
                locator = OnShuffleTriggerPatch.Locator.class
        )
        public static void Insert(ShuffleAction __instance, boolean ___triggerRelics) {
                for (AbstractCard c : AbstractDungeon.player.hand.group) {
                    if (c instanceof EquipmentCard) {
                        ((EquipmentCard) c).triggerOnShuffle();
                    }
                }
        }

        private static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher matcher = new Matcher.FieldAccessMatcher(AbstractDungeon.class, "player");
                return LineFinder.findInOrder(ctMethodToPatch, matcher);
            }
        }
    }

    @SpirePatch(
            clz = EmptyDeckShuffleAction.class,
            method = SpirePatch.CONSTRUCTOR
    )
    public static class OnShuffleTriggerEmptyPatch {
        @SpireInsertPatch(
                locator = OnShuffleTriggerEmptyPatch.Locator.class
        )
        public static void Insert(EmptyDeckShuffleAction __instance) {
            for (AbstractCard c : AbstractDungeon.player.hand.group) {
                if (c instanceof EquipmentCard) {
                    ((EquipmentCard) c).triggerOnShuffle();
                }
            }
        }

        private static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher matcher = new Matcher.FieldAccessMatcher(AbstractDungeon.class, "player");
                return LineFinder.findInOrder(ctMethodToPatch, matcher);
            }
        }
    }
}
