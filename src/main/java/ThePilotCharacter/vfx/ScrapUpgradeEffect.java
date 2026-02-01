package ThePilotCharacter.vfx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ScreenShake;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.UpgradeHammerImprintEffect;
import com.megacrit.cardcrawl.vfx.UpgradeShineParticleEffect;

public class ScrapUpgradeEffect extends AbstractGameEffect {
    private float x;
    private float y;
    private boolean clang1 = false;
    private boolean clang2 = false;
    private int level;

    public ScrapUpgradeEffect(float x, float y, int level) {
        this.x = x;
        this.y = y;
        this.level = level;
        this.duration = 0.8F;
    }

    public void update() {
        if (this.duration < 0.6F && !this.clang1 && level >= 1) {
            this.clang1 = true;
            this.clank(this.x - 80.0F * Settings.scale, this.y + 0.0F * Settings.scale);
            CardCrawlGame.screenShake.shake(ScreenShake.ShakeIntensity.HIGH, ScreenShake.ShakeDur.SHORT, false);
        }

        if (this.duration < 0.2F && !this.clang2 && level >= 2) {
            this.clang2 = true;
            this.clank(this.x + 90.0F * Settings.scale, this.y - 110.0F * Settings.scale);
            CardCrawlGame.screenShake.shake(ScreenShake.ShakeIntensity.HIGH, ScreenShake.ShakeDur.SHORT, false);
        }

        this.duration -= Gdx.graphics.getDeltaTime();
        if (this.duration < 0.0F) {
            if (level >= 3) {
                this.clank(this.x + 30.0F * Settings.scale, this.y + 120.0F * Settings.scale);
                CardCrawlGame.screenShake.shake(ScreenShake.ShakeIntensity.HIGH, ScreenShake.ShakeDur.SHORT, false);
            }
            this.isDone = true;
        }

    }

    private void clank(float x, float y) {
        AbstractDungeon.topLevelEffectsQueue.add(new UpgradeHammerImprintEffect(x, y));
        if (!Settings.DISABLE_EFFECTS) {
            for(int i = 0; i < 15 * Math.min(level, 3); ++i) {
                AbstractDungeon.topLevelEffectsQueue.add(new UpgradeShineParticleEffect(x + MathUtils.random(-10.0F, 10.0F) * Settings.scale, y + MathUtils.random(-10.0F, 10.0F) * Settings.scale));
            }

        }
    }

    public void render(SpriteBatch sb) {
    }

    public void dispose() {
    }
}
