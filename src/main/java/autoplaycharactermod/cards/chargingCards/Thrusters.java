package autoplaycharactermod.cards.chargingCards;

import autoplaycharactermod.cards.BaseCard;
import autoplaycharactermod.character.PilotCharacter;
import autoplaycharactermod.powers.ChargePower;
import autoplaycharactermod.powers.OneLessEnergyPower;
import autoplaycharactermod.util.CardStats;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.utility.ScryAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class Thrusters extends BaseCard {
    public static final String ID = makeID("Thrusters");
    private static final CardStats info = new CardStats(
            PilotCharacter.Meta.CARD_COLOR,
            CardType.SKILL,
            CardRarity.COMMON,
            CardTarget.SELF,
            -2
    );
    private static final int MAGIC = 6;
    private static final int UPG_MAGIC = 3;

    public Thrusters() {
        super(ID, info);
        returnToHand = true;
        setMagic(MAGIC, UPG_MAGIC);
        setCustomVar("SCRY", 2, 1);
        checkEvolve();
    }
    @Override
    public void evolveCard() {
        setCustomVar("SCRY", 5);
        setMagic(12);
        super.evolveCard();

    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ScryAction(customVar("SCRY")));
        addToBot(new ApplyPowerAction(p, p, new ChargePower(p, magicNumber)));
        addToBot(new GainEnergyAction(1));
        if (!this.alreadyEvolved)
            addToBot(new ApplyPowerAction(p, p, new OneLessEnergyPower(p, 1)));
        PlayOnce = false;
    }

    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        return PlayOnce && super.canUse(p, m);
    }
}
