package autoplaycharactermod.cards.equipment;

import autoplaycharactermod.BasicMod;
import autoplaycharactermod.cards.EquipmentCard;
import autoplaycharactermod.cards.traitIgnitionCards.ThermalSurge;
import autoplaycharactermod.cards.traitScavengeCards.DuctTape;
import autoplaycharactermod.cards.traitScavengeCards.LostAndFound;
import autoplaycharactermod.character.MyCharacter;
import autoplaycharactermod.powers.RedPower;
import autoplaycharactermod.util.CardStats;
import basemod.helpers.TooltipInfo;
import com.badlogic.gdx.graphics.Color;
import com.evacipated.cardcrawl.mod.stslib.patches.FlavorText;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.StrengthPower;

import java.util.ArrayList;
import java.util.List;

public class Flamethrower extends EquipmentCard {
    public static final String ID = makeID("Flamethrower");
    private static final CardStats info = new CardStats(
            MyCharacter.Meta.CARD_COLOR,
            CardType.ATTACK,
            CardRarity.UNCOMMON,
            CardTarget.NONE,
            0 
    );
    private static final int BASE_HP = 8;
    private static final int DAMAGE = 2;
    private static final int DAMAGE_UPG = 1;

    public Flamethrower() {
        super(ID, info, BASE_HP);
        setDamage(DAMAGE, DAMAGE_UPG);
        this.isMultiDamage = true;
        setBackgroundTexture(BasicMod.imagePath("character/cardback/bg_red_attack.png"), BasicMod.imagePath("character/cardback/bg_red_attack_p.png"));
        FlavorText.AbstractCardFlavorFields.boxColor.set(this, Color.FIREBRICK.cpy());
        FlavorText.AbstractCardFlavorFields.textColor.set(this, Color.WHITE.cpy());
        FlavorText.AbstractCardFlavorFields.flavor.set(this, BasicMod.keywords.get("Ignition").DESCRIPTION);
        checkEvolve();
    }

    @Override
    public void evolveCard() {
        setDamage(5);
        super.evolveCard();
    }

    @Override
    public void Activate() {
        if (!Equipped) return;
        AbstractPlayer p = AbstractDungeon.player;

        int amountUpg = damage;
        if (p.hasPower(StrengthPower.POWER_ID))
            amountUpg += p.getPower(StrengthPower.POWER_ID).amount;

        calculateCardDamage(null);
        addToBot(new DamageAllEnemiesAction(p, damage, DamageInfo.DamageType.NORMAL, AbstractGameAction.AttackEffect.FIRE));
        upgradeDamage(amountUpg);

        this.initializeDescription();

        super.Activate();
    }

    @Override
    protected int getUpgradeDurability() {
        return 2;
    }

    protected void onEquip() {
        addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new RedPower(AbstractDungeon.player, 1)));
        for (AbstractCard c : AbstractDungeon.player.discardPile.group) {
            if (c instanceof DuctTape && c.uuid != this.uuid) {
                ((DuctTape) c).triggerReturnToHand();
            }
        }
    }

    protected void onUnequip() {
        this.addToTop(new ReducePowerAction(AbstractDungeon.player, AbstractDungeon.player, RedPower.POWER_ID, 1));
        for (AbstractCard c : AbstractDungeon.player.discardPile.group) {
            if (c instanceof DuctTape && c.uuid != this.uuid) {
                ((DuctTape) c).triggerReturnToHand();
            }
        }
    }

    public void triggerOnExhaust() {
        this.addToTop(new ReducePowerAction(AbstractDungeon.player, AbstractDungeon.player, RedPower.POWER_ID, 1));
    }

    public void triggerOnManualDiscard() {
        this.addToTop(new ReducePowerAction(AbstractDungeon.player, AbstractDungeon.player, RedPower.POWER_ID, 1));
    }

    public void triggerOnEndOfTurnForPlayingCard() {
        Activate();
    }

    public void onRetained() {
        addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new RedPower(AbstractDungeon.player, 1)));
    }

    @Override
    public List<TooltipInfo> getCustomTooltips() {
        ArrayList<TooltipInfo> customTooltips = new ArrayList<>();
        customTooltips.add(new TooltipInfo(BasicMod.keywords.get("Equipment").PROPER_NAME, BasicMod.keywords.get("Equipment").DESCRIPTION));
        customTooltips.add(new TooltipInfo(BasicMod.keywords.get("Ignition").PROPER_NAME, BasicMod.keywords.get("Trait").DESCRIPTION));
        return customTooltips;
    }

    @Override
    public AbstractCard replaceWith(ArrayList<AbstractCard> currentRewardCards) {
        if (BasicMod.unseenTutorials[1] || BasicMod.unseenTutorials[2]) {
            return new ThermalSurge();
        }
        return this;
    }

    @Override
    public List<String> getCardDescriptors() {
        List<String> descriptorReturn = new ArrayList<>();
        descriptorReturn.add(descriptor.TEXT[3]);
        descriptorReturn.add(descriptor.TEXT[0]);

        return descriptorReturn;
    }
}
