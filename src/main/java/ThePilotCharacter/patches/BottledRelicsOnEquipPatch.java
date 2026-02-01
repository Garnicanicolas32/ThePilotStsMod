package ThePilotCharacter.patches;

import ThePilotCharacter.character.PilotCharacter;
import ThePilotCharacter.util.PlayTurnStartModifier;
import basemod.helpers.CardModifierManager;
import com.evacipated.cardcrawl.modthespire.lib.ByRef;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.LocalizedStrings;
import com.megacrit.cardcrawl.relics.BottledFlame;
import com.megacrit.cardcrawl.relics.BottledLightning;
import com.megacrit.cardcrawl.relics.BottledTornado;
import com.megacrit.cardcrawl.relics.UnceasingTop;
import com.megacrit.cardcrawl.rooms.AbstractRoom;

public class BottledRelicsOnEquipPatch {
    @SpirePatch(
            clz = BottledFlame.class,
            method = "onEquip"
    )
    public static class patchBottledFlame {
        @SpirePrefixPatch
        public static SpireReturn<Void> Prefix(BottledFlame __instance, @ByRef boolean[] ___cardSelected) {
            if (AbstractDungeon.player instanceof PilotCharacter) {
                CardGroup list = AbstractDungeon.player.masterDeck.getPurgeableCards().getAttacks();
                list.group.removeIf(c -> c.isInnate || CardModifierManager.hasModifier(c, PlayTurnStartModifier.ID));
                if (!list.isEmpty()) {
                    ___cardSelected[0] = false;
                    if (AbstractDungeon.isScreenUp) {
                        AbstractDungeon.dynamicBanner.hide();
                        AbstractDungeon.overlayMenu.cancelButton.hide();
                        AbstractDungeon.previousScreen = AbstractDungeon.screen;
                    }
                    AbstractDungeon.getCurrRoom().phase = AbstractRoom.RoomPhase.INCOMPLETE;
                    AbstractDungeon.gridSelectScreen.open(list, 1, __instance.DESCRIPTIONS[1] + __instance.name + LocalizedStrings.PERIOD, false, false, false, false);
                }
                return SpireReturn.Return();
            } else
                return SpireReturn.Continue();
        }
    }

    @SpirePatch(
            clz = BottledLightning.class,
            method = "onEquip"
    )
    public static class patchBottledLightning {
        @SpirePrefixPatch
        public static SpireReturn<Void> Prefix(BottledLightning __instance, @ByRef boolean[] ___cardSelected) {
            if (AbstractDungeon.player instanceof PilotCharacter) {
                CardGroup list = AbstractDungeon.player.masterDeck.getPurgeableCards().getSkills();
                list.group.removeIf(c -> c.isInnate || CardModifierManager.hasModifier(c, PlayTurnStartModifier.ID));
                if (!list.isEmpty()) {
                    ___cardSelected[0] = false;
                    if (AbstractDungeon.isScreenUp) {
                        AbstractDungeon.dynamicBanner.hide();
                        AbstractDungeon.overlayMenu.cancelButton.hide();
                        AbstractDungeon.previousScreen = AbstractDungeon.screen;
                    }
                    AbstractDungeon.getCurrRoom().phase = AbstractRoom.RoomPhase.INCOMPLETE;
                    AbstractDungeon.gridSelectScreen.open(list, 1, __instance.DESCRIPTIONS[1] + __instance.name + LocalizedStrings.PERIOD, false, false, false, false);
                }
                return SpireReturn.Return();
            } else
                return SpireReturn.Continue();
        }
    }

    @SpirePatch(
            clz = BottledTornado.class,
            method = "onEquip"
    )
    public static class patchBottledTornado {
        @SpirePrefixPatch
        public static SpireReturn<Void> Prefix(BottledTornado __instance, @ByRef boolean[] ___cardSelected) {
            if (AbstractDungeon.player instanceof PilotCharacter) {
                CardGroup list = AbstractDungeon.player.masterDeck.getPurgeableCards().getPowers();
                list.group.removeIf(c -> c.isInnate || CardModifierManager.hasModifier(c, PlayTurnStartModifier.ID));
                if (!list.isEmpty()) {
                    ___cardSelected[0] = false;
                    if (AbstractDungeon.isScreenUp) {
                        AbstractDungeon.dynamicBanner.hide();
                        AbstractDungeon.overlayMenu.cancelButton.hide();
                        AbstractDungeon.previousScreen = AbstractDungeon.screen;
                    }
                    AbstractDungeon.getCurrRoom().phase = AbstractRoom.RoomPhase.INCOMPLETE;
                    AbstractDungeon.gridSelectScreen.open(list, 1, __instance.DESCRIPTIONS[1] + __instance.name + LocalizedStrings.PERIOD, false, false, false, false);
                }
                return SpireReturn.Return();
            } else
                return SpireReturn.Continue();
        }
    }

    @SpirePatch(
            clz = UnceasingTop.class,
            method = "atTurnStart"
    )
    public static class patchUnceasingTop {
        @SpirePrefixPatch
        public static SpireReturn<Void> Prefix(UnceasingTop __instance, @ByRef boolean[] ___canDraw) {
            if (AbstractDungeon.player instanceof PilotCharacter) {
                ___canDraw[0] = false;
                return SpireReturn.Return();
            } else
                return SpireReturn.Continue();
        }
    }
}

