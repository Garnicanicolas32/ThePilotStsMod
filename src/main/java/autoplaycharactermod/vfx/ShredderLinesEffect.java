package autoplaycharactermod.vfx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

public class ShredderLinesEffect extends AbstractGameEffect {
    private static final float EFFECT_DUR = 0.5F;
    private float x;
    private float y;
    private Vector2 speedVector;
    private float speed;
    private TextureAtlas.AtlasRegion img;
    private float gravity;

    public ShredderLinesEffect(float x, float y) {
        if (MathUtils.randomBoolean()) {
            this.img = ImageMaster.STRIKE_LINE;
        } else {
            this.img = ImageMaster.STRIKE_LINE_2;
        }
        this.duration = 0.5F;
        this.startingDuration = 0.5F;
        this.x = x - (float)this.img.packedWidth / 2.0F;
        this.y = y - (float)this.img.packedHeight / 2.0F;
        this.speed = MathUtils.random(20.0F * Settings.scale, 40.0F * Settings.scale);
        this.speedVector = new Vector2(MathUtils.random(-1.0F, 1.0F), MathUtils.random(-1.0F, 1.0F));
        this.speedVector.nor();
        this.speedVector.angle();
        this.rotation = this.speedVector.angle();
        this.gravity = -20.0F * Settings.scale;
        Vector2 var10000 = this.speedVector;
        var10000.x *= this.speed;
        var10000.y *= this.speed;
        if (MathUtils.randomBoolean()) {
            this.color = Color.CYAN.cpy();
        } else {
            this.color = Color.BLUE.cpy();
        }

    }

    public void update() {
        this.speed -= Gdx.graphics.getDeltaTime() * 60.0F;
        this.speedVector.y += gravity * Gdx.graphics.getDeltaTime();
        this.x += this.speedVector.x * Gdx.graphics.getDeltaTime() * 60.0F;
        this.y += this.speedVector.y * Gdx.graphics.getDeltaTime() * 60.0F;

        this.scale = Settings.scale * this.duration / 0.5F;
        super.update();
    }

    public void render(SpriteBatch sb) {
        if (!this.isDone) {
            sb.setColor(this.color);
            sb.draw(this.img, this.x, this.y, (float)this.img.packedWidth / 2.0F, (float)this.img.packedHeight / 2.0F, (float)this.img.packedWidth, (float)this.img.packedHeight, this.scale, this.scale, this.rotation);
        }

    }

    public void dispose() {
    }
}