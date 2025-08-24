package autoplaycharactermod.ui;

import autoplaycharactermod.BasicMod;
import autoplaycharactermod.util.TextureLoader;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.controller.CInputActionSet;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.localization.TutorialStrings;
import com.megacrit.cardcrawl.ui.FtueTip;

public class TraitTutorials extends FtueTip {
    private static final TutorialStrings tutorialStrings = CardCrawlGame.languagePack.getTutorialString(BasicMod.makeID("TraitTutorials"));
    public static final String[] txt = tutorialStrings.TEXT;
    public static final String[] LABEL = tutorialStrings.LABEL;
    private static final float IMG1_OFFSET_X = 135;
    private static final float IMG1_OFFSET_Y = -310f;
    private static final float TXT1_OFFSET_X = -265f;
    private static final float TXT1_OFFSET_Y = -20f;
    private static final float FOOTER1_OFFSET_Y = -390f;
    private static final float FOOTER2_OFFSET_Y = -430f;

    private Texture img1;
    private final Color screen = Color.valueOf("1c262a00");

    public TraitTutorials() {
        img1 = TextureLoader.getTexture(BasicMod.imagePath("/tip/t3.png"));

        AbstractDungeon.player.releaseCard();
        if (AbstractDungeon.isScreenUp) {
            AbstractDungeon.dynamicBanner.hide();
            AbstractDungeon.previousScreen = AbstractDungeon.screen;
        }
        AbstractDungeon.isScreenUp = true;
        AbstractDungeon.screen = AbstractDungeon.CurrentScreen.FTUE;
        AbstractDungeon.overlayMenu.showBlackScreen();

        AbstractDungeon.overlayMenu.proceedButton.show();
        AbstractDungeon.overlayMenu.proceedButton.setLabel(LABEL[1]);
    }

    @Override
    public void update() {
        if (screen.a != 0.8F) {
            screen.a = Math.min(0.8F, screen.a + Gdx.graphics.getDeltaTime());
        }
        if ((AbstractDungeon.overlayMenu.proceedButton.isHovered && InputHelper.justClickedLeft) || CInputActionSet.proceed.isJustPressed()) {
            CInputActionSet.proceed.unpress();
            CardCrawlGame.sound.play("DECK_CLOSE");
            AbstractDungeon.closeCurrentScreen();

            if (AbstractDungeon.previousScreen != AbstractDungeon.CurrentScreen.COMBAT_REWARD) {
                AbstractDungeon.overlayMenu.proceedButton.hide();
            }
            AbstractDungeon.effectList.clear();
        }
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.setColor(screen);
        sb.draw(ImageMaster.WHITE_SQUARE_IMG, 0f, 0f, Settings.WIDTH, Settings.HEIGHT);
        sb.setColor(Color.WHITE.cpy());
        sb.draw(img1, (IMG1_OFFSET_X) * Settings.scale, (Settings.HEIGHT / 2.0F) + IMG1_OFFSET_Y * Settings.scale,
                0, 0, 750 * 0.7f, 820 * 0.7f,
                Settings.scale, Settings.scale, 0f,
                0, 0, 750, 820, false, false);
        FontHelper.renderSmartText(sb, FontHelper.panelNameFont, txt[0],
                Settings.WIDTH / 2.0F + TXT1_OFFSET_X * Settings.scale,
                Settings.HEIGHT / 2f - (FontHelper.getSmartHeight(FontHelper.panelNameFont, txt[0], 700f * Settings.scale, 40f * Settings.scale) / 2f)
                        + TXT1_OFFSET_Y * Settings.scale,
                770f * Settings.scale, 40f * Settings.scale, Settings.CREAM_COLOR);
        FontHelper.renderFontCenteredWidth(sb, FontHelper.panelNameFont, LABEL[2],
                Settings.WIDTH / 2f, Settings.HEIGHT / 2f + FOOTER1_OFFSET_Y * Settings.yScale, Settings.GOLD_COLOR);
        FontHelper.renderFontCenteredWidth(sb, FontHelper.tipBodyFont,
                LABEL[3] + "1/1" + LABEL[4],
                Settings.WIDTH / 2f, Settings.HEIGHT / 2f + FOOTER2_OFFSET_Y * Settings.yScale, Settings.CREAM_COLOR);
        AbstractDungeon.overlayMenu.proceedButton.render(sb);
    }
}