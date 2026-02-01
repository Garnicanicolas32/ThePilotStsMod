package ThePilotCharacter.vfx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.MathHelper;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

public class NFTcardEffect extends AbstractGameEffect {
    private float x;
    private float y;
    private float incrementTimer;
    private String num = "";
    private boolean dontIncrement = false;

    public NFTcardEffect() {
        this.renderBehind = true;
        this.x = MathUtils.random(0.0F, 1870.0F) * Settings.xScale;
        this.y = MathUtils.random(50.0F, 990.0F) * Settings.yScale;
        this.duration = MathUtils.random(1.0F, 2.0F);
        this.color = new Color(MathUtils.random(0.5F, 1.0F), MathUtils.random(0.5F, 1.0F), MathUtils.random(0.2F, 0.4F), 0.0F);
        this.scale = MathUtils.random(0.4F, 0.9F);
        this.incrementTimer = MathUtils.random(0.02F, 0.07F);
        int num = MathUtils.random(11);
        switch (num) {
            case 0:
                if (MathUtils.randomBoolean()) {
                    this.num = "KROMER";
                    this.dontIncrement = true;
                }
                break;
            case 1:
                this.num = "$VALUE";
                this.dontIncrement = true;
                break;
            case 2:
                this.num = "$BARGAIN";
                this.dontIncrement = true;
                break;
            case 3:
                this.num = "PRICES";
                this.dontIncrement = true;
                break;
            case 4:
                this.num = "SALES";
                this.dontIncrement = true;
                break;
        }
    }

    public void update() {
        this.incrementTimer -= Gdx.graphics.getDeltaTime();
        if (this.incrementTimer < 0.0F) {
            if (!this.dontIncrement || MathUtils.randomBoolean()) {
                if (MathUtils.random(2) == 0) {
                    this.num = this.num + "!";
                } else {
                    this.num = this.num + "$";
                }
            }
            this.incrementTimer = MathUtils.random(0.1F, 0.4F);
        }

        this.duration -= Gdx.graphics.getDeltaTime();
        if (this.duration < 0.0F) {
            this.isDone = true;
        } else {
            if (this.duration < 0.8F) {
                this.color.a = Interpolation.bounceOut.apply(0.0F, 0.5F, this.duration);
            } else {
                this.color.a = MathHelper.slowColorLerpSnap(this.color.a, 0.5F);
            }
        }
    }

    public void render(SpriteBatch sb) {
        sb.setBlendFunction(770, 1);
        FontHelper.energyNumFontGreen.getData().setScale(this.scale);
        FontHelper.renderFont(sb, FontHelper.energyNumFontGreen, this.num, this.x, this.y, this.color);
        FontHelper.energyNumFontGreen.getData().setScale(1.0F);
        sb.setBlendFunction(770, 771);
    }

    public void dispose() {
    }
}
