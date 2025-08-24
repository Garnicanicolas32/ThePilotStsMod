package autoplaycharactermod.powers;

import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.unique.LoseEnergyAction;
import com.megacrit.cardcrawl.core.AbstractCreature;

import static autoplaycharactermod.BasicMod.makeID;

public class OneLessEnergyPower extends BasePower {
    public static final String POWER_ID = makeID("OneLessEnergyPower");
    private static final PowerType TYPE = PowerType.DEBUFF;
    private static final boolean TURN_BASED = false;
    private final boolean justApplied = true;
    //The only thing TURN_BASED controls is the color of the number on the power icon.
    //Turn based powers are white, non-turn based powers are red or green depending on if their amount is positive or negative.
    //For a power to actually decrease/go away on its own they do it themselves.
    //Look at powers that do this like VulnerablePower and DoubleTapPower.

    public OneLessEnergyPower(AbstractCreature owner, int amount) {
        super(POWER_ID, TYPE, TURN_BASED, owner, amount);
    }

    public void atStartOfTurn() {
        addToBot(new LoseEnergyAction(1));
        if (amount > 1)
            amount--;
        else
            addToBot(new RemoveSpecificPowerAction(this.owner, this.owner, OneLessEnergyPower.POWER_ID));
        this.flash();
    }

    public void updateDescription() {
        if (DESCRIPTIONS != null && DESCRIPTIONS.length > 2) {
            if (this.amount == 1) {
                this.description = DESCRIPTIONS[0];
            } else {
                this.description = DESCRIPTIONS[1] + this.amount + DESCRIPTIONS[2];
            }
        } else
            this.description = "[MISSING DESCRIPTION]";
    }
}
