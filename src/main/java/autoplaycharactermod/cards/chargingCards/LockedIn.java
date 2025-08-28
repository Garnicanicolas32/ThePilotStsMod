package autoplaycharactermod.cards.chargingCards;

import autoplaycharactermod.BasicMod;
import autoplaycharactermod.cards.BaseCard;
import autoplaycharactermod.character.MyCharacter;
import autoplaycharactermod.powers.ChargePower;
import autoplaycharactermod.util.CardStats;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DiscardAction;
import com.megacrit.cardcrawl.actions.utility.ScryAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class LockedIn extends BaseCard {
    public static final String ID = makeID("LockedIn");
    private static final CardStats info = new CardStats(
            MyCharacter.Meta.CARD_COLOR,
            CardType.SKILL,
            CardRarity.UNCOMMON,
            CardTarget.SELF,
            0 
    );
    private static final int MAGIC = 8;
    private static final int UPG_MAGIC = 2;

    public LockedIn() {
        super(ID, info);
        returnToHand = true;
        setMagic(MAGIC, UPG_MAGIC);
        setCustomVar("SCRY", 4, 1);
        tags.add(BasicMod.CustomTags.NoEnergyText);
        checkEvolve();
    }

    @Override
    public void evolveCard() {
        setMagic(18);
        super.evolveCard();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (PlayOnce) {
            PlayOnce = false;
            returnToHand = true;
            addToBot(new ApplyPowerAction(p, p, new ChargePower(p, magicNumber)));
        } else {
            addToBot(new DiscardAction(p, p, this.alreadyEvolved ? 1 : 2, false));
            addToBot(new ScryAction(alreadyEvolved ? 10 : customVar("SCRY")));
            returnToHand = false;
        }
    }

    @Override
    public boolean freeToPlay() {
        return true;
    }
}
