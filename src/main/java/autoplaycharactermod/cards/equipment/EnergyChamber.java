package autoplaycharactermod.cards.equipment;

import autoplaycharactermod.ThePilotMod;
import autoplaycharactermod.actions.EnergyChamberAction;
import autoplaycharactermod.actions.SfxActionVolume;
import autoplaycharactermod.cards.EquipmentCard;
import autoplaycharactermod.character.PilotCharacter;
import autoplaycharactermod.util.CardStats;
import autoplaycharactermod.vfx.EnergyChamberEffect;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class EnergyChamber extends EquipmentCard {
    public static final String ID = makeID("EnergyChamber");

    private static final CardStats info = new CardStats(
            PilotCharacter.Meta.CARD_COLOR,
            CardType.ATTACK,
            CardRarity.UNCOMMON,
            CardTarget.NONE,
            0 
    );
    private static final int BASE_HP = 28;
    private static final int MAGIC = 4;
    private static final int MAGIC_UPG = 2;
    private static final int DAMAGE = 6;
    private static final int DAMAGE_UPG = 3;
    private int count = 0;

    public EnergyChamber() {
        super(ID, info, BASE_HP);
        setDamage(DAMAGE, DAMAGE_UPG);
        setMagic(MAGIC, MAGIC_UPG);
        checkEvolve();
        this.tags.remove(ThePilotMod.CustomTags.ignoreDuplication);
    }

    @Override
    public void evolveCard() {
        setMagic(10);
        setDamage(15);
        super.evolveCard();
    }

    protected void onEquip() {
        count = 0;
    }

    protected void onUnequip() {
        calculateCardDamage(PilotCharacter.getTarget());
        addToBot(new EnergyChamberAction(this, AbstractGameAction.AttackEffect.NONE));
        super.Activate();
    }

    @Override
    public void Activate() {
        if (!Equipped) return;
        super.Activate();
    }

    @Override
    protected int getUpgradeDurability() {
        return 10;
    }

    public void damageReceived(int damageAmount) {
        if (!Equipped || damageAmount < 1)
            return;
        addToBot(new SfxActionVolume("ORB_DARK_EVOKE", Math.min(-0.3F + count * 0.05F, 0.5f) , 2f));
        this.addToBot(new VFXAction(new EnergyChamberEffect(AbstractDungeon.player.hb.cX, AbstractDungeon.player.hb.cY, this.hb.cX, this.hb.cY, damageAmount, false), 0.5F));
        count++;
        upgradeDamage(magicNumber);
        initializeDescription();
        Activate();
    }
}
