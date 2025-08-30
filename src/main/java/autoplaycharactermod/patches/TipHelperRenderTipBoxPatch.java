package autoplaycharactermod.patches;

import basemod.ReflectionHacks;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.TipHelper;
import javassist.CtBehavior;

public class TipHelperRenderTipBoxPatch {
    @SpirePatch(
            clz = TipHelper.class,
            method = "renderTipBox",
            paramtypez = {float.class, float.class, SpriteBatch.class, String.class, String.class}
    )
    public static class TipHelperRenderTipBoxColorPatch { //So that was a lie

        @SpireInsertPatch(locator = Locator.class)
        public static void Insert(float x, float y, SpriteBatch sb, String title, String description) {
            if (MyTooltipColor.overrideColor != null) {
                Color baseColor = ReflectionHacks.getPrivateStatic(TipHelper.class, "BASE_COLOR");
                baseColor.set(MyTooltipColor.overrideColor);
            }
        }

        @SpirePostfixPatch
        public static void After(float x, float y, SpriteBatch sb, String title, String description) {
            if (MyTooltipColor.overrideColor != null) {
                Color baseColor = ReflectionHacks.getPrivateStatic(TipHelper.class, "BASE_COLOR");
                baseColor.set(new Color(1.0F, 0.9725F, 0.8745F, 1.0F)); // COPIED FROM VANILLA DONT TOUCH
                MyTooltipColor.clear();
            }
        }

        public static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(
                        FontHelper.class, "renderSmartText"
                );
                return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
            }
        }
    }

    public static class MyTooltipColor {
        public static Color overrideColor = null;

        public static void setOverride(Color color) {
            overrideColor = color;
        }

        public static void clear() {
            overrideColor = null;
        }
    }
}
