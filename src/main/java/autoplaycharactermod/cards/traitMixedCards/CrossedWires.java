package autoplaycharactermod.cards.traitMixedCards;
import autoplaycharactermod.BasicMod;
import autoplaycharactermod.cards.TraitCard;
import autoplaycharactermod.character.MyCharacter;
import autoplaycharactermod.powers.BluePower;
import autoplaycharactermod.util.CardStats;
import autoplaycharactermod.util.GeneralUtils;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.tempCards.Insight;
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
        setMagic(2,0);
        checkEvolve();
    }

    @Override
    public void evolveCard() {
        super.evolveCard();
        if (BasicMod.isInCombat() && AbstractDungeon.player.hand.contains(this)){
            addPower();
        }
        countsTwiceOnUpgrade = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        PlayOnce = false;
        for (int i = 0; i < magicNumber; i++) {
            AbstractCard card;
            switch (AbstractDungeon.cardRandomRng.random(2)){
                case 0:
                    card = GeneralUtils.getRandomIgitionCard();
                    break;
                case 1:
                    card = GeneralUtils.getRandomBastionCard();
                    break;
                default:
                    card = GeneralUtils.getRandomScavengeCard();
                    break;
            }
            if (upgraded || alreadyEvolved){
                card.upgrade();
            }
            addToBot(new MakeTempCardInDrawPileAction(card,1,true,true,false));
        }
        super.use(p, m);
    }


}
