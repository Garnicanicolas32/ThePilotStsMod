package ThePilotCharacter.relics;

import ThePilotCharacter.character.PilotCharacter;
import ThePilotCharacter.ui.InnateRestButton;
import ThePilotCharacter.util.InnateRelicMod;
import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.ui.campfire.AbstractCampfireOption;

import java.util.ArrayList;

import static ThePilotCharacter.ThePilotMod.makeID;

public class MemoryCard extends BaseRelic {
    private static final String NAME = "MemoryCard"; //The name will be used for determining the image file as well as the ID.
    public static final String ID = makeID(NAME); //This adds the mod's prefix to the relic ID, resulting in modID:MyRelic
    private static final RelicTier RARITY = RelicTier.RARE; //The relic's rarity.
    private static final LandingSound SOUND = LandingSound.MAGICAL; //The sound played when the relic is clicked.

    public MemoryCard() {
        super(ID, NAME, PilotCharacter.Meta.CARD_COLOR, RARITY, SOUND);
        this.counter = 0;
    }

    public void addCampfireOption(ArrayList<AbstractCampfireOption> options) {
        boolean innateCardsAvailable = AbstractDungeon.player.masterDeck.group.stream()
                .anyMatch(c -> !CardModifierManager.hasModifier(c, InnateRelicMod.ID) && !c.isInnate
                        && !c.inBottleFlame && !c.inBottleLightning && !c.inBottleTornado);
        options.add(new InnateRestButton(innateCardsAvailable && this.counter < 3));

    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }
}
