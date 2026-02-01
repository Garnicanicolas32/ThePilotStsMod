package ThePilotCharacter.relics;

import ThePilotCharacter.character.PilotCharacter;
import basemod.BaseMod;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.potions.PotionSlot;

import static ThePilotCharacter.ThePilotMod.makeID;

public class EmptyDisk extends BaseRelic {
    private static final String NAME = "EmptyDisk"; //The name will be used for determining the image file as well as the ID.
    public static final String ID = makeID(NAME); //This adds the mod's prefix to the relic ID, resulting in modID:MyRelic
    private static final RelicTier RARITY = RelicTier.BOSS; //The relic's rarity.
    private static final LandingSound SOUND = LandingSound.CLINK; //The sound played when the relic is clicked.

    public EmptyDisk() {
        super(ID, NAME, PilotCharacter.Meta.CARD_COLOR, RARITY, SOUND);
    }

    public void onEquip() {
        BaseMod.MAX_HAND_SIZE += 4;
        AbstractPlayer var10000 = AbstractDungeon.player;
        var10000.potionSlots += 1;
        AbstractDungeon.player.potions.add(new PotionSlot(AbstractDungeon.player.potionSlots - 1));
        AbstractDungeon.player.increaseMaxHp(8, true);
    }

    public void onUnequip() {
        BaseMod.MAX_HAND_SIZE -= 4;
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }
}
