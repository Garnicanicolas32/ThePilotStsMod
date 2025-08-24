package autoplaycharactermod.cards.equipment;

import autoplaycharactermod.actions.DamageCurrentTargetAction;
import autoplaycharactermod.cards.EquipmentCard;
import autoplaycharactermod.character.MyCharacter;
import autoplaycharactermod.util.CardStats;
import autoplaycharactermod.vfx.MicroMissilesEffect;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class MicroMissiles extends EquipmentCard {
    public static final String ID = makeID("MicroMissiles");

    private static final CardStats info = new CardStats(
            MyCharacter.Meta.CARD_COLOR,
            CardType.ATTACK,
            CardRarity.UNCOMMON,
            CardTarget.NONE,
            0
    );
    private static final int DAMAGE = 3;
    private static final int UPG_DAMAGE = 1;
    private static final int MAGIC = 4;
    private static final int UPG_MAGIC = -1;
    private static final int BASE_HP = 14;


    public MicroMissiles() {
        super(ID, info, BASE_HP);
        setDamage(DAMAGE, UPG_DAMAGE);
        setMagic(MAGIC, UPG_MAGIC);
        checkEvolve();
    }

    @Override
    public void evolveCard() {
        setDamage(5);
        setMagic(2);
        super.evolveCard();
    }

    @Override
    public void Activate() {
        if (!Equipped) return;
        AbstractPlayer p = AbstractDungeon.player;
        AbstractMonster m = MyCharacter.getTarget();

        int loops = (p.drawPile.size() + p.hand.size() + p.discardPile.size()) / magicNumber;
        if (loops > 0)
            this.addToBot(new VFXAction(new MicroMissilesEffect(this.hb.cX, this.hb.cY, m.hb.cX, m.hb.cY), 0.5F));
        for (int i = 0; i < loops; ++i) {
            calculateCardDamage(m);
            addToBot(new DamageCurrentTargetAction(this, AbstractGameAction.AttackEffect.NONE));
        }

        super.Activate();
    }

    @Override
    protected int getUpgradeDurability() {
        return 6;
    }

    public void triggerOnShuffle() {
        Activate();
    }


}
