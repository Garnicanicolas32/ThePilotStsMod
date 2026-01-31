package autoplaycharactermod.actions;

import autoplaycharactermod.character.PilotCharacter;
import autoplaycharactermod.util.PlayTurnStartModifier;
import basemod.BaseMod;
import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.utility.QueueCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;

public class ModifiedCardFromDeckToHandAction extends AbstractGameAction {
    private static final UIStrings uiStrings;
    private static final UIStrings uiStrings2;
    public static final String[] TEXT;
    public static final String[] TEXT2;
    private AbstractPlayer p;
    private boolean autoPlayColorless;
    private AbstractCard.CardType cardType;
    private boolean anycard;

    public ModifiedCardFromDeckToHandAction(int amount, boolean autoPlayColorless, AbstractCard.CardType cardType, boolean anycard) {
        this.p = AbstractDungeon.player;
        this.setValues(this.p, AbstractDungeon.player, amount);
        this.actionType = ActionType.CARD_MANIPULATION;
        this.duration = Settings.ACTION_DUR_MED;
        this.autoPlayColorless = autoPlayColorless;
        this.cardType = cardType;
        this.anycard = anycard;
    }

    public ModifiedCardFromDeckToHandAction(int amount, boolean autoPlayColorless, AbstractCard.CardType cardType) {
        this(amount,autoPlayColorless, cardType, false);
    }

    public ModifiedCardFromDeckToHandAction(int amount, boolean autoPlayColorless) {
        this(amount,autoPlayColorless, AbstractCard.CardType.ATTACK, true);
    }

    public void update() {
        if (this.duration == Settings.ACTION_DUR_MED) {
            CardGroup tmp = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);

            for (AbstractCard c : this.p.drawPile.group) {
                if (c.type == cardType || this.anycard) {
                    tmp.addToRandomSpot(c);
                }
            }

            if (tmp.isEmpty()) {
                this.isDone = true;
            } else if (tmp.size() == 1) {
                AbstractCard card = tmp.getTopCard();
                if (this.p.hand.size() == BaseMod.MAX_HAND_SIZE) {
                    this.p.drawPile.moveToDiscardPile(card);
                    this.p.createHandIsFullDialog();
                } else {
                    card.unhover();
                    card.lighten(true);
                    card.setAngle(0.0F);
                    card.drawScale = 0.12F;
                    card.targetDrawScale = 0.75F;
                    card.current_x = CardGroup.DRAW_PILE_X;
                    card.current_y = CardGroup.DRAW_PILE_Y;
                    this.p.drawPile.removeCard(card);
                    AbstractDungeon.player.hand.addToTop(card);
                    if (card.color == PilotCharacter.Meta.CARD_COLOR || autoPlayColorless) {
                        if (AbstractDungeon.actionManager.turnHasEnded) {
                            CardModifierManager.addModifier(card, new PlayTurnStartModifier());
                        } else{
                            card.setCostForTurn(0);
                            AbstractDungeon.actionManager.addToBottom(new QueueCardAction(card, PilotCharacter.getTarget()));
                        }

                    } else {
                        AbstractDungeon.player.hand.refreshHandLayout();
                        AbstractDungeon.player.hand.applyPowers();
                    }
                }

                this.isDone = true;
            } else {
                AbstractDungeon.gridSelectScreen.open(tmp, this.amount, this.cardType == AbstractCard.CardType.ATTACK ? TEXT2[0] : TEXT[0], false);
                this.tickDuration();
            }
        } else {
            if (!AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) {
                for (AbstractCard c : AbstractDungeon.gridSelectScreen.selectedCards) {
                    c.unhover();
                    if (this.p.hand.size() == BaseMod.MAX_HAND_SIZE) {
                        this.p.drawPile.moveToDiscardPile(c);
                        this.p.createHandIsFullDialog();
                    } else {
                        this.p.drawPile.removeCard(c);
                        this.p.hand.addToTop(c);
                        if (c.color == PilotCharacter.Meta.CARD_COLOR || autoPlayColorless) {
                            if (AbstractDungeon.actionManager.turnHasEnded) {
                                CardModifierManager.addModifier(c, new PlayTurnStartModifier());
                            } else{
                                c.setCostForTurn(0);
                                AbstractDungeon.actionManager.addToBottom(new QueueCardAction(c, PilotCharacter.getTarget()));
                            }
                        } else {
                            this.p.hand.refreshHandLayout();
                            this.p.hand.applyPowers();
                        }
                    }
                }
                AbstractDungeon.gridSelectScreen.selectedCards.clear();
                this.p.hand.refreshHandLayout();
            }

            this.tickDuration();
        }
    }

    static {
        uiStrings = CardCrawlGame.languagePack.getUIString("SkillFromDeckToHandAction");
        uiStrings2 = CardCrawlGame.languagePack.getUIString("AttackFromDeckToHandAction");
        TEXT = uiStrings.TEXT;
        TEXT2 = uiStrings2.TEXT;
    }
}
