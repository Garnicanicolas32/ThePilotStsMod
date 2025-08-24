package autoplaycharactermod.relics;

import autoplaycharactermod.character.MyCharacter;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.actions.utility.ScryAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import static autoplaycharactermod.BasicMod.makeID;

public class RGBLED extends BaseRelic {
    public static final int SCRY = 10;
    private static final String NAME = "RGBLED"; //The name will be used for determining the image file as well as the ID.
    public static final String ID = makeID(NAME); //This adds the mod's prefix to the relic ID, resulting in modID:MyRelic
    private static final RelicTier RARITY = RelicTier.BOSS; //The relic's rarity.
    private static final LandingSound SOUND = LandingSound.MAGICAL; //The sound played when the relic is clicked.

    public RGBLED() {
        super(ID, NAME, MyCharacter.Meta.CARD_COLOR, RARITY, SOUND);
    }

    public void atBattleStart() {
        if (!(AbstractDungeon.player instanceof MyCharacter))
            doAction();
    }

    public void doAction() {
        addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));
        addToBot(new ScryAction(SCRY));
    }

    @Override
    public void obtain() {
        if (AbstractDungeon.player.hasRelic(Lightbulb.ID)) {
            for (int i = 0; i < AbstractDungeon.player.relics.size(); ++i) {
                if (AbstractDungeon.player.relics.get(i).relicId.equals(Lightbulb.ID)) {
                    instantObtain(AbstractDungeon.player, i, true);
                    break;
                }
            }
        } else {
            super.obtain();
        }
    }

    @Override
    public boolean canSpawn() {
        return AbstractDungeon.player != null && AbstractDungeon.player.hasRelic(Lightbulb.ID);
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0] + SCRY + ".";
    }
}
