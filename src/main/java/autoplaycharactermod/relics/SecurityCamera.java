package autoplaycharactermod.relics;

import autoplaycharactermod.character.MyCharacter;

import static autoplaycharactermod.BasicMod.makeID;

public class SecurityCamera extends BaseRelic {
    private static final String NAME = "SecurityCamera"; //The name will be used for determining the image file as well as the ID.
    public static final String ID = makeID(NAME); //This adds the mod's prefix to the relic ID, resulting in modID:MyRelic
    private static final RelicTier RARITY = RelicTier.UNCOMMON; //The relic's rarity.
    private static final LandingSound SOUND = LandingSound.HEAVY; //The sound played when the relic is clicked.

    public SecurityCamera() {
        super(ID, NAME, MyCharacter.Meta.CARD_COLOR, RARITY, SOUND);
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }
}
