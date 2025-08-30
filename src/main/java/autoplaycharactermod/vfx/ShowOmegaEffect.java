package autoplaycharactermod.vfx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.combat.CardPoofEffect;

public class ShowOmegaEffect extends AbstractGameEffect {
    private AbstractCard card;
    private AbstractCard cardDiscard;
    private boolean soundPlayed;

    public ShowOmegaEffect(AbstractCard card, float x, float y) {
        this.card = card;
        this.cardDiscard = card.makeStatEquivalentCopy();
        if (Settings.FAST_MODE) {
            this.duration = 1.30F;
        } else {
            this.duration = 1.6F;
        }
        this.card.current_x = x;
        this.card.current_y = y;
        this.card.target_x = this.card.current_x;
        this.card.target_y = this.card.current_y;
        AbstractDungeon.effectsQueue.add(new CardPoofEffect(card.target_x, card.target_y));
        card.drawScale = 0.01F;
        card.targetDrawScale = 1.5F;
        AbstractDungeon.player.discardPile.addToTop(cardDiscard);
    }

    public void update() {
        this.duration -= Gdx.graphics.getDeltaTime();
        this.card.update();

        if (!soundPlayed){
            CardCrawlGame.sound.play("SELECT_WATCHER");
            soundPlayed = true;
        }
        if (this.duration < 0.0F) {
            for(AbstractRelic r : AbstractDungeon.player.relics) {
                r.onObtainCard(this.card);
            }

            this.isDone = true;
            this.card.shrink();
            this.cardDiscard.shrink();
            AbstractDungeon.getCurrRoom().souls.obtain(this.card, true);
            AbstractDungeon.getCurrRoom().souls.discard(this.cardDiscard, true);

            for(AbstractRelic r : AbstractDungeon.player.relics) {
                r.onMasterDeckChange();
            }
        }

    }

    public void render(SpriteBatch sb) {
        if (!this.isDone) {
            sb.setColor(0f, 0f, 0f, 0.7f);
            sb.draw(
                    ImageMaster.WHITE_SQUARE_IMG,
                    0, 0,
                    Settings.WIDTH, Settings.HEIGHT
            );
            this.card.render(sb);
        }
    }

    public void dispose() {
    }
}