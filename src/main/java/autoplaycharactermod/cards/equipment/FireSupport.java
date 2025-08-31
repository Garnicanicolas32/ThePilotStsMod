package autoplaycharactermod.cards.equipment;

import autoplaycharactermod.actions.DamageCurrentTargetAction;
import autoplaycharactermod.actions.SfxActionVolume;
import autoplaycharactermod.cards.EquipmentCard;
import autoplaycharactermod.character.MyCharacter;
import autoplaycharactermod.ui.ConfigPanel;
import autoplaycharactermod.util.CardStats;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.SweepingBeamEffect;

public class FireSupport extends EquipmentCard {
    public static final String ID = makeID("FireSupport");

    private static final CardStats info = new CardStats(
            MyCharacter.Meta.CARD_COLOR,
            CardType.ATTACK,
            CardRarity.COMMON,
            CardTarget.NONE,
            0
    );
    private static final int BASE_HP = 35;
    private static final int DAMAGE = 4;
    private static final int UPG_DAMAGE = 2;

    public FireSupport() {
        super(ID, info, BASE_HP);
        setDamage(DAMAGE, UPG_DAMAGE);
        checkEvolve();
    }

    @Override
    public void evolveCard() {
        setDamage(9);
        super.evolveCard();
    }

    @Override
    public void Activate() {
        if (!Equipped) return;
        AbstractPlayer p = AbstractDungeon.player;
        AbstractMonster m = MyCharacter.getTarget();

        calculateCardDamage(m);
        if (ConfigPanel.experimentalSounds)
            this.addToBot(new SfxActionVolume("ATTACK_DEFECT_BEAM", -0.25F, 1F));
        this.addToBot(new VFXAction(p, new SweepingBeamEffect(this.hb.cX, this.hb.cY, AbstractDungeon.player.flipHorizontal), 0.4F));
        addToBot(new DamageCurrentTargetAction(this, AbstractGameAction.AttackEffect.FIRE));

        super.Activate();
    }

    public void onBetterScry() {
        Activate();
    }

    @Override
    protected int getUpgradeDurability() {
        return 10;
    }
}
