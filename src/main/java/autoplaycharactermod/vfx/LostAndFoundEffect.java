package autoplaycharactermod.vfx;

import autoplaycharactermod.ui.ConfigPanel;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.FallingDustEffect;
import com.megacrit.cardcrawl.vfx.scene.CeilingDustCloudEffect;

public class LostAndFoundEffect extends AbstractGameEffect {
    private float x;
    private float y;
    private float vfxTimer;
    private int count = 12;

    public LostAndFoundEffect(float newX, float newY) {
        this.x = newX;
        this.y = newY;
    }

    public void update() {
        this.vfxTimer -= Gdx.graphics.getDeltaTime();
        if (this.vfxTimer < 0.0F) {
            --this.count;
            this.vfxTimer = MathUtils.random(0.0F, 0.04F);

            for(int i = 0; i < (ConfigPanel.lessParticles ? 2 :5); ++i) {
                if (MathUtils.randomBoolean(0.3f))
                    AbstractDungeon.effectsQueue.add(new LostAndFoundParticle(this.x, this.y));
                AbstractDungeon.topLevelEffectsQueue.add(new DustCloudVFX(this.x + MathUtils.random(180.0F, -180.0F) * Settings.scale
                        , this.y + MathUtils.random(180.0F, -180.0F) * Settings.scale));
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