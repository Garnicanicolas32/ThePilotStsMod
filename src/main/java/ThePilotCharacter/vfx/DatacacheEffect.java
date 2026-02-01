package ThePilotCharacter.vfx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.MathHelper;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

public class DatacacheEffect extends AbstractGameEffect {
    private float x;
    private float y;
    private float appearTime;
    private String num = "";

    public DatacacheEffect(int num) {
        this.renderBehind = true;
        this.x = MathUtils.random(0.0F, 1870.0F) * Settings.xScale;
        this.y = MathUtils.random(50.0F, 990.0F) * Settings.yScale;
        float incrementTimer = 0.05f * num;
        this.startingDuration = 0.7f + incrementTimer;
        this.duration = startingDuration;
        this.appearTime = startingDuration - incrementTimer;
        this.color = new Color(
                MathUtils.random(0.2F, 0.5F),
                MathUtils.random(0.2F, 0.5F),
                MathUtils.random(0.5F, 1.0F),
                0.1F
        );
        this.scale = MathUtils.random(2.0F, 2.6F);
        this.num = Integer.toString(num);
    }

    @Override
    public void update() {
        this.duration -= Gdx.graphics.getDeltaTime();
        if (this.duration <= 0.0F) {
            this.isDone = true;
        } else if (this.duration < appearTime) {
            this.color.a = MathHelper.slowColorLerpSnap(this.color.a, 0.8f);
        }
    }

    @Override
    public void render(SpriteBatch sb) {
        if (this.duration < appearTime) {
            sb.setBlendFunction(770, 1);
            FontHelper.energyNumFontBlue.getData().setScale(this.scale);
            FontHelper.renderFont(sb, FontHelper.energyNumFontBlue, this.num, this.x, this.y, this.color);
            FontHelper.energyNumFontBlue.getData().setScale(1.0F);
            sb.setBlendFunction(770, 771);
        }
    }

    @Override
    public void dispose() {
    }
}