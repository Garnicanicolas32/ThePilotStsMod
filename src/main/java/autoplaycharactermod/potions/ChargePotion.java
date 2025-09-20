package autoplaycharactermod.potions;

import autoplaycharactermod.ThePilotMod;
import autoplaycharactermod.actions.ScryWithChargeAction;
import autoplaycharactermod.character.PilotCharacter;
import autoplaycharactermod.util.KeywordInfo;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.helpers.TipHelper;
import com.megacrit.cardcrawl.rooms.AbstractRoom;

import static autoplaycharactermod.ThePilotMod.makeID;

public class ChargePotion extends BasePotion{
    public static final String ID = makeID("ChargePotion");
    public static final KeywordInfo ChargeString = ThePilotMod.keywords.get("Charge");

    public ChargePotion(){
        super(ID, 5, PotionRarity.UNCOMMON, PotionSize.SPHERE, PotionColor.SWIFT);
        isThrown = false;
        playerClass = PilotCharacter.Meta.THEPILOTCHARACTER;
        labOutlineColor = Color.valueOf("#50eb9b");
    }
    @Override
    public String getDescription() {
        return DESCRIPTIONS[0] + potency + DESCRIPTIONS[1] + (potency - 2) + DESCRIPTIONS[2];
    }

    @Override
    public void use(AbstractCreature abstractCreature) {
        if (AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT) {
            this.addToBot(new ScryWithChargeAction(potency, potency - 2));
        }
    }

    @Override
    public void addAdditionalTips() {
        this.tips.add(new PowerTip(TipHelper.capitalize(ChargeString.PROPER_NAME), ChargeString.DESCRIPTION));
    }
}
