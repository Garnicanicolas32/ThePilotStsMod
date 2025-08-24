package autoplaycharactermod.vfx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

public class EquipmentShowCardBrieflyEffect extends AbstractGameEffect {
    private AbstractCard card;
    private float timeOffset;
    private static final float TIMEADDED;
    private static final float DURATION;

    public EquipmentShowCardBrieflyEffect(AbstractCard card) {
        this.card = card;
        this.duration = DURATION;
        this.startingDuration = DURATION;
        this.identifySpawnLocation((float) Settings.WIDTH - 96.0F * Settings.scale, (float)Settings.HEIGHT - 32.0F * Settings.scale);
        card.drawScale = 0.01F;
        card.targetDrawScale = 0.5F;
    }

    private void identifySpawnLocation(float x, float y) {
        int effectCount = 0;

        for(AbstractGameEffect e : AbstractDungeon.effectList) {
            if (e instanceof EquipmentShowCardBrieflyEffect) {
                ++effectCount;
            }
        }

        this.card.current_x = (float)Settings.WIDTH / 2.0F;
        this.card.current_y = (float)Settings.HEIGHT / 2.0F;

        if (effectCount < 10){
            this.card.target_y = (float)Settings.HEIGHT * 0.73F;
            this.card.target_x = (float)Settings.WIDTH * 0.08F + AbstractCard.IMG_WIDTH * 0.55F * effectCount;
        }
        else {

            this.card.target_x = MathUtils.random((float)Settings.WIDTH * 0.1F, (float)Settings.WIDTH * 0.9F);
            this.card.target_y = MathUtils.random((float)Settings.HEIGHT * 0.2F, (float)Settings.HEIGHT * 0.8F);
        }

        this.timeOffset = duration + (effectCount * TIMEADDED / 2);
        this.duration += TIMEADDED * effectCount;
        this.startingDuration += TIMEADDED * effectCount;
    }

    public void update() {
        this.duration -= Gdx.graphics.getDeltaTime();
        if (this.duration < 0.3F) {
            this.card.fadingOut = true;
        }
        if (this.duration < timeOffset)
            this.card.update();

        if (this.duration < 0.0F) {
            this.isDone = true;
        }

    }

    public void render(SpriteBatch sb) {
        if (!this.isDone && this.duration < timeOffset) {
            this.card.render(sb);
        }

    }

    public void dispose() {
    }

    static {
        TIMEADDED = 0.17F;
        DURATION = 1.2F;
    }
}
