package autoplaycharactermod.powers;

import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.OnDrawPileShufflePower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import static autoplaycharactermod.ThePilotMod.makeID;

public class EnergyBurstPower extends BasePower implements OnDrawPileShufflePower {
    public static final String POWER_ID = makeID("EnergyBurstPower");
    private static final PowerType TYPE = PowerType.BUFF;
    private static final boolean TURN_BASED = false;

    //The only thing TURN_BASED controls is the color of the number on the power icon.
    //Turn based powers are white, non-turn based powers are red or green depending on if their amount is positive or negative.
    //For a power to actually decrease/go away on its own they do it themselves.
    //Look at powers that do this like VulnerablePower and DoubleTapPower.

    public EnergyBurstPower(AbstractCreature owner, int amount) {
        super(POWER_ID, TYPE, TURN_BASED, owner, amount);
    }

    public void playApplyPowerSfx() {
        CardCrawlGame.sound.play("POWER_CONSTRICTED", 0.05F);
    }
    //public void updateDescription() {        this.description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1];    }

    @Override
    public void onShuffle() {
        int amt = (AbstractDungeon.player.drawPile.size() + AbstractDungeon.player.hand.size() + AbstractDungeon.player.discardPile.size() + 1) / 2;
        flash();
        addToBot(new ApplyPowerAction(owner, owner, new ChargePower(owner, amt * amount)));
    }

    public void updateDescription() {
        if (DESCRIPTIONS != null && DESCRIPTIONS.length > 3){
        this.description = DESCRIPTIONS[0];
        if (amount == 1){
            this.description += DESCRIPTIONS[1];
        } else if (amount == 2){
            this.description += DESCRIPTIONS[2];
        }else{
            this.description += " #b" + (amount / 2) + (amount % 2 == 0 ? "" : ".5") + DESCRIPTIONS[3];
        }

        } else
            this.description = "[MISSING DESCRIPTION]";
    }
}
