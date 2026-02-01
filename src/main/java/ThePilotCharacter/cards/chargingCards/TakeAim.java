package ThePilotCharacter.cards.chargingCards;

import ThePilotCharacter.ThePilotMod;
import ThePilotCharacter.actions.ScryWithChargeAction;
import ThePilotCharacter.cards.BaseCard;
import ThePilotCharacter.character.PilotCharacter;
import ThePilotCharacter.util.CardStats;
import com.megacrit.cardcrawl.actions.common.EmptyDeckShuffleAction;
import com.megacrit.cardcrawl.actions.common.ShuffleAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class TakeAim extends BaseCard {
    public static final String ID = makeID("TakeAim");
    private static final CardStats info = new CardStats(
            PilotCharacter.Meta.CARD_COLOR,
            CardType.SKILL,
            CardRarity.RARE,
            CardTarget.SELF,
            0 
    );
    private static final int MAGIC = 7;
    private static final int UPG_MAGIC = 5;
    private static final int CHARGE = 3;

    public TakeAim() {
        super(ID, info);
        returnToHand = true;
        setMagic(MAGIC, UPG_MAGIC);
        setCustomVar("CHARGE", CHARGE);
        tags.add(ThePilotMod.CustomTags.NoEnergyText);
        checkEvolve();
    }

    @Override
    public void evolveCard() {
        setMagic(30);
        setCustomVar("CHARGE", 4);
        super.evolveCard();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (PlayOnce && !Duplicated) {
            PlayOnce = false;
            returnToHand = true;
            addToBot(new EmptyDeckShuffleAction());
            this.addToBot(new ShuffleAction(AbstractDungeon.player.drawPile, false));
        } else {
            addToBot(new ScryWithChargeAction(magicNumber, customVar("CHARGE")));
            setExhaust(true);
            returnToHand = false;
        }
    }

    @Override
    public boolean freeToPlay() {
        return true;
    }
}
