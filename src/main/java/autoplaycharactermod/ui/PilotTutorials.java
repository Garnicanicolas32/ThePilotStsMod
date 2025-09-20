package autoplaycharactermod.ui;

import autoplaycharactermod.ThePilotMod;
import autoplaycharactermod.util.TextureLoader;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.controller.CInputActionSet;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.localization.TutorialStrings;
import com.megacrit.cardcrawl.ui.FtueTip;

public class PilotTutorials extends FtueTip {
    private static final TutorialStrings tutorialStrings = CardCrawlGame.languagePack.getTutorialString(ThePilotMod.makeID("PilotTutorials"));
    public static final String[] txt = tutorialStrings.TEXT;
    public static final String[] LABEL = tutorialStrings.LABEL;

    private static final float IMG1_OFFSET_X = 145;
    private static final float IMG1_OFFSET_Y = -430f;
    private static final float IMG2_OFFSET_X = 145f;
    private static final float IMG2_OFFSET_Y = -300f;

    private static final float TXT1_OFFSET_X = -265f;
    private static final float TXT1_OFFSET_Y = 160f;
    private static final float TXT2_OFFSET_Y = -230f;
    private static final float TXT3_OFFSET_X = -235f;
    private static final float TXT3_OFFSET_Y = 10f;

    private static final float FOOTER1_OFFSET_Y = -390f;
    private static final float FOOTER2_OFFSET_Y = -430f;

    private Texture img1, img2;
    private final Color screen = Color.valueOf("1c262a00");

    private float x = 0f, targetX = 0f, startX = 0f, scrollTimer = 0f;
    private int currentSlot = 0, closeScreen = -1;

    public PilotTutorials() {
        img1 = TextureLoader.getTexture(ThePilotMod.imagePath("/tip/t1.png"));
        img2 = TextureLoader.getTexture(ThePilotMod.imagePath("/tip/t2.png"));

        AbstractDungeon.player.releaseCard();
        if (AbstractDungeon.isScreenUp) {
            AbstractDungeon.dynamicBanner.hide();
            AbstractDungeon.previousScreen = AbstractDungeon.screen;
        }
        AbstractDungeon.isScreenUp = true;
        AbstractDungeon.screen = AbstractDungeon.CurrentScreen.FTUE;
        AbstractDungeon.overlayMenu.showBlackScreen();

        AbstractDungeon.overlayMenu.proceedButton.show();
        AbstractDungeon.overlayMenu.proceedButton.setLabel(LABEL[0]);
    }

    @Override
    public void update() {
        AbstractDungeon.overlayMenu.proceedButton.setLabel(currentSlot <= closeScreen ? LABEL[1] : LABEL[0]);

        if (screen.a != 0.8F) {
            screen.a = Math.min(0.8F, screen.a + Gdx.graphics.getDeltaTime());
        }

        if ((AbstractDungeon.overlayMenu.proceedButton.isHovered && InputHelper.justClickedLeft) || CInputActionSet.proceed.isJustPressed()) {
            CInputActionSet.proceed.unpress();
            if (currentSlot <= closeScreen) {
                CardCrawlGame.sound.play("DECK_CLOSE");
                AbstractDungeon.closeCurrentScreen();
                AbstractDungeon.overlayMenu.proceedButton.hide();
                AbstractDungeon.effectList.clear();
            }
            currentSlot--;
            startX = x;
            targetX = currentSlot * Settings.WIDTH;
            scrollTimer = 0.3f;
        }

        if (scrollTimer > 0f) {
            scrollTimer = Math.max(0f, scrollTimer - Gdx.graphics.getDeltaTime());
        }
        x = Interpolation.fade.apply(targetX, startX, scrollTimer / 0.3f);
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.setColor(screen);
        sb.draw(ImageMaster.WHITE_SQUARE_IMG, 0f, 0f, Settings.WIDTH, Settings.HEIGHT);
        sb.setColor(Color.WHITE.cpy());

        sb.draw(img1, (x + IMG1_OFFSET_X) * Settings.scale, (Settings.HEIGHT / 2.0F) + IMG1_OFFSET_Y * Settings.scale,
                0, 0, 510f, 820f,
                Settings.scale, Settings.scale, 0f,
                0, 0, 510, 820, false, false);

        sb.draw(img2,
                x + Settings.WIDTH + IMG2_OFFSET_X * Settings.xScale, Settings.HEIGHT / 2.0F + IMG2_OFFSET_Y * Settings.yScale,
                0, 0, 570f, 620f,
                Settings.xScale, Settings.yScale, 0f,
                0, 0, 570, 620, false, false);

        FontHelper.renderSmartText(sb, FontHelper.panelNameFont, txt[0],
                x + Settings.WIDTH / 2.0F + TXT1_OFFSET_X * Settings.scale,
                Settings.HEIGHT / 2f - (FontHelper.getSmartHeight(FontHelper.panelNameFont, txt[0], 700f * Settings.scale, 40f * Settings.scale) / 2f)
                         + TXT1_OFFSET_Y * Settings.scale,
                770f * Settings.scale, 40f * Settings.scale, Settings.CREAM_COLOR);

        FontHelper.renderSmartText(sb, FontHelper.panelNameFont, txt[1],
                x + Settings.WIDTH / 2.0F + TXT1_OFFSET_X * Settings.scale,
                Settings.HEIGHT / 2f - (FontHelper.getSmartHeight(FontHelper.panelNameFont, txt[1], 700f * Settings.scale, 40f * Settings.scale) / 2f)
                        + TXT2_OFFSET_Y * Settings.scale,
                770f * Settings.scale, 40f * Settings.scale, Settings.CREAM_COLOR);

        FontHelper.renderSmartText(sb, FontHelper.panelNameFont, txt[2],
                x + Settings.WIDTH * 1.5F + TXT3_OFFSET_X * Settings.scale,
                Settings.HEIGHT / 2f - (FontHelper.getSmartHeight(FontHelper.panelNameFont, txt[2], 700f * Settings.scale, 40f * Settings.scale) / 2f)
                        + TXT3_OFFSET_Y * Settings.scale,
                770f * Settings.scale, 40f * Settings.scale, Settings.CREAM_COLOR);

        FontHelper.renderFontCenteredWidth(sb, FontHelper.panelNameFont, LABEL[2],
                Settings.WIDTH / 2f, Settings.HEIGHT / 2f + FOOTER1_OFFSET_Y * Settings.yScale, Settings.GOLD_COLOR);

        FontHelper.renderFontCenteredWidth(sb, FontHelper.tipBodyFont,
                LABEL[3] + Math.abs(currentSlot - 1) + "/" + Math.abs(closeScreen - 1) + LABEL[4],
                Settings.WIDTH / 2f, Settings.HEIGHT / 2f + FOOTER2_OFFSET_Y * Settings.yScale, Settings.CREAM_COLOR);

        AbstractDungeon.overlayMenu.proceedButton.render(sb);
    }
}