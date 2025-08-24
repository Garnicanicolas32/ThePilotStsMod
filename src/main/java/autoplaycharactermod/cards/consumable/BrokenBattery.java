package autoplaycharactermod.cards.consumable;

import autoplaycharactermod.cards.ConsumableCards;
import autoplaycharactermod.character.MyCharacter;
import autoplaycharactermod.powers.ChargePower;
import autoplaycharactermod.powers.SavePower;
import autoplaycharactermod.util.CardStats;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class BrokenBattery extends ConsumableCards {
    public static final String ID = makeID("BrokenBattery");
    private static final CardStats info = new CardStats(
            MyCharacter.Meta.CARD_COLOR,
            CardType.SKILL,
            CardRarity.COMMON,
            CardTarget.SELF,
            -2
    );
    private static final int MAGIC = 6;
    private static final int MAGIC_UPG = 6;

    public BrokenBattery() {
        super(ID, info, 5, 3);
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
