package autoplaycharactermod.relics;

import autoplaycharactermod.character.PilotCharacter;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.actions.utility.ScryAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import static autoplaycharactermod.ThePilotMod.makeID;

public class Lightbulb extends BaseRelic {
    public static final int SCRY = 5;
    private static final String NAME = "Lightbulb"; //The name will be used for determining the image file as well as the ID.
    public static final String ID = makeID(NAME); //This adds the mod's prefix to the relic ID, resulting in modID:MyRelic
    private static final RelicTier RARITY = RelicTier.STARTER; //The relic's rarity.
    private static final LandingSound SOUND = LandingSound.MAGICAL; //The sound played when the relic is clicked.

    public Lightbulb() {
        super(ID, NAME, PilotCharacter.Meta.CARD_COLOR, RARITY, SOUND);
    }

    public void atBattleStart() {
        if (!(AbstractDungeon.player instanceof PilotCharacter))
            doAction();
    }

    public void doAction() {
        addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));
        addToBot(new ScryAction(SCRY));
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0] + SCRY + ".";
    }
}
