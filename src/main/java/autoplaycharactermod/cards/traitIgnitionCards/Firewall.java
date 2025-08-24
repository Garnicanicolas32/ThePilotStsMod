package autoplaycharactermod.cards.traitIgnitionCards;

import autoplaycharactermod.cards.BaseCard;
import autoplaycharactermod.character.MyCharacter;
import autoplaycharactermod.powers.FirewallPower;
import autoplaycharactermod.util.CardStats;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class Firewall extends BaseCard {
    public static final String ID = makeID("Firewall");
    private static final CardStats info = new CardStats(
            MyCharacter.Meta.CARD_COLOR,
            CardType.POWER,
            CardRarity.UNCOMMON,
            CardTarget.SELF,
            -2
    );
    private static final int MAGIC = 3;
    private static final int UPG_MAGIC = 2;

    public Firewall() {
        super(ID, info);
        setMagic(MAGIC, UPG_MAGIC);
        checkEvolve();
    }

    @Override
    public void evolveCard() {
        setMagic(10);
        super.evolveCard();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(p, p, new FirewallPower(p, magicNumber)));
    }
}
