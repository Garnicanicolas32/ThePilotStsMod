package autoplaycharactermod.relics.Reworks;

import autoplaycharactermod.character.MyCharacter;
import autoplaycharactermod.relics.BaseRelic;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.input.InputHelper;

import static autoplaycharactermod.BasicMod.makeID;

public class StrangeFork extends BaseRelic {
    private static final String NAME = "StrangeFork"; //The name will be used for determining the image file as well as the ID.
    public static final String ID = makeID(NAME); //This adds the mod's prefix to the relic ID, resulting in modID:MyRelic
    private static final RelicTier RARITY = RelicTier.SHOP; //The relic's rarity.
    private static final LandingSound SOUND = LandingSound.CLINK; //The sound played when the relic is clicked.

    public StrangeFork() {
        super(ID, NAME, MyCharacter.Meta.CARD_COLOR, RARITY, SOUND);
    }

    public static final int DURABILITYCHANCE = 25;

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0] + DURABILITYCHANCE + DESCRIPTIONS[1];
    }

}
