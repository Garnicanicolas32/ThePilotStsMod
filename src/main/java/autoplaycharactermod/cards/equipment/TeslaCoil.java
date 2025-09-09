package autoplaycharactermod.cards.equipment;

import autoplaycharactermod.BasicMod;
import autoplaycharactermod.actions.DamageCurrentTargetAction;
import autoplaycharactermod.cards.EquipmentCard;
import autoplaycharactermod.character.MyCharacter;
import autoplaycharactermod.relics.OilCan;
import autoplaycharactermod.util.CardStats;
import autoplaycharactermod.vfx.EquipmentShowCardBrieflyEffect;
import basemod.helpers.TooltipInfo;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import java.util.ArrayList;
import java.util.List;

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

    public TeslaCoil() {
        super(ID, info, BASE_HP);
        setDamage(DAMAGE, UPG_DAMAGE);
        setMagic(MAGIC);
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
        return 10;
    }

    public void onGainCharge(){
        if (Equipped){
            flash(Color.GREEN.cpy());
            healEquipment(magicNumber, false, true);
            AbstractDungeon.effectList.add(new EquipmentShowCardBrieflyEffect(this.makeStatEquivalentCopy()));
            if (AbstractDungeon.player.hasRelic(OilCan.ID)) {
                ((OilCan) AbstractDungeon.player.getRelic(OilCan.ID)).activate();
            }
            checkActivations();
        }
    }

    @Override
    public List<TooltipInfo> getCustomTooltips() {
        ArrayList<TooltipInfo> customTooltips = new ArrayList<>();
        customTooltips.add(new TooltipInfo(BasicMod.keywords.get("Equipment").PROPER_NAME, BasicMod.keywords.get("Equipment").DESCRIPTION));
        customTooltips.add(new TooltipInfo(BasicMod.keywords.get("Charge").PROPER_NAME, BasicMod.keywords.get("Charge").DESCRIPTION));
        return customTooltips;
    }

    public void atTurnStart() {
        Activate();
    }

    public void triggerOnEndOfTurnForPlayingCard() {
        Activate();
    }
}
