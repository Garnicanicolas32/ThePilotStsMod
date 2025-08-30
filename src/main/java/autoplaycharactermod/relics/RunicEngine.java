package autoplaycharactermod.relics;

import autoplaycharactermod.cards.EquipmentCard;
import autoplaycharactermod.character.MyCharacter;
import autoplaycharactermod.patches.ObtainKeyEffectUpdatePatch;
import autoplaycharactermod.vfx.HealEquipmentEffect;
import basemod.abstracts.CustomSavable;
import com.badlogic.gdx.graphics.Color;
import com.evacipated.cardcrawl.mod.stslib.actions.tempHp.AddTemporaryHPAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.tempCards.Miracle;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.powers.EnergizedPower;
import com.megacrit.cardcrawl.powers.LoseStrengthPower;
import com.megacrit.cardcrawl.powers.StrengthPower;

import java.util.ArrayList;
import java.util.Collections;

import static autoplaycharactermod.BasicMod.makeID;

public class RunicEngine extends BaseRelic implements CustomSavable<Integer[]> {
    private static final String NAME = "RunicEngine"; //The name will be used for determining the image file as well as the ID.
    public static final String ID = makeID(NAME); //This adds the mod's prefix to the relic ID, resulting in modID:MyRelic
    private static final RelicTier RARITY = RelicTier.STARTER; //The relic's rarity.
    private static final LandingSound SOUND = LandingSound.HEAVY; //The sound played when the relic is clicked.
    public int lvl1 = 0;
    public int lvl2 = 0;
    public int lvl3 = 0;
    private int countStr = 0;

    public RunicEngine() {
        super(ID, NAME, MyCharacter.Meta.CARD_COLOR, RARITY, SOUND);
        counter = 0;
    }

    public void atBattleStart() {
        if (!(AbstractDungeon.player instanceof MyCharacter) && lvl1 > 0)
            doAction();
    }

    public void onObtainKey() {
        if (counter < 3) {
            this.flash();
            CardCrawlGame.sound.playV("EVENT_ANCIENT", 1.5f);

            if (AbstractDungeon.screen == AbstractDungeon.CurrentScreen.COMBAT_REWARD) {
                AbstractDungeon.overlayMenu.proceedButton.hide();
                AbstractDungeon.previousScreen = AbstractDungeon.screen;
            }
            switch (counter) {
                case 0:
                    AbstractDungeon.cardRewardScreen.chooseOneOpen(ObtainKeyEffectUpdatePatch.getLvl1Cards());
                    break;
                case 1:
                    AbstractDungeon.cardRewardScreen.chooseOneOpen(ObtainKeyEffectUpdatePatch.getLvl2Cards());
                    break;
                case 2:
                    AbstractDungeon.cardRewardScreen.chooseOneOpen(ObtainKeyEffectUpdatePatch.getLvl3Cards());
                    break;
            }
            counter++;
            updateDescription(null);
        }
    }

    public void doAction() {
        countStr = 0;
        AbstractPlayer p = AbstractDungeon.player;

        switch (lvl1) {
            case 1:
                this.flash();
                addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));
                addToBot(new AddTemporaryHPAction(p, AbstractDungeon.player, 9));
                break;
            case 2:
                this.flash();
                addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));
                AbstractCard toShuffle = new Miracle();
                toShuffle.upgrade();
                addToBot(new MakeTempCardInDrawPileAction(toShuffle, 2, true, true, false));
                break;
            case 3:
                this.flash();
                addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));
                addToTop(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new StrengthPower(AbstractDungeon.player, 3), 3));
                addToTop(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new LoseStrengthPower(AbstractDungeon.player, 3), 3));
                break;
        }
        if (lvl3 == 2){
            addToBot(new GainEnergyAction(1));
            addToBot(new ApplyPowerAction(p, p, new EnergizedPower(p, 1), 1));
        }
    }

    public void onPlayerEndTurn() {
        switch (lvl2) {
            case 1:
                if (AbstractDungeon.player.hand.group.size() > 1) {
                    flash();
                    addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));
                    addToBot(new GainBlockAction(AbstractDungeon.player, null, AbstractDungeon.player.hand.group.size() / 2));
                }
                break;
            case 2:
                ArrayList<EquipmentCard> targetPool = getEquipmentCards();
                if (!targetPool.isEmpty()) {
                    this.flash();
                    addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));
                    Collections.shuffle(targetPool, new java.util.Random(AbstractDungeon.cardRandomRng.randomLong()));
                    addToBot(new SFXAction("HEAL_2"));
                    for (int i = 0; i < 1; i++) {
                        EquipmentCard toHeal = targetPool.get(i);
                        toHeal.flash(Color.GREEN.cpy());
                        AbstractDungeon.topLevelEffectsQueue.add(
                                new HealEquipmentEffect(toHeal, this.currentX, this.currentY, 1)
                        );
                        toHeal.healEquipment(1, false, true);
                    }
                }
                break;
            case 3:
                if (countStr < 3) {
                    this.flash();
                    addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));
                    addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new StrengthPower(AbstractDungeon.player, 1)));
                    countStr++;
                }
                break;
        }
    }

    private static ArrayList<EquipmentCard> getEquipmentCards() {
        ArrayList<EquipmentCard> damaged = new ArrayList<>();
        ArrayList<EquipmentCard> allEquipments = new ArrayList<>();

        for (AbstractCard c : AbstractDungeon.player.hand.group) {
            if (c instanceof EquipmentCard) {
                EquipmentCard eq = ((EquipmentCard) c);
                allEquipments.add(eq);
                if (eq.equipmentHp < eq.equipmentMaxHp) {
                    damaged.add(eq);
                }
            }
        }

        return !damaged.isEmpty() ? damaged : allEquipments;
    }

    @Override
    public Integer[] onSave() {
        return new Integer[]{
                lvl1, lvl2, lvl3
        };
    }

    @Override
    public void onLoad(Integer[] integers) {
        if (integers != null && integers.length >= 3) {
            lvl1 = integers[0];
            lvl2 = integers[1];
            lvl3 = integers[2];
        }
        updateDescription(null);
    }

    @Override
    public String getUpdatedDescription() {
        if (DESCRIPTIONS == null || DESCRIPTIONS.length < 12)
            return "[ERROR]";

        StringBuilder sb = new StringBuilder(DESCRIPTIONS[0]); //BASE
        if (counter >= 1 && lvl1 > 0 && lvl1 <= 3) {
            sb.append(DESCRIPTIONS[1]); // Combat start
            sb.append(DESCRIPTIONS[1 + lvl1]);
        }

        if (counter >= 2 && lvl2 > 0 && lvl2 <= 3) {
            sb.append(DESCRIPTIONS[5]); // Turn end
            sb.append(DESCRIPTIONS[5 + lvl2]);
        }

        if (counter >= 3 && lvl3 > 0 && lvl3 <= 3) {
            sb.append(DESCRIPTIONS[9]); // Permanent
            sb.append(DESCRIPTIONS[9 + lvl3]);
        }
        return sb.toString();
    }

    @Override
    public void updateDescription(AbstractPlayer.PlayerClass c) {
        description = getUpdatedDescription();
        tips.clear();
        tips.add(new PowerTip(name, description));
        initializeTips();
    }
}
