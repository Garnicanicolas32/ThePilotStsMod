package ThePilotCharacter.powers;

import ThePilotCharacter.ThePilotMod;
import ThePilotCharacter.cards.optionSelection.gambling.*;
import ThePilotCharacter.cards.optionSelection.traitReward.YellowOptionOne;
import ThePilotCharacter.cards.optionSelection.traitReward.YellowOptionThree;
import ThePilotCharacter.cards.optionSelection.traitReward.YellowOptionTwo;
import ThePilotCharacter.ui.ConfigPanel;
import ThePilotCharacter.ui.ScrapReward;
import ThePilotCharacter.vfx.GachaPullEffect;
import com.badlogic.gdx.math.MathUtils;
import com.evacipated.cardcrawl.mod.stslib.actions.tempHp.AddTemporaryHPAction;
import com.evacipated.cardcrawl.mod.stslib.patches.NeutralPowertypePatch;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.GainGoldAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.actions.watcher.ChooseOneAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.GainPennyEffect;
import com.megacrit.cardcrawl.vfx.ShineSparkleEffect;
import com.megacrit.cardcrawl.vfx.UpgradeShineEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardBrieflyEffect;

import java.util.ArrayList;
import java.util.Collections;

import static ThePilotCharacter.ThePilotMod.makeID;

public class YellowPower extends BasePower {
    public static final String POWER_ID = makeID("YellowPower");
    public static final int GOLDAMOUNTSTART = 5;
    public static final int GOLDAMOUNT = 1;
    public static final int PUNISHMENTAMOUNT = 2;
    private static final PowerType TYPE = NeutralPowertypePatch.NEUTRAL;
    private static final boolean TURN_BASED = false;
    public static boolean usedScrap = false;
    public static boolean usedPotion = false;
    public static boolean usedCardReward = false;
    public static boolean usedUpgrade = false;
    public static int usesGold = 0;

    //The only thing TURN_BASED controls is the color of the number on the power icon.
    //Turn based powers are white, non-turn based powers are red or green depending on if their amount is positive or negative.
    //For a power to actually decrease/go away on its own they do it themselves.
    //Look at powers that do this like VulnerablePower and DoubleTapPower.
    private static ArrayList<AbstractCard> rewardlist() {
        ArrayList<AbstractCard> listreturn = new ArrayList<AbstractCard>();
        listreturn.add(new GamblingBlock());
        listreturn.add(new GamblingDamage());
        listreturn.add(new GamblingScry());
        listreturn.add(new GamblingEnergy());
        if (AbstractDungeon.cardRandomRng.randomBoolean(0.7f)) {
            listreturn.add(new GamblingDexterity());
            listreturn.add(new GamblingStrength());
            listreturn.add(new GamblingCreateScavenge());
            listreturn.add(new GamblingPlatedArmor());
        }
        if (AbstractDungeon.cardRandomRng.randomBoolean(0.5f)) {
            listreturn.add(new GamblingHeal());
            listreturn.add(new GamblingScrap());
        }
        if (AbstractDungeon.cardRandomRng.randomBoolean(0.3f)) {
            listreturn.add(new GamblingCardReward());
            listreturn.add(new GamblingPotion());
            listreturn.add(new GamblingGold());
        }
        for (AbstractCard c : AbstractDungeon.player.masterDeck.group) {
            if ((c.hasTag(AbstractCard.CardTags.STARTER_DEFEND) || c.hasTag(AbstractCard.CardTags.STARTER_STRIKE)) && !c.upgraded) {
                if (AbstractDungeon.cardRandomRng.randomBoolean(0.3f)) {
                    listreturn.add(new GamblingUpgradeCards());
                }
                break;
            }
        }
        return listreturn;
    }

    public YellowPower(AbstractCreature owner, int amount) {
        super(POWER_ID, TYPE, TURN_BASED, owner, amount);
    }

    public void playApplyPowerSfx() {
        CardCrawlGame.sound.playAV("GOLD_GAIN_4", -0.5F + amount * 0.05F, 2f);
    }

