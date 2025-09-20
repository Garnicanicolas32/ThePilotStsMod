package autoplaycharactermod.relics;

import autoplaycharactermod.character.PilotCharacter;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import static autoplaycharactermod.ThePilotMod.makeID;

public class ScheduledUpdate extends BaseRelic {
    private static final String NAME = "ScheduledUpdate"; //The name will be used for determining the image file as well as the ID.
    public static final String ID = makeID(NAME); //This adds the mod's prefix to the relic ID, resulting in modID:MyRelic
    private static final RelicTier RARITY = RelicTier.BOSS; //The relic's rarity.
    private static final LandingSound SOUND = LandingSound.FLAT; //The sound played when the relic is clicked.

    public ScheduledUpdate() {
        super(ID, NAME, PilotCharacter.Meta.CARD_COLOR, RARITY, SOUND);
    }

    public void onEquip() {
        AbstractDungeon.player.energy.energyMaster++;
    }

    public void onUnequip() {
        AbstractDungeon.player.energy.energyMaster--;
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }
}
