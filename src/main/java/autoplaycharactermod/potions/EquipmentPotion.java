package autoplaycharactermod.potions;

import autoplaycharactermod.actions.DiscoveryEquipmentAction;
import autoplaycharactermod.character.MyCharacter;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.rooms.AbstractRoom;

import static autoplaycharactermod.BasicMod.makeID;

public class EquipmentPotion extends BasePotion{
    public static final String ID = makeID("EquipmentPotion");

    public EquipmentPotion(){
        super(ID, 5, PotionRarity.COMMON, PotionSize.CARD, PotionColor.SMOKE);
        isThrown = false;
        playerClass = MyCharacter.Meta.THEPILOTCHARACTER;
        labOutlineColor = Color.valueOf("#50eb9b");
    }
    @Override
    public String getDescription() {
        if (CardCrawlGame.isInARun() && AbstractDungeon.player != null && AbstractDungeon.player.hasRelic("SacredBark")) {
            return DESCRIPTIONS[1];
        } else {
            return DESCRIPTIONS[0];
        }
    }

    @Override
    public void use(AbstractCreature abstractCreature) {
        if (AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT) {
            this.addToBot(new DiscoveryEquipmentAction(this.potency));
        }
    }

    public int getPotency(int ascensionLevel) {
        return 1;
    }

    public AbstractPotion makeCopy() {
        return new EquipmentPotion();
    }
}
