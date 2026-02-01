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

public class UltraRareScrapStarsEffect extends AbstractGameEffect {
    private float x;
    private float y;
    private float vX;
    private float vY;
    private TextureAtlas.AtlasRegion img;

    public UltraRareScrapStarsEffect() {
        this.duration = MathUtils.random(2F, 3F);
        this.startingDuration = this.duration;
        this.renderBehind = true;
        if (MathUtils.randomBoolean()) {
            this.img = ImageMaster.ROOM_SHINE_1;
            this.rotation = MathUtils.random(-5.0F, 5.0F);
        } else {
            this.img = ImageMaster.GLOW_SPARK_2;
        }

        this.x = MathUtils.random(-100.0F, 1870.0F) * Settings.xScale - (float)this.img.packedWidth / 2.0F;
        float h = MathUtils.random(0.15F, 0.9F);
        this.y = (float)Settings.HEIGHT * h;
        this.vX = MathUtils.random(60.0F, 100.0F) * Settings.scale;
        this.vY = MathUtils.random(-25.0F, 25.0F) * Settings.scale;
        this.color = new Color(MathUtils.random(0.55F, 0.6F), MathUtils.random(0.8F, 1.0F), MathUtils.random(0.95F, 1.0F), 0.0F);
        this.scale = h * MathUtils.random(1.5F, 1.8F) * Settings.scale;
    }

    public void update() {
        this.y += this.vY * Gdx.graphics.getDeltaTime();
        this.x += this.vX * Gdx.graphics.getDeltaTime();
        if (this.duration > this.startingDuration / 1.75F) {
            this.color.a = Interpolation.fade.apply(0.9F, 0.0F, (this.duration - this.startingDuration / 1.75F) / (this.startingDuration / 1.75F));
        } else {
            this.color.a = Interpolation.fade.apply(0.0F, 0.9F, this.duration / (this.startingDuration / 1.75F));
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