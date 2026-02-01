package ThePilotCharacter.actions;

import ThePilotCharacter.cards.BaseCard;
import ThePilotCharacter.powers.ChargePower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class ScryWithChargeAction extends AbstractGameAction {
    public static final String[] TEXT;
    private static final UIStrings uiStrings;

    static {
        uiStrings = CardCrawlGame.languagePack.getUIString("ReprogramAction");
        TEXT = uiStrings.TEXT;
    }

    private final float startingDuration;
    private int chargeAmount;

    public ScryWithChargeAction(int numCards, int save) {
        this.amount = numCards;
        if (AbstractDungeon.player.hasRelic("GoldenEye")) {
            AbstractDungeon.player.getRelic("GoldenEye").flash();
            this.amount += 2;
        }
        chargeAmount = save;
        this.actionType = ActionType.CARD_MANIPULATION;
        this.startingDuration = Settings.ACTION_DUR_FAST;
        this.duration = this.startingDuration;
    }

    public void update() {
        if (AbstractDungeon.getMonsters().areMonstersBasicallyDead()) {
            this.isDone = true;
        } else {
            if (this.duration == this.startingDuration) {
                for (AbstractPower p : AbstractDungeon.player.powers) {
                    p.onScry();
                }

                if (AbstractDungeon.player.drawPile.isEmpty()) {
                    this.isDone = true;
                    return;
                }

                CardGroup tmpGroup = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
                if (this.amount != -1) {
                    for (int i = 0; i < Math.min(this.amount, AbstractDungeon.player.drawPile.size()); ++i) {
                        tmpGroup.addToTop(AbstractDungeon.player.drawPile.group.get(AbstractDungeon.player.drawPile.size() - i - 1));
                    }
                } else {
                    for (AbstractCard c : AbstractDungeon.player.drawPile.group) {
                        tmpGroup.addToBottom(c);
                    }
                }

                AbstractDungeon.gridSelectScreen.open(tmpGroup, this.amount, true, TEXT[0]);
            } else if (!AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) {
                int count = 0;
                for (AbstractCard c : AbstractDungeon.gridSelectScreen.selectedCards) {
                    if (c instanceof BaseCard) {
                        ((BaseCard) c).onScrySelected();
                    }
                    AbstractDungeon.player.drawPile.moveToDiscardPile(c);
                    count++;
                }
                AbstractDungeon.gridSelectScreen.selectedCards.clear();
                if (chargeAmount > 0 && count > 0)
                    addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new ChargePower(AbstractDungeon.player, count * chargeAmount)));
            }

            for (AbstractCard c : AbstractDungeon.player.discardPile.group) {
                c.triggerOnScry();
            }

            this.tickDuration();
        }
    }
}
