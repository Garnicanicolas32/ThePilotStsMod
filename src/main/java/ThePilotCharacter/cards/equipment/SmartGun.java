package ThePilotCharacter.cards.equipment;

import ThePilotCharacter.actions.DamageCurrentTargetAction;
import ThePilotCharacter.actions.SfxActionVolume;
import ThePilotCharacter.actions.SmartGunLocateAnimation;
import ThePilotCharacter.cards.EquipmentCard;
import ThePilotCharacter.character.PilotCharacter;
import ThePilotCharacter.ui.ConfigPanel;
import ThePilotCharacter.util.CardStats;
import basemod.helpers.TooltipInfo;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.List;

public class SmartGun extends EquipmentCard {
    public static final String ID = makeID("SmartGun");

    private static final CardStats info = new CardStats(
            PilotCharacter.Meta.CARD_COLOR,
            CardType.ATTACK,
            CardRarity.UNCOMMON,
            CardTarget.NONE,
            0
    );
    private static final int BASE_HP = 66;
    private static final int DAMAGE = 3;
    private static final int UPG_DAMAGE = 1;


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
        AbstractMonster m = PilotCharacter.getTarget();

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

    @Override
    public List<TooltipInfo> getCustomTooltips() {
        return null;
    }
}
