package autoplaycharactermod.vfx;

import autoplaycharactermod.ui.ConfigPanel;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.combat.LightRayFlyOutEffect;

public class FlashbangEffect extends AbstractGameEffect {
    private float x;
    private float y;
    private float vfxTimer;
    private int count = 10;

    public FlashbangEffect(float newX, float newY) {
        this.x = newX;
        this.y = newY;
    }

    public void update() {
        this.vfxTimer -= Gdx.graphics.getDeltaTime();
        if (this.vfxTimer < 0.0F) {
            --this.count;
            this.vfxTimer = MathUtils.random(0.0F, 0.02F);

            for(int i = 0; i < (ConfigPanel.lessParticles ? 2:5); ++i) {
                AbstractDungeon.effectsQueue.add(new FlashbangParticleEffect(this.x, this.y, new Color(1.0F, 0.9F, 0.7F, 0.0F)));
            }
        }

        if (this.count <= 0) {
            this.isDone = true;
        }

    }

    public void render(SpriteBatch sb) {
    }

    public void dispose() {
    }
}