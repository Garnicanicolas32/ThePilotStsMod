package autoplaycharactermod.cards.equipment;

import autoplaycharactermod.BasicMod;
import autoplaycharactermod.actions.RustyBladeAction;
import autoplaycharactermod.cards.EquipmentCard;
import autoplaycharactermod.character.MyCharacter;
import autoplaycharactermod.util.CardStats;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class RustyBlade extends EquipmentCard {
    public static final String ID = makeID("RustyBlade");
    private static final CardStats info = new CardStats(
            MyCharacter.Meta.CARD_COLOR,
            CardType.ATTACK,
            CardRarity.COMMON,
            CardTarget.NONE,
            0
    );
    private static final int BASE_HP = 10;
    private static final int DAMAGE = 4;
    private static final int DAMAGE_UPG = 4;


    public RustyBlade() {
        super(ID, info, BASE_HP);
        setDamage(DAMAGE, DAMAGE_UPG);
        checkEvolve();
        this.tags.remove(BasicMod.CustomTags.ignoreDuplication);
    }

    @Override
    public void evolveCard() {
        setDamage(10);
        super.evolveCard();
    }

    protected void onEquip() {
        Activate();
    }

    @Override
    public void Activate() {
        if (!Equipped) return;
        AbstractMonster m = MyCharacter.getTarget();
        calculateCardDamage(m);
        addToBot(new RustyBladeAction(this, AbstractGameAction.AttackEffect.POISON));
        super.Activate();
    }

    public void triggerOnEndOfTurnForPlayingCard() {
        if (alreadyEvolved)
            Activate();
    }

    public void atTurnStart() {
        Activate();
    }
}
