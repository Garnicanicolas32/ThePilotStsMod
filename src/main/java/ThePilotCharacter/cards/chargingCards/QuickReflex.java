package ThePilotCharacter.cards.chargingCards;

import ThePilotCharacter.ThePilotMod;
import ThePilotCharacter.actions.EjectedEffectAction;
import ThePilotCharacter.cards.BaseCard;
import ThePilotCharacter.character.PilotCharacter;
import ThePilotCharacter.powers.ChargePower;
import ThePilotCharacter.powers.EfficiencyPower;
import ThePilotCharacter.util.CardStats;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.WeakPower;

public class QuickReflex extends BaseCard {
    public static final String ID = makeID("QuickReflex");
    private static final CardStats info = new CardStats(
            PilotCharacter.Meta.CARD_COLOR,
            CardType.SKILL,
            CardRarity.COMMON,
            CardTarget.SELF,
            -2 
    );
    private static final int MAGIC = 4;
    private static final int UPG_MAGIC = 2;
    private static final int CAPACITY = 5;
    private static final int UPG_CAPACITY = 3;

    public QuickReflex() {
        super(ID, info);
        returnToHand = true;
        setMagic(MAGIC, UPG_MAGIC);
        setCustomVar("CAPACITY", CAPACITY, UPG_CAPACITY);
        setCustomVar("WEAK", 1, 1);
        checkEvolve();
    }

    @Override
    public void evolveCard() {
        setMagic(2);
        setCustomVar("WEAK", 3);
        setCustomVar("CAPACITY", 14);
        super.evolveCard();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new GainBlockAction(p, p, block));
        addToBot(new ApplyPowerAction(p, p, new ChargePower(p, customVar("CAPACITY"))));
        addToBot(new ApplyPowerAction(PilotCharacter.getTarget(), p, new WeakPower(p, customVar("WEAK"), false)));
        PlayOnce = false;
    }

    public void updateTextCount() {
        if (!this.alreadyEvolved) {
                this.rawDescription = cardStrings.DESCRIPTION;
            if (ThePilotMod.energySpentTurn > 0)
                this.rawDescription += cardStrings.EXTENDED_DESCRIPTION[1] + ThePilotMod.energySpentTurn + cardStrings.EXTENDED_DESCRIPTION[2];
            initializeDescription();
        }
    }

    public void triggerOnManualDiscard() {
        eject();
    }

    public void eject() {
        if (AbstractDungeon.player.hasPower(EfficiencyPower.POWER_ID))
            ((EfficiencyPower)AbstractDungeon.player.getPower(EfficiencyPower.POWER_ID)).triggerEject();
        if (this.alreadyEvolved) {
            addToBot(new EjectedEffectAction());
            addToBot(new GainEnergyAction(2));
        }else if (ThePilotMod.energySpentTurn <= magicNumber){
            addToBot(new EjectedEffectAction());
            addToBot(new GainEnergyAction(1));
        }
    }

    public void triggerOnGlowCheck() {
        this.glowColor = ThePilotMod.energySpentTurn <= this.magicNumber ? AbstractCard.GOLD_BORDER_GLOW_COLOR : AbstractCard.BLUE_BORDER_GLOW_COLOR;
    }

    public void onScrySelected() {
        eject();
    }

    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        return PlayOnce && super.canUse(p, m);
    }
}
