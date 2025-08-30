package autoplaycharactermod.powers;

import autoplaycharactermod.character.MyCharacter;
import com.evacipated.cardcrawl.mod.stslib.patches.NeutralPowertypePatch;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;

import static autoplaycharactermod.BasicMod.makeID;

public class TargetedPower extends BasePower {
    public static final String POWER_ID = makeID("MarkedPower");
    private static final AbstractPower.PowerType TYPE = NeutralPowertypePatch.NEUTRAL;
    private static final boolean TURN_BASED = false;

    //The only thing TURN_BASED controls is the color of the number on the power icon.
    //Turn based powers are white, non-turn based powers are red or green depending on if their amount is positive or negative.
    //For a power to actually decrease/go away on its own they do it themselves.
    //Look at powers that do this like VulnerablePower and DoubleTapPower.

    public TargetedPower(AbstractCreature owner, int amount) {
        super(POWER_ID, TYPE, TURN_BASED, owner, amount);

    }

    public void onInitialApplication() {
        for (AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
            if (m.hasPower(TargetedPower.POWER_ID) && m != owner) {
                addToTop(new RemoveSpecificPowerAction(m, m, TargetedPower.POWER_ID));
            }
        }
    }

    @Override
    public void stackPower(int stackAmount){
        super.stackPower(stackAmount);
        for (AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
            if (m.hasPower(TargetedPower.POWER_ID) && m != owner) {
                addToTop(new RemoveSpecificPowerAction(m, m, TargetedPower.POWER_ID));
            }
        }
    }

    public void updateDescription() {
        if (DESCRIPTIONS != null && DESCRIPTIONS.length > 0){
        this.description = DESCRIPTIONS[0];
        } else
            this.description = "[MISSING DESCRIPTION]";
    }

    public void onDeath() {
        AbstractDungeon.actionManager.addToTop(new AbstractGameAction() {
            @Override
            public void update() {
                MyCharacter.ApplyRandomTarget(false);
                this.isDone = true;
            }
        });
    }
}
