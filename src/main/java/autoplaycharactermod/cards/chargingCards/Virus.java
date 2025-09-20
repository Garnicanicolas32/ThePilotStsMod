package autoplaycharactermod.cards.chargingCards;

import autoplaycharactermod.actions.AutoplayTopCardAction;
import autoplaycharactermod.cards.BaseCard;
import autoplaycharactermod.character.PilotCharacter;
import autoplaycharactermod.ui.ConfigPanel;
import autoplaycharactermod.util.CardStats;
import autoplaycharactermod.vfx.VirusEffect;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.VulnerablePower;

public class Virus extends BaseCard {
    public static final String ID = makeID("Virus");
    private static final CardStats info = new CardStats(
            PilotCharacter.Meta.CARD_COLOR,
            CardType.SKILL,
            CardRarity.RARE,
            CardTarget.SELF,
            -2 
    );
    private static final int MAGIC = 2;
    private static final int UPG = 1;

    public Virus() {
        super(ID, info);
        returnToHand = true;
        setMagic(MAGIC, UPG);
        checkEvolve();
    }

    @Override
    public void evolveCard() {
        setMagic(4);
        super.evolveCard();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new SFXAction("INTIMIDATE"));
        for (int i = 0; i < (ConfigPanel.lessParticles ? 15 : 30); ++i) {
            AbstractDungeon.effectList.add(new VirusEffect());
        }
        for (int i = 0; i < magicNumber; i++) {
            addToBot(new WaitAction(0.2f));
            addToBot(new AutoplayTopCardAction());
        }
        if (!this.alreadyEvolved) {
            addToBot(new ApplyPowerAction(p,p, new VulnerablePower(p,1,false)));
        }
        PlayOnce = false;
    }

    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        return PlayOnce && super.canUse(p, m);
    }
}
