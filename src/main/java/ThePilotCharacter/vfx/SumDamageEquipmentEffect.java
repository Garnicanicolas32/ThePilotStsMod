package ThePilotCharacter.vfx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

public class SumDamageEquipmentEffect extends AbstractGameEffect {
    private static final float EFFECT_DUR = 2.5F;
    private float x;
    private float y;
    private float vY;
    private static final float OFFSET_Y;
    private int amt;
    private float scale;
    public AbstractCard target;

    public SumDamageEquipmentEffect(AbstractCard target, float x, float y, int amt) {
        this.scale = 3.0F * Settings.scale;
        this.duration = 2.5F;
        this.startingDuration = 2.5F;
        this.x = x;
        this.y = y + OFFSET_Y;
        this.vY = 90.0F * Settings.scale;
        this.target = target;
        this.amt = amt;
        this.color = Settings.GOLD_COLOR.cpy();
    }

    public void update() {
        this.duration -= Gdx.graphics.getDeltaTime();
        this.y += this.vY * Gdx.graphics.getDeltaTime();
        if (this.vY > 0.0F) {
            this.vY -= 50.0F * Settings.scale * Gdx.graphics.getDeltaTime();
        }

        this.scale = Settings.scale * this.duration / 2.5F + 1.3F;
        if (this.duration < 1.0F) {
            this.color.a = this.duration;
        }

        if (this.duration < 0.0F) {
            this.isDone = true;
        }

    }

    public void render(SpriteBatch sb) {
        FontHelper.damageNumberFont.getData().setScale(this.scale);
        FontHelper.renderFontCentered(sb, FontHelper.damageNumberFont, Integer.toString(this.amt), this.x, this.y, this.color);
    }

    public void dispose() {
    }

    public void refresh(int amt) {
        this.amt += amt;
        this.duration = 2.5F;
        this.color.a = 1.0F;
    }

    static {
        OFFSET_Y = 200.0F * Settings.scale;
    }
}