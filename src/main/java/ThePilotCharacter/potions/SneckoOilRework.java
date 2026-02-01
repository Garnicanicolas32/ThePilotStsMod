package ThePilotCharacter.potions;

import ThePilotCharacter.character.PilotCharacter;
import ThePilotCharacter.powers.HackedPower;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.helpers.TipHelper;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.rooms.AbstractRoom;

import static ThePilotCharacter.ThePilotMod.makeID;

public class SneckoOilRework extends BasePotion{
    public static final String ID = makeID("SneckoOilRework");
    public static final PowerStrings HackedString = CardCrawlGame.languagePack.getPowerStrings(HackedPower.POWER_ID);

    public  SneckoOilRework(){
        super(ID, 5, PotionRarity.RARE, PotionSize.SNECKO, PotionColor.POISON);
        isThrown = false;
        playerClass = PilotCharacter.Meta.THEPILOTCHARACTER;
        labOutlineColor = Color.valueOf("#50eb9b");
    }
    @Override
    public String getDescription() {
        return DESCRIPTIONS[0] + potency + DESCRIPTIONS[1];
    }

    @Override
    public void use(AbstractCreature abstractCreature) {
        if (AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT) {
            this.addToBot(new DrawCardAction(AbstractDungeon.player, this.potency));
            this.addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new HackedPower(AbstractDungeon.player)));
        }
    }

    @Override
    public void addAdditionalTips() {
        this.tips.add(new PowerTip(TipHelper.capitalize(HackedString.NAME), HackedString.DESCRIPTIONS[0]));
    }
}
