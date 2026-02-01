package ThePilotCharacter.actions;

import ThePilotCharacter.cards.BaseCard;
import ThePilotCharacter.character.PilotCharacter;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.utility.NewQueueCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static ThePilotCharacter.ThePilotMod.makeID;

public class CheckUnplayedCards extends AbstractGameAction {
    public static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(makeID("AutoPlayTopCardAction"));

    public CheckUnplayedCards() {
        this.duration = Settings.ACTION_DUR_FAST;
        this.actionType = ActionType.WAIT;
        this.source = AbstractDungeon.player;
    }

    @Override
    public void update() {
        if (this.duration == Settings.ACTION_DUR_FAST) {
            AbstractPlayer player = AbstractDungeon.player;
            AbstractMonster target = PilotCharacter.getTarget();
            for (AbstractCard c : player.hand.group){
                if (c instanceof BaseCard && ((BaseCard)c).PlayOnce == true)
                    AbstractDungeon.actionManager.addToTop(new NewQueueCardAction(c, target, false, true));
            }
            this.isDone = true;
        }
    }
}