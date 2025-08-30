package autoplaycharactermod.relics;

import autoplaycharactermod.character.MyCharacter;
import autoplaycharactermod.ui.ConfigPanel;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import static autoplaycharactermod.BasicMod.makeID;

public class OilCan extends BaseRelic {
    private static final String NAME = "OilCan"; //The name will be used for determining the image file as well as the ID.
    public static final String ID = makeID(NAME); //This adds the mod's prefix to the relic ID, resulting in modID:MyRelic
    private static final RelicTier RARITY = RelicTier.COMMON; //The relic's rarity.
    private static final LandingSound SOUND = LandingSound.SOLID; //The sound played when the relic is clicked.

    public OilCan() {
        super(ID, NAME, MyCharacter.Meta.CARD_COLOR, RARITY, SOUND);
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    public void activate() {
        addToBot(new GainBlockAction(AbstractDungeon.player, 1));
        if (ConfigPanel.experimentalSounds)
            flash();
    }
}
