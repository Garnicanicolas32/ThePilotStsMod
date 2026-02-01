package ThePilotCharacter.relics;

import ThePilotCharacter.character.PilotCharacter;

import static ThePilotCharacter.ThePilotMod.makeID;

public class Schematics extends BaseRelic {
    private static final String NAME = "Schematics"; //The name will be used for determining the image file as well as the ID.
    public static final String ID = makeID(NAME); //This adds the mod's prefix to the relic ID, resulting in modID:MyRelic
    private static final RelicTier RARITY = RelicTier.COMMON; //The relic's rarity.
    private static final LandingSound SOUND = LandingSound.FLAT; //The sound played when the relic is clicked.
    public static final int AMOUNTTOHEALREPAIR = 10; //The sound played when the relic is clicked.

    public Schematics() {
        super(ID, NAME, PilotCharacter.Meta.CARD_COLOR, RARITY, SOUND);
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0] + AMOUNTTOHEALREPAIR + DESCRIPTIONS[1];
    }
}
