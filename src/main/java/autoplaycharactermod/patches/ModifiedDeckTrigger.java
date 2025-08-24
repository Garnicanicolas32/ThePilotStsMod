package autoplaycharactermod.patches;

import autoplaycharactermod.BasicMod;
import autoplaycharactermod.cards.equipment.TrashCannon;
import autoplaycharactermod.cards.traitScavengeCards.NFT;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.audio.SoundMaster;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.FastCardObtainEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;
import javassist.CtBehavior;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

public class ModifiedDeckTrigger {
    private static void CheckNFT(AbstractCard card) {
        if (card instanceof NFT || Objects.equals(card.cardID, NFT.ID)) {
            BasicMod.checkNFT();
        }
    }

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
            clz = ShowCardAndObtainEffect.class,
            method = SpirePatch.CONSTRUCTOR,
            paramtypez = {AbstractCard.class, float.class, float.class, boolean.class}
    )
    public static class OnAddMasterCardPatch {
        @SpireInsertPatch(
                locator = LocatorShowCardAndObtainEffect.class
        )
        public static void Insert(ShowCardAndObtainEffect __instance, AbstractCard card, float x, float y, boolean convergeCards) {
            CheckNFT(card);
        }

        private static class LocatorShowCardAndObtainEffect extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher matcher = new Matcher.FieldAccessMatcher(AbstractCard.class, "targetDrawScale");
                return LineFinder.findInOrder(ctMethodToPatch, matcher);
            }
        }
    }

    @SpirePatch(
            clz = FastCardObtainEffect.class,
            method = SpirePatch.CONSTRUCTOR,
            paramtypez = {AbstractCard.class, float.class, float.class}
    )
    public static class OnFastAddMasterCardPatch {
        @SpireInsertPatch(
                locator = LocatorFastCardObtainEffect.class
        )
        public static void Insert(FastCardObtainEffect __instance, AbstractCard card, float x, float y) {
            CheckNFT(card);
        }

        private static class LocatorFastCardObtainEffect extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher matcher = new Matcher.MethodCallMatcher(SoundMaster.class, "play");
                return LineFinder.findInOrder(ctMethodToPatch, matcher);
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
            CheckNFT(c);
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
            if (Objects.equals(targetID, NFT.ID)) {
                BasicMod.checkNFT();
            }
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
