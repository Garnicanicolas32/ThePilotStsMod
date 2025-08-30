package autoplaycharactermod.cards.equipment;

import autoplaycharactermod.BasicMod;
import autoplaycharactermod.actions.DamageCurrentTargetAction;
import autoplaycharactermod.actions.SfxActionVolume;
import autoplaycharactermod.cards.EquipmentCard;
import autoplaycharactermod.cards.traitScavengeCards.DuctTape;
import autoplaycharactermod.cards.traitScavengeCards.GachaPull;
import autoplaycharactermod.character.MyCharacter;
import autoplaycharactermod.powers.YellowPower;
import autoplaycharactermod.ui.ConfigPanel;
import autoplaycharactermod.util.CardStats;
import autoplaycharactermod.vfx.FallingGoldEffect;
import basemod.helpers.TooltipInfo;
import com.badlogic.gdx.graphics.Color;
import com.evacipated.cardcrawl.mod.stslib.patches.FlavorText;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.ArrayList;
import java.util.List;

public class TrashCannon extends EquipmentCard {
    public static final String ID = makeID("TrashCannon");
    private static final CardStats info = new CardStats(
            MyCharacter.Meta.CARD_COLOR,
            CardType.ATTACK,
            CardRarity.UNCOMMON,
            CardTarget.NONE,
            0
    );
    private static final int BASE_HP = 8;
    private static final int MAGIC = 3;
    private static final int MAGIC_UPG = 1;


    public TrashCannon() {
        super(ID, info, BASE_HP);
        this.misc = 5;
        setMagic(3, 1);
        this.baseDamage = this.misc;

        setCustomVar("DURABILITY", 1, 1);
        setBackgroundTexture(BasicMod.imagePath("character/cardback/bg_yellow_skill.png"), BasicMod.imagePath("character/cardback/bg_yellow_skill_p.png"));
        FlavorText.AbstractCardFlavorFields.boxColor.set(this, Color.GOLD.cpy());
        FlavorText.AbstractCardFlavorFields.flavor.set(this, BasicMod.keywords.get("Scavenge").DESCRIPTION);
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
        setMagic(7);
        super.evolveCard();
    }

    protected void onEquip() {
        addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new YellowPower(AbstractDungeon.player, 1)));
        for (AbstractCard c : AbstractDungeon.player.discardPile.group) {
            if (c instanceof DuctTape && c.uuid != this.uuid) {
                ((DuctTape) c).triggerReturnToHand();
            }
        }
    }

    protected void onUnequip() {
        this.addToTop(new ReducePowerAction(AbstractDungeon.player, AbstractDungeon.player, YellowPower.POWER_ID, 1));
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
        AbstractMonster m = MyCharacter.getTarget();

        calculateCardDamage(m);
        addToBot(new SFXAction("ATTACK_WHIFF_2"));
        for (int i = 0; i < (ConfigPanel.lessParticles ? 4 : this.damage / MAGIC); ++i) {
            AbstractDungeon.effectsQueue.add(new FallingGoldEffect(this.damage / MAGIC, AbstractDungeon.getMonsters().shouldFlipVfx()));
        }
        if (alreadyEvolved) {
            addToBot(new DamageAllEnemiesAction(AbstractDungeon.player, BasicMod.fusionsmade * magicNumber, DamageInfo.DamageType.NORMAL, AbstractGameAction.AttackEffect.BLUNT_LIGHT));
        } else
            addToBot(new DamageCurrentTargetAction(this));

        super.Activate();
    }

    public void triggerOnExhaust() {
        this.addToTop(new ReducePowerAction(AbstractDungeon.player, AbstractDungeon.player, YellowPower.POWER_ID, 1));
    }

    public void triggerOnManualDiscard() {
        this.addToTop(new ReducePowerAction(AbstractDungeon.player, AbstractDungeon.player, YellowPower.POWER_ID, 1));
    }

    public void atTurnStart() {
        Activate();
    }

    public void onRetained() {
        addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new YellowPower(AbstractDungeon.player, 1)));
    }

    public void AddStack() {
        if (!alreadyEvolved) {
            this.misc += magicNumber;
            this.applyPowers();
            this.baseDamage = this.misc;
            this.isDamageModified = false;
            this.equipmentMaxHp += customVar("DURABILITY");
            this.equipmentHp += customVar("DURABILITY");
            initializeDescription();
        }
    }

    public void applyPowers() {
        this.baseBlock = this.misc;
        super.applyPowers();
        this.initializeDescription();
    }

    @Override
    public void onLoadedMisc() {
        baseDamage = misc;
    }

    @Override
    protected int getUpgradeDurability() {
        return 4;
    }

    @Override
    public List<TooltipInfo> getCustomTooltips() {
        ArrayList<TooltipInfo> customTooltips = new ArrayList<>();
        customTooltips.add(new TooltipInfo(BasicMod.keywords.get("Equipment").PROPER_NAME, BasicMod.keywords.get("Equipment").DESCRIPTION));
        customTooltips.add(new TooltipInfo(BasicMod.keywords.get("Scavenge").PROPER_NAME, BasicMod.keywords.get("Trait").DESCRIPTION));
        return customTooltips;
    }

    @Override
    public AbstractCard replaceWith(ArrayList<AbstractCard> currentRewardCards) {
        if (BasicMod.unseenTutorials[1] || BasicMod.unseenTutorials[2]) {
            return new GachaPull();
        }
        return this;
    }

    @Override
    public List<String> getCardDescriptors() {
        List<String> descriptorReturn = new ArrayList<>();
        descriptorReturn.add(descriptor.TEXT[3]);
        descriptorReturn.add(descriptor.TEXT[2]);

        return descriptorReturn;
    }
}
