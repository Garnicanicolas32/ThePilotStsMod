package ThePilotCharacter.cards.traitBastionCards;

import ThePilotCharacter.cards.TraitCard;
import ThePilotCharacter.character.PilotCharacter;
import ThePilotCharacter.util.CardStats;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.BlurPower;

public class Cryostasis extends TraitCard {
    public static final String ID = makeID("Cryostasis");
    private static final CardStats info = new CardStats(
            PilotCharacter.Meta.CARD_COLOR,
            CardType.SKILL,
            CardRarity.UNCOMMON,
            CardTarget.SELF,
            -2 
    );
    private static final int BLOCK = 4;
    private static final int UPG_BLOCK = 4;

    public Cryostasis() {
        super(ID, info, TraitCard.TraitColor.BASTION, false);
        setBlock(BLOCK, UPG_BLOCK);
        checkEvolve();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new GainBlockAction(p, p, block));
        addToBot(new ApplyPowerAction(p, p, new BlurPower(p, this.alreadyEvolved ? 3 : 1)));
        PlayOnce = false;
        super.use(p, m);
    }

    @Override
    public void evolveCard() {
        setBlock(12);
        super.evolveCard();
    }
}
