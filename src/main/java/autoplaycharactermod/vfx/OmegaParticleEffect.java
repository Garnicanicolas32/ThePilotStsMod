package autoplaycharactermod.vfx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.CatmullRomSpline;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.CardTrailEffect;

import java.util.ArrayList;

import static com.megacrit.cardcrawl.cards.Soul.trailEffectPool;

public class OmegaParticleEffect extends AbstractGameEffect {
    private AbstractCard card;

    private Vector2 pos;
    private Vector2 target;
    private Vector2 tmp = new Vector2();
    private float lifetime = 0f;
    private float waitTime = 0.15f;
    private static final float MAX_LIFETIME = 1.0f;
    private float rotation;
    private float rotationRate;
    private boolean rotateClockwise;
    private boolean stopRotating = false;
    private float currentSpeed = 0f;

    private CatmullRomSpline<Vector2> crs = new CatmullRomSpline<>();
    private ArrayList<Vector2> controlPoints = new ArrayList<>();
    private Vector2[] points = new Vector2[20];
    private float vfxTimer = 0.015f;

    private static final float START_VELOCITY = 200f * Settings.scale;
    private static final float MAX_VELOCITY = 4000f * Settings.scale;
    private static final float VELOCITY_RAMP_RATE = 2000f * Settings.scale;
    private static final float DST_THRESHOLD = 36f * Settings.scale;
    private static final float HOME_IN_THRESHOLD = 72f * Settings.scale;

    public OmegaParticleEffect(AbstractCard card, float startX, float startY, float targetX, float targetY, int tier) {
        this.card = card;
        this.pos = new Vector2(startX, startY);
        this.target = new Vector2(targetX, targetY);
        switch (tier) {
            case 1:
                this.card.targetDrawScale *= 0.8f;
                this.card.drawScale = this.card.drawScale * 0.5f;

                break;
            case 2:
                this.card.targetDrawScale *= 1.0f;
                this.card.drawScale = this.card.drawScale * 0.5f;
                break;
            case 3:
                this.card.targetDrawScale *= 1.2f;
                this.card.drawScale = this.card.drawScale * 0.5f;
                break;
        }
        card.current_x = pos.x;
        card.current_y = pos.y;
        card.target_x = pos.x;
        card.target_y = pos.y;

        this.rotation = MathUtils.random(0, 359);
        this.rotateClockwise = MathUtils.randomBoolean();
        this.rotationRate = 150f * Settings.scale * MathUtils.random(1f, 2f);
        this.currentSpeed = START_VELOCITY * MathUtils.random(0.5f, 2f);
        this.isDone = false;
    }

    @Override
    public void update() {
        waitTime -= Gdx.graphics.getDeltaTime();
        if (waitTime < 0) {
            lifetime += Gdx.graphics.getDeltaTime();
            card.current_x = pos.x;
            card.current_y = pos.y;
            card.target_x = pos.x;
            card.target_y = pos.y;
            card.update();

            updateMovement();
            if (lifetime > MAX_LIFETIME / 2f) {
                card.fadingOut = true;
            }

            if (lifetime > MAX_LIFETIME) {
                isDone = true;
            }
        }
        else{
            card.update();
        }
    }

    private void updateMovement() {
        tmp.set(pos.x - target.x, pos.y - target.y).nor();
        float targetAngle = tmp.angle();

        rotationRate += Gdx.graphics.getDeltaTime() * 800f;
        if (!stopRotating) {
            if (rotateClockwise) {
                rotation += Gdx.graphics.getDeltaTime() * rotationRate;
            } else {
                rotation -= Gdx.graphics.getDeltaTime() * rotationRate;
            }
            rotation = (rotation + 360f) % 360f;

            if (target.dst(pos) < HOME_IN_THRESHOLD || angleDiff(rotation, targetAngle) < Gdx.graphics.getDeltaTime() * rotationRate) {
                rotation = targetAngle;
                stopRotating = true;
            }
        }

        Vector2 move = tmp.cpy().setAngle(rotation).scl(Gdx.graphics.getDeltaTime() * currentSpeed);

        if (move.len() > target.dst(pos)) {
            move.setLength(target.dst(pos));
        }

        pos.sub(move);

        currentSpeed += Gdx.graphics.getDeltaTime() * VELOCITY_RAMP_RATE * (stopRotating ? 3f : 1.5f);
        if (currentSpeed > MAX_VELOCITY) currentSpeed = MAX_VELOCITY;

        if (target.dst(pos) < DST_THRESHOLD) {
            isDone = true;
        }

        vfxTimer -= Gdx.graphics.getDeltaTime();
        if (!isDone && vfxTimer < 0f) {
            vfxTimer = 0.015f;

            if (controlPoints.isEmpty() || !controlPoints.get(0).equals(pos)) {
                controlPoints.add(pos.cpy());
            }
            if (controlPoints.size() > 10) controlPoints.remove(0);

            if (controlPoints.size() > 3) {
                crs.set(controlPoints.toArray(new Vector2[0]), false);
                for (int i = 0; i < 20; i++) {
                    if (points[i] == null) points[i] = new Vector2();
                    Vector2 derp = crs.valueAt(points[i], i / 19f);
                    CardTrailEffect effect = (CardTrailEffect) trailEffectPool.obtain();
                    effect.init(derp.x, derp.y);
                    AbstractDungeon.topLevelEffects.add(effect);
                }
            }
        }
    }

    private float angleDiff(float a, float b) {
        float diff = (a - b + 180f) % 360f - 180f;
        return Math.abs(diff);
    }

    @Override
    public void render(SpriteBatch sb) {
        if (!isDone) {
            card.render(sb);
        }
    }

    @Override
    public void dispose() {
    }
}