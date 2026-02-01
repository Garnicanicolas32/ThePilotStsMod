package ThePilotCharacter.cards.chargingCards;

import ThePilotCharacter.ThePilotMod;
import ThePilotCharacter.cards.BaseCard;
import ThePilotCharacter.character.PilotCharacter;
import ThePilotCharacter.powers.SavePower;
import ThePilotCharacter.util.CardStats;
import basemod.helpers.TooltipInfo;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DiscardAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.ArrayList;
import java.util.List;

public class Maintenance extends BaseCard {
    public static final String ID = makeID("Maintenance");
    private static final CardStats info = new CardStats(
            PilotCharacter.Meta.CARD_COLOR,
            CardType.SKILL,
            CardRarity.COMMON,
            CardTarget.SELF,
            0 
    );
    private static final int MAGIC = 7;
    private static final int UPG_MAGIC = 2;
    private static final int BLOCK = 7;
    private static final int UPG_BLOCK = 2;

    public Maintenance() {
        super(ID, info);
        returnToHand = true;
        setMagic(MAGIC, UPG_MAGIC);
        setBlock(BLOCK, UPG_BLOCK);
        tags.add(ThePilotMod.CustomTags.NoEnergyText);
        checkEvolve();
    }

    @Override
    public void evolveCard() {
        setMagic(10);
        setBlock(10);
        super.evolveCard();

    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (PlayOnce) {
            PlayOnce = false;
            //stuff to do the first time played
            addToBot(new GainBlockAction(p, p, block));
            if (this.alreadyEvolved)
                addToBot(new ApplyPowerAction(p, p, new SavePower(p, magicNumber)));
            // end of stuff to do first time played
            returnToHand = true;
        } else {
            //stuff to do when echo
            addToBot(new DiscardAction(p, p, 1, false));
            addToBot(new ApplyPowerAction(p, p, new SavePower(p, magicNumber)));
            if (this.alreadyEvolved)
                addToBot(new GainBlockAction(p, p, block));
            // end of stuff to do when echo
            returnToHand = false;
        }
    }

    @Override
    public boolean freeToPlay() {
        return true;
    }

    @Override
    public List<TooltipInfo> getCustomTooltips() {
        ArrayList<TooltipInfo> customTooltips = new ArrayList<>();
        customTooltips.add(new TooltipInfo(ThePilotMod.keywords.get("Charge").PROPER_NAME, ThePilotMod.keywords.get("Charge").DESCRIPTION));
        return customTooltips;
    }


}
