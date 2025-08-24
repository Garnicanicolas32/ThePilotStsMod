package autoplaycharactermod.patches;

import autoplaycharactermod.BasicMod;
import autoplaycharactermod.character.MyCharacter;
import basemod.BaseMod;
import basemod.abstracts.CustomCard;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.screens.SingleCardViewPopup;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Objects;

public class AugmentCardsBGPATCH {
    @SpirePatch(
            clz= AbstractCard.class,
            method="renderCardBg"
    )
    public static class RenderBGevolutionCorrect {
        public static SpireReturn<?> Prefix(AbstractCard __instance, SpriteBatch sb, float xPos, float yPos, Color ___renderColor)
        {
            if (!(__instance.hasTag(BasicMod.CustomTags.Evolution))
            ) {
                return SpireReturn.Continue();
            }
            AbstractCard.CardColor color = __instance.color;
            CustomCard card = (CustomCard) __instance;
            Texture texture = null;
            TextureAtlas.AtlasRegion region = null;

            if (card.textureBackgroundSmallImg != null && !card.textureBackgroundSmallImg.isEmpty()) {
                texture = card.getBackgroundSmallTexture();
            }
            else
            {
                if (Objects.requireNonNull(card.type) == AbstractCard.CardType.POWER) {
                    if (BaseMod.getPowerBgTexture(color) == null) {
                        BaseMod.savePowerBgTexture(color, ImageMaster.loadImage(BaseMod.getPowerBg(color)));
                    }
                    texture = BaseMod.getPowerBgTexture(color);
                } else {
                    region = ImageMaster.CARD_SKILL_BG_BLACK;
                }
            }

            if (texture != null) {
                region = new TextureAtlas.AtlasRegion(texture, 0, 0, texture.getWidth(), texture.getHeight());
            }

            if (region == null) {
                BaseMod.logger.info(color.toString() + " texture is null wtf");
                return SpireReturn.Continue();
            }

            renderHelper(card, sb, ___renderColor, region, xPos, yPos);

            return SpireReturn.Return(null);
        }

        private static void renderHelper(AbstractCard card, SpriteBatch sb, Color color, TextureAtlas.AtlasRegion region, float xPos, float yPos)
        {
            try {
                renderHelperMethod.invoke(card, sb, color, region, xPos, yPos);
            } catch (IllegalAccessException | IllegalArgumentException | SecurityException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }

        private static Method renderHelperMethod;
        private static Method renderHelperMethodWithScale;

        static
        {
            try
            {
                renderHelperMethod = AbstractCard.class.getDeclaredMethod("renderHelper", SpriteBatch.class, Color.class, TextureAtlas.AtlasRegion.class, float.class, float.class);
                renderHelperMethod.setAccessible(true);
                renderHelperMethodWithScale = AbstractCard.class.getDeclaredMethod("renderHelper", SpriteBatch.class, Color.class, TextureAtlas.AtlasRegion.class, float.class, float.class, float.class);
                renderHelperMethodWithScale.setAccessible(true);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
    }

    public static class RenderBGevolutionBIGcorrect {
        @SpirePatch(
                clz = SingleCardViewPopup.class,
                method = "renderCardBack"
        )
        public static class BackgroundTexture {
            public static SpireReturn<?> Prefix(Object __obj_instance, Object sbObject) {
                try {
                    SingleCardViewPopup popup = (SingleCardViewPopup) __obj_instance;
                    SpriteBatch sb = (SpriteBatch) sbObject;
                    Field cardField;
                    cardField = popup.getClass().getDeclaredField("card");
                    cardField.setAccessible(true);
                    AbstractCard card = (AbstractCard) cardField.get(popup);
                    AbstractCard.CardColor color = card.color;
                    if (card.hasTag(BasicMod.CustomTags.Evolution)) {
                        if (Objects.requireNonNull(card.type) == AbstractCard.CardType.POWER) {
                            Texture bgTexture = null;
                            if (card instanceof CustomCard) {
                                bgTexture = ((CustomCard) card).getBackgroundLargeTexture();
                            }
                            if (bgTexture == null) {
                                bgTexture = BaseMod.getPowerBgPortraitTexture(MyCharacter.Meta.CARD_COLOR);
                                if (bgTexture == null) {
                                    bgTexture = ImageMaster.loadImage(BaseMod.getPowerBgPortrait(color));
                                    BaseMod.savePowerBgPortraitTexture(color, bgTexture);
                                }
                            }
                            sb.draw(bgTexture,
                                    Settings.WIDTH / 2.0F - 512.0F,
                                    Settings.HEIGHT / 2.0F - 512.0F,
                                    512.0F,
                                    512.0F,
                                    1024.0F,
                                    1024.0F,
                                    Settings.scale,
                                    Settings.scale,
                                    0.0F,
                                    0,
                                    0,
                                    1024,
                                    1024,
                                    false,
                                    false);
                        }
                        return SpireReturn.Return(null);
                    }
                } catch (NoSuchFieldException | SecurityException | IllegalAccessException e) {
                    e.printStackTrace();
                }
                return SpireReturn.Continue();
            }
        }
    }
}
