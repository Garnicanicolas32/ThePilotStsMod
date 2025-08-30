package autoplaycharactermod.cards.equipment;

import autoplaycharactermod.BasicMod;
import autoplaycharactermod.actions.EnergyChamberAction;
import autoplaycharactermod.actions.SfxActionVolume;
import autoplaycharactermod.cards.EquipmentCard;
import autoplaycharactermod.character.MyCharacter;
import autoplaycharactermod.util.CardStats;
import autoplaycharactermod.vfx.EnergyChamberEffect;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class EnergyChamber extends EquipmentCard {
    public static final String ID = makeID("EnergyChamber");

    private static final CardStats info = new CardStats(
            MyCharacter.Meta.CARD_COLOR,
            CardType.ATTACK,
            CardRarity.UNCOMMON,
            CardTarget.NONE,
            0 
    );
    private static final int BASE_HP = 22;
    private int count = 0;

    public EnergyChamber() {
        super(ID, info, BASE_HP);
        setInnate(false, true);
        setDamage(1);
        checkEvolve();
        this.tags.remove(BasicMod.CustomTags.ignoreDuplication);
    }

    @Override
    public void evolveCard() {
        setInnate(true);
        super.evolveCard();

    }

    protected void onEquip() {
        count = 0;
    }

    protected void onUnequip() {
        calculateCardDamage(MyCharacter.getTarget());
        addToBot(new EnergyChamberAction(this, AbstractGameAction.AttackEffect.NONE));
        super.Activate();
    }

    @Override
    public void Activate() {
        if (!Equipped) return;
        AbstractPlayer p = AbstractDungeon.player;
        super.Activate();
    }

    @Override
    protected int getUpgradeDurability() {
        return 8;
    }

    public void damageReceived(int damageAmount) {
        if (!Equipped)
            return;
        if (alreadyEvolved)
            damageAmount += damageAmount;
        addToBot(new SfxActionVolume("ORB_DARK_EVOKE", -0.5F + count * 0.05F, 2f));
        this.addToBot(new VFXAction(new EnergyChamberEffect(AbstractDungeon.player.hb.cX, AbstractDungeon.player.hb.cY, this.hb.cX, this.hb.cY, damageAmount, false), 0.5F));
        count++;
        upgradeDamage(damageAmount);
        initializeDescription();
        Activate();
    }
}
