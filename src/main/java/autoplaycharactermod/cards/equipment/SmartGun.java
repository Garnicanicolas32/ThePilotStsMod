package autoplaycharactermod.cards.equipment;

import autoplaycharactermod.actions.DamageCurrentTargetAction;
import autoplaycharactermod.actions.SfxActionVolume;
import autoplaycharactermod.actions.SmartGunLocateAnimation;
import autoplaycharactermod.cards.EquipmentCard;
import autoplaycharactermod.character.MyCharacter;
import autoplaycharactermod.ui.ConfigPanel;
import autoplaycharactermod.util.CardStats;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class SmartGun extends EquipmentCard {
    public static final String ID = makeID("SmartGun");

    private static final CardStats info = new CardStats(
            MyCharacter.Meta.CARD_COLOR,
            CardType.ATTACK,
            CardRarity.UNCOMMON,
            CardTarget.NONE,
            0
    );
    private static final int BASE_HP = 66;
    private static final int DAMAGE = 4;
    private static final int UPG_DAMAGE = 2;


    public SmartGun() {
        super(ID, info, BASE_HP);
        setDamage(DAMAGE, UPG_DAMAGE);
        checkEvolve();
    }

    @Override
    public void evolveCard() {
        setInnate(true);
        setDamage(12);
        super.evolveCard();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (equipmentHp < 1) {
            this.setExhaust(true);
        }
        if (PlayOnce) {
            Equipped = true;
            returnToHand = true;
            addToBot(new SFXAction("ORB_FROST_DEFEND_1"));
        } else {
            Equipped = false;
            returnToHand = false;
        }
        PlayOnce = false;
    }

    @Override
    public void Activate() {
        if (!Equipped) return;
        AbstractPlayer p = AbstractDungeon.player;
        AbstractMonster m = MyCharacter.getTarget();

        calculateCardDamage(m);
        if (ConfigPanel.experimentalSounds)
            addToBot(new SfxActionVolume("SPHERE_DETECT_VO_2", MathUtils.random(0.3f, 0.9F), 3.5F));
        if (!ConfigPanel.lessParticles)
            addToBot(new SmartGunLocateAnimation(this));
        addToBot(new DamageCurrentTargetAction(this, AbstractGameAction.AttackEffect.BLUNT_LIGHT));
        super.Activate();
    }

    @Override
    protected int getUpgradeDurability() {
        return 33;
    }
}
