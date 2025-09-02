package autoplaycharactermod.util;

import autoplaycharactermod.BasicMod;
import com.badlogic.gdx.Input;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.input.InputAction;
import com.megacrit.cardcrawl.helpers.input.InputActionSet;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.screens.options.InputSettingsScreen;
import com.megacrit.cardcrawl.screens.options.RemapInputElement;
import javassist.CtBehavior;

import java.util.ArrayList;

public class Hotkeys {
    private static final String PLAY_KEY = BasicMod.makeID("PLAY");
    private static final String SCRY_KEY = BasicMod.makeID("SCRY");
    public static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(BasicMod.makeID("CUSTOMIZE"));

    public static class ActionSet {


        public static InputAction PlayButton;
        public static InputAction ScryButton;

        public static void load() {
            PlayButton = new InputAction(InputActionSet.prefs.getInteger(PLAY_KEY, Input.Keys.Q));
            ScryButton = new InputAction(InputActionSet.prefs.getInteger(SCRY_KEY, Input.Keys.W));
        }

        public static void save() {
            InputActionSet.prefs.putInteger(PLAY_KEY, PlayButton.getKey());
            InputActionSet.prefs.putInteger(SCRY_KEY, ScryButton.getKey());
        }

        public static void resetToDefault() {
            PlayButton.remap(Input.Keys.Q);
            ScryButton.remap(Input.Keys.W);
        }
    }

    @SpirePatch2(
            clz = InputSettingsScreen.class,
            method = "refreshData"
    )
    public static class SettingsScreen {
        @SpireInsertPatch(
                locator = Locator.class,
                localvars = {"elements"}
        )
        public static void insert(InputSettingsScreen __instance, ArrayList<RemapInputElement> elements) {
            if (!Settings.isControllerMode) {
                elements.add(new RemapInputElement(__instance, uiStrings.TEXT[0], ActionSet.PlayButton));
                elements.add(new RemapInputElement(__instance, uiStrings.TEXT[1], ActionSet.ScryButton));
            }
        }

        private static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctBehavior) throws Exception {
                Matcher finalMatcher = new Matcher.FieldAccessMatcher(InputSettingsScreen.class, "maxScrollAmount");
                return LineFinder.findInOrder(ctBehavior, finalMatcher);
            }
        }
    }

    private static class ActionSetPatches {

        @SpirePatch2(
                clz = InputActionSet.class,
                method = "load"
        )
        public static class Load {
            @SpirePrefixPatch
            public static void prefix() {
                ActionSet.load();
            }
        }

        @SpirePatch2(
                clz = InputActionSet.class,
                method = "save"
        )
        public static class Save {
            @SpirePrefixPatch
            public static void prefix() {
                ActionSet.save();
            }
        }

        @SpirePatch2(
                clz = InputActionSet.class,
                method = "resetToDefaults"
        )
        public static class Reset {
            @SpirePrefixPatch
            public static void prefix() {
                ActionSet.resetToDefault();
            }
        }
    }
}
