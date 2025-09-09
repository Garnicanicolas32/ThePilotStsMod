package autoplaycharactermod.cards.equipment;

import autoplaycharactermod.BasicMod;
import autoplaycharactermod.actions.SfxActionVolume;
import autoplaycharactermod.cards.EquipmentCard;
import autoplaycharactermod.cards.scrap.ScrapCommon;
import autoplaycharactermod.cards.scrap.ScrapCommonDef;
import autoplaycharactermod.cards.traitScavengeCards.DuctTape;
import autoplaycharactermod.cards.traitScavengeCards.LostAndFound;
import autoplaycharactermod.character.MyCharacter;
import autoplaycharactermod.powers.YellowPower;
import autoplaycharactermod.ui.ConfigPanel;
import autoplaycharactermod.util.CardStats;
import autoplaycharactermod.vfx.PoisonMineDrillEffect;
import basemod.helpers.TooltipInfo;
import basemod.patches.com.megacrit.cardcrawl.cards.AbstractCard.MultiCardPreview;
import com.badlogic.gdx.graphics.Color;
import com.evacipated.cardcrawl.mod.stslib.actions.tempHp.AddTemporaryHPAction;
import com.evacipated.cardcrawl.mod.stslib.patches.FlavorText;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.ExhaustSpecificCardAction;
import com.megacrit.cardcrawl.actions.common.GainGoldAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.GainPennyEffect;

import java.util.ArrayList;
import java.util.List;

public class Drill extends EquipmentCard {
    public static final String ID = makeID("Drill");
    private static final CardStats info = new CardStats(
            MyCharacter.Meta.CARD_COLOR,
            CardType.SKILL,
            CardRarity.RARE,
            CardTarget.NONE,
            0
    );
    private static final int BASE_HP = 10;
    private static final int MAGIC = 8;
    private static final int UPG_MAGIC = 4;

    public Drill() {
        super(ID, info, BASE_HP);
        setMagic(MAGIC, UPG_MAGIC);
        tags.add(CardTags.HEALING);
        setBackgroundTexture(BasicMod.imagePath("character/cardback/bg_yellow_skill.png"), BasicMod.imagePath("character/cardback/bg_yellow_skill_p.png"));
        FlavorText.AbstractCardFlavorFields.boxColor.set(this, Color.GOLD.cpy());
        FlavorText.AbstractCardFlavorFields.flavor.set(this, BasicMod.keywords.get("Scavenge").DESCRIPTION);
        checkEvolve();
        if (!alreadyEvolved)
            MultiCardPreview.add(this, true, new ScrapCommon(), new ScrapCommonDef());
    }

    @Override
    public void evolveCard() {
        MultiCardPreview.clear(this);
        setMagic(30);
        super.evolveCard();
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
        addToBot(new SfxActionVolume("ATTACK_WHIRLWIND", -0.2F, 1.2F));
        for (int i = 0; i < (ConfigPanel.lessParticles ? 10 : magicNumber); i++) {
            AbstractDungeon.topLevelEffects.add(new GainPennyEffect(this.hb.cX, this.hb.cY));
            AbstractDungeon.effectsQueue.add(new PoisonMineDrillEffect(this.hb.cX, this.hb.cY, true));
            AbstractDungeon.effectsQueue.add(new PoisonMineDrillEffect(this.hb.cX, this.hb.cY, true));
        }
        addToBot(new GainGoldAction(magicNumber));
        if (alreadyEvolved) {
            addToBot(new AddTemporaryHPAction(AbstractDungeon.player, AbstractDungeon.player, 10));
        }
        super.Activate();
    }

    public void triggerOnExhaust() {
        this.addToTop(new ReducePowerAction(AbstractDungeon.player, AbstractDungeon.player, YellowPower.POWER_ID, 1));
    }

    public void triggerOnManualDiscard() {
        this.addToTop(new ReducePowerAction(AbstractDungeon.player, AbstractDungeon.player, YellowPower.POWER_ID, 1));
    }

    public void triggerOnEndOfTurnForPlayingCard() {
        Activate();
    }

    public void onRetained() {
        addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new YellowPower(AbstractDungeon.player, 1)));
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
    public List<String> getCardDescriptors() {
        List<String> descriptorReturn = new ArrayList<>();
        descriptorReturn.add(descriptor.TEXT[3]);
        descriptorReturn.add(descriptor.TEXT[2]);

        return descriptorReturn;
    }

    @Override
    public AbstractCard replaceWith(ArrayList<AbstractCard> currentRewardCards) {
        if (BasicMod.unseenTutorials[1] || BasicMod.unseenTutorials[2]) {
            return new LostAndFound();
        }
        return this;
    }

}
