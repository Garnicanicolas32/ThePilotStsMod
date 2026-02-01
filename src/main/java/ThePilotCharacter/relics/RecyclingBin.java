package ThePilotCharacter.relics;

import ThePilotCharacter.cards.scrap.ScrapCommon;
import ThePilotCharacter.cards.scrap.ScrapCommonDef;
import ThePilotCharacter.character.PilotCharacter;
import com.evacipated.cardcrawl.mod.stslib.relics.CardRewardSkipButtonRelic;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.UpgradeShineEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;

import static ThePilotCharacter.ThePilotMod.makeID;

public class RecyclingBin extends BaseRelic implements CardRewardSkipButtonRelic {
    private static final String NAME = "RecyclingBin"; //The name will be used for determining the image file as well as the ID.
    public static final String ID = makeID(NAME); //This adds the mod's prefix to the relic ID, resulting in modID:MyRelic
    private static final RelicTier RARITY = RelicTier.UNCOMMON; //The relic's rarity.
    private static final LandingSound SOUND = LandingSound.CLINK; //The sound played when the relic is clicked.

    public RecyclingBin() {
        super(ID, NAME, PilotCharacter.Meta.CARD_COLOR, RARITY, SOUND);
        counter = 0;
    }

    @Override
    public void onClickedButton() {
        flash();
        if (counter > 0) {
            AbstractCard c = AbstractDungeon.cardRandomRng.randomBoolean() ? new ScrapCommon() : new ScrapCommonDef();
            AbstractDungeon.effectsQueue.add(new UpgradeShineEffect(Settings.WIDTH / 2.0F, (float) Settings.HEIGHT / 2.0F - 340.0F * Settings.scale));
            AbstractDungeon.effectsQueue.add(new ShowCardAndObtainEffect(c, (float) Settings.WIDTH / 2.0F, (float) Settings.HEIGHT / 2.0F - 340.0F * Settings.scale));
            counter = 0;
        } else
            counter++;
    }

    @Override
    public String getButtonLabel() {
        return DESCRIPTIONS[1] + counter + DESCRIPTIONS[2];
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }
}
