package autoplaycharactermod.vfx;

import autoplaycharactermod.BasicMod;
import basemod.ReflectionHacks;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

import java.lang.reflect.Field;

public class AfterImageEffectVFX extends AbstractGameEffect {
    private final AbstractCard ghostCard;
    private static final Color[] neonPalette = {
            new Color(1f, 0f, 1f, 0.75f),
            new Color(0f, 1f, 1f, 0.75f),
            new Color(1f, 1f, 0f, 0.75f),
            new Color(0f, 1f, 0f, 0.75f),
            new Color(1f, 0f, 0f, 0.75f)
    };

    public AfterImageEffectVFX(AbstractCard card) {
        this.ghostCard = card.makeStatEquivalentCopy();
        this.ghostCard.cost = -2;
        this.ghostCard.current_x = card.current_x;
        this.ghostCard.current_y = card.current_y;
        this.ghostCard.target_x = ghostCard.current_x;
        this.ghostCard.target_y = ghostCard.current_y;
        this.ghostCard.drawScale = card.drawScale;
        this.ghostCard.angle = card.angle;
        this.ghostCard.rawDescription = "";
        this.ghostCard.initializeDescription();
        this.ghostCard.fadingOut = true;

        this.duration = 0.6f;
        this.startingDuration = this.duration;

        this.color = neonPalette[MathUtils.random(neonPalette.length - 1)];
        ReflectionHacks.setPrivate(ghostCard, AbstractCard.class, "tintColor", this.color);
    }

    @Override
    public void update() {
        duration -= Gdx.graphics.getDeltaTime();
        ghostCard.drawScale = Interpolation.fade.apply(1, 0f, 1f - duration / startingDuration * 0.8f);
        ghostCard.update();

        if (duration <= 0f) {
            isDone = true;
        }
    }

    @Override
    public void render(SpriteBatch sb) {
        ghostCard.render(sb);
    }

    @Override
    public void dispose() {}
}