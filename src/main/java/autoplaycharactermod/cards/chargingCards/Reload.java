package autoplaycharactermod.cards.chargingCards;

import autoplaycharactermod.ThePilotMod;
import autoplaycharactermod.actions.AutoplayTopCardAction;
import autoplaycharactermod.cards.BaseCard;
import autoplaycharactermod.character.PilotCharacter;
import autoplaycharactermod.powers.SavePower;
import autoplaycharactermod.util.CardStats;
import basemod.helpers.TooltipInfo;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DiscardAction;
import com.megacrit.cardcrawl.actions.unique.DiscardPileToTopOfDeckAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.ArrayList;
import java.util.List;

public class Reload extends BaseCard {
    public static final String ID = makeID("Reload");
    private static final CardStats info = new CardStats(
            PilotCharacter.Meta.CARD_COLOR,
            CardType.SKILL,
            CardRarity.UNCOMMON,
            CardTarget.SELF,
            0
    );
    private static final int MAGIC = 6;
    private static final int UPG_MAGIC = 4;

    public Reload() {
        super(ID, info);
        returnToHand = true;
        setMagic(MAGIC, UPG_MAGIC);
        tags.add(ThePilotMod.CustomTags.NoEnergyText);
        checkEvolve();
    }

    @Override
    public void evolveCard() {
        setMagic(20);
        super.evolveCard();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (PlayOnce) {
            PlayOnce = false;
            returnToHand = true;
            addToBot(new ApplyPowerAction(p, p, new SavePower(p, magicNumber)));
        } else {
            returnToHand = false;
            addToBot(new DiscardAction(p, p, 1, false));
            addToBot(new DiscardPileToTopOfDeckAction(p));
            if (this.alreadyEvolved && !p.discardPile.isEmpty()) {
                addToBot(new AutoplayTopCardAction());
            }
        }
    }

    @Override
    public List<TooltipInfo> getCustomTooltips() {
        ArrayList<TooltipInfo> customTooltips = new ArrayList<>();
        customTooltips.add(new TooltipInfo(ThePilotMod.keywords.get("Charge").PROPER_NAME, ThePilotMod.keywords.get("Charge").DESCRIPTION));
        return customTooltips;
    }

    @Override
    public boolean freeToPlay() {
        return true;
    }

}
