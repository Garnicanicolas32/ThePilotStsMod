package ThePilotCharacter.cards.traitBastionCards;

import ThePilotCharacter.cards.BaseCard;
import ThePilotCharacter.character.PilotCharacter;
import ThePilotCharacter.powers.FeedbackLoopPower;
import ThePilotCharacter.util.CardStats;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class FeedbackLoop extends BaseCard {
    public static final String ID = makeID("FeedbackLoop");
    private static final CardStats info = new CardStats(
            PilotCharacter.Meta.CARD_COLOR,
            CardType.POWER,
            CardRarity.RARE,
            CardTarget.SELF,
            -2
    );

    private static final int MAGIC = 3;
    private static final int UPG_MAGIC = 1;

    public FeedbackLoop() {
        super(ID, info);
        setMagic(MAGIC, UPG_MAGIC);
        checkEvolve();
    }

    @Override
    public void evolveCard() {
        setMagic(9);
        super.evolveCard();

    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(p, p, new FeedbackLoopPower(p, magicNumber)));
    }
}
