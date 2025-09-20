package autoplaycharactermod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.actions.unique.LoseEnergyAction;
import com.megacrit.cardcrawl.cards.status.VoidCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;

public class InnateAction extends AbstractGameAction {
    public InnateAction() {
        this.duration = Settings.ACTION_DUR_FAST;
        this.actionType = ActionType.WAIT;
        this.source = AbstractDungeon.player;
    }

    @Override
    public void update() {
        if (this.duration == Settings.ACTION_DUR_FAST) {
            AbstractDungeon.actionManager.addToTop(new AutoplayTopCardAction());
            if (EnergyPanel.totalCount > 0){
                AbstractDungeon.actionManager.addToTop(new LoseEnergyAction(1));
            }else{
                AbstractDungeon.actionManager.addToTop(new MakeTempCardInDrawPileAction(new VoidCard(), 1, true, true));
            }
            this.isDone = true;
        }
    }
}