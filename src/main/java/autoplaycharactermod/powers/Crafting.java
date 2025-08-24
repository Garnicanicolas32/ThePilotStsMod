package autoplaycharactermod.powers;

import autoplaycharactermod.BasicMod;
import autoplaycharactermod.actions.CraftingScrapAction;
import com.evacipated.cardcrawl.mod.stslib.patches.NeutralPowertypePatch;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;

import static autoplaycharactermod.BasicMod.makeID;

public class Crafting extends BasePower {
    public static final String POWER_ID = makeID("Crafting");
    private static final PowerType TYPE = NeutralPowertypePatch.NEUTRAL;
    private static final boolean TURN_BASED = false;

    private int commons = 0;
    private int uncommons = 0;
    private int rares = 0;

    public Crafting(AbstractCreature owner, int amount) {
        super(POWER_ID, TYPE, TURN_BASED, owner, amount);
    }

    public void onInitialApplication() {
        switch (amount) {
            case 1:
                commons++;
                break;
            case 2:
                uncommons++;
                break;
            case 3:
                rares++;
                break;
        }
        amount = 1;
        updateDescription();
    }

    @Override
    public void stackPower(int stackAmount){
        super.stackPower(1);
        switch (stackAmount) {
            case 1:
                commons++;
                break;
            case 2:
                uncommons++;
                break;
            case 3:
                rares++;
                break;
        }
        if (commons >= 3) {
            addToBot(new CraftingScrapAction(BasicMod.CustomTags.ScrapCommon, 1));
            commons -= 3;
            removeConsumed();
        }
        if (uncommons >= 3) {
            addToBot(new CraftingScrapAction(BasicMod.CustomTags.ScrapUncommon, 2));
            uncommons -= 3;
            removeConsumed();
        }
        if (rares >= 3) {
            addToBot(new CraftingScrapAction(BasicMod.CustomTags.ScrapRare, 3));
            rares -= 3;
            removeConsumed();
        };
        updateDescription();
    }

    public void updateDescription() {
        if (DESCRIPTIONS != null && DESCRIPTIONS.length > 4){
            StringBuilder Desc = new StringBuilder();
            Desc.append(DESCRIPTIONS[4]);
            if (commons > 0) {
                Desc.append("#b").append(commons).append(DESCRIPTIONS[0]).append(3).append(DESCRIPTIONS[1]);
            }
            if (uncommons > 0) {
                if (commons > 0) Desc.append(" NL ");
                Desc.append("#b").append(uncommons).append(DESCRIPTIONS[0]).append(3).append(DESCRIPTIONS[2]);
            }
            if (rares > 0) {
                if (commons > 0 || uncommons > 0) Desc.append(" NL ");
                Desc.append("#b").append(rares).append(DESCRIPTIONS[0]).append(3).append(DESCRIPTIONS[3]);
            }
            this.description = Desc.toString();
        } else
            this.description = "[MISSING DESCRIPTION]";
    }

    public void removeConsumed() {
        if (amount <= 3) {
            addToBot(new RemoveSpecificPowerAction(owner, owner, Crafting.POWER_ID));
        }else{
            amount -= 3;
        }
    }
}
