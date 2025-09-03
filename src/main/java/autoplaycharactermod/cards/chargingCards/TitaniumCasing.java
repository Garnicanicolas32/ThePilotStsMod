package autoplaycharactermod.cards.chargingCards;

import autoplaycharactermod.BasicMod;
import autoplaycharactermod.actions.SfxActionVolume;
import autoplaycharactermod.cards.BaseCard;
import autoplaycharactermod.character.MyCharacter;
import autoplaycharactermod.powers.SavePower;
import autoplaycharactermod.util.CardStats;
import basemod.helpers.TooltipInfo;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.unique.DoubleYourBlockAction;
import com.megacrit.cardcrawl.actions.unique.TripleYourBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;

import java.util.ArrayList;
import java.util.List;

public class TitaniumCasing extends BaseCard {
    public static final String ID = makeID("TitaniumCasing");
    private static final CardStats info = new CardStats(
            MyCharacter.Meta.CARD_COLOR,
            CardType.SKILL,
            CardRarity.UNCOMMON,
            CardTarget.SELF,
            -2
    );
    private static final int MAGIC = 5;
    private static final int UPG_MAGIC = 5;

    public TitaniumCasing() {
        super(ID, info);
        returnToHand = true;
        setMagic(MAGIC, UPG_MAGIC);
        checkEvolve();
    }

    @Override
    public void evolveCard() {
        super.evolveCard();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractPower pwr = p.getPower(SavePower.POWER_ID);
        int number = (pwr != null) ? pwr.amount : 0;
        if (number > 0){
            if (alreadyEvolved){
                number *= 2;
            } else if(!upgraded){
                number = number / 2;
            }
            addToBot(new ApplyPowerAction(p, p, new SavePower(p, number)));
        }
        addToBot(new SfxActionVolume("SHOVEL", 0f,1.6F));
        if (this.alreadyEvolved)
            addToBot(new TripleYourBlockAction(p));
        else if (upgraded)
            addToBot(new DoubleYourBlockAction(p));
        else
            addToBot(new GainBlockAction(p,p,p.currentBlock / 2));
        PlayOnce = false;
    }

    @Override
    public List<TooltipInfo> getCustomTooltips() {
        ArrayList<TooltipInfo> customTooltips = new ArrayList<>();
        customTooltips.add(new TooltipInfo(BasicMod.keywords.get("Charge").PROPER_NAME, BasicMod.keywords.get("Charge").DESCRIPTION));
        return customTooltips;
    }

    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        return PlayOnce && super.canUse(p, m);
    }
}