    @Override
    public void updateDescription() {
        if (DESCRIPTIONS != null && DESCRIPTIONS.length > 9) {
            StringBuilder Desc = new StringBuilder();
            Desc.append(DESCRIPTIONS[0]).append(PUNISHMENTAMOUNT).append(DESCRIPTIONS[1]);
            if (amount >= 2) {
                if (!usedPotion)
                    Desc.append(DESCRIPTIONS[6]);
                else
                    Desc.append(" NL 2. ").append(DESCRIPTIONS[10]);
            }
            if (amount >= 3) {
                if (usesGold < 2)
                    Desc.append(DESCRIPTIONS[2]).append(ThePilotMod.scavengeCount).append(DESCRIPTIONS[3]).append(GOLDAMOUNT).append(DESCRIPTIONS[4]);
                else
                    Desc.append(" NL 3. ").append(DESCRIPTIONS[10]);
            }
            if (amount >= 4) {
                if (!usedScrap)
                    Desc.append(DESCRIPTIONS[5]);
                else
                    Desc.append(" NL 4. ").append(DESCRIPTIONS[10]);
            }
            if (amount >= 5) {
                if (!usedCardReward)
                    Desc.append(DESCRIPTIONS[7]);
                else
                    Desc.append(" NL 5. ").append(DESCRIPTIONS[10]);
            }
            if (amount >= 6) {
                if (!usedUpgrade)
                    Desc.append(DESCRIPTIONS[8]);
                else
                    Desc.append(" NL 6. ").append(DESCRIPTIONS[10]);
            }
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

    private void sparkles() {
        addToBot(new AbstractGameAction() {
            @Override
            public void update() {
                for (int i = 0; i < 10; i++) {
                    AbstractDungeon.effectsQueue.add(new ShineSparkleEffect(MathUtils.random(0.0F, 1870.0F) * Settings.xScale, MathUtils.random(50.0F, 990.0F) * Settings.yScale));
                }
                isDone = true;
            }
        });
        addToBot(new WaitAction(0.1f));
        addToBot(new WaitAction(0.1f));
    }

    public void atEndOfTurnPreEndTurnCards(boolean isPlayer) {
        if (this.amount > 6 && !ThePilotMod.usedYellowJACKPOT) {
            ArrayList<AbstractCard> stanceChoices = new ArrayList<>();
            stanceChoices.add(new YellowOptionThree());
            stanceChoices.add(new YellowOptionTwo());
            stanceChoices.add(new YellowOptionOne());
            addToBot(new ChooseOneAction(stanceChoices));
        }

        if (this.amount > 2 && usesGold < 2) {
            sparkles();
            addToBot(new GainGoldAction(ThePilotMod.scavengeCount));
            for (int i = 0; i < ThePilotMod.scavengeCount; i++)
                AbstractDungeon.effectList.add(new GainPennyEffect(AbstractDungeon.player.hb_x, AbstractDungeon.player.hb_y));
            ThePilotMod.scavengeCount += GOLDAMOUNT;
            usesGold++;
        }
        if (this.amount > 3 && !usedScrap) {
            sparkles();
            AbstractDungeon.getCurrRoom().rewards.add(new ScrapReward());
            usedScrap = true;
        }
        if (this.amount > 4 && !usedCardReward) {
            sparkles();
            ThePilotMod.extracards++;
            usedCardReward = true;
        }
        if (this.amount > 5 && !usedUpgrade) {
            sparkles();
            ArrayList<AbstractCard> possibleCards = new ArrayList<>();
            for (AbstractCard c : AbstractDungeon.player.masterDeck.group) {
                if (c.canUpgrade())
                    possibleCards.add(c);
            }
            if (!possibleCards.isEmpty()) {
                AbstractCard theCard = possibleCards.get(AbstractDungeon.miscRng.random(0, possibleCards.size() - 1));
                theCard.upgrade();
                AbstractDungeon.player.bottledCardUpgradeCheck(theCard);
                AbstractDungeon.effectsQueue.add(new UpgradeShineEffect((float) Settings.WIDTH / 2.0F, (float) Settings.HEIGHT / 2.0F));
                AbstractDungeon.topLevelEffectsQueue.add(new ShowCardBrieflyEffect(theCard.makeStatEquivalentCopy()));
                usedUpgrade = true;
            }
        }

    }

    public void atEndOfTurn(boolean isPlayer) {
        if (this.amount > 0) {
            sparkles();
            addToBot(new AddTemporaryHPAction(owner, owner, PUNISHMENTAMOUNT));
        }
        if (this.amount > 1 && !usedPotion) {
            sparkles();
            addToBot(new SFXAction("UNLOCK_PING"));
            if (!ConfigPanel.lessParticles)
                AbstractDungeon.effectList.add(new GachaPullEffect(25));
            ArrayList<AbstractCard> possiblecards = rewardlist();
            Collections.shuffle(possiblecards, AbstractDungeon.cardRandomRng.random);
            ArrayList<AbstractCard> RewardChoices = new ArrayList<>();
            for (int i = 0; i < 2 && i < possiblecards.size(); i++) {
                RewardChoices.add(possiblecards.get(i).makeCopy());
            }
            addToBot(new ChooseOneAction(RewardChoices));
            usedPotion = true;
        }
        addToBot(new RemoveSpecificPowerAction(this.owner, this.owner, YellowPower.POWER_ID));
    }
}
