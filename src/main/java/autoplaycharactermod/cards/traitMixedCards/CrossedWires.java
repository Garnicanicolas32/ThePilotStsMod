package autoplaycharactermod.cards.traitMixedCards;

import autoplaycharactermod.actions.AutoPlayTraitCards;
import autoplaycharactermod.actions.AutoplayTopCardAction;
import autoplaycharactermod.cards.TraitCard;
import autoplaycharactermod.character.MyCharacter;
import autoplaycharactermod.util.CardStats;
import autoplaycharactermod.util.GeneralUtils;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDiscardAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.status.Burn;
import com.megacrit.cardcrawl.cards.status.VoidCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class CrossedWires extends TraitCard {
    public static final String ID = makeID("CrossedWires");
    private static final CardStats info = new CardStats(
            MyCharacter.Meta.CARD_COLOR,
            CardType.SKILL,
            CardRarity.RARE,
            CardTarget.SELF,
            -2 
    );

    public CrossedWires() {
        super(ID, info, TraitCard.TraitColor.OTHER, false);
        setMagic(2,2);
        checkEvolve();
    }

    @Override
    public void evolveCard() {
        countsTwiceOnUpgrade = true;
        setMagic(5);
        super.evolveCard();

    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        PlayOnce = false;
        for (int i = 0; i < magicNumber; i++) {
            switch (AbstractDungeon.cardRandomRng.random(2)){
                case 0:
                    addToBot(new MakeTempCardInDrawPileAction(GeneralUtils.getRandomIgitionCard(), 1, true, true));
                    break;
                case 1:
                    addToBot(new MakeTempCardInDrawPileAction(GeneralUtils.getRandomBastionCard(), 1, true, true));
                    break;
                case 2:
                    addToBot(new MakeTempCardInDrawPileAction(GeneralUtils.getRandomScavengeCard(), 1, true, true));
                    break;
            }
        }
        super.use(p, m);
    }


}
