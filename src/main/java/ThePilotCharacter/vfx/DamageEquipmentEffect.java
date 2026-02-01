package ThePilotCharacter.vfx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

public class DamageEquipmentEffect extends AbstractGameEffect {
    private static final float EFFECT_DUR = 1.2F;
    private float x;
    private float y;
    private float vX;
    private float vY;
    private static final float OFFSET_Y;
    private static final float GRAVITY_Y;
    private int amt;
    private float scale = 1.0F;
    public AbstractCard target;

    public DamageEquipmentEffect(AbstractCard target, float x, float y, int amt) {
        this.duration = 1.2F;
        this.startingDuration = 1.2F;
        this.x = x;
        this.y = y + OFFSET_Y;
        this.target = target;
        this.vX = MathUtils.random(100.0F * Settings.scale, 105.0F * Settings.scale);
        if (MathUtils.randomBoolean()) {
            this.vX = -this.vX;
        }

        this.vY = MathUtils.random(460.0F * Settings.scale, 480.0F * Settings.scale);
        this.amt = amt;
        this.color = Color.RED.cpy();
    }

    public void update() {
        this.x += Gdx.graphics.getDeltaTime() * this.vX;
        this.y += Gdx.graphics.getDeltaTime() * this.vY;
        this.vY += Gdx.graphics.getDeltaTime() * GRAVITY_Y;
        super.update();
        if (this.color.g != 1.0F) {
            Color var10000 = this.color;
            var10000.g += Gdx.graphics.getDeltaTime() * 4.0F;
            if (this.color.g > 1.0F) {
                this.color.g = 1.0F;
            }
        }

        if (this.color.b != 1.0F) {
            Color var1 = this.color;
            var1.b += Gdx.graphics.getDeltaTime() * 4.0F;
            if (this.color.b > 1.0F) {
                this.color.b = 1.0F;
            }
        }

        this.scale = Interpolation.pow4Out.apply(6.0F, 1.2F, 1.0F - this.duration / 1.2F) * Settings.scale;
    }

    public void render(SpriteBatch sb) {
        FontHelper.damageNumberFont.getData().setScale(this.scale);
        FontHelper.renderFontCentered(sb, FontHelper.damageNumberFont, Integer.toString(this.amt), this.x, this.y, this.color);
    }

    public void dispose() {
    }

    static {
        OFFSET_Y = 150.0F * Settings.scale;
        GRAVITY_Y = -2000.0F * Settings.scale;
    }
}

