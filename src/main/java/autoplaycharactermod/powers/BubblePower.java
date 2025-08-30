package autoplaycharactermod.powers;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;

import static autoplaycharactermod.BasicMod.makeID;

public class BubblePower extends BasePower {
    public static final String POWER_ID = makeID("BubblePower");
    private static final PowerType TYPE = PowerType.BUFF;
    private static final boolean TURN_BASED = false;

    //The only thing TURN_BASED controls is the color of the number on the power icon.
    //Turn based powers are white, non-turn based powers are red or green depending on if their amount is positive or negative.
    //For a power to actually decrease/go away on its own they do it themselves.
    //Look at powers that do this like VulnerablePower and DoubleTapPower.

    public BubblePower(AbstractCreature owner, int amount) {
        super(POWER_ID, TYPE, TURN_BASED, owner, amount);
    }

    public void playApplyPowerSfx() {
        CardCrawlGame.sound.playAV("DARKLING_REGROW_1", MathUtils.random(-0.2f, 0.2f),2.8F);
    }

    public void updateDescription() {
        if (DESCRIPTIONS != null && DESCRIPTIONS.length > 0) {
            this.description = DESCRIPTIONS[0] + amount + ".";
        } else
            this.description = "[MISSING DESCRIPTION]";
    }

    public void atStartOfTurn() {
        addToBot(new RemoveSpecificPowerAction(this.owner, this.owner, BubblePower.POWER_ID));
    }

    public int ChangeDamage(DamageInfo info, int damageAmount) {
        this.flash();
        return Math.max(damageAmount - amount, 0);
    }

}
