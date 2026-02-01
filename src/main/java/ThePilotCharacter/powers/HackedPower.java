package ThePilotCharacter.powers;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;

import static ThePilotCharacter.ThePilotMod.makeID;

public class HackedPower extends BasePower {
    public static final String POWER_ID = makeID("HackedPower");
    private static final PowerType TYPE = PowerType.DEBUFF;
    private static final boolean TURN_BASED = false;
    //The only thing TURN_BASED controls is the color of the number on the power icon.
    //Turn based powers are white, non-turn based powers are red or green depending on if their amount is positive or negative.
    //For a power to actually decrease/go away on its own they do it themselves.
    //Look at powers that do this like VulnerablePower and DoubleTapPower.

    public HackedPower(AbstractCreature owner) {
        super(POWER_ID, TYPE, TURN_BASED, owner, -1);

    }
    public void playApplyPowerSfx() {
        CardCrawlGame.sound.play("POWER_CONFUSION", 0.05F);
    }
    public void updateDescription() {
        if (DESCRIPTIONS != null && DESCRIPTIONS.length > 0){
        this.description = DESCRIPTIONS[0];
        } else
            this.description = "[MISSING DESCRIPTION]";
    }
}
