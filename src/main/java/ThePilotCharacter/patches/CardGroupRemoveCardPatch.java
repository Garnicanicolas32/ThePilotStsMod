package ThePilotCharacter.patches;

import ThePilotCharacter.cards.equipment.TrashCannon;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import javassist.CtBehavior;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class CardGroupRemoveCardPatch {
    private static void triggerTrashCannon() {
        List<CardGroup> groups = Arrays.asList(
                AbstractDungeon.player.masterDeck,
                AbstractDungeon.player.hand,
                AbstractDungeon.player.discardPile,
                AbstractDungeon.player.drawPile
        );
        for (CardGroup group : groups) {
            for (AbstractCard c : group.group) {
                if (c instanceof TrashCannon) {
                    ((TrashCannon) c).AddStack();
                }
            }
        }
    }

    @SpirePatch(
            clz = CardGroup.class,
            method = "removeCard",
            paramtypez = {AbstractCard.class}

    )
    public static class OnRemoveMasterCardPatch {

        @SpireInsertPatch(
                locator = Locator.class
        )
        public static void Insert(CardGroup __instance, AbstractCard c) {
            triggerTrashCannon();
        }

        private static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher matcher = new Matcher.MethodCallMatcher(AbstractCard.class, "onRemoveFromMasterDeck");
                return LineFinder.findInOrder(ctMethodToPatch, matcher);
            }
        }
    }

    @SpirePatch(
            clz = CardGroup.class,
            method = "removeCard",
            paramtypez = {String.class}
    )
    public static class OnRemoveMasterCardPatch2 {

        @SpireInsertPatch(
                locator = Locator.class
        )
        public static void Insert(CardGroup __instance, String targetID) {
            triggerTrashCannon();
        }

        private static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher matcher = new Matcher.MethodCallMatcher(Iterator.class, "remove");
                return LineFinder.findInOrder(ctMethodToPatch, matcher);
            }
        }
    }
}
