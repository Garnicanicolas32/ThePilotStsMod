package autoplaycharactermod.cards;

import autoplaycharactermod.ThePilotMod;
import autoplaycharactermod.actions.SfxActionVolume;
import autoplaycharactermod.cards.equipment.*;
import autoplaycharactermod.character.PilotCharacter;
import autoplaycharactermod.patches.OnUseCardPowersAndRelicsPatch;
import autoplaycharactermod.relics.OilCan;
import autoplaycharactermod.relics.reworks.StrangeFork;
import autoplaycharactermod.ui.DurabilityTutorial;
import autoplaycharactermod.util.CardStats;
import autoplaycharactermod.util.EquipmentVisualModifier;
import autoplaycharactermod.vfx.DamageEquipmentEffect;
import autoplaycharactermod.vfx.EquipmentShowCardBrieflyEffect;
import basemod.helpers.CardModifierManager;
import basemod.helpers.TooltipInfo;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ExhaustSpecificCardAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class EquipmentCard extends BaseCard {

    protected static final UIStrings descriptor = CardCrawlGame.languagePack.getUIString(makeID("ComboTags"));
    public int equipmentHp;
    public int equipmentMaxHp;
    protected boolean Equipped = false;
    public boolean canSpawnTutorial = false;

    public EquipmentCard(String ID, CardStats info, int hp) {
        super(ID, info);
        this.setSelfRetain(true);
        this.returnToHand = true;
        this.equipmentMaxHp = hp;
        this.equipmentHp = hp;
        this.tags.add(ThePilotMod.CustomTags.ignoreDuplication);
        CardModifierManager.addModifier(this, new EquipmentVisualModifier());
    }

    protected static void checkActivations() {
        for (AbstractCard c : AbstractDungeon.player.hand.group) {
            if (c instanceof SmartGun)
                ((SmartGun) c).Activate();
        }
    }

    @Override
    public void evolveCard() {
        equipmentMaxHp = 99;
        equipmentHp = 99;
        super.evolveCard();
    }

    public boolean isEquipped() {
        return Equipped;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (Duplicated && this instanceof EnergyChamber) {
            Equipped = false;
            returnToHand = false;
            onUnequip();
            return;
        }

        if (equipmentHp < 1) {
            addToBot(new SfxActionVolume("ORB_FROST_EVOKE", 0f, 2.5F));
            setExhaust(true);
            addToBot(new ExhaustSpecificCardAction(this, AbstractDungeon.player.hand, true));
        } else if (PlayOnce) {
            Equipped = true;
            returnToHand = true;
            onEquip();
            addToBot(new SFXAction("ORB_FROST_DEFEND_1"));
            checkActivations();
        } else {
            Equipped = false;
            returnToHand = false;
            onUnequip();
        }
        PlayOnce = false;
    }

    @Override
    public void triggerOnManualDiscard() {
        onUnequip();
        super.triggerOnManualDiscard();
    }

    @Override
    public void triggerOnGlowCheck() {
        if (dontsparkle) {
            this.stopGlowing();
        }
    }

    public void Activate() {
        flash(Color.RED.cpy());
        if (equipmentHp > 0)
            AbstractDungeon.effectList.add(new EquipmentShowCardBrieflyEffect(this.makeStatEquivalentCopy()));

        addToBot(new AbstractGameAction() {
            @Override
            public void update() {
                if (!AbstractDungeon.getCurrRoom().monsters.areMonstersBasicallyDead())
                    damageEquipment(1);
                isDone = true;
            }
        });

        if (!(this instanceof RocketPunch) && this.type == CardType.ATTACK) {
            OnUseCardPowersAndRelicsPatch.checkPenNibVigor();
        }
        if (!(this instanceof Shredder) && AbstractDungeon.player.hasRelic(OilCan.ID)) {
            if (this instanceof SmartGun) {
                for (AbstractCard c : AbstractDungeon.player.hand.group) {
                    if (c instanceof Shredder) {
                        ((Shredder) c).skipNext = true;
                    }
                }
            }
            ((OilCan) AbstractDungeon.player.getRelic(OilCan.ID)).activate();
        }

        if (!(this instanceof SmartGun))
            checkActivations();
    }

    public final void onMoveToDiscard() {
        Equipped = false;
        PlayOnce = true;
        returnToHand = true;
    }

    public final void damageEquipment(int amount) {
        if (AbstractDungeon.player.hasRelic(StrangeFork.ID) && AbstractDungeon.cardRandomRng.randomBoolean(0.25f)) {
            AbstractDungeon.player.getRelic(StrangeFork.ID).flash();
        } else {
            equipmentHp -= amount;
            if (equipmentHp < 0) equipmentHp = 0;
            syncToMasterDeck();
            AbstractDungeon.topLevelEffectsQueue.add(new DamageEquipmentEffect(this, this.current_x, this.current_y, 1));
            if (equipmentHp < 1 || this instanceof FailSafe && this.alreadyEvolved) {
                setExhaust(true);
                addToTop(new ExhaustSpecificCardAction(this, AbstractDungeon.player.hand, true));
                if (!(this instanceof FailSafe))
                    addToTop(new SfxActionVolume("ORB_FROST_EVOKE", 0f, 2.5F));
            }
        }
    }

    public void syncToMasterDeck() {
        if (AbstractDungeon.player == null)
            return;
        for (AbstractCard c : AbstractDungeon.player.masterDeck.group) {
            if (c.uuid.equals(this.uuid) && c instanceof EquipmentCard) {
                ((EquipmentCard) c).equipmentHp = Math.min(((EquipmentCard) c).equipmentMaxHp, this.equipmentHp);
            }
        }
        if (ThePilotMod.isInCombat())
            this.applyPowers();
    }

    @Override
    public boolean freeToPlay() {
        return true;
    }

    @Override
    public void upgrade() {
        super.upgrade();
        this.equipmentMaxHp += getUpgradeDurability();
        this.equipmentHp += getUpgradeDurability();
        syncToMasterDeck();
    }

    protected int getUpgradeDurability() {
        return 2;
    }

    protected void onEquip() {

    }

    protected void onUnequip() {
    }

    public void onGainCharge() {

    }

    public void triggerOnShuffle() {
    }

    public void onGainBlock() {
    }

    public void onBetterScry() {
    }

    public int customOnLoseHpLast(int damageAmount) {
        return damageAmount;
    }

    public void damageReceived(int damageAmount) {

    }


    public void healEquipment(int healAmount, boolean asPercentage, boolean inCombat) {
        if (asPercentage) {
            healAmount = healAmount * equipmentMaxHp / 100;
        }
        equipmentHp = Math.min(equipmentHp + healAmount, equipmentMaxHp);
        if (inCombat) {
            syncToMasterDeck();
        }
    }

    @Override
    public List<String> getCardDescriptors() {
        return Collections.singletonList(descriptor.TEXT[3]);
    }

    @Override
    public List<TooltipInfo> getCustomTooltips() {
        ArrayList<TooltipInfo> customTooltips = new ArrayList<>();
        customTooltips.add(new TooltipInfo(ThePilotMod.keywords.get("Equipment").PROPER_NAME, ThePilotMod.keywords.get("Equipment").DESCRIPTION));
        return customTooltips;
    }

    @Override
    public AbstractCard makeStatEquivalentCopy() {
        EquipmentCard copy = (EquipmentCard) super.makeStatEquivalentCopy();
        copy.equipmentHp = this.equipmentHp;
        copy.equipmentMaxHp = this.equipmentMaxHp;
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
        if (hb.hovered && canSpawnTutorial && ThePilotMod.unseenTutorials[2]) {
            if (WaitTimer <= 0F) {
                AbstractCard card = this.makeStatEquivalentCopy();
                card.current_x = Settings.WIDTH / 2f - 180f * Settings.scale;
                card.current_y = Settings.HEIGHT / 2f - 35f;
                card.drawScale *= 1.65f;
                AbstractDungeon.ftue = new DurabilityTutorial(card);
                ThePilotMod.unseenTutorials[2] = false;
                try {
                    ThePilotMod.saveTutorialsSeen();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                canSpawnTutorial = false;
            } else {
                WaitTimer -= Gdx.graphics.getDeltaTime();
            }
        }
    }
}
