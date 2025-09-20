package autoplaycharactermod.cards.traitIgnitionCards;

import autoplaycharactermod.actions.DamageCurrentTargetAction;
import autoplaycharactermod.actions.ModifiedCardInHandAction;
import autoplaycharactermod.cards.TraitCard;
import autoplaycharactermod.cards.statusAndCurses.Melt;
import autoplaycharactermod.character.PilotCharacter;
import autoplaycharactermod.util.CardStats;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDiscardAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class BurningPayload extends TraitCard {
    public static final String ID = makeID("BurningPayload");
    private static final CardStats info = new CardStats(
            PilotCharacter.Meta.CARD_COLOR,
            CardType.ATTACK,
            CardRarity.UNCOMMON,
            CardTarget.NONE,
            -2 
    );
    private static final int DAMAGE = 4;
    private static final int UPG_DAMAGE = 2;

    public BurningPayload() {
        super(ID, info, TraitColor.IGNITE, false);
        setDamage(DAMAGE, UPG_DAMAGE);
        checkEvolve();
        if (!alreadyEvolved)
            cardsToPreview = new Melt();

    }

    @Override
    public void evolveCard() {
        setDamage(8);
        super.evolveCard();

    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new DamageCurrentTargetAction(this, AbstractGameAction.AttackEffect.BLUNT_HEAVY));
        addToBot(new DamageCurrentTargetAction(this, AbstractGameAction.AttackEffect.BLUNT_HEAVY));
        if (this.alreadyEvolved) {
            addToBot(new DamageCurrentTargetAction(this, AbstractGameAction.AttackEffect.BLUNT_HEAVY));
            addToBot(new MakeTempCardInDiscardAction(makeStatEquivalentCopy(), 1));
        } else {
            addToBot(new ModifiedCardInHandAction(new Melt(), 1));
        }
        addToBot(new MakeTempCardInDiscardAction(makeStatEquivalentCopy(), 1));

        PlayOnce = false;
        super.use(p, m);
    }
}
