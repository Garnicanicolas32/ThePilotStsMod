package autoplaycharactermod.cards;

import autoplaycharactermod.BasicMod;
import autoplaycharactermod.cards.traitBastionCards.Beacon;
import autoplaycharactermod.cards.traitScavengeCards.DuctTape;
import autoplaycharactermod.character.MyCharacter;
import autoplaycharactermod.powers.BluePower;
import autoplaycharactermod.powers.RedPower;
import autoplaycharactermod.powers.YellowPower;
import autoplaycharactermod.ui.TraitTutorials;
import autoplaycharactermod.util.CardStats;
import autoplaycharactermod.vfx.TraitFlashesEffect;
import basemod.helpers.TooltipInfo;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.evacipated.cardcrawl.mod.stslib.cards.interfaces.SpawnModificationCard;
import com.evacipated.cardcrawl.mod.stslib.patches.FlavorText;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class TraitCard extends BaseCard implements SpawnModificationCard {

    private static final UIStrings descriptor = CardCrawlGame.languagePack.getUIString(makeID("ComboTags"));
    protected boolean countsTwiceOnUpgrade;
    protected TraitColor traitColor;
    private boolean canSpawnTutorial = false;

    public TraitCard(String ID, CardStats info, TraitColor color, Boolean countsTwice) {
        super(ID, info);
        this.returnToHand = true;
        countsTwiceOnUpgrade = countsTwice;
        traitColor = color;
        switch (color) {
            case BASTION:
                if (info.cardType == CardType.SKILL)
                    setBackgroundTexture(BasicMod.imagePath("character/cardback/bg_blue_skill.png"), BasicMod.imagePath("character/cardback/bg_blue_skill_p.png"));
                FlavorText.AbstractCardFlavorFields.boxColor.set(this, Color.BLUE.cpy());
                FlavorText.AbstractCardFlavorFields.textColor.set(this, Color.WHITE.cpy());
                FlavorText.AbstractCardFlavorFields.flavor.set(this, BasicMod.keywords.get("Bastion").DESCRIPTION);
                break;
            case IGNITE:
                if (info.cardType == CardType.ATTACK)
                    setBackgroundTexture(BasicMod.imagePath("character/cardback/bg_red_attack.png"), BasicMod.imagePath("character/cardback/bg_red_attack_p.png"));
                else if (info.cardType == CardType.SKILL)
                    setBackgroundTexture(BasicMod.imagePath("character/cardback/bg_red_skill.png"), BasicMod.imagePath("character/cardback/bg_red_skill_p.png"));
                FlavorText.AbstractCardFlavorFields.boxColor.set(this, Color.FIREBRICK.cpy());
                FlavorText.AbstractCardFlavorFields.textColor.set(this, Color.WHITE.cpy());
                FlavorText.AbstractCardFlavorFields.flavor.set(this, BasicMod.keywords.get("Ignition").DESCRIPTION);
                break;
            case SCAVENGE:
                if (info.cardType == CardType.ATTACK)
                    setBackgroundTexture(BasicMod.imagePath("character/cardback/bg_yellow_attack.png"), BasicMod.imagePath("character/cardback/bg_yellow_attack_p.png"));
                else if (info.cardType == CardType.SKILL)
                    setBackgroundTexture(BasicMod.imagePath("character/cardback/bg_yellow_skill.png"), BasicMod.imagePath("character/cardback/bg_yellow_skill_p.png"));
                FlavorText.AbstractCardFlavorFields.boxColor.set(this, Color.GOLD.cpy());
                FlavorText.AbstractCardFlavorFields.flavor.set(this, BasicMod.keywords.get("Scavenge").DESCRIPTION);
                break;
            case OTHER:
                setBackgroundTexture(BasicMod.imagePath("character/cardback/bg_crosswire_skill.png"), BasicMod.imagePath("character/cardback/bg_crosswire_skill_p.png"));
                FlavorText.AbstractCardFlavorFields.boxColor.set(this, Color.PURPLE);
                FlavorText.AbstractCardFlavorFields.textColor.set(this, Color.WHITE);
                FlavorText.AbstractCardFlavorFields.flavor.set(this, BasicMod.keywords.get("CrossWire").DESCRIPTION);
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
                    setBackgroundTexture(BasicMod.imagePath("character/cardback/Evolved/bg_blue_skill.png"), BasicMod.imagePath("character/cardback/Evolved/bg_blue_skill_p.png"));
                break;
            case IGNITE:
                if (this.type == CardType.ATTACK)
                    setBackgroundTexture(BasicMod.imagePath("character/cardback/Evolved/bg_red_attack.png"), BasicMod.imagePath("character/cardback/Evolved/bg_red_attack_p.png"));
                else if (this.type == CardType.SKILL)
                    setBackgroundTexture(BasicMod.imagePath("character/cardback/Evolved/bg_red_skill.png"), BasicMod.imagePath("character/cardback/Evolved/bg_red_skill_p.png"));
                break;
            case SCAVENGE:
                if (this.type == CardType.ATTACK)
                    setBackgroundTexture(BasicMod.imagePath("character/cardback/Evolved/bg_yellow_attack.png"), BasicMod.imagePath("character/cardback/Evolved/bg_yellow_attack_p.png"));
                else if (this.type == CardType.SKILL)
                    setBackgroundTexture(BasicMod.imagePath("character/cardback/Evolved/bg_yellow_skill.png"), BasicMod.imagePath("character/cardback/Evolved/bg_yellow_skill_p.png"));
                break;
            case OTHER:
                setBackgroundTexture(BasicMod.imagePath("character/cardback/Evolved/bg_crosswire_skill.png"), BasicMod.imagePath("character/cardback/Evolved/bg_crosswire_skill_p.png"));
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
                customTooltips.add(new TooltipInfo(BasicMod.keywords.get("Bastion").PROPER_NAME, BasicMod.keywords.get("Trait").DESCRIPTION));
                break;
            case IGNITE:
                customTooltips.add(new TooltipInfo(BasicMod.keywords.get("Ignition").PROPER_NAME, BasicMod.keywords.get("Trait").DESCRIPTION));
                break;
            case SCAVENGE:
                customTooltips.add(new TooltipInfo(BasicMod.keywords.get("Scavenge").PROPER_NAME, BasicMod.keywords.get("Trait").DESCRIPTION));
                break;
            case OTHER:
                customTooltips.add(new TooltipInfo(BasicMod.keywords.get("Trait").PROPER_NAME, BasicMod.keywords.get("Trait").DESCRIPTION));
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
        canSpawnTutorial = AbstractDungeon.player instanceof MyCharacter;
    }

    private float WaitTimer = 0.4F;

    @Override
    public void update() {
        super.update();
        if (hb.hovered && canSpawnTutorial && BasicMod.unseenTutorials[1]) {
            if (WaitTimer <= 0F) {
                AbstractDungeon.ftue = new TraitTutorials();
                BasicMod.unseenTutorials[1] = false;
                canSpawnTutorial = false;
                try {
                    BasicMod.saveTutorialsSeen();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                WaitTimer -= Gdx.graphics.getDeltaTime();
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
