package autoplaycharactermod.cards.chargingCards;

import autoplaycharactermod.BasicMod;
import autoplaycharactermod.cards.BaseCard;
import autoplaycharactermod.character.MyCharacter;
import autoplaycharactermod.powers.ChargePower;
import autoplaycharactermod.powers.SavePower;
import autoplaycharactermod.util.CardStats;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DiscardAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class DefenseMatrix extends BaseCard {
    public static final String ID = makeID("DefenseMatrix");
    private static final CardStats info = new CardStats(
            MyCharacter.Meta.CARD_COLOR,
            CardType.SKILL,
            CardRarity.UNCOMMON,
            CardTarget.SELF,
            0
    );
    private static final int MAGIC = 6;
    private static final int UPG_MAGIC = 4;

    public DefenseMatrix() {
        super(ID, info);
        returnToHand = true;
        setMagic(MAGIC, UPG_MAGIC);
        tags.add(BasicMod.CustomTags.NoEnergyText);
        checkEvolve();
    }

    @Override
    public void evolveCard() {
        super.evolveCard();
        setMagic(10);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (PlayOnce) {
            PlayOnce = false;
            returnToHand = true;
            addToBot(new ApplyPowerAction(p, p, new SavePower(p, magicNumber)));
        } else {
            addToBot(new DiscardAction(p, p, 1, false));
            AbstractPower pwr = p.getPower(ChargePower.POWER_ID);
            int number = (pwr != null) ? pwr.amount : 0;
            if (number > 0) {
                if (this.alreadyEvolved)
                    addToBot(new GainBlockAction(p, p, number * 2));
                else
                    addToBot(new GainBlockAction(p, p, number / 2));
            }
            returnToHand = false;
        }
    }

    @Override
    public boolean freeToPlay() {
        return true;
    }
}
