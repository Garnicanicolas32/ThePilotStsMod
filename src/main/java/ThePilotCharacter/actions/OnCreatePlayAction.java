package ThePilotCharacter.actions;

import ThePilotCharacter.character.PilotCharacter;
import ThePilotCharacter.util.PlayTurnStartModifier;
import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.utility.NewQueueCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class OnCreatePlayAction extends AbstractGameAction {
    private final AbstractCard card;

    public OnCreatePlayAction(AbstractCard card) {
        this.actionType = ActionType.SPECIAL;
        this.duration = Settings.ACTION_DUR_FAST;
        this.card = card;
    }

    @Override
    public void update() {
        if (this.duration == Settings.ACTION_DUR_FAST) {
            if (AbstractDungeon.actionManager.turnHasEnded) {
                CardModifierManager.addModifier(card, new PlayTurnStartModifier());
            } else {
                AbstractDungeon.actionManager.addToTop(new NewQueueCardAction(card, PilotCharacter.getTarget(), false, true));
            }
        }
        this.isDone = true;
    }

}
