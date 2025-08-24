package autoplaycharactermod.vfx;

import autoplaycharactermod.cards.BaseCard;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

public class EvolveCardEffect extends AbstractGameEffect {
    private AbstractCard oldCard;
    private AbstractCard newCard;
    private AbstractCard realCard;
    private boolean transformed = false;
    private boolean smokeSpawned = false;
    private float sparktimer;
    private float smokeTime;
    private float swapTime;
    private float starstime;
    private boolean wasGlowing;

    public EvolveCardEffect(AbstractCard Card, float x, float y) {
        this.duration = 2.5f;
        this.startingDuration = this.duration;
        this.sparktimer = 0;
        this.realCard = Card;
        this.oldCard = Card.makeStatEquivalentCopy();
        this.newCard = Card.makeStatEquivalentCopy();
        ((BaseCard) newCard).evolveCard();
        ((BaseCard) newCard).dontsparkle = true;
        this.realCard.fadingOut = true;
        ((BaseCard)realCard).dontsparkle = true;
        this.wasGlowing = realCard.isGlowing;
        this.realCard.stopGlowing();
        this.oldCard.current_x = this.newCard.current_x = x;
        this.oldCard.current_y = this.newCard.current_y = y;
        this.oldCard.target_x = this.newCard.target_x = x;
        this.oldCard.target_y = this.newCard.target_y = y;

        this.oldCard.drawScale = 0.01f;
        this.newCard.drawScale = 0.85f;
        this.oldCard.targetDrawScale = this.newCard.targetDrawScale = 0.85f;

        this.smokeTime = startingDuration * 0.85f;
        this.swapTime = startingDuration * 0.70f;
        this.starstime = startingDuration * 0.60f;

        this.newCard.fadingOut = true;
    }

    @Override
    public void update() {
        duration -= Gdx.graphics.getDeltaTime();
        if (!smokeSpawned && duration < this.smokeTime) {
            smokeSpawned = true;
            for (int i = 0; i < 150; i++) {
                AbstractDungeon.topLevelEffects.add(new TransformParticles(oldCard.current_x, oldCard.current_y));
            }
        }

        if (!transformed && duration < swapTime) {
            CardCrawlGame.sound.play("SELECT_WATCHER");
            transformed = true;
            oldCard.fadingOut = true;
            newCard.fadingOut = false;
        }

        if (duration < starstime) {
            this.sparktimer -= Gdx.graphics.getDeltaTime();
            if (this.sparktimer < 0.0F) {
                this.sparktimer = 0.3F;
                AbstractDungeon.topLevelEffectsQueue.add(new EvolvedCardVFX(newCard.hb.cX, newCard.hb.cY, newCard.hb.width, newCard.hb.height));
            }
        }

        if (this.duration < 0.3F) {
            newCard.fadingOut = true;
        }

        oldCard.update();
        newCard.update();

        if (duration <= 0.0f) {
            if (this.wasGlowing)
                this.realCard.beginGlowing();
            ((BaseCard)realCard).dontsparkle = false;
            this.realCard.fadingOut = false;
            isDone = true;
        }
    }

    @Override
    public void render(SpriteBatch sb) {
        float fadeWindow = 0.3f;
        float t = Math.max(0f, (swapTime - duration) / fadeWindow);
        if (!transformed) {
            oldCard.render(sb);
        } else {
            if (t < 1f) {
                sb.setColor(1f, 1f, 1f, 1f - t);
                oldCard.render(sb);
                sb.setColor(1f, 1f, 1f, t);
                newCard.render(sb);
                sb.setColor(1f, 1f, 1f, 1f);
            } else {
                newCard.render(sb);
            }
        }
    }

    public void dispose() {
    }
}