package autoplaycharactermod.relics;

import autoplaycharactermod.character.PilotCharacter;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import static autoplaycharactermod.ThePilotMod.makeID;

public class CoolingFan extends BaseRelic {
    private static final String NAME = "CoolingFan"; //The name will be used for determining the image file as well as the ID.
    public static final String ID = makeID(NAME); //This adds the mod's prefix to the relic ID, resulting in modID:MyRelic
    private static final AbstractRelic.RelicTier RARITY = RelicTier.SHOP; //The relic's rarity.
    private static final AbstractRelic.LandingSound SOUND = AbstractRelic.LandingSound.CLINK; //The sound played when the relic is clicked.

    public CoolingFan() {
        super(ID, NAME, PilotCharacter.Meta.CARD_COLOR, RARITY, SOUND);
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }
}
