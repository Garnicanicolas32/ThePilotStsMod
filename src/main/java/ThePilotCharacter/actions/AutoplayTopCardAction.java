package ThePilotCharacter.actions;

import ThePilotCharacter.cards.equipment.Coolant;
import ThePilotCharacter.character.PilotCharacter;
import basemod.BaseMod;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.EmptyDeckShuffleAction;
import com.megacrit.cardcrawl.actions.utility.NewQueueCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.EntanglePower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.vfx.ThoughtBubble;

import static ThePilotCharacter.ThePilotMod.makeID;

public class AutoplayTopCardAction extends AbstractGameAction {
    public static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(makeID("AutoPlayTopCardAction"));

    public AutoplayTopCardAction() {
        this.duration = Settings.ACTION_DUR_FAST;
        this.actionType = ActionType.WAIT;
        this.source = AbstractDungeon.player;
    }

    @Override
    public void update() {
        if (this.duration == Settings.ACTION_DUR_FAST) {
            if (AbstractDungeon.player.drawPile.size() + AbstractDungeon.player.discardPile.size() == 0) {
                this.isDone = true;
                return;
            }

            if (AbstractDungeon.player.drawPile.isEmpty()) {
                this.addToTop(new AutoplayTopCardAction());
                this.addToTop(new EmptyDeckShuffleAction());
                this.isDone = true;
                return;
            }

            if (AbstractDungeon.player.hand.size() >= BaseMod.MAX_HAND_SIZE) {
                AbstractDungeon.player.createHandIsFullDialog();
                this.isDone = true;
                return;
            }

            AbstractPlayer player = AbstractDungeon.player;
            AbstractMonster target = PilotCharacter.getTarget();
            AbstractCard card = player.drawPile.getTopCard();

            if (card.cost > 0){
                card.setCostForTurn(0);
            }
            if ((card.type == AbstractCard.CardType.STATUS || card.type == AbstractCard.CardType.CURSE) && !card.canUse(player, target)) {
                player.drawPile.removeTopCard();
                card.current_x = CardGroup.DRAW_PILE_X;
                card.current_y = CardGroup.DRAW_PILE_Y;
                card.setAngle(0.0F, true);
                card.lighten(false);
                card.drawScale = 0.12F;
                card.targetDrawScale = 0.75F;
                card.triggerWhenDrawn();
                player.hand.addToHand(card);
                for (AbstractPower p : player.powers) {
                    p.onCardDraw(card);
                }
                for (AbstractRelic r : player.relics) {
                    r.onCardDraw(card);
                }
                if (card.type == AbstractCard.CardType.STATUS)
                    for (AbstractCard ca : player.hand.group) {
                        if (ca instanceof Coolant)
                            ca.triggerOnCardPlayed(card);
                    }
                this.isDone = true;
                return;
            }

            if (player.hasPower(EntanglePower.POWER_ID) && card.type == AbstractCard.CardType.ATTACK) {
                String bubble = uiStrings.TEXT[0];
                AbstractDungeon.effectList.add(new ThoughtBubble(AbstractDungeon.player.dialogX, AbstractDungeon.player.dialogY, 3.0F, bubble, true));
                player.drawPile.moveToDiscardPile(card);
            } else {
                player.drawPile.removeCard(card);
                player.hand.addToTop(card);
                AbstractDungeon.actionManager.addToTop(new NewQueueCardAction(card, target, false, true));
            }
            this.isDone = true;
        }
    }
}