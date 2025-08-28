package autoplaycharactermod.actions;

import autoplaycharactermod.BasicMod;
import autoplaycharactermod.cards.equipment.Coolant;
import autoplaycharactermod.character.MyCharacter;
import basemod.BaseMod;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.EmptyDeckShuffleAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.actions.unique.LoseEnergyAction;
import com.megacrit.cardcrawl.actions.utility.NewQueueCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.status.VoidCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.EntanglePower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import com.megacrit.cardcrawl.vfx.ThoughtBubble;
import org.apache.logging.log4j.Level;

import static autoplaycharactermod.BasicMod.makeID;

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
                BasicMod.logger.log(Level.INFO, "Current energy: " + EnergyPanel.totalCount);
            }else{
                BasicMod.logger.log(Level.INFO, "Trigger");
                AbstractDungeon.actionManager.addToTop(new MakeTempCardInDrawPileAction(new VoidCard(), 1, true, true));
            }
            this.isDone = true;
        }
    }
}