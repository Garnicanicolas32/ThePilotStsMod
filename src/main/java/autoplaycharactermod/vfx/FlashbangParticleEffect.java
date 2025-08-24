package autoplaycharactermod.vfx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

public class FlashbangParticleEffect extends AbstractGameEffect {
    private static final float EFFECT_DUR = 2.0F;
    private float x;
    private float y;
    private float speed;
    private float speedStart;
    private float speedTarget;
    private float scaleXMod;
    private float flipper;
    private TextureAtlas.AtlasRegion img;

    public FlashbangParticleEffect(float x, float y, Color color) {
        this.img = ImageMaster.GLOW_SPARK;
        this.rotation = MathUtils.random(360.0F);
        this.scale = MathUtils.random(2.0F, 2.4F);
        this.scaleXMod = MathUtils.random(0.0F, 1.5F);
        this.x = x - (float)this.img.packedWidth / 2.0F;
        this.y = y - (float)this.img.packedHeight / 2.0F;
        this.duration = 0.7F;
        this.color = color;
        this.renderBehind = MathUtils.randomBoolean();
        this.speedStart = MathUtils.random(000.0F, 500.0F) * Settings.scale;
        this.speedTarget = 800.0F * Settings.scale;
        this.speed = this.speedStart;
        if (MathUtils.randomBoolean()) {
            this.flipper = 90.0F;
        } else {
            this.flipper = 270.0F;
        }

        color.g -= MathUtils.random(0.1F);
        color.b -= MathUtils.random(0.2F);
        color.a = 0.0F;
    }

    public void update() {
        Vector2 tmp = new Vector2(MathUtils.cosDeg(this.rotation), MathUtils.sinDeg(this.rotation));
        tmp.x *= this.speed * Gdx.graphics.getDeltaTime();
        tmp.y *= this.speed * Gdx.graphics.getDeltaTime();
        this.speed = Interpolation.fade.apply(this.speedStart, this.speedTarget, 1.0F - this.duration / 2.0F);
        this.x += tmp.x;
        this.y += tmp.y;
        this.scale += Gdx.graphics.getDeltaTime();
        this.duration -= Gdx.graphics.getDeltaTime();
        if (this.duration < 0.0F) {
            this.isDone = true;
        } else if (this.duration > 0.35F) {
            this.color.a = Interpolation.fade.apply(0.0F, 0.7F, (2.0F - this.duration) * 2.0F);
        } else if (this.duration < 0.35F) {
            this.color.a = Interpolation.fade.apply(0.0F, 0.7F, this.duration * 2.0F);
        }
    }

    public void render(SpriteBatch sb) {
        sb.setBlendFunction(770, 1);
        sb.setColor(this.color);
        sb.draw(this.img, this.x, this.y, (float)this.img.packedWidth / 2.0F, (float)this.img.packedHeight / 2.0F, (float)this.img.packedWidth, (float)this.img.packedHeight, (this.scale + MathUtils.random(-0.08F, 0.08F)) * Settings.scale, (this.scale + this.scaleXMod + MathUtils.random(-0.08F, 0.08F)) * Settings.scale, this.rotation + this.flipper + MathUtils.random(-5.0F, 5.0F));
        sb.setBlendFunction(770, 771);
    }

    public void dispose() {
    }
}