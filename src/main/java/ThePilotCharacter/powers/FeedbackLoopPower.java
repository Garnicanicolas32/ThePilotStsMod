package ThePilotCharacter.powers;

import ThePilotCharacter.character.PilotCharacter;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.powers.AbstractPower;

import static ThePilotCharacter.ThePilotMod.makeID;

public class FeedbackLoopPower extends BasePower {
    public static final String POWER_ID = makeID("FeedbackLoopPower");
    private static final PowerType TYPE = PowerType.BUFF;
    private static final boolean TURN_BASED = false;

    //The only thing TURN_BASED controls is the color of the number on the power icon.
    //Turn based powers are white, non-turn based powers are red or green depending on if their amount is positive or negative.
    //For a power to actually decrease/go away on its own they do it themselves.
    //Look at powers that do this like VulnerablePower and DoubleTapPower.

    public FeedbackLoopPower(AbstractCreature owner, int amount) {
        super(POWER_ID, TYPE, TURN_BASED, owner, amount);
    }

    public void playApplyPowerSfx() {
        CardCrawlGame.sound.play("ORB_DARK_CHANNEL", 0.05F);
    }

    public void updateDescription() {
        if (DESCRIPTIONS != null && DESCRIPTIONS.length > 1){
        this.description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1];
        } else
            this.description = "[MISSING DESCRIPTION]";
    }

    public void onApplyPower(AbstractPower power, AbstractCreature target, AbstractCreature source) {
        if ((power.type == AbstractPower.PowerType.DEBUFF || power.type == PowerType.BUFF)
                && !power.ID.equals("Shackled") && source == this.owner
                && (target == this.owner || !target.hasPower("Artifact"))) {
            flash();
            addToBot(new DamageAction(PilotCharacter.getTarget(), new DamageInfo(this.owner, this.amount, DamageInfo.DamageType.THORNS), AbstractGameAction.AttackEffect.FIRE));
        }
    }
}
