package ThePilotCharacter.relics;

import ThePilotCharacter.cards.EquipmentCard;
import ThePilotCharacter.character.PilotCharacter;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static ThePilotCharacter.ThePilotMod.makeID;

public class Motherboard extends BaseRelic {
    private static final String NAME = "Motherboard"; //The name will be used for determining the image file as well as the ID.
    public static final String ID = makeID(NAME); //This adds the mod's prefix to the relic ID, resulting in modID:MyRelic
    private static final RelicTier RARITY = RelicTier.BOSS; //The relic's rarity.
    private static final LandingSound SOUND = LandingSound.HEAVY; //The sound played when the relic is clicked.
    private boolean triggeredThisTurn = false;

    public Motherboard() {
        super(ID, NAME, PilotCharacter.Meta.CARD_COLOR, RARITY, SOUND);
    }

    public void atTurnStart() {
        this.triggeredThisTurn = false;
        this.grayscale = false;
    }

    public void onPlayCard(AbstractCard c, AbstractMonster m) {
        if (!this.triggeredThisTurn && c instanceof EquipmentCard) {
            this.triggeredThisTurn = true;
            this.flash();
            this.addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));
            this.addToBot(new GainEnergyAction(1));
            this.grayscale = true;
        }
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }
}
