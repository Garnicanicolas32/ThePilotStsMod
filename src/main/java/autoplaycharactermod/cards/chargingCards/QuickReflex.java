package autoplaycharactermod.cards.chargingCards;

import autoplaycharactermod.BasicMod;
import autoplaycharactermod.actions.EjectedEffectAction;
import autoplaycharactermod.cards.BaseCard;
import autoplaycharactermod.character.MyCharacter;
import autoplaycharactermod.powers.ChargePower;
import autoplaycharactermod.powers.EfficiencyPower;
import autoplaycharactermod.util.CardStats;
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
            MyCharacter.Meta.CARD_COLOR,
            CardType.SKILL,
            CardRarity.UNCOMMON,
            CardTarget.SELF,
            -2 
    );
    private static final int MAGIC = 5;
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
        super.evolveCard();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new GainBlockAction(p, p, block));
        addToBot(new ApplyPowerAction(p, p, new ChargePower(p, alreadyEvolved ? 12 : customVar("CAPACITY"))));
        addToBot(new ApplyPowerAction(MyCharacter.getTarget(), p, new WeakPower(p, customVar("WEAK"), false)));
        PlayOnce = false;
    }

    public void updateTextCount() {
        if (!this.alreadyEvolved) {
            if (upgraded)
                this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;
            else
                this.rawDescription = cardStrings.DESCRIPTION;

            if (BasicMod.energySpentTurn > 0)
                this.rawDescription += cardStrings.EXTENDED_DESCRIPTION[1] + (BasicMod.energySpentTurn) + cardStrings.EXTENDED_DESCRIPTION[2];
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
            addToBot(new GainBlockAction(AbstractDungeon.player, AbstractDungeon.player, block));
            addToBot(new GainEnergyAction(2));
        }else if (BasicMod.energySpentTurn <= magicNumber){
            addToBot(new EjectedEffectAction());
            addToBot(new GainEnergyAction(1));
        }
    }

    public void triggerOnGlowCheck() {
        this.glowColor = BasicMod.energySpentTurn <= this.magicNumber ? AbstractCard.GOLD_BORDER_GLOW_COLOR : AbstractCard.BLUE_BORDER_GLOW_COLOR;
    }

    public void onScrySelected() {
        eject();
    }

    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        return PlayOnce && super.canUse(p, m);
    }
}
