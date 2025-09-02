package autoplaycharactermod.actions;

import autoplaycharactermod.cards.traitScavengeCards.DuctTape;
import basemod.BaseMod;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class DuctTapeAction extends AbstractGameAction {
    private AbstractCard card;

    public DuctTapeAction(AbstractCard card) {
        this.actionType = ActionType.CARD_MANIPULATION;
        this.card = card;
        this.duration = Settings.ACTION_DUR_FAST;
    }

    public void update() {
        if (this.duration == Settings.ACTION_DUR_FAST) {
            if (AbstractDungeon.player.discardPile.contains(this.card) && AbstractDungeon.player.hand.size() < BaseMod.MAX_HAND_SIZE - 1) {
                AbstractDungeon.player.hand.addToHand(this.card);
                this.card.unhover();
                this.card.setAngle(0.0F, true);
                this.card.lighten(false);
                this.card.drawScale = 0.12F;
                this.card.targetDrawScale = 0.75F;
                this.card.applyPowers();
                AbstractDungeon.player.discardPile.removeCard(this.card);
                if (card instanceof DuctTape){
                    ((DuctTape)card).PlayOnce = false;
                    ((DuctTape)card).addPower();
                }
            }

            AbstractDungeon.player.hand.refreshHandLayout();
            AbstractDungeon.player.hand.glowCheck();
        }

        this.tickDuration();
        this.isDone = true;
    }
}
