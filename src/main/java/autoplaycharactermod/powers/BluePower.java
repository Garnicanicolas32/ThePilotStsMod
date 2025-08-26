package autoplaycharactermod.powers;

import autoplaycharactermod.BasicMod;
import autoplaycharactermod.cards.optionSelection.TraitReward.BlueOptionOne;
import autoplaycharactermod.cards.optionSelection.TraitReward.BlueOptionThree;
import autoplaycharactermod.cards.optionSelection.TraitReward.BlueOptionTwo;
import autoplaycharactermod.character.MyCharacter;
import autoplaycharactermod.ui.ConfigPanel;
import com.badlogic.gdx.math.MathUtils;
import com.evacipated.cardcrawl.mod.stslib.patches.NeutralPowertypePatch;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.actions.unique.DoubleYourBlockAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.actions.watcher.ChooseOneAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.PlatedArmorPower;
import com.megacrit.cardcrawl.powers.ThornsPower;
import com.megacrit.cardcrawl.powers.WeakPower;
import com.megacrit.cardcrawl.vfx.UpgradeHammerImprintEffect;
import com.megacrit.cardcrawl.vfx.combat.GiantFireEffect;

import javax.sound.midi.Soundbank;
import java.util.ArrayList;

import static autoplaycharactermod.BasicMod.makeID;

public class BluePower extends BasePower {
    public static final String POWER_ID = makeID("BluePower");
    public static final int SHIELDAMOUNT = 4;
    public static final int THORNS = 2;
    public static final int PLATEDARMOR = 2;
    private static final PowerType TYPE = NeutralPowertypePatch.NEUTRAL;
    private static final boolean TURN_BASED = false;

    //The only thing TURN_BASED controls is the color of the number on the power icon.
    //Turn based powers are white, non-turn based powers are red or green depending on if their amount is positive or negative.
    //For a power to actually decrease/go away on its own they do it themselves.
    //Look at powers that do this like VulnerablePower and DoubleTapPower.

    public BluePower(AbstractCreature owner, int amount) {
        super(POWER_ID, TYPE, TURN_BASED, owner, amount);
    }

    public void playApplyPowerSfx() {
        CardCrawlGame.sound.playAV("SPHERE_DETECT_VO_2", -0.5F + amount * 0.05F,2f);
    }

    @Override
    public void updateDescription() {
        if (DESCRIPTIONS != null && DESCRIPTIONS.length > 9){
        StringBuilder Desc = new StringBuilder();
        Desc.append(DESCRIPTIONS[0]).append(SHIELDAMOUNT).append(DESCRIPTIONS[1]);
        if (amount >= 2)
            Desc.append(DESCRIPTIONS[3]).append(THORNS).append(DESCRIPTIONS[4]);
        if (amount >= 3)
            Desc.append(DESCRIPTIONS[2]);
        if (amount >= 4)
            Desc.append(DESCRIPTIONS[5]).append(PLATEDARMOR).append(DESCRIPTIONS[6]);
        if (amount >= 5)
            Desc.append(DESCRIPTIONS[8]);
        if (amount >= 6)
            Desc.append(DESCRIPTIONS[7]);
        if (amount >= 7)
            Desc.append(DESCRIPTIONS[9]);

        this.description = Desc.toString();
        } else
            this.description = "[MISSING DESCRIPTION]";
    }

    public void stackPower(int stackAmount) {
        if (this.amount != -1) {
            this.fontScale = 8.0F;
            this.amount += stackAmount;
        }
        updateDescription();
    }

    public void reducePower(int reduceAmount) {
        if (this.amount - reduceAmount <= 0) {
            this.fontScale = 8.0F;
            this.amount = 0;
        } else {
            this.fontScale = 8.0F;
            this.amount -= reduceAmount;
        }
        updateDescription();
    }

    private void HammerImprints(){
        addToBot(new AbstractGameAction() {
            @Override
            public void update() {
                if (ConfigPanel.experimentalSounds)
                CardCrawlGame.sound.playAV("SPHERE_DETECT_VO_1", -0.2F,1.4f);
                for (int i = 0; i < 3; i++) {
                    AbstractDungeon.effectsQueue.add(new UpgradeHammerImprintEffect(MathUtils.random(0.0F, 1870.0F) * Settings.xScale, MathUtils.random(50.0F, 990.0F) * Settings.yScale));
                }
                isDone = true;
            }
        });

        addToBot(new WaitAction(0.1f));
    }

    public void atEndOfTurnPreEndTurnCards(boolean isPlayer) {
        if (this.amount > 6 && !BasicMod.usedBlueJACKPOT) {
            BasicMod.usedBlueJACKPOT = true;
            ArrayList<AbstractCard> stanceChoices = new ArrayList<>();
            stanceChoices.add(new BlueOptionOne());
            stanceChoices.add(new BlueOptionTwo());
            stanceChoices.add(new BlueOptionThree());
            addToBot(new ChooseOneAction(stanceChoices));
        }
    }

    public void atEndOfTurn(boolean isPlayer) {
        if (this.amount > 0) {
            HammerImprints();
            addToBot(new GainBlockAction(this.owner, this.owner, SHIELDAMOUNT));
        }
        if (this.amount > 1){
            HammerImprints();
            addToBot(new ApplyPowerAction(this.owner, this.owner, new ThornsPower(this.owner, THORNS)));}
        if (this.amount > 2){
            HammerImprints();
            addToBot(new ApplyPowerAction(MyCharacter.getTarget(), this.owner, new WeakPower(MyCharacter.getTarget(), 1, false)));}
        if (this.amount > 3){
            HammerImprints();
            addToBot(new ApplyPowerAction(this.owner, this.owner, new PlatedArmorPower(this.owner, PLATEDARMOR)));}
        addToBot(new GainBlockAction(this.owner, this.owner, PLATEDARMOR));
        if (this.amount > 4) {
            HammerImprints();
            addToBot(new AbstractGameAction() {
                @Override
                public void update() {
                    //addToTop(new DamageAllEnemiesAction(AbstractDungeon.player, AbstractDungeon.player.currentBlock, DamageInfo.DamageType.THORNS, AttackEffect.BLUNT_HEAVY));
                    addToTop(new DamageAction(MyCharacter.getTarget(), new DamageInfo(AbstractDungeon.player, AbstractDungeon.player.currentBlock, DamageInfo.DamageType.THORNS), AttackEffect.BLUNT_HEAVY));
                    isDone = true;
                }
            });
        }
        if (this.amount > 5) {
            HammerImprints();
            addToBot(new DoubleYourBlockAction(this.owner));
        }
        addToBot(new RemoveSpecificPowerAction(this.owner, this.owner, BluePower.POWER_ID));
    }
}
