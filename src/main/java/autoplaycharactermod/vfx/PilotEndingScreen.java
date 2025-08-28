package autoplaycharactermod.vfx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.MathHelper;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

public class PilotEndingScreen extends AbstractGameEffect {
    private float x;
    private float y;
    private String num = "";
    public PilotEndingScreen() {
        this.renderBehind = true;
        this.x = MathUtils.random(0.0F, 1870.0F) * Settings.xScale;
        this.y = MathUtils.random(50.0F, 990.0F) * Settings.yScale;
        this.duration = MathUtils.random(2.0F, 4.0F);
        this.color = new Color(MathUtils.random(0.5F, 1.0F), MathUtils.random(0.5F, 1.0F), MathUtils.random(0.5F, 1.0F), 0.0F);
        this.scale = MathUtils.random(0.7F, 1.3F);
        switch (MathUtils.random(2)) {
            case 0:
                this.num = "THE END?";
                break;
            case 1:
                this.num = "END?";
                break;
            case 2:
                this.num = "<ERR0R>";
        }

    }

    public void update() {
        this.duration -= Gdx.graphics.getDeltaTime();
        if (this.duration < 0.0F) {
            this.isDone = true;
        } else {
            if (this.duration < 1.0F) {
                this.color.a = Interpolation.bounceOut.apply(0.0F, 0.5F, this.duration);
            } else {
                this.color.a = MathHelper.slowColorLerpSnap(this.color.a, 0.5F);
            }

        }
    }

    public void render(SpriteBatch sb) {
        sb.setBlendFunction(770, 1);
        FontHelper.energyNumFontBlue.getData().setScale(this.scale);
        FontHelper.renderFont(sb, FontHelper.energyNumFontBlue, this.num, this.x, this.y, this.color);
        FontHelper.energyNumFontBlue.getData().setScale(1.0F);
        sb.setBlendFunction(770, 771);
    }

    public void dispose() {
    }
}
