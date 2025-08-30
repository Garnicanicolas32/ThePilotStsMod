package autoplaycharactermod.vfx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

public class ShowVolatileCopies extends AbstractGameEffect {
    private static final float EFFECT_DUR = 2.5F;
    private AbstractCard card;

    public ShowVolatileCopies(AbstractCard card) {
        this.card = card;
        this.card.rawDescription = "";
        this.card.initializeDescription();
        this.duration = 2.0F;
        this.startingDuration = 2.0F;
        this.card.drawScale = 0.01F;
        this.card.targetDrawScale = 0.5F;
        this.card.current_x = (float)Settings.WIDTH / 2.0F;
        this.card.current_y = (float)Settings.HEIGHT / 2.0F;
        this.card.target_x = MathUtils.random((float)Settings.WIDTH * 0.1F, (float)Settings.WIDTH * 0.9F);
        this.card.target_y = MathUtils.random((float)Settings.HEIGHT * 0.2F, (float)Settings.HEIGHT * 0.8F);
    }

    public void update() {
        this.duration -= Gdx.graphics.getDeltaTime();
        if (this.duration < 0.6F) {
            this.card.fadingOut = true;
        }

        this.card.update();
        if (this.duration < 0.0F) {
            this.isDone = true;
        }
    }

    public void render(SpriteBatch sb) {
        if (!this.isDone) {
            this.card.render(sb);
        }

    }

    public void dispose() {
    }
}
