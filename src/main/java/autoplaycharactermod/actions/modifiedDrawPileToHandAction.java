package autoplaycharactermod.actions;

import autoplaycharactermod.character.MyCharacter;
import basemod.BaseMod;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.utility.QueueCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class modifiedDrawPileToHandAction extends AbstractGameAction {
    private AbstractPlayer p;
    private AbstractCard.CardType typeToCheck;

    public modifiedDrawPileToHandAction(int amount, AbstractCard.CardType type) {
        this.p = AbstractDungeon.player;
        this.setValues(this.p, this.p, amount);
        this.actionType = ActionType.CARD_MANIPULATION;
        this.duration = Settings.ACTION_DUR_MED;
        this.typeToCheck = type;
    }

    public void update() {
        if (this.duration == Settings.ACTION_DUR_MED) {
            if (this.p.drawPile.isEmpty()) {
                this.isDone = true;
                return;
            }

            CardGroup tmp = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);

            for(AbstractCard c : this.p.drawPile.group) {
                if (c.type == this.typeToCheck) {
                    tmp.addToRandomSpot(c);
                }
            }

            if (tmp.isEmpty()) {
                this.isDone = true;
                return;
            }

            for(int i = 0; i < this.amount; ++i) {
                if (!tmp.isEmpty()) {
                    tmp.shuffle();
                    AbstractCard card = tmp.getBottomCard();
                    tmp.removeCard(card);
                    if (this.p.hand.size() == BaseMod.MAX_HAND_SIZE) {
                        this.p.drawPile.moveToDiscardPile(card);
                        this.p.createHandIsFullDialog();
                    } else {
                        card.unhover();
                        if (card.color == MyCharacter.Meta.CARD_COLOR) {
                            this.p.drawPile.removeCard(card);
                            this.p.hand.addToTop(card);
                            card.setCostForTurn(0);
                            AbstractDungeon.actionManager.addToBottom(new QueueCardAction(card, MyCharacter.getTarget()));
                        } else {
                            card.lighten(true);
                            card.setAngle(0.0F);
                            card.drawScale = 0.12F;
                            card.targetDrawScale = 0.75F;
                            card.current_x = CardGroup.DRAW_PILE_X;
                            card.current_y = CardGroup.DRAW_PILE_Y;
                            this.p.drawPile.removeCard(card);
                            this.p.hand.addToTop(card);
                            this.p.hand.refreshHandLayout();
                            this.p.hand.applyPowers();
                        }
                    }
                }
            }

            this.isDone = true;
        }

        this.tickDuration();
    }
}
