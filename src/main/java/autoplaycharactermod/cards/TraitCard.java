package autoplaycharactermod.cards;

import autoplaycharactermod.ThePilotMod;
import autoplaycharactermod.cards.traitBastionCards.Beacon;
import autoplaycharactermod.cards.traitScavengeCards.DuctTape;
import autoplaycharactermod.character.PilotCharacter;
import autoplaycharactermod.powers.BluePower;
import autoplaycharactermod.powers.RedPower;
import autoplaycharactermod.powers.YellowPower;
import autoplaycharactermod.ui.TraitTutorials;
import autoplaycharactermod.util.CardStats;
import autoplaycharactermod.util.TextureLoader;
import autoplaycharactermod.vfx.TraitFlashesEffect;
import basemod.helpers.TooltipInfo;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.evacipated.cardcrawl.mod.stslib.cards.interfaces.SpawnModificationCard;
import com.evacipated.cardcrawl.mod.stslib.patches.FlavorText;
import com.evacipated.cardcrawl.mod.stslib.util.extraicons.ExtraIcons;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class TraitCard extends BaseCard implements SpawnModificationCard {

    private static final UIStrings descriptor = CardCrawlGame.languagePack.getUIString(makeID("ComboTags"));
    public static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(makeID("HoverTextTutorial"));

    protected boolean countsTwiceOnUpgrade;
    protected TraitColor traitColor;
    private boolean canSpawnTutorial = false;
    private static final Texture textHover = TextureLoader.getTexture(ThePilotMod.imagePath("tip/hover.png"));
    private final Color iconColour = new Color(1, 1, 1, 1);
    private float time;

    public TraitCard(String ID, CardStats info, TraitColor color, Boolean countsTwice) {
        super(ID, info);
        this.returnToHand = true;
        countsTwiceOnUpgrade = countsTwice;
        traitColor = color;
        switch (color) {
            case BASTION:
                if (info.cardType == CardType.SKILL)
                    setBackgroundTexture(ThePilotMod.imagePath("character/cardback/bg_blue_skill.png"), ThePilotMod.imagePath("character/cardback/bg_blue_skill_p.png"));
                FlavorText.AbstractCardFlavorFields.boxColor.set(this, Color.BLUE.cpy());
                FlavorText.AbstractCardFlavorFields.textColor.set(this, Color.WHITE.cpy());
                FlavorText.AbstractCardFlavorFields.flavor.set(this, ThePilotMod.keywords.get("Bastion").DESCRIPTION);
                break;
            case IGNITE:
                if (info.cardType == CardType.ATTACK)
                    setBackgroundTexture(ThePilotMod.imagePath("character/cardback/bg_red_attack.png"), ThePilotMod.imagePath("character/cardback/bg_red_attack_p.png"));
                else if (info.cardType == CardType.SKILL)
                    setBackgroundTexture(ThePilotMod.imagePath("character/cardback/bg_red_skill.png"), ThePilotMod.imagePath("character/cardback/bg_red_skill_p.png"));
                FlavorText.AbstractCardFlavorFields.boxColor.set(this, Color.FIREBRICK.cpy());
                FlavorText.AbstractCardFlavorFields.textColor.set(this, Color.WHITE.cpy());
                FlavorText.AbstractCardFlavorFields.flavor.set(this, ThePilotMod.keywords.get("Ignition").DESCRIPTION);
                break;
            case SCAVENGE:
                if (info.cardType == CardType.ATTACK)
                    setBackgroundTexture(ThePilotMod.imagePath("character/cardback/bg_yellow_attack.png"), ThePilotMod.imagePath("character/cardback/bg_yellow_attack_p.png"));
                else if (info.cardType == CardType.SKILL)
                    setBackgroundTexture(ThePilotMod.imagePath("character/cardback/bg_yellow_skill.png"), ThePilotMod.imagePath("character/cardback/bg_yellow_skill_p.png"));
                FlavorText.AbstractCardFlavorFields.boxColor.set(this, Color.GOLD.cpy());
                FlavorText.AbstractCardFlavorFields.flavor.set(this, ThePilotMod.keywords.get("Scavenge").DESCRIPTION);
                break;
            case OTHER:
                setBackgroundTexture(ThePilotMod.imagePath("character/cardback/bg_crosswire_skill.png"), ThePilotMod.imagePath("character/cardback/bg_crosswire_skill_p.png"));
                FlavorText.AbstractCardFlavorFields.boxColor.set(this, Color.PURPLE);
                FlavorText.AbstractCardFlavorFields.textColor.set(this, Color.WHITE);
                FlavorText.AbstractCardFlavorFields.flavor.set(this, ThePilotMod.keywords.get("CrossWire").DESCRIPTION);
                break;
        }
    }


    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addPower();
        for (AbstractCard c : AbstractDungeon.player.discardPile.group) {
            if (c instanceof DuctTape && c.uuid != this.uuid) {
                ((DuctTape) c).triggerReturnToHand();
            }
        }
    }

    @Override
    public void evolveCard() {
        super.evolveCard();
        switch (traitColor) {
            case BASTION:
                if (this.type == CardType.SKILL)
                    setBackgroundTexture(ThePilotMod.imagePath("character/cardback/Evolved/bg_blue_skill.png"), ThePilotMod.imagePath("character/cardback/Evolved/bg_blue_skill_p.png"));
                break;
            case IGNITE:
                if (this.type == CardType.ATTACK)
                    setBackgroundTexture(ThePilotMod.imagePath("character/cardback/Evolved/bg_red_attack.png"), ThePilotMod.imagePath("character/cardback/Evolved/bg_red_attack_p.png"));
                else if (this.type == CardType.SKILL)
                    setBackgroundTexture(ThePilotMod.imagePath("character/cardback/Evolved/bg_red_skill.png"), ThePilotMod.imagePath("character/cardback/Evolved/bg_red_skill_p.png"));
                break;
            case SCAVENGE:
                if (this.type == CardType.ATTACK)
                    setBackgroundTexture(ThePilotMod.imagePath("character/cardback/Evolved/bg_yellow_attack.png"), ThePilotMod.imagePath("character/cardback/Evolved/bg_yellow_attack_p.png"));
                else if (this.type == CardType.SKILL)
                    setBackgroundTexture(ThePilotMod.imagePath("character/cardback/Evolved/bg_yellow_skill.png"), ThePilotMod.imagePath("character/cardback/Evolved/bg_yellow_skill_p.png"));
                break;
            case OTHER:
                setBackgroundTexture(ThePilotMod.imagePath("character/cardback/Evolved/bg_crosswire_skill.png"), ThePilotMod.imagePath("character/cardback/Evolved/bg_crosswire_skill_p.png"));
                break;
        }
    }

    public void triggerOnExhaust() {
        removePower();
    }

    public void triggerOnManualDiscard() {
        removePower();
    }

    public void onRetained() {
        addPower();
    }

    public void addPower() {
        if (!this.Duplicated) {
            int amount = (this.alreadyEvolved || upgraded) && countsTwiceOnUpgrade ? 2 : 1;
            switch (traitColor) {
                case BASTION:
                    AbstractDungeon.effectsQueue.add(new TraitFlashesEffect(new Color(0.3F, 0.3F, 1.0F, 1F)));
                    addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new BluePower(AbstractDungeon.player, amount)));
                    break;
                case IGNITE:
                    AbstractDungeon.effectsQueue.add(new TraitFlashesEffect(new Color(1.0F, 0.1F, 0.1F, 1F)));
                    addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new RedPower(AbstractDungeon.player, amount)));
                    break;
                case SCAVENGE:
                    AbstractDungeon.effectsQueue.add(new TraitFlashesEffect(new Color(1.0F, 1.0F, 0.1F, 1F)));
                    addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new YellowPower(AbstractDungeon.player, amount)));
                    break;
                case OTHER:
                    AbstractDungeon.effectsQueue.add(new TraitFlashesEffect(new Color(1.0F, 0.1F, 1.0F, 1F)));
                    addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new YellowPower(AbstractDungeon.player, amount)));
                    addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new BluePower(AbstractDungeon.player, amount)));
                    addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new RedPower(AbstractDungeon.player, amount)));
                    break;
            }
        }
    }

    public void removePower() {
        if (!this.Duplicated) {
            int amount = (this.alreadyEvolved || upgraded) && countsTwiceOnUpgrade ? 2 : 1;
            switch (traitColor) {
                case BASTION:
                    addToTop(new ReducePowerAction(AbstractDungeon.player, AbstractDungeon.player, BluePower.POWER_ID, amount));
                    break;
                case IGNITE:
                    addToTop(new ReducePowerAction(AbstractDungeon.player, AbstractDungeon.player, RedPower.POWER_ID, amount));
                    break;
                case SCAVENGE:
                    addToTop(new ReducePowerAction(AbstractDungeon.player, AbstractDungeon.player, YellowPower.POWER_ID, amount));
                    break;
                case OTHER:
                    addToTop(new ReducePowerAction(AbstractDungeon.player, AbstractDungeon.player, YellowPower.POWER_ID, amount));
                    addToTop(new ReducePowerAction(AbstractDungeon.player, AbstractDungeon.player, BluePower.POWER_ID, amount));
                    addToTop(new ReducePowerAction(AbstractDungeon.player, AbstractDungeon.player, RedPower.POWER_ID, amount));
                    break;
            }
        }
    }

    @Override
    public List<TooltipInfo> getCustomTooltips() {
        ArrayList<TooltipInfo> customTooltips = new ArrayList<>();
        switch (traitColor) {
            case BASTION:
                customTooltips.add(new TooltipInfo(ThePilotMod.keywords.get("Bastion").PROPER_NAME, ThePilotMod.keywords.get("Trait").DESCRIPTION));
                break;
            case IGNITE:
                customTooltips.add(new TooltipInfo(ThePilotMod.keywords.get("Ignition").PROPER_NAME, ThePilotMod.keywords.get("Trait").DESCRIPTION));
                break;
            case SCAVENGE:
                customTooltips.add(new TooltipInfo(ThePilotMod.keywords.get("Scavenge").PROPER_NAME, ThePilotMod.keywords.get("Trait").DESCRIPTION));
                break;
            case OTHER:
                customTooltips.add(new TooltipInfo(ThePilotMod.keywords.get("Trait").PROPER_NAME, ThePilotMod.keywords.get("Trait").DESCRIPTION));
                break;
        }
        return customTooltips;
    }

    @Override
    public List<String> getCardDescriptors() {
        List<String> descriptorReturn;
        switch (traitColor) {
            case BASTION:
                descriptorReturn = Collections.singletonList(descriptor.TEXT[1]);
                break;
            case IGNITE:
                descriptorReturn = Collections.singletonList(descriptor.TEXT[0]);
                break;
            case SCAVENGE:
                descriptorReturn = Collections.singletonList(descriptor.TEXT[2]);
                break;
            default:
                descriptorReturn = Collections.emptyList();
                break;
        }
        return descriptorReturn;
    }

    @Override
    public void triggerOnGlowCheck() {
        if (!dontsparkle) {
            switch (traitColor) {
                case BASTION:
                    this.glowColor = Color.BLUE.cpy();
                    this.beginGlowing();
                    break;
                case IGNITE:
                    this.glowColor = Color.RED.cpy();
                    this.beginGlowing();
                    break;
                case SCAVENGE:
                    this.glowColor = Color.GOLD.cpy();
                    this.beginGlowing();
                    break;
                case OTHER:
                    this.glowColor = Color.PURPLE.cpy();
                    this.beginGlowing();
                    break;
            }
        } else {
            this.stopGlowing();
        }
    }

    @Override
    public AbstractCard makeStatEquivalentCopy() {
        TraitCard copy = (TraitCard) super.makeStatEquivalentCopy();
        copy.traitColor = this.traitColor;
        copy.countsTwiceOnUpgrade = this.countsTwiceOnUpgrade;
        return copy;
    }

    @Override
    public void onRewardListCreated(ArrayList<AbstractCard> rewardCards) {
        super.onRewardListCreated(rewardCards);
        canSpawnTutorial = AbstractDungeon.player instanceof PilotCharacter;
    }

    private float WaitTimer = 0.8F;

    @Override
    public void update() {
        super.update();
        if (canSpawnTutorial && ThePilotMod.unseenTutorials[1]) {
            time = (time + Gdx.graphics.getDeltaTime()) % (MathUtils.PI2);
            float normalized = (MathUtils.sin(time * 2f) + 1f) * 0.5f;
            iconColour.a = this.transparency;
            FontHelper.cardEnergyFont_L.getData().setScale(this.drawScale);

            ExtraIcons.icon(textHover)
                    .text(uiStrings.TEXT[1])
                    .drawColor(iconColour)
                    .textOffsetX(-18f)
                    .offsetY(-265f + normalized * 25f)
                    .offsetX(145f)
                    .render(this);

            if (hb.hovered) {
                if (WaitTimer <= 0F) {
                    AbstractDungeon.ftue = new TraitTutorials();
                    ThePilotMod.unseenTutorials[1] = false;
                    canSpawnTutorial = false;
                    try {
                        ThePilotMod.saveTutorialsSeen();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    WaitTimer -= Gdx.graphics.getDeltaTime();
                }
            }
        }
    }

    @Override
    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        return (PlayOnce || this instanceof Beacon) && super.canUse(p, m);
    }

    public enum TraitColor {
        BASTION,
        IGNITE,
        SCAVENGE,
        OTHER
    }
}
