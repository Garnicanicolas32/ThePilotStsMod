package autoplaycharactermod.cards.traitIgnitionCards;

import autoplaycharactermod.actions.DamageCurrentTargetAction;
import autoplaycharactermod.cards.TraitCard;
import autoplaycharactermod.character.MyCharacter;
import autoplaycharactermod.util.CardStats;
import basemod.BaseMod;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ExhaustAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class ThermalSurge extends TraitCard {
    public static final String ID = makeID("ThermalSurge");
    private static final CardStats info = new CardStats(
            MyCharacter.Meta.CARD_COLOR,
            CardType.ATTACK,
            CardRarity.UNCOMMON,
            CardTarget.NONE,
            -2
    );
    private static final int DAMAGE = 6;
    private static final int UPG_DAMAGE = 2;
    private static final int MAGIC = 1;
    private static final int UPG_MAGIC = 1;

    public ThermalSurge() {
        super(ID, info, TraitCard.TraitColor.IGNITE, false);
        setDamage(DAMAGE, UPG_DAMAGE);
        setMagic(MAGIC, UPG_MAGIC);
        checkEvolve();
    }

    @Override
    public void evolveCard() {
        setDamage(12);
        super.evolveCard();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new DamageCurrentTargetAction(this, AbstractGameAction.AttackEffect.FIRE));
        if (this.alreadyEvolved) {
            addToBot(new ExhaustAction(BaseMod.MAX_HAND_SIZE, false, true, true));
        } else {
            addToBot(new ExhaustAction(magicNumber, false, true, upgraded));
        }
        PlayOnce = false;
        super.use(p, m);
    }
}
