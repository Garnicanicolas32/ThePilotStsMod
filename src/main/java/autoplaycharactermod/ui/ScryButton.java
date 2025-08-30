package autoplaycharactermod.ui;

import autoplaycharactermod.BasicMod;
import autoplaycharactermod.powers.HackedPower;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.common.EmptyDeckShuffleAction;
import com.megacrit.cardcrawl.actions.utility.ScryAction;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.*;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.EndTurnGlowEffect;
import com.megacrit.cardcrawl.vfx.EndTurnLongPressBarFlashEffect;

import java.util.ArrayList;
import java.util.Iterator;

import static autoplaycharactermod.BasicMod.makeID;

public class ScryButton {
    public static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(makeID("ScryButton"));
    private static final Color DISABLED_COLOR = new Color(0.7F, 0.7F, 0.7F, 1.0F);
    private static final float SHOW_X = 198.0F * Settings.xScale;//1640.0F * Settings.xScale;
    private static final float SHOW_Y = 430.0F * Settings.yScale;
    private static final float HIDE_X = SHOW_X - 500.0F * Settings.xScale;
    private static final float GLOW_INTERVAL = 1.2F;
    private static final float HOLD_DUR = 0.4F;
    public static int SCRYSTARTAMOUNT = 4;
    public static int scryAmount = ConfigPanel.debugScry;
    private final String label;
    private final float current_y;
    private final ArrayList<EndTurnGlowEffect> glowList;
    private final Hitbox hb;
    private final Color holdBarColor;
    public boolean enabled;
    public boolean isGlowing;
    private float current_x;
    private float target_x;
    private boolean isHidden;
    private boolean isDisabled;
    private float glowTimer;
    private float holdProgress;
    private boolean canTrigger;

    public ScryButton() {
        this.label = uiStrings.TEXT[0]; // 40
        this.current_x = HIDE_X;// 46
        this.current_y = SHOW_Y;
        this.canTrigger = false;
        this.target_x = this.current_x;// 47
        this.isHidden = true;// 48
        this.enabled = false;// 49
        this.isDisabled = false;// 50
        this.glowList = new ArrayList<>();// 54
        this.glowTimer = 0.0F;// 56
        this.isGlowing = false;// 57
        this.hb = new Hitbox(0.0F, 0.0F, 230.0F * Settings.scale, 110.0F * Settings.scale);// 60
        this.holdProgress = 0.0F;// 63
        this.holdBarColor = new Color(1.0F, 1.0F, 1.0F, 0.0F);// 65
    }

    public void update() {
        this.enabled = canTrigger && EnergyPanel.totalCount >= 1;
        this.glow();// 68
        this.updateHoldProgress();// 69
        if (this.current_x != this.target_x) {// 71
            this.current_x = MathUtils.lerp(this.current_x, this.target_x, Gdx.graphics.getDeltaTime() * 9.0F);// 72
            if (Math.abs(this.current_x - this.target_x) < Settings.UI_SNAP_THRESHOLD) {// 73
                this.current_x = this.target_x;// 74
            }
        }

        this.hb.move(this.current_x, this.current_y);// 78
        if (this.enabled) {
            this.isDisabled = AbstractDungeon.isScreenUp || AbstractDungeon.player.isDraggingCard || AbstractDungeon.player.inSingleTargetMode;// 84

            if (AbstractDungeon.player.hoveredCard == null) {// 87
                this.hb.update();// 88
            }

            if (!Settings.USE_LONG_PRESS && InputHelper.justClickedLeft && this.hb.hovered && !this.isDisabled && !AbstractDungeon.isScreenUp) {// 91
                this.hb.clickStarted = true;// 93
                CardCrawlGame.sound.play("UI_CLICK_1");// 94
            }

            if (this.hb.hovered && !this.isDisabled && !AbstractDungeon.isScreenUp) {// 97
                if (this.hb.justHovered && AbstractDungeon.player.hoveredCard == null) {// 99
                    CardCrawlGame.sound.play("UI_HOVER");// 100
                    // 101
                }
            }
        }

        if (this.holdProgress == HOLD_DUR && !this.isDisabled && !AbstractDungeon.isScreenUp) {
            this.trigger();
            this.holdProgress = 0.0F;
            AbstractDungeon.effectsQueue.add(new EndTurnLongPressBarFlashEffect());
        }

        if ((!Settings.USE_LONG_PRESS) && (this.hb.clicked && !this.isDisabled && this.enabled)) {
            this.hb.clicked = false;
            if (!AbstractDungeon.isScreenUp) {
                this.trigger();
            }
        }
    }

