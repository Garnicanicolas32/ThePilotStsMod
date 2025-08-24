package autoplaycharactermod.relics;

import autoplaycharactermod.character.MyCharacter;
import basemod.BaseMod;

import static autoplaycharactermod.BasicMod.makeID;

public class EmptyDisk extends BaseRelic {
    private static final String NAME = "EmptyDisk"; //The name will be used for determining the image file as well as the ID.
    public static final String ID = makeID(NAME); //This adds the mod's prefix to the relic ID, resulting in modID:MyRelic
    private static final RelicTier RARITY = RelicTier.BOSS; //The relic's rarity.
    private static final LandingSound SOUND = LandingSound.CLINK; //The sound played when the relic is clicked.

    public EmptyDisk() {
        super(ID, NAME, MyCharacter.Meta.CARD_COLOR, RARITY, SOUND);
    }

    public void onEquip() {
        BaseMod.MAX_HAND_SIZE += 4;
    }

    public void onUnequip() {
        BaseMod.MAX_HAND_SIZE -= 4;
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }
}
