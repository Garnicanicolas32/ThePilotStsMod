package autoplaycharactermod.util;

import autoplaycharactermod.ThePilotMod;
import com.badlogic.gdx.Input;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.controller.CInputAction;
import com.megacrit.cardcrawl.helpers.controller.CInputActionSet;
import com.megacrit.cardcrawl.helpers.input.InputAction;
import com.megacrit.cardcrawl.helpers.input.InputActionSet;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.screens.options.InputSettingsScreen;
import com.megacrit.cardcrawl.screens.options.RemapInputElement;
import javassist.CtBehavior;

import java.util.ArrayList;

public class Hotkeys {
    private static final String PLAY_KEY = ThePilotMod.makeID("PLAY");
    private static final String SCRY_KEY = ThePilotMod.makeID("SCRY");
    private static final String PLAY_KEY_Controller = ThePilotMod.makeID("PLAYC");
    private static final String SCRY_KEY_Controller = ThePilotMod.makeID("SCRYC");
    public static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(ThePilotMod.makeID("CUSTOMIZE"));

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

    public static class CActionSet {
        public static CInputAction PlayButton;
        public static CInputAction ScryButton;

        public static void load() {
            PlayButton = new CInputAction(InputActionSet.prefs.getInteger(PLAY_KEY_Controller, 8));
            ScryButton = new CInputAction(InputActionSet.prefs.getInteger(SCRY_KEY_Controller, 9));
        }

        public static void save() {
            CInputActionSet.prefs.putInteger(PLAY_KEY_Controller, PlayButton.getKey());
            CInputActionSet.prefs.putInteger(SCRY_KEY_Controller, ScryButton.getKey());
        }

        public static void resetToDefault() {
            PlayButton.remap(8);
            ScryButton.remap(9);
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
            elements.add(new RemapInputElement(__instance, uiStrings.TEXT[0], ActionSet.PlayButton, CActionSet.PlayButton));
            elements.add(new RemapInputElement(__instance, uiStrings.TEXT[1], ActionSet.ScryButton, CActionSet.ScryButton));
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

    private static class cActionSetPatches {
        @SpirePatch2(
                clz = CInputActionSet.class,
                method = "load"
        )
        public static class Load {
            @SpirePrefixPatch
            public static void prefix() {
                CActionSet.load();
            }
        }

        @SpirePatch2(
                clz = CInputActionSet.class,
                method = "save"
        )
        public static class Save {
            @SpirePrefixPatch
            public static void prefix() {
                CActionSet.save();
            }
        }

        @SpirePatch2(
                clz = CInputActionSet.class,
                method = "resetToDefaults"
        )
        public static class Reset {
            @SpirePrefixPatch
            public static void prefix() {
                CActionSet.resetToDefault();
            }
        }
    }
}
