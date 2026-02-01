package ThePilotCharacter.powers;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import static ThePilotCharacter.ThePilotMod.makeID;

public class EfficiencyPower extends BasePower {
    public static final String POWER_ID = makeID("EfficiencyPower");
    private static final PowerType TYPE = PowerType.BUFF;
    private static final boolean TURN_BASED = false;
    //The only thing TURN_BASED controls is the color of the number on the power icon.
    //Turn based powers are white, non-turn based powers are red or green depending on if their amount is positive or negative.
    //For a power to actually decrease/go away on its own they do it themselves.
    //Look at powers that do this like VulnerablePower and DoubleTapPower.

    public EfficiencyPower(AbstractCreature owner, int amount) {
        super(POWER_ID, TYPE, TURN_BASED, owner, amount);
    }

    public void playApplyPowerSfx() {
        CardCrawlGame.sound.play("POWER_FLIGHT", 0.05F);
    }

    public void triggerEject(){
        flash();
        addToBot(new GainBlockAction(AbstractDungeon.player, AbstractDungeon.player, amount));
    }

    public void updateDescription() {
        if (DESCRIPTIONS != null && DESCRIPTIONS.length > 1) {
            this.description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1];
        } else
            this.description = "[MISSING DESCRIPTION]";
    }
}
