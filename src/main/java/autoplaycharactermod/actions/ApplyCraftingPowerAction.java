package autoplaycharactermod.actions;

import autoplaycharactermod.powers.Crafting;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class ApplyCraftingPowerAction extends AbstractGameAction {
    private final int tier;

    public ApplyCraftingPowerAction(AbstractCreature target, int tier) {
        this.target = target;
        this.source = target;
        this.tier = tier;
        this.actionType = ActionType.POWER;
    }

    @Override
    public void update() {
        if (target.hasPower(Crafting.POWER_ID)) {

        } else {
            AbstractDungeon.actionManager.addToTop(
                    new ApplyPowerAction(target, target, new Crafting(target, tier), 1)
            );
        }
        this.isDone = true;
    }
}