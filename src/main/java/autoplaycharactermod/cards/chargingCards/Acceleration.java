package autoplaycharactermod.cards.chargingCards;

import autoplaycharactermod.ThePilotMod;
import autoplaycharactermod.actions.SfxActionVolume;
import autoplaycharactermod.cards.BaseCard;
import autoplaycharactermod.character.PilotCharacter;
import autoplaycharactermod.powers.ChargePower;
import autoplaycharactermod.util.CardStats;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DiscardAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.EnergizedPower;
import com.megacrit.cardcrawl.vfx.combat.WhirlwindEffect;

public class Acceleration extends BaseCard {
    public static final String ID = makeID("Acceleration");
    private static final CardStats info = new CardStats(
            PilotCharacter.Meta.CARD_COLOR,
            CardType.SKILL,
            CardRarity.UNCOMMON,
            CardTarget.SELF,
            0
    );
    private static final int MAGIC = 3;
    private static final int UPG_MAGIC = 0;

    public Acceleration() {
        super(ID, info);
        returnToHand = true;
        setMagic(MAGIC, UPG_MAGIC);
        tags.add(ThePilotMod.CustomTags.NoEnergyText);
        checkEvolve();
    }

    public void evolvecard(){
        setMagic(4);
        super.evolveCard();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (PlayOnce) {
            PlayOnce = false;
            addToBot(new SfxActionVolume("ATTACK_DEFECT_BEAM", 0.8F, 1.8F));
            this.addToBot(new VFXAction(new WhirlwindEffect(new Color(0.0F, 1.0F, 1.0F, 0.7F), false), 0.0F));
            this.addToBot(new ApplyPowerAction(p, p, new EnergizedPower(p, upgraded || alreadyEvolved ? 2 : 1)));
            returnToHand = true;
        } else {
            addToBot(new DiscardAction(p, p, 1, false));
            if (this.alreadyEvolved) {
                addToBot(new ApplyPowerAction(p, p, new ChargePower(p, magicNumber * ThePilotMod.energySpentCombat)));
            } else {
                if (ThePilotMod.energySpentTurn > 0)
                    addToBot(new ApplyPowerAction(p, p, new ChargePower(p, magicNumber * ThePilotMod.energySpentTurn)));
            }
            returnToHand = false;
        }
    }

    public void updateTextCount() {
        if (!this.alreadyEvolved) {
            this.rawDescription = upgraded ? cardStrings.UPGRADE_DESCRIPTION : cardStrings.DESCRIPTION;
            if (ThePilotMod.energySpentTurn > 0)
                this.rawDescription += cardStrings.EXTENDED_DESCRIPTION[1] + (ThePilotMod.energySpentTurn) + cardStrings.EXTENDED_DESCRIPTION[2];
            initializeDescription();
        }
    }

    @Override
    public boolean freeToPlay() {
        return true;
    }
}
