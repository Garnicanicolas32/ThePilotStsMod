package autoplaycharactermod.patches;

import autoplaycharactermod.cards.BaseCard;
import autoplaycharactermod.cards.EquipmentCard;
import autoplaycharactermod.relics.CoolingFan;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.common.ExhaustSpecificCardAction;
import com.megacrit.cardcrawl.actions.utility.ScryAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import javassist.CtBehavior;

import java.util.ArrayList;

@SpirePatch(
        clz = ScryAction.class,
        method = "update"
)
public class ScryActionUpdatePatch {
    @SpireInsertPatch(
            locator = LocatorOnScry.class
    )
    public static void OnScryInHand(ScryAction __instance) {
        for (AbstractCard c : AbstractDungeon.player.hand.group) {
            if (c instanceof EquipmentCard) {
                ((EquipmentCard) c).onBetterScry();
            }
        }
    }

    @SpireInsertPatch(
            locator = LocatorSelectedOnScry.class,
            localvars = {"c"}
    )
    public static void InsertOnScrySelected(ScryAction __instance, AbstractCard c) {
        if (c instanceof BaseCard) {
            ((BaseCard) c).onScrySelected();
        }
    }

    @SpireInsertPatch(
            locator = LocatorHasRelic.class
    )
    public static void InsertHasRelic(ScryAction __instance) {
        if (AbstractDungeon.player.hasRelic(CoolingFan.ID)) {
            boolean purged = false;
            for (AbstractCard c : AbstractDungeon.gridSelectScreen.selectedCards) {
                if (c.type == AbstractCard.CardType.STATUS) {
                    AbstractDungeon.player.discardPile.moveToExhaustPile(c);
                    purged = true;
                }
            }
            if (purged){
                AbstractDungeon.player.getRelic(CoolingFan.ID).flash();
            }
        }
    }

    private static class LocatorOnScry extends SpireInsertLocator {
        @Override
        public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
            Matcher matcher = new Matcher.FieldAccessMatcher(AbstractPlayer.class, "powers");
            return LineFinder.findInOrder(ctMethodToPatch, matcher);
        }
    }

    private static class LocatorSelectedOnScry extends SpireInsertLocator {
        @Override
        public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
            Matcher matcher = new Matcher.MethodCallMatcher(CardGroup.class, "moveToDiscardPile");
            return LineFinder.findInOrder(ctMethodToPatch, matcher);
        }
    }

    private static class LocatorHasRelic extends SpireInsertLocator {
        @Override
        public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
            Matcher matcher = new Matcher.MethodCallMatcher(ArrayList.class, "clear");
            return LineFinder.findInOrder(ctMethodToPatch, matcher);
        }
    }
}
