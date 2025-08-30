package autoplaycharactermod.vfx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

public class EjectLightingEffect extends AbstractGameEffect {
    private Texture img;
    private int index = 0;
    private float x;
    private float y;
    private boolean flipX;
    private boolean flipY;
    private float intervalDuration;

    public EjectLightingEffect(float x, float y, float offset) {
        this.renderBehind = MathUtils.randomBoolean();
        this.x = x;
        this.y = y;
        this.color = Settings.LIGHT_YELLOW_COLOR.cpy();
        this.img = ImageMaster.LIGHTNING_PASSIVE_VFX.get(this.index);
        this.scale = MathUtils.random(0.6F, 1.0F) * Settings.scale * 3.5f;
        this.rotation = MathUtils.random(90.0F);

        this.renderBehind = false;

        this.flipX = false;
        this.flipY = false;
        this.intervalDuration = MathUtils.random(0.01F, 0.02F);
        this.duration = this.intervalDuration + offset * 0.05f;
    }

    public void update() {
        this.duration -= Gdx.graphics.getDeltaTime();
        if (this.duration < 0.0F) {
            ++this.index;
            if (this.index > ImageMaster.LIGHTNING_PASSIVE_VFX.size() - 1) {
                this.isDone = true;
                return;
            }

            this.img = ImageMaster.LIGHTNING_PASSIVE_VFX.get(this.index);
            this.duration = this.intervalDuration;
        }
    }

    public void render(SpriteBatch sb) {
        if (duration <= intervalDuration) {
            sb.setColor(this.color);
            sb.setBlendFunction(770, 1);
            sb.draw(this.img, this.x - 61.0F, this.y - 61.0F, 61.0F, 61.0F, 122.0F, 122.0F, this.scale, this.scale, this.rotation, 0, 0, 122, 122, this.flipX, this.flipY);
            sb.setBlendFunction(770, 771);
        }
    }

    public void dispose() {
    }
}