    private void updateHoldProgress() {
        if (!Settings.USE_LONG_PRESS && !InputHelper.isMouseDown) {// 129
            this.holdProgress -= Gdx.graphics.getDeltaTime();// 131
            if (this.holdProgress < 0.0F) {// 132
                this.holdProgress = 0.0F;// 133
            }

        } else {
            if ((this.hb.hovered && InputHelper.isMouseDown) && !this.isDisabled && this.enabled) {// 138 139
                this.holdProgress += Gdx.graphics.getDeltaTime();// 140
                if (this.holdProgress > HOLD_DUR) {// 141
                    this.holdProgress = HOLD_DUR;// 142
                }
            } else {
                this.holdProgress -= Gdx.graphics.getDeltaTime();// 145
                if (this.holdProgress < 0.0F) {// 146
                    this.holdProgress = 0.0F;// 147
                }
            }

        }
    }// 135 150

    public void enable() {
        enabled = true;
        isGlowing = true;
        canTrigger = true;
    }

    public void disable() {
        enabled = false;
        hb.hovered = false;
        isGlowing = false;
        canTrigger = false;
    }


    public void trigger() {
        hb.hovered = false;
        AbstractDungeon.player.loseEnergy(1);
        if (AbstractDungeon.player.drawPile.isEmpty())
            AbstractDungeon.actionManager.addToBottom(new EmptyDeckShuffleAction());

        AbstractDungeon.actionManager.addToBottom(new ScryAction((AbstractDungeon.player.hasPower(HackedPower.POWER_ID) ? scryAmount - 1 : scryAmount)));

        BasicMod.energySpentTrigger();
    }

    private void glow() {
        if (this.isGlowing && !this.isHidden && this.enabled) {// 195
            if (this.glowTimer < 0.0F) {// 196
                this.glowList.add(new EndTurnGlowEffect());// 197
                this.glowTimer = GLOW_INTERVAL;// 198
            } else {
                this.glowTimer -= Gdx.graphics.getDeltaTime();// 200
            }
        }

        Iterator<EndTurnGlowEffect> i = this.glowList.iterator();// 205
        while (i.hasNext()) {
            AbstractGameEffect e = i.next();// 206
            e.update();// 207
            if (e.isDone) {// 208
                i.remove();// 209
            }
        }

    }// 212

    public void hide() {
        if (!this.isHidden) {// 215
            this.target_x = HIDE_X;// 216
            this.isHidden = true;// 217
        }

    }// 219

    public void show() {
        if (this.isHidden) {// 222
            this.target_x = SHOW_X;// 223
            this.isHidden = false;// 224
            if (this.isGlowing) {// 225
                this.glowTimer = -1.0F;// 226
            }
        }

    }// 229

    public void render(SpriteBatch sb) {
        if (!Settings.hideEndTurn) {// 232
            float tmpY = this.current_y;// 233
            this.renderHoldEndTurn(sb);// 234
            Color textColor;
            if (!this.isDisabled && this.enabled) {// 237
                if (this.hb.hovered) {// 244
                    textColor = Color.CYAN.cpy();// 248
                } else if (this.isGlowing) {// 251
                    textColor = Settings.GOLD_COLOR;// 252
                } else {
                    textColor = Settings.CREAM_COLOR;// 254
                }

                if (this.hb.hovered && !AbstractDungeon.isScreenUp && !Settings.isTouchScreen) {// 258
                    float dy = 162f;
                    TipHelper.renderGenericTip(this.current_x - 155F * Settings.scale, this.current_y + dy * Settings.scale, uiStrings.TEXT[1], uiStrings.TEXT[2] + (AbstractDungeon.player.hasPower(HackedPower.POWER_ID) ? scryAmount - 1 : scryAmount) + uiStrings.TEXT[3]);
                }
            } else {
                textColor = Color.LIGHT_GRAY.cpy();// 241
            }

            if (this.hb.clickStarted && !AbstractDungeon.isScreenUp) {// 267
                tmpY -= 2.0F * Settings.scale;// 268
            } else if (this.hb.hovered && !AbstractDungeon.isScreenUp) {// 269
                tmpY += 2.0F * Settings.scale;// 270
            }

            if (!this.enabled) {// 273
                ShaderHelper.setShader(sb, ShaderHelper.Shader.GRAYSCALE);// 280
            } else if (!this.isDisabled && (!this.hb.clickStarted || !this.hb.hovered)) {// 274
                sb.setColor(Color.WHITE);// 277
            } else {
                sb.setColor(DISABLED_COLOR);// 275
            }

            Texture buttonImg;
            if (this.isGlowing && !this.hb.clickStarted) {// 284
                buttonImg = ImageMaster.END_TURN_BUTTON_GLOW;// 285
            } else {
                buttonImg = ImageMaster.END_TURN_BUTTON;// 287
            }

            if (this.hb.hovered && !this.isDisabled && !AbstractDungeon.isScreenUp) {// 289
                sb.draw(ImageMaster.END_TURN_HOVER, this.current_x - 128.0F, tmpY - 128.0F, 128.0F, 128.0F, 256.0F, 256.0F, Settings.scale, Settings.scale, 0.0F, 0, 0, 256, 256, false, false);// 290
            }

            sb.draw(buttonImg, this.current_x - 128.0F, tmpY - 128.0F, 128.0F, 128.0F, 256.0F, 256.0F, Settings.scale, Settings.scale, 0.0F, 0, 0, 256, 256, false, false);// 309
            if (!this.enabled) {// 327
                ShaderHelper.setShader(sb, ShaderHelper.Shader.DEFAULT);// 328
            }

            this.renderGlowEffect(sb, this.current_x, this.current_y);// 331
            if ((this.hb.hovered || this.holdProgress > 0.0F) && !this.isDisabled && !AbstractDungeon.isScreenUp) {// 333
                sb.setBlendFunction(770, 1);// 334
                sb.setColor(Settings.HALF_TRANSPARENT_WHITE_COLOR);// 335
                sb.draw(buttonImg, this.current_x - 128.0F, tmpY - 128.0F, 128.0F, 128.0F, 256.0F, 256.0F, Settings.scale, Settings.scale, 0.0F, 0, 0, 256, 256, false, false);// 336
                sb.setBlendFunction(770, 771);// 353
            }

            FontHelper.renderFontCentered(sb, FontHelper.panelEndTurnFont, this.label + (AbstractDungeon.player.hasPower(HackedPower.POWER_ID) ? scryAmount - 1 : scryAmount), this.current_x - 0.0F * Settings.scale, tmpY - 3.0F * Settings.scale, textColor);// 381
            if (!this.isHidden) {// 389
                this.hb.render(sb);// 390
            }
        }

    }// 393

    private void renderHoldEndTurn(SpriteBatch sb) {
        if (Settings.USE_LONG_PRESS) {// 396
            this.holdBarColor.r = 0.0F;// 400
            this.holdBarColor.g = 0.0F;// 401
            this.holdBarColor.b = 0.0F;// 402
            this.holdBarColor.a = this.holdProgress * 1.5F;// 403
            sb.setColor(this.holdBarColor);// 404
            sb.draw(ImageMaster.WHITE_SQUARE_IMG, this.current_x - 107.0F * Settings.scale, this.current_y + 53.0F * Settings.scale - 7.0F * Settings.scale, 525.0F * Settings.scale * this.holdProgress + 14.0F * Settings.scale, 20.0F * Settings.scale);// 405
            this.holdBarColor.r = this.holdProgress * 2.5F;// 412
            this.holdBarColor.g = 0.6F + this.holdProgress;// 413
            this.holdBarColor.b = 0.6F;// 414
            this.holdBarColor.a = 1.0F;// 415
            sb.setColor(this.holdBarColor);// 416
            sb.draw(ImageMaster.WHITE_SQUARE_IMG, this.current_x - 100.0F * Settings.scale, this.current_y + 53.0F * Settings.scale, 525.0F * Settings.scale * this.holdProgress, 6.0F * Settings.scale);// 417
        }
    }// 397 423

    private void renderGlowEffect(SpriteBatch sb, float x, float y) {
        // 426

        for (EndTurnGlowEffect e : this.glowList) {
            e.render(sb, x, y);// 427
        }
    }// 429
}