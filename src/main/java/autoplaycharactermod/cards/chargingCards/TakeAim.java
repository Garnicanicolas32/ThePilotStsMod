package autoplaycharactermod.cards.chargingCards;

import autoplaycharactermod.BasicMod;
import autoplaycharactermod.actions.ScryWithChargeAction;
import autoplaycharactermod.cards.BaseCard;
import autoplaycharactermod.character.MyCharacter;
import autoplaycharactermod.util.CardStats;
import com.megacrit.cardcrawl.actions.common.EmptyDeckShuffleAction;
import com.megacrit.cardcrawl.actions.common.ShuffleAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class TakeAim extends BaseCard {
    public static final String ID = makeID("TakeAim");
    private static final CardStats info = new CardStats(
            MyCharacter.Meta.CARD_COLOR,
            CardType.SKILL,
            CardRarity.RARE,
            CardTarget.SELF,
            0 
    );
    private static final int MAGIC = 9;
    private static final int UPG_MAGIC = 6;
    private static final int CHARGE = 3;
    private static final int CHARGE_UPG = -1;

    public TakeAim() {
        super(ID, info);
        returnToHand = true;
        setMagic(MAGIC, UPG_MAGIC);
        setCustomVar("CHARGE", CHARGE, CHARGE_UPG);
        tags.add(BasicMod.CustomTags.NoEnergyText);
        checkEvolve();
    }

    @Override
    public void evolveCard() {
        setMagic(30);
        super.evolveCard();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (PlayOnce) {
            PlayOnce = false;
            returnToHand = true;
            addToBot(new EmptyDeckShuffleAction());
            this.addToBot(new ShuffleAction(AbstractDungeon.player.drawPile, false));
        } else {
            addToBot(new ScryWithChargeAction(magicNumber, alreadyEvolved ? 4 : customVar("CHARGE")));
            setExhaust(true);
            returnToHand = false;
        }
    }

    @Override
    public boolean freeToPlay() {
        return true;
    }
}
