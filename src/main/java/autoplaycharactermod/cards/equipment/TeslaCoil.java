package autoplaycharactermod.cards.equipment;

import autoplaycharactermod.actions.DamageCurrentTargetAction;
import autoplaycharactermod.cards.EquipmentCard;
import autoplaycharactermod.character.MyCharacter;
import autoplaycharactermod.util.CardStats;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;

public class TeslaCoil extends EquipmentCard {
    public static final String ID = makeID("TeslaCoil");
    private static final CardStats info = new CardStats(
            MyCharacter.Meta.CARD_COLOR,
            CardType.ATTACK,
            CardRarity.COMMON,
            CardTarget.NONE,
            0
    );
    private static final int BASE_HP = 30;
    private static final int DAMAGE = 4;
    private static final int UPG_DAMAGE = 2;
    private static final int MAGIC = 1;
    private static final int UPG_MAGIC = 1;

    public TeslaCoil() {
        super(ID, info, BASE_HP);
        setDamage(DAMAGE, UPG_DAMAGE);
        setMagic(MAGIC, UPG_MAGIC);
        checkEvolve();
    }

    @Override
    public void evolveCard() {
        setDamage(14);
        super.evolveCard();
    }

    @Override
    public void Activate() {
        if (!Equipped) return;
        addToBot(new DamageCurrentTargetAction(this, AbstractGameAction.AttackEffect.LIGHTNING));
        super.Activate();
    }

    @Override
    protected int getUpgradeDurability() {
        return 5;
    }

    public void didDiscard() {
        if (Equipped){
            flash(Color.GREEN.cpy());
            healEquipment(magicNumber, false, true);
        }
    }

    public void atTurnStart() {
        Activate();
    }

    public void triggerOnEndOfTurnForPlayingCard() {
        Activate();
    }
}
