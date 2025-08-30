package autoplaycharactermod.relics.reworks;

import autoplaycharactermod.character.MyCharacter;
import autoplaycharactermod.powers.HackedPower;
import autoplaycharactermod.relics.BaseRelic;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import static autoplaycharactermod.BasicMod.makeID;

public class SneckoAI extends BaseRelic {
    private static final String NAME = "SneckoAI"; //The name will be used for determining the image file as well as the ID.
    public static final String ID = makeID(NAME); //This adds the mod's prefix to the relic ID, resulting in modID:MyRelic
    private static final RelicTier RARITY = RelicTier.BOSS; //The relic's rarity.
    private static final LandingSound SOUND = LandingSound.FLAT; //The sound played when the relic is clicked.

    public SneckoAI() {
        super(ID, NAME, MyCharacter.Meta.CARD_COLOR, RARITY, SOUND);
    }

    public void atPreBattle() {
        this.flash();
        this.addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new HackedPower(AbstractDungeon.player)));
    }

    public void onEquip() {
        AbstractDungeon.player.energy.energyMaster += 1;
    }

    public void onUnequip() {
        AbstractDungeon.player.energy.energyMaster -= 1;
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }
}
