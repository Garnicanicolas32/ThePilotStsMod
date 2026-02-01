package ThePilotCharacter.cards.consumable;

import ThePilotCharacter.cards.ConsumableCards;
import ThePilotCharacter.character.PilotCharacter;
import ThePilotCharacter.powers.ChargePower;
import ThePilotCharacter.powers.SavePower;
import ThePilotCharacter.util.CardStats;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class BrokenBattery extends ConsumableCards {
    public static final String ID = makeID("BrokenBattery");
    private static final CardStats info = new CardStats(
            PilotCharacter.Meta.CARD_COLOR,
            CardType.SKILL,
            CardRarity.COMMON,
            CardTarget.SELF,
            -2
    );
    private static final int MAGIC = 7;
    private static final int MAGIC_UPG = 4;

    public BrokenBattery() {
        super(ID, info, 4, 3);
        setMagic(MAGIC, MAGIC_UPG);
        checkEvolve();
    }

    @Override
    public void evolveCard() {
        setMagic(22);
        super.evolveCard();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(p, p, new ChargePower(p, magicNumber)));
        addToBot(new ApplyPowerAction(p, p, new SavePower(p, magicNumber)));
        super.use(p, m);
    }
}
