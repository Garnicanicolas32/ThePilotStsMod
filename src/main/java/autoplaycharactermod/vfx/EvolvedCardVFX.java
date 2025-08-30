package autoplaycharactermod.vfx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

public class EvolvedCardVFX extends AbstractGameEffect {
    private float x;
    private float y;
    private float vX;
    private float vY;
    private TextureAtlas.AtlasRegion img;

    public EvolvedCardVFX(float x, float y, float width, float height) {
        this.duration = MathUtils.random(1F, 1.5F);
        this.startingDuration = this.duration;
        this.renderBehind = false;
        if (MathUtils.randomBoolean()) {
            this.img = ImageMaster.ROOM_SHINE_1;
            this.rotation = MathUtils.random(-5.0F, 5.0F);
        } else {
            this.img = ImageMaster.GLOW_SPARK_2;
        }
        this.x = x + MathUtils.random(-width / 2.0F - 50.0F * Settings.scale, width / 2.0F + 50.0F * Settings.scale);
        this.y = y + MathUtils.random(-height / 2.0F - 20.0F * Settings.scale, height / 2.0F + 20.0F * Settings.scale);
        this.vX = MathUtils.random(-25.0F, 25.0F) * Settings.scale;
        this.vY = MathUtils.random(-25.0F, 25.0F) * Settings.scale;
        this.color = new Color(MathUtils.random(0.75F, 0.8F), MathUtils.random(0.9F, 1.0F), MathUtils.random(0.98F, 1.0F), 0.0F);
        this.scale = MathUtils.random(1.5F, 2F) * Settings.scale;
    }

    public void update() {
        this.y += this.vY * Gdx.graphics.getDeltaTime();
        this.x += this.vX * Gdx.graphics.getDeltaTime();
        if (this.duration > this.startingDuration / 2F) {
            this.color.a = Interpolation.fade.apply(0.9F, 0.0F, (this.duration - this.startingDuration / 2F) / (this.startingDuration / 2F));
        } else {
            this.color.a = Interpolation.fade.apply(0.0F, 0.9F, this.duration / (this.startingDuration / 2F));
        }

        this.duration -= Gdx.graphics.getDeltaTime();
        if (this.duration < 0.0F) {
            this.isDone = true;
        }

    }

    public void render(SpriteBatch sb) {
        sb.setColor(this.color);
        sb.draw(this.img, this.x, this.y, (float)this.img.packedWidth / 2.0F, (float)this.img.packedHeight / 2.0F, (float)this.img.packedWidth, (float)this.img.packedHeight, this.scale * MathUtils.random(0.9F, 1.1F), this.scale * MathUtils.random(0.8F, 1.3F), this.rotation);
    }

    public void dispose() {
    }
}
