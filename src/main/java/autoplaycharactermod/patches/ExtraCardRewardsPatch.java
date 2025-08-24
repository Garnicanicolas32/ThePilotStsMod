package autoplaycharactermod.patches;

import autoplaycharactermod.BasicMod;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.saveAndContinue.SaveFile;
import com.megacrit.cardcrawl.screens.CombatRewardScreen;
import javassist.CtBehavior;

import java.util.ArrayList;


public class ExtraCardRewardsPatch {
    public static boolean calledonce = false;
    @SpirePatch(
            clz = CombatRewardScreen.class,
            method = "setupItemReward"
    )
    public static class spawnrewards {
        @SpireInsertPatch(
                locator = Locator.class
        )
        public static void Insert(CombatRewardScreen __instance, @ByRef ArrayList<RewardItem>[] ___rewards) {
            for (int i = 0; i < BasicMod.extracards; i++) {
                RewardItem cardReward = new RewardItem();
                if (!cardReward.cards.isEmpty())
                    ___rewards[0].add(cardReward);
            }
        }

        private static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher matcher = new Matcher.MethodCallMatcher(AbstractPlayer.class, "hasRelic");
                return LineFinder.findInOrder(ctMethodToPatch, matcher);
            }
        }
    }

    @SpirePatch(
            clz = AbstractDungeon.class,
            method = "nextRoomTransition",
            paramtypez = {
                    SaveFile.class
            }
    )
    public static class resetExtraCards {
        @SpireInsertPatch(
                locator = LocatorDungeon.class
        )
        public static void Insert(AbstractDungeon __instance, SaveFile saveFile) {
            calledonce = false;
            BasicMod.extracards = 0;
            BasicMod.extracardsoption = false;
        }

        private static class LocatorDungeon extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher matcher = new Matcher.FieldAccessMatcher(AbstractPlayer.class, "relics");
                return LineFinder.findInOrder(ctMethodToPatch, matcher);
            }
        }
    }

    @SpirePatch(
            clz = AbstractDungeon.class,
            method = "getRewardCards"
    )
    public static class scavengePlusOne {
        @SpireInsertPatch(
                locator = LocatorDungeon.class,
                localvars = {"numCards"}
        )
        public static void Insert(@ByRef int[] numCards) {
            System.out.println("Triggered extra card: " + BasicMod.extracardsoption);
            if (!calledonce && BasicMod.extracardsoption)
                numCards[0]++;
            calledonce = true;
        }

        private static class LocatorDungeon extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher matcher = new Matcher.FieldAccessMatcher(AbstractPlayer.class, "relics");
                return LineFinder.findInOrder(ctMethodToPatch, matcher);
            }
        }
    }
}


