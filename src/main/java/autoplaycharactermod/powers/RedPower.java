package autoplaycharactermod.powers;

import autoplaycharactermod.BasicMod;
import autoplaycharactermod.actions.SfxActionVolume;
import autoplaycharactermod.cards.optionSelection.TraitReward.RedOptionOne;
import autoplaycharactermod.cards.optionSelection.TraitReward.RedOptionThree;
import autoplaycharactermod.cards.optionSelection.TraitReward.RedOptionTwo;
import autoplaycharactermod.character.MyCharacter;
import com.evacipated.cardcrawl.mod.stslib.actions.common.StunMonsterAction;
import com.evacipated.cardcrawl.mod.stslib.patches.NeutralPowertypePatch;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.actions.watcher.ChooseOneAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.vfx.combat.GiantFireEffect;

import java.util.ArrayList;

import static autoplaycharactermod.BasicMod.makeID;

public class RedPower extends BasePower {
    public static final String POWER_ID = makeID("RedPower");
    public static final int RANDOMDAMAGE = 4;
    public static final int TARGETDAMAGE = 5;
    public static final int AOEDAMAGE = 6;
    public static final int STRENGTHTOADD = 2;
    private static final AbstractPower.PowerType TYPE = NeutralPowertypePatch.NEUTRAL;
    private static final boolean TURN_BASED = false;
    public static boolean usedStun = true;

    public RedPower(AbstractCreature owner, int amount) {
        super(POWER_ID, TYPE, TURN_BASED, owner, amount);
    }

    public void playApplyPowerSfx() {
        addToBot(new SfxActionVolume("GHOST_ORB_IGNITE_1", -0.5F + amount * 0.05F, 1.3f));
    }

    @Override
    public void updateDescription() {
        if (DESCRIPTIONS != null && DESCRIPTIONS.length > 10) {
            StringBuilder Desc = new StringBuilder();
            Desc.append(DESCRIPTIONS[0]).append(RANDOMDAMAGE).append(DESCRIPTIONS[1]);
            if (amount >= 2)
                Desc.append(DESCRIPTIONS[2]).append(TARGETDAMAGE).append(DESCRIPTIONS[3]);
            if (amount >= 3)
                Desc.append(DESCRIPTIONS[4]).append(AOEDAMAGE).append(DESCRIPTIONS[5]);
            if (amount >= 4)
                Desc.append(DESCRIPTIONS[6]).append(STRENGTHTOADD).append(DESCRIPTIONS[7]);
            if (amount >= 5)
                Desc.append(DESCRIPTIONS[8]);
            if (amount >= 6)
                Desc.append(DESCRIPTIONS[9]);
            if (amount >= 7)
                Desc.append(DESCRIPTIONS[10]);

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

    private void makeFire() {
        addToBot(new AbstractGameAction() {
            @Override
            public void update() {
                for (int i = 0; i < 6; i++) {
                    AbstractDungeon.effectsQueue.add(new GiantFireEffect());
                }
                isDone = true;
            }
        });
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

    public void atEndOfTurnPreEndTurnCards(boolean isPlayer) {
        if (this.amount > 6 && !BasicMod.usedRedJACKPOT) {
            ArrayList<AbstractCard> stanceChoices = new ArrayList<>();
            stanceChoices.add(new RedOptionOne());
            stanceChoices.add(new RedOptionTwo());
            stanceChoices.add(new RedOptionThree());
            addToBot(new ChooseOneAction(stanceChoices));
        }
    }

    public void atEndOfTurn(boolean isPlayer) {
        if (this.amount > 0) {
            makeFire();
            addToBot(new DamageRandomEnemyAction(new DamageInfo(this.owner, RANDOMDAMAGE, DamageInfo.DamageType.THORNS), AbstractGameAction.AttackEffect.FIRE));
        }
        if (this.amount > 1) {
            makeFire();
            addToBot(new DamageAction(MyCharacter.getTarget(), new DamageInfo(this.owner, TARGETDAMAGE, DamageInfo.DamageType.THORNS), AbstractGameAction.AttackEffect.FIRE));
        }
        if (this.amount > 2) {
            makeFire();
            addToBot(new DamageAllEnemiesAction((AbstractPlayer) this.owner, AOEDAMAGE, DamageInfo.DamageType.THORNS, AbstractGameAction.AttackEffect.FIRE));
        }
        if (this.amount > 3)
            addToBot(new ApplyPowerAction(this.owner, this.owner, new StrengthPower(this.owner, STRENGTHTOADD)));
        if (this.amount > 4) {
            makeFire();
            addToBot(new DamageRandomEnemyAction(new DamageInfo(this.owner, RANDOMDAMAGE, DamageInfo.DamageType.THORNS), AbstractGameAction.AttackEffect.FIRE));
            addToBot(new DamageAction(MyCharacter.getTarget(), new DamageInfo(this.owner, TARGETDAMAGE, DamageInfo.DamageType.THORNS), AbstractGameAction.AttackEffect.FIRE));
            addToBot(new DamageAllEnemiesAction((AbstractPlayer) this.owner, AOEDAMAGE, DamageInfo.DamageType.THORNS, AbstractGameAction.AttackEffect.FIRE));
            addToBot(new ApplyPowerAction(this.owner, this.owner, new StrengthPower(this.owner, STRENGTHTOADD)));
        }
        if (this.amount > 5 && !usedStun) {
            makeFire();
            for (AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
                addToBot(new StunMonsterAction(m, this.owner, 1));
            }
            usedStun = true;
        }
        addToBot(new RemoveSpecificPowerAction(this.owner, this.owner, RedPower.POWER_ID));
    }
}
