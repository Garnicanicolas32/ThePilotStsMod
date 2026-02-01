package ThePilotCharacter.vfx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

public class TransformParticles extends AbstractGameEffect {
    private float x;
    private float y;
    private float vX;
    private float vY;
    private float vA;
    private float delay;
    private float scale;
    private boolean flipX;
    private boolean flipY;
    private TextureAtlas.AtlasRegion img;

    public TransformParticles(float x, float y, float scaleMod) {
        this.scale = Settings.scale * scaleMod;
        this.flipX = MathUtils.randomBoolean();
        this.flipY = MathUtils.randomBoolean();
        switch (MathUtils.random(2)) {
            case 0:
                this.img = ImageMaster.SMOKE_1;
                break;
            case 1:
                this.img = ImageMaster.SMOKE_2;
                break;
            default:
                this.img = ImageMaster.SMOKE_3;
        }

        this.duration = 1F;
        this.startingDuration = this.duration;
        this.delay = MathUtils.random(0.0F, 0.3F);
        float angle = MathUtils.random(0f, MathUtils.PI2);
        float radius = (float) Math.sqrt(MathUtils.random()) * 200.0F * Settings.scale;
        float power = 0.7f;
        float cos = MathUtils.cos(angle);
        float sin = MathUtils.sin(angle);
        float offsetX = Math.signum(cos) * (float) Math.pow(Math.abs(cos), power) * radius;
        float offsetY = Math.signum(sin) * (float) Math.pow(Math.abs(sin), power) * radius;
        this.x = x + offsetX * scaleMod - (float) this.img.packedWidth / 2.0F;
        this.y = y + offsetY * scaleMod - (float) this.img.packedHeight / 2.0F;
        float rg = MathUtils.random(0.4F, 0.8F);
        this.color = new Color(rg + 0.05F, rg, rg + 0.05F, 0.0F);
        this.vA = MathUtils.random(-400.0F, 400.0F) * Settings.scale;
        this.vX = MathUtils.random(-170.0F, 170.0F) * Settings.scale;
        this.vY = MathUtils.random(-170.0F, 170.0F) * Settings.scale;
        this.scale = MathUtils.random(0.8F, 2.5F) * Settings.scale * scaleMod;
        this.rotation = MathUtils.random(360.0F);
        this.renderBehind = false;
    }

    public TransformParticles(float x, float y) {
        this(x, y, 1f);
    }

    public void update() {
        if (this.delay > 0.0F) {
            this.delay -= Gdx.graphics.getDeltaTime();
        } else {
            this.rotation += this.vA * Gdx.graphics.getDeltaTime();
            this.x += this.vX * Gdx.graphics.getDeltaTime();
            this.y += this.vY * Gdx.graphics.getDeltaTime();
            this.scale += Gdx.graphics.getDeltaTime() * 5.0F;
            if (this.duration > this.startingDuration / 2.0F) {
                this.color.a = Interpolation.pow3Out.apply(0.0F, 0.7F, 1.0F - this.duration);
            } else {
                this.color.a = Interpolation.fade.apply(0.0F, 0.7F, this.duration * 2.0F);
            }

            this.duration -= Gdx.graphics.getDeltaTime();
            if (this.duration < 0.0F) {
                this.isDone = true;
            }

        }
    }

    public void render(SpriteBatch sb) {
        sb.setColor(this.color);
        if (this.flipX && !this.img.isFlipX()) {
            this.img.flip(true, false);
        } else if (!this.flipX && this.img.isFlipX()) {
            this.img.flip(true, false);
        }

        if (this.flipY && !this.img.isFlipY()) {
            this.img.flip(false, true);
        } else if (!this.flipY && this.img.isFlipY()) {
            this.img.flip(false, true);
        }

        sb.draw(this.img, this.x, this.y, (float) this.img.packedWidth / 2.0F, (float) this.img.packedHeight / 2.0F, (float) this.img.packedWidth, (float) this.img.packedHeight, this.scale, this.scale, this.rotation);
    }

    public void dispose() {
    }
}