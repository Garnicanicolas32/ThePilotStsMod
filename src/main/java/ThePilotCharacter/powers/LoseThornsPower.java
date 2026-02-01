package ThePilotCharacter.powers;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.ThornsPower;

import static ThePilotCharacter.ThePilotMod.makeID;

public class LoseThornsPower extends BasePower {
    public static final String POWER_ID = makeID("LoseThornsPower");
    private static final PowerType TYPE = PowerType.DEBUFF;
    private static final boolean TURN_BASED = false;
    //The only thing TURN_BASED controls is the color of the number on the power icon.
    //Turn based powers are white, non-turn based powers are red or green depending on if their amount is positive or negative.
    //For a power to actually decrease/go away on its own they do it themselves.
    //Look at powers that do this like VulnerablePower and DoubleTapPower.

    public LoseThornsPower(AbstractCreature owner, int amount) {
        super(POWER_ID, TYPE, TURN_BASED, owner, amount);
    }

    public void updateDescription() {
        if (DESCRIPTIONS != null && DESCRIPTIONS.length > 1){
        this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1];
        } else
            this.description = "[MISSING DESCRIPTION]";
    }

    public void atStartOfTurn() {
        flash();
        int currentThorns = AbstractDungeon.player.hasPower(ThornsPower.POWER_ID)
                ? AbstractDungeon.player.getPower(ThornsPower.POWER_ID).amount
                : 0;
        if (amount >= currentThorns)
            addToBot(new RemoveSpecificPowerAction(owner, owner, ThornsPower.POWER_ID));
        else
            addToBot(new ApplyPowerAction(this.owner, this.owner, new ThornsPower(this.owner, -this.amount), -this.amount));
        addToBot(new RemoveSpecificPowerAction(this.owner, this.owner, this.ID));
    }
}
