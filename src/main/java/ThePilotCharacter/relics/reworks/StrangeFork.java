package ThePilotCharacter.relics.reworks;

import ThePilotCharacter.character.PilotCharacter;
import ThePilotCharacter.relics.BaseRelic;

import static ThePilotCharacter.ThePilotMod.makeID;

public class StrangeFork extends BaseRelic {
    private static final String NAME = "StrangeFork"; //The name will be used for determining the image file as well as the ID.
    public static final String ID = makeID(NAME); //This adds the mod's prefix to the relic ID, resulting in modID:MyRelic
    private static final RelicTier RARITY = RelicTier.SHOP; //The relic's rarity.
    private static final LandingSound SOUND = LandingSound.CLINK; //The sound played when the relic is clicked.

    public StrangeFork() {
        super(ID, NAME, PilotCharacter.Meta.CARD_COLOR, RARITY, SOUND);
    }

    public static final int DURABILITYCHANCE = 25;

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0] + DURABILITYCHANCE + DESCRIPTIONS[1];
    }

}
