package ThePilotCharacter.cards.chargingCards;

import ThePilotCharacter.ThePilotMod;
import ThePilotCharacter.cards.BaseCard;
import ThePilotCharacter.character.PilotCharacter;
import ThePilotCharacter.powers.SavePower;
import ThePilotCharacter.util.CardStats;
import basemod.helpers.TooltipInfo;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.ExhaustAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.ArrayList;
import java.util.List;

public class ExposedWire extends BaseCard {
    public static final String ID = makeID("ExposedWire");
    private static final CardStats info = new CardStats(
            PilotCharacter.Meta.CARD_COLOR,
            CardType.SKILL,
            CardRarity.UNCOMMON,
            CardTarget.SELF,
            -2 
    );
    private static final int MAGIC = 7;
    private static final int UPG_MAGIC = 0;

    public ExposedWire() {
        super(ID, info);
        returnToHand = true;
        setMagic(MAGIC, UPG_MAGIC);
        setSelfRetain(true);
        checkEvolve();
    }

    @Override
    public void evolveCard() {
        setMagic(15);
        setSelfRetain(false);
        super.evolveCard();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(p, p, new SavePower(p, magicNumber)));
        addToBot(new GainEnergyAction(upgraded || alreadyEvolved ? 3 : 2));
        if (!this.alreadyEvolved)
            addToBot(new ExhaustAction(1, true, false, false));
        PlayOnce = false;
    }

    @Override
    public List<TooltipInfo> getCustomTooltips() {
        ArrayList<TooltipInfo> customTooltips = new ArrayList<>();
        customTooltips.add(new TooltipInfo(ThePilotMod.keywords.get("Charge").PROPER_NAME, ThePilotMod.keywords.get("Charge").DESCRIPTION));
        return customTooltips;
    }

    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        return PlayOnce && super.canUse(p, m);
    }
}
