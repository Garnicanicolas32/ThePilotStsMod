package ThePilotCharacter.cards.chargingCards;

import ThePilotCharacter.ThePilotMod;
import ThePilotCharacter.cards.BaseCard;
import ThePilotCharacter.character.PilotCharacter;
import ThePilotCharacter.powers.ChargePower;
import ThePilotCharacter.util.CardStats;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.utility.LoseBlockAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.vfx.stance.StanceChangeParticleGenerator;

public class DoubleOrNothing extends BaseCard {
    public static final String ID = makeID("DoubleOrNothing");
    private static final CardStats info = new CardStats(
            PilotCharacter.Meta.CARD_COLOR,
            CardType.SKILL,
            CardRarity.RARE,
            CardTarget.SELF,
            0
    );

    public DoubleOrNothing() {
        super(ID, info);
        returnToHand = true;
        tags.add(ThePilotMod.CustomTags.NoEnergyText);
        checkEvolve();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (PlayOnce && !Duplicated) {
            PlayOnce = false;
            returnToHand = true;
            if (!this.alreadyEvolved)
                addToBot(new LoseBlockAction(p, p, upgraded ? p.currentBlock : p.currentBlock / 2));
        } else {
            AbstractPower pwr = p.getPower(ChargePower.POWER_ID);
            int number = (pwr != null) ? pwr.amount : 0;
            if (number > 0) {
                addToBot(new SFXAction("GUARDIAN_ROLL_UP"));
                AbstractDungeon.topLevelEffectsQueue.add(new StanceChangeParticleGenerator(p.hb.cX, p.hb.cY, "Neutral"));
                if (this.alreadyEvolved) {
                    addToBot(new ApplyPowerAction(p, p, new ChargePower(p, number * 2)));
                } else
                    addToBot(new ApplyPowerAction(p, p, new ChargePower(p, this.upgraded ? number : number / 2)));
            }
            returnToHand = false;
        }
        PlayOnce = false;
    }

    @Override
    public boolean freeToPlay() {
        return true;
    }
}
