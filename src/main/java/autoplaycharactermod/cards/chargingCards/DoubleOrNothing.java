package autoplaycharactermod.cards.chargingCards;

import autoplaycharactermod.BasicMod;
import autoplaycharactermod.cards.BaseCard;
import autoplaycharactermod.character.MyCharacter;
import autoplaycharactermod.powers.ChargePower;
import autoplaycharactermod.util.CardStats;
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
            MyCharacter.Meta.CARD_COLOR,
            CardType.SKILL,
            CardRarity.RARE,
            CardTarget.SELF,
            -2 
    );

    public DoubleOrNothing() {
        super(ID, info);
        returnToHand = true;
        setMagic(1, 2);
        checkEvolve();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractPower pwr = p.getPower(ChargePower.POWER_ID);
        int number = (pwr != null) ? pwr.amount : 0;
        if (number > 0) {
            addToBot(new SFXAction("GUARDIAN_ROLL_UP"));
            AbstractDungeon.topLevelEffectsQueue.add(new StanceChangeParticleGenerator(p.hb.cX, p.hb.cY, "Neutral"));
            addToBot(new ApplyPowerAction(p, p, new ChargePower(p, this.alreadyEvolved ? number * 2 : number)));
        }
        if (!this.alreadyEvolved)
            addToBot(new LoseBlockAction(p, p, p.currentBlock / magicNumber));
        PlayOnce = false;
    }

    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        return PlayOnce && super.canUse(p, m);
    }
}
