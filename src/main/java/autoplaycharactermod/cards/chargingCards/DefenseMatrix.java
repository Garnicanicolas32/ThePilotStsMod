package autoplaycharactermod.cards.chargingCards;

import autoplaycharactermod.BasicMod;
import autoplaycharactermod.cards.BaseCard;
import autoplaycharactermod.character.MyCharacter;
import autoplaycharactermod.powers.SavePower;
import autoplaycharactermod.util.CardStats;
import basemod.helpers.TooltipInfo;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DiscardAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;

import java.util.ArrayList;
import java.util.List;

public class DefenseMatrix extends BaseCard {
    public static final String ID = makeID("DefenseMatrix");
    private static final CardStats info = new CardStats(
            MyCharacter.Meta.CARD_COLOR,
            CardType.SKILL,
            CardRarity.UNCOMMON,
            CardTarget.SELF,
            0 
    );
    private static final int MAGIC = 4;
    private static final int UPG_MAGIC = 3;

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
            AbstractPower pwr = p.getPower(SavePower.POWER_ID);
            int number = (pwr != null) ? pwr.amount : magicNumber;
            if (this.alreadyEvolved)
                addToBot(new GainBlockAction(p, p, number * 2));
            else
                addToBot(new GainBlockAction(p, p, number / 2));
            returnToHand = false;
        }
    }

    @Override
    public List<TooltipInfo> getCustomTooltips() {
        ArrayList<TooltipInfo> customTooltips = new ArrayList<>();
        customTooltips.add(new TooltipInfo(BasicMod.keywords.get("Charge").PROPER_NAME, BasicMod.keywords.get("Charge").DESCRIPTION));
        return customTooltips;
    }

    @Override
    public boolean freeToPlay() {
        return true;
    }
}
