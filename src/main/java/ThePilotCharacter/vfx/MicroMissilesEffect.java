package ThePilotCharacter.vfx;

import ThePilotCharacter.ui.ConfigPanel;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

public class MicroMissilesEffect extends AbstractGameEffect {
    private float x;
    private float y;
    private float tX;
    private float tY;

    public MicroMissilesEffect(float sX, float sY, float tX, float tY) {
        this.x = sX;
        this.y = sY;
        this.tX = tX;
        this.tY = tY;
        this.scale = 0.12F;
        this.duration = 0.5F;
    }

    public void update() {
        this.scale -= Gdx.graphics.getDeltaTime();
        if (this.scale < 0.0F) {
            AbstractDungeon.topLevelEffectsQueue.add(new MicroMisilesParticle(this.x + MathUtils.random(60.0F, -60.0F) * Settings.scale, this.y + MathUtils.random(60.0F, -60.0F) * Settings.scale, this.tX, this.tY, AbstractDungeon.player.flipHorizontal, Color.WHITE.cpy(),true, 0.5f));
            this.scale = ConfigPanel.lessParticles ? 0.08f : 0.04F;
        }

        this.duration -= Gdx.graphics.getDeltaTime();
        if (this.duration < 0.0F) {
            this.isDone = true;
        }

    }

    public void render(SpriteBatch sb) {
    }

    public void dispose() {
    }
}
