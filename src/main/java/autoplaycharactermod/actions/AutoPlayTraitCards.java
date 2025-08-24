package autoplaycharactermod.actions;

import autoplaycharactermod.cards.TraitCard;
import autoplaycharactermod.cards.equipment.Coolant;
import autoplaycharactermod.character.MyCharacter;
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

import static autoplaycharactermod.BasicMod.makeID;

public class AutoPlayTraitCards extends AbstractGameAction {
    public static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(makeID("AutoPlayTopCardAction"));

    public AutoPlayTraitCards() {
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
                this.addToTop(new AutoPlayTraitCards());
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
            AbstractMonster target = MyCharacter.getTarget();
            AbstractCard card = player.drawPile.getTopCard();

            if (!(card instanceof TraitCard)) {
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