package autoplaycharactermod.actions;

import autoplaycharactermod.character.PilotCharacter;
import basemod.BaseMod;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.utility.QueueCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import java.util.ArrayList;

public class ModifiedDiscardPileToHandAction extends AbstractGameAction {
    public static final String[] TEXT;
    private AbstractPlayer player;
    private int numberOfCards;
    private boolean optional;
    private int newCost;
    private boolean setCost;

    public ModifiedDiscardPileToHandAction(int numberOfCards, boolean optional) {
        this.newCost = 0;
        this.actionType = ActionType.CARD_MANIPULATION;
        this.duration = this.startDuration = Settings.ACTION_DUR_FAST;
        this.player = AbstractDungeon.player;
        this.numberOfCards = numberOfCards;
        this.optional = optional;
        this.setCost = false;
    }

    public ModifiedDiscardPileToHandAction(int numberOfCards) {
        this(numberOfCards, false);
    }

    public ModifiedDiscardPileToHandAction(int numberOfCards, int newCost) {
        this.newCost = 0;
        this.actionType = ActionType.CARD_MANIPULATION;
        this.duration = this.startDuration = Settings.ACTION_DUR_FAST;
        this.player = AbstractDungeon.player;
        this.numberOfCards = numberOfCards;
        this.optional = false;
        this.setCost = true;
        this.newCost = newCost;
    }

    public void update() {
        if (this.duration == this.startDuration) {
            if (!this.player.discardPile.isEmpty() && this.numberOfCards > 0) {
                if (this.player.discardPile.size() <= this.numberOfCards && !this.optional) {

                    ArrayList<AbstractCard> cardsToMove = new ArrayList<>(this.player.discardPile.group);

                    for (AbstractCard c : cardsToMove) {
                        if (this.player.hand.size() < BaseMod.MAX_HAND_SIZE) {
                            if (c.color == PilotCharacter.Meta.CARD_COLOR) {
                                this.player.discardPile.removeCard(c);
                                this.player.hand.addToTop(c);
                                c.setCostForTurn(0);
                                AbstractDungeon.actionManager.addToBottom(new QueueCardAction(c, PilotCharacter.getTarget()));
                            } else {
                                this.player.hand.addToHand(c);
                                if (this.setCost) {
                                    c.setCostForTurn(this.newCost);
                                }
                                this.player.discardPile.removeCard(c);
                            }
                        }

                        c.lighten(false);
                        c.applyPowers();
                    }

                    this.isDone = true;
                } else {
                    if (this.numberOfCards == 1) {
                        if (this.optional) {
                            AbstractDungeon.gridSelectScreen.open(this.player.discardPile, this.numberOfCards, true, TEXT[0]);
                        } else {
                            AbstractDungeon.gridSelectScreen.open(this.player.discardPile, this.numberOfCards, TEXT[0], false);
                        }
                    } else if (this.optional) {
                        AbstractDungeon.gridSelectScreen.open(this.player.discardPile, this.numberOfCards, true, TEXT[1] + this.numberOfCards + TEXT[2]);
                    } else {
                        AbstractDungeon.gridSelectScreen.open(this.player.discardPile, this.numberOfCards, TEXT[1] + this.numberOfCards + TEXT[2], false);
                    }

                    this.tickDuration();
                }
            } else {
                this.isDone = true;
            }
        } else {
            if (!AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) {
                for (AbstractCard c : AbstractDungeon.gridSelectScreen.selectedCards) {
                    if (this.player.hand.size() < BaseMod.MAX_HAND_SIZE) {
                        if (c.color == PilotCharacter.Meta.CARD_COLOR) {
                            this.player.discardPile.removeCard(c);
                            this.player.hand.addToTop(c);
                            c.setCostForTurn(0);
                            AbstractDungeon.actionManager.addToBottom(new QueueCardAction(c, PilotCharacter.getTarget()));
                        } else {
                            this.player.hand.addToHand(c);
                            if (this.setCost) {
                                c.setCostForTurn(this.newCost);
                            }
                            this.player.discardPile.removeCard(c);
                        }
                    }
                    c.lighten(false);
                    c.unhover();
                    c.applyPowers();
                }
                for (AbstractCard c : this.player.discardPile.group) {
                    c.unhover();
                    c.target_x = (float) CardGroup.DISCARD_PILE_X;
                    c.target_y = 0.0F;
                }
                AbstractDungeon.gridSelectScreen.selectedCards.clear();
                AbstractDungeon.player.hand.refreshHandLayout();
            }

            this.tickDuration();
            if (this.isDone) {
                for (AbstractCard c : this.player.hand.group) {
                    c.applyPowers();
                }
            }

        }
    }

    static {
        TEXT = CardCrawlGame.languagePack.getUIString("BetterToHandAction").TEXT;
    }
}