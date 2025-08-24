package autoplaycharactermod.cards.chargingCards;

import autoplaycharactermod.actions.DamageCurrentTargetAction;
import autoplaycharactermod.cards.BaseCard;
import autoplaycharactermod.character.MyCharacter;
import autoplaycharactermod.util.CardStats;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.utility.ScryAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class PreemptiveStrike extends BaseCard {
    public static final String ID = makeID("PreemptiveStrike");
    private static final CardStats info = new CardStats(
            MyCharacter.Meta.CARD_COLOR,
            CardType.ATTACK,
            CardRarity.COMMON,
            CardTarget.NONE,
            -2 
    );
    private static final int MAGIC = 3;
    private static final int UPG_MAGIC = 1;
    private static final int DAMAGE = 6;
    private static final int UPG_DAMAGE = 3;

    public PreemptiveStrike() {
        super(ID, info);
        returnToHand = true;
        setMagic(MAGIC, UPG_MAGIC);
        setDamage(DAMAGE, UPG_DAMAGE);
        tags.add(CardTags.STRIKE);
        checkEvolve();
    }

    @Override
    public void evolveCard() {
        setMagic(6);
        setDamage(14);
        super.evolveCard();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new DamageCurrentTargetAction(this, AbstractGameAction.AttackEffect.BLUNT_LIGHT));
        addToBot(new ScryAction(magicNumber));
        PlayOnce = false;
    }

    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        return PlayOnce && super.canUse(p, m);
    }
}
