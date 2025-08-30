package autoplaycharactermod.relics.reworks;

import autoplaycharactermod.character.MyCharacter;
import autoplaycharactermod.relics.BaseRelic;

import static autoplaycharactermod.BasicMod.makeID;

public class UnceasingBottom extends BaseRelic {
    private static final String NAME = "UnceasingBottom"; //The name will be used for determining the image file as well as the ID.
    public static final String ID = makeID(NAME); //This adds the mod's prefix to the relic ID, resulting in modID:MyRelic
    private static final RelicTier RARITY = RelicTier.RARE; //The relic's rarity.
    private static final LandingSound SOUND = LandingSound.CLINK; //The sound played when the relic is clicked.
    boolean lockTrigger = false;

    public UnceasingBottom() {
        super(ID, NAME, MyCharacter.Meta.CARD_COLOR, RARITY, SOUND);
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }
}
