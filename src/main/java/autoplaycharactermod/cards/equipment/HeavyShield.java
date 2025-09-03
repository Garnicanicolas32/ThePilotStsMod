package autoplaycharactermod.cards.equipment;

import autoplaycharactermod.BasicMod;
import autoplaycharactermod.actions.SfxActionVolume;
import autoplaycharactermod.cards.EquipmentCard;
import autoplaycharactermod.cards.traitBastionCards.Flash;
import autoplaycharactermod.cards.traitScavengeCards.DuctTape;
import autoplaycharactermod.character.MyCharacter;
import autoplaycharactermod.powers.BluePower;
import autoplaycharactermod.powers.OneLessEnergyPower;
import autoplaycharactermod.util.CardStats;
import basemod.helpers.TooltipInfo;
import com.badlogic.gdx.graphics.Color;
import com.evacipated.cardcrawl.mod.stslib.patches.FlavorText;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.ExhaustSpecificCardAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.WeakPower;

import java.util.ArrayList;
import java.util.List;

public class HeavyShield extends EquipmentCard {
    public static final String ID = makeID("HeavyShield");

    private static final CardStats info = new CardStats(
            MyCharacter.Meta.CARD_COLOR,
            CardType.SKILL,
            CardRarity.COMMON,
            CardTarget.NONE,
            0 
    );
    private static final int BASE_HP = 26;
    private static final int BLOCK = 4;
    private static final int BLOCKUPG = 3;

    public HeavyShield() {
        super(ID, info, BASE_HP);
        setMagic(1);
        setBlock(BLOCK, BLOCKUPG);
            setBackgroundTexture(BasicMod.imagePath("character/cardback/bg_blue_skill.png"), BasicMod.imagePath("character/cardback/bg_blue_skill_p.png"));
        FlavorText.AbstractCardFlavorFields.boxColor.set(this, Color.BLUE.cpy());
        FlavorText.AbstractCardFlavorFields.textColor.set(this, Color.WHITE.cpy());
        FlavorText.AbstractCardFlavorFields.flavor.set(this, BasicMod.keywords.get("Bastion").DESCRIPTION);
        checkEvolve();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (PlayOnce) {
            if (equipmentHp < 1) {
                addToBot(new SfxActionVolume("ORB_FROST_EVOKE", 0f, 2.5F));
                setExhaust(true);
                addToBot(new ExhaustSpecificCardAction(this, AbstractDungeon.player.hand, true));
            }else {
                addToBot(new SFXAction("ORB_FROST_DEFEND_1"));
                checkActivations();
                Equipped = true;
                returnToHand = true;
            }
            onEquip();

        } else {
            if (equipmentHp < 1) {
                addToBot(new SfxActionVolume("ORB_FROST_EVOKE", 0f, 2.5F));
                setExhaust(true);
                addToBot(new ExhaustSpecificCardAction(this, AbstractDungeon.player.hand, true));
            }else{
                Equipped = false;
                returnToHand = false;
            }
            onUnequip();
        }
        PlayOnce = false;
    }

    @Override
    public void evolveCard() {
        setBlock(10);
        super.evolveCard();
    }

    protected void onEquip() {
        addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new BluePower(AbstractDungeon.player, 1)));
        for (AbstractCard c : AbstractDungeon.player.discardPile.group) {
            if (c instanceof DuctTape && c.uuid != this.uuid) {
                ((DuctTape) c).triggerReturnToHand();
            }
        }
    }

    protected void onUnequip() {
        addToTop(new ReducePowerAction(AbstractDungeon.player, AbstractDungeon.player, BluePower.POWER_ID, 1));
        for (AbstractCard c : AbstractDungeon.player.discardPile.group) {
            if (c instanceof DuctTape && c.uuid != this.uuid) {
                ((DuctTape) c).triggerReturnToHand();
            }
        }
    }

    @Override
    public void Activate() {
        if (!Equipped) return;
        AbstractPlayer p = AbstractDungeon.player;
        if (!this.alreadyEvolved)
            addToBot(new ApplyPowerAction(p, p, new WeakPower(p, magicNumber, true)));
        calculateCardDamage(null);
        addToBot(new GainBlockAction(p, p, block));

        super.Activate();
    }

    @Override
    protected int getUpgradeDurability() {
        return 6;
    }

    public void triggerOnExhaust() {
        this.addToTop(new ReducePowerAction(AbstractDungeon.player, AbstractDungeon.player, BluePower.POWER_ID, 1));
    }

    public void triggerOnManualDiscard() {
        this.addToTop(new ReducePowerAction(AbstractDungeon.player, AbstractDungeon.player, BluePower.POWER_ID, 1));
    }

    public void triggerOnEndOfTurnForPlayingCard() {
        Activate();
    }

    public void onRetained() {
        addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new BluePower(AbstractDungeon.player, 1)));
    }

    @Override
    public List<TooltipInfo> getCustomTooltips() {
        ArrayList<TooltipInfo> customTooltips = new ArrayList<>();
        customTooltips.add(new TooltipInfo(BasicMod.keywords.get("Equipment").PROPER_NAME, BasicMod.keywords.get("Equipment").DESCRIPTION));
        customTooltips.add(new TooltipInfo(BasicMod.keywords.get("Bastion").PROPER_NAME, BasicMod.keywords.get("Trait").DESCRIPTION));
        return customTooltips;
    }

    @Override
    public AbstractCard replaceWith(ArrayList<AbstractCard> currentRewardCards) {
        if (BasicMod.unseenTutorials[1] || BasicMod.unseenTutorials[2]) {
            return new Flash();
        }
        return this;
    }

    @Override
    public List<String> getCardDescriptors() {
        List<String> descriptorReturn = new ArrayList<>();
        descriptorReturn.add(descriptor.TEXT[3]);
        descriptorReturn.add(descriptor.TEXT[1]);

        return descriptorReturn;
    }
}
