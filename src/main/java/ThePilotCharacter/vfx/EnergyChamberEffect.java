package ThePilotCharacter.vfx;

import ThePilotCharacter.ui.ConfigPanel;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

public class EnergyChamberEffect extends AbstractGameEffect {
    private float x;
    private float y;
    private float tX;
    private float tY;
    private int counter;
    private boolean sound;

    public EnergyChamberEffect(float sX, float sY, float tX, float tY, int modifier, boolean sound) {
        this.x = sX;
        this.y = sY;
        this.tX = tX;
        this.tY = tY;
        this.scale = 0.1F;
        this.duration = 0.6F;
        this.counter = Math.min(modifier, ConfigPanel.lessParticles ? 10 : 50) / 3 + 1;
        this.sound = sound;
    }

    public void update() {
        this.scale -= Gdx.graphics.getDeltaTime();
        if (this.scale < 0.0F && counter > 0) {
            AbstractDungeon.topLevelEffectsQueue.add(new MicroMisilesParticle(this.x + MathUtils.random(60.0F, -60.0F) * Settings.scale, this.y + MathUtils.random(60.0F, -60.0F) * Settings.scale, this.tX, this.tY, AbstractDungeon.player.flipHorizontal, Color.PURPLE.cpy(),sound, 0.8f));
            this.counter--;
            this.scale = 0.03f;
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
