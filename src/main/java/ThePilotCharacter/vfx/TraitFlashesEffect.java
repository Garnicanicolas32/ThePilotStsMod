package ThePilotCharacter.vfx;

import ThePilotCharacter.ui.ConfigPanel;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Interpolation;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

public class TraitFlashesEffect extends AbstractGameEffect {
    private TextureAtlas.AtlasRegion img;
    private static final float DUR = 1.0F;
    private boolean additive;

    public TraitFlashesEffect(Color color) {
        this(color, true);
    }

    public TraitFlashesEffect(Color color, boolean additive) {
        this.img = ImageMaster.BORDER_GLOW_2;
        this.duration = ConfigPanel.lessParticles ? 0.3f :  0.7F;
        this.color = color.cpy();
        this.color.a = 0.0F;
        this.additive = additive;
    }

    public void update() {
        if (1.0F - this.duration < 0.1F) {
            this.color.a = Interpolation.fade.apply(0.0F, 1.0F, (1.0F - this.duration) * 8.0F);
        } else {
            this.color.a = Interpolation.pow2Out.apply(0.0F, 1.0F, this.duration);
        }

        this.duration -= Gdx.graphics.getDeltaTime();
        if (this.duration < 0.0F) {
            this.isDone = true;
        }

    }

    public void render(SpriteBatch sb) {
        if (this.additive) {
            sb.setBlendFunction(770, 1);
            sb.setColor(this.color);
            sb.draw(this.img, 0.0F, 0.0F, (float) Settings.WIDTH, (float)Settings.HEIGHT);
            sb.setBlendFunction(770, 771);
        } else {
            sb.setColor(this.color);
            sb.draw(this.img, 0.0F, 0.0F, (float)Settings.WIDTH, (float)Settings.HEIGHT);
        }

    }

    public void dispose() {
    }
}