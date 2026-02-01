package ThePilotCharacter.patches;

import ThePilotCharacter.character.PilotCharacter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireField;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.core.OverlayMenu;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.ui.buttons.EndTurnButton;

public class OverlayMenuButtonPatch {
    private static boolean shownThisCombat;

    public static boolean shouldShowButton() {
        return shownThisCombat || AbstractDungeon.player instanceof PilotCharacter;
    }

    @SpirePatch2(clz = OverlayMenu.class, method = SpirePatch.CLASS)
    public static class DrawButton {
        public static final SpireField<ThePilotCharacter.ui.DrawButton> button = new SpireField<>(ThePilotCharacter.ui.DrawButton::new);
    }

    @SpirePatch2(clz = OverlayMenu.class, method = SpirePatch.CLASS)
    public static class ScryButton {
        public static final SpireField<ThePilotCharacter.ui.ScryButton> button = new SpireField<>(ThePilotCharacter.ui.ScryButton::new);
    }

    @SpirePatch2(clz = GameActionManager.class, method = "clear")
    public static class ResetShownState {
        @SpirePrefixPatch
        public static void reset() {
            shownThisCombat = false;
        }
    }

    @SpirePatch2(clz = OverlayMenu.class, method = "update")
    public static class UpdateAuraButton {
        @SpirePrefixPatch
        public static void plz(OverlayMenu __instance) {
            DrawButton.button.get(__instance).update();
            ScryButton.button.get(__instance).update();
        }
    }

    @SpirePatch2(clz = OverlayMenu.class, method = "render")
    public static class RenderAuraButton {
        @SpirePrefixPatch
        public static void plz(OverlayMenu __instance, SpriteBatch sb) {
            DrawButton.button.get(__instance).render(sb);
            ScryButton.button.get(__instance).render(sb);
        }
    }

    @SpirePatch2(clz = OverlayMenu.class, method = "showCombatPanels")
    public static class ShowAuraButton {
        @SpirePrefixPatch
        public static void plz(OverlayMenu __instance) {
            if (shouldShowButton()) {
                DrawButton.button.get(__instance).show();
                ScryButton.button.get(__instance).show();
                shownThisCombat = true;
            }
        }
    }

    @SpirePatch2(clz = OverlayMenu.class, method = "hideCombatPanels")
    public static class HideAuraButton {
        @SpirePrefixPatch
        public static void plz(OverlayMenu __instance) {
            DrawButton.button.get(__instance).hide();
            ScryButton.button.get(__instance).hide();
        }
    }

    @SpirePatch2(clz = EndTurnButton.class, method = "enable")
    public static class EnableAuraButton {
        @SpirePrefixPatch
        public static void plz() {
            DrawButton.button.get(AbstractDungeon.overlayMenu).enable();
            ScryButton.button.get(AbstractDungeon.overlayMenu).enable();
        }
    }

    @SpirePatch2(clz = EndTurnButton.class, method = "disable", paramtypez = {})
    @SpirePatch2(clz = EndTurnButton.class, method = "disable", paramtypez = {boolean.class})
    public static class DisableAuraButton {
        @SpirePrefixPatch
        public static void plz() {
            DrawButton.button.get(AbstractDungeon.overlayMenu).disable();
            ScryButton.button.get(AbstractDungeon.overlayMenu).disable();
        }
    }
}