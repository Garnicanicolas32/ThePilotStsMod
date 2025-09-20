package autoplaycharactermod.cards.chargingCards;

import autoplaycharactermod.cards.BaseCard;
import autoplaycharactermod.character.PilotCharacter;
import autoplaycharactermod.powers.EnergyBurstPower;
import autoplaycharactermod.util.CardStats;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.WobblyLineEffect;

public class EnergyBurst extends BaseCard {
    public static final String ID = makeID("EnergyBurst");

    private static final CardStats info = new CardStats(
            PilotCharacter.Meta.CARD_COLOR,
            CardType.POWER,
            CardRarity.RARE,
            CardTarget.SELF,
            -2
    );

    public EnergyBurst() {
        super(ID, info);
        setMagic(1,1);
        checkEvolve();
    }

    @Override
    public void evolveCard() {
        super.evolveCard();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.effectsQueue.add(new WobblyLineEffect(p.hb_x, p.hb_y, Color.CYAN.cpy()));
        addToBot(new ApplyPowerAction(p, p, new EnergyBurstPower(p, this.alreadyEvolved ? 4 : magicNumber)));
    }
}
