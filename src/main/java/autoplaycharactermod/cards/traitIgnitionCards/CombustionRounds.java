package autoplaycharactermod.cards.traitIgnitionCards;

import autoplaycharactermod.actions.DamageCurrentTargetAction;
import autoplaycharactermod.cards.TraitCard;
import autoplaycharactermod.character.PilotCharacter;
import autoplaycharactermod.util.CardStats;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.cards.status.Dazed;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.ArtifactPower;

public class CombustionRounds extends TraitCard {
    public static final String ID = makeID("CombustionRounds");
    private static final CardStats info = new CardStats(
            PilotCharacter.Meta.CARD_COLOR,
            CardType.ATTACK,
            CardRarity.COMMON,
            CardTarget.NONE,
            -2 
    );
    private static final int DAMAGE = 10;
    private static final int UPG_DAMAGE = 4;

    public CombustionRounds() {
        super(ID, info, TraitColor.IGNITE, false);
        setDamage(DAMAGE, UPG_DAMAGE);
        checkEvolve();
    }

    @Override
    public void evolveCard() {
        setDamage(14);
        super.evolveCard();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new DamageCurrentTargetAction(this, AbstractGameAction.AttackEffect.FIRE));
        if (this.alreadyEvolved) {
            addToBot(new ApplyPowerAction(p, p, new ArtifactPower(p, 1)));
        } else {
            addToBot(new MakeTempCardInDrawPileAction(new Dazed(), 1, true, true));
        }
        PlayOnce = false;
        super.use(p, m);
    }
}
