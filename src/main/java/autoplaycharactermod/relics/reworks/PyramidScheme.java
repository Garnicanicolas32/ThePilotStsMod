package autoplaycharactermod.relics.reworks;

import autoplaycharactermod.character.PilotCharacter;
import autoplaycharactermod.relics.BaseRelic;
import basemod.BaseMod;
import com.megacrit.cardcrawl.actions.unique.RetainCardsAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import static autoplaycharactermod.ThePilotMod.makeID;

public class PyramidScheme extends BaseRelic {
    private static final String NAME = "PyramidScheme"; //The name will be used for determining the image file as well as the ID.
    public static final String ID = makeID(NAME); //This adds the mod's prefix to the relic ID, resulting in modID:MyRelic
    private static final RelicTier RARITY = RelicTier.BOSS; //The relic's rarity.
    private static final LandingSound SOUND = LandingSound.FLAT; //The sound played when the relic is clicked.

    public PyramidScheme() {
        super(ID, NAME, PilotCharacter.Meta.CARD_COLOR, RARITY, SOUND);
    }

    public void onPlayerEndTurn() {
        if (!AbstractDungeon.player.hand.isEmpty() && !AbstractDungeon.player.hasRelic("Runic Pyramid") && !AbstractDungeon.player.hasPower("Equilibrium")) {
            this.addToBot(new RetainCardsAction(AbstractDungeon.player, 2));
        }
    }

    public void onEquip() {
        BaseMod.MAX_HAND_SIZE += 2;
    }

    public void onUnequip() {
        BaseMod.MAX_HAND_SIZE -= 2;
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }
}
