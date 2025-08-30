package autoplaycharactermod.relics.reworks;

import autoplaycharactermod.character.MyCharacter;
import autoplaycharactermod.relics.BaseRelic;
import autoplaycharactermod.ui.ScryButton;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import static autoplaycharactermod.BasicMod.makeID;

public class ChemicalY extends BaseRelic {
    private static final String NAME = "ChemicalY"; //The name will be used for determining the image file as well as the ID.
    public static final String ID = makeID(NAME); //This adds the mod's prefix to the relic ID, resulting in modID:MyRelic
    private static final RelicTier RARITY = RelicTier.SHOP; //The relic's rarity.
    private static final LandingSound SOUND = LandingSound.CLINK; //The sound played when the relic is clicked.

    public ChemicalY() {
        super(ID, NAME, MyCharacter.Meta.CARD_COLOR, RARITY, SOUND);
    }

    public void onEquip() {
        ScryButton.scryAmount++;
    }

    public void onUnequip() {
        ScryButton.scryAmount--;
    }

    @Override
    public boolean canSpawn() {
        return AbstractDungeon.player != null && !AbstractDungeon.player.hasRelic(SneckoAI.ID);
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }
}
