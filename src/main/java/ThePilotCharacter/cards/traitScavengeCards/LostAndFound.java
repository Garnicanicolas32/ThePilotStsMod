package ThePilotCharacter.cards.traitScavengeCards;

import ThePilotCharacter.cards.TraitCard;
import ThePilotCharacter.character.PilotCharacter;
import ThePilotCharacter.util.CardStats;
import ThePilotCharacter.vfx.LostAndFoundEffect;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class LostAndFound extends TraitCard {
    public static final String ID = makeID("LostAndFound");
    private static final CardStats info = new CardStats(
            PilotCharacter.Meta.CARD_COLOR,
            CardType.SKILL,
            CardRarity.RARE,
            CardTarget.SELF,
            -2 
    );
    private static final int MAGIC = 2;
    private static final int UPG_MAGIC = 1;

    public LostAndFound() {
        super(ID, info, TraitCard.TraitColor.SCAVENGE, false);
        setMagic(MAGIC, UPG_MAGIC);
        checkEvolve();
    }

    @Override
    public void evolveCard() {
        setMagic(4);
        super.evolveCard();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (baseMagicNumber > 0) {
            addToBot(new SFXAction("GOLD_GAIN_2"));
            addToBot(new VFXAction(new LostAndFoundEffect(Settings.WIDTH / 2f, Settings.HEIGHT / 2f)));
            addToBot(new GainEnergyAction(baseMagicNumber));
            if (!this.alreadyEvolved) {
                baseMagicNumber--;
            }
        }

        if (!this.alreadyEvolved) {
            if (baseMagicNumber > 0) {
                StringBuilder energySymbols = new StringBuilder();
                for (int i = 0; i < baseMagicNumber; i++) {
                    energySymbols.append("[E] ");
                }
                this.rawDescription = cardStrings.EXTENDED_DESCRIPTION[1] + energySymbols + cardStrings.EXTENDED_DESCRIPTION[2];
            } else {
                this.rawDescription = cardStrings.EXTENDED_DESCRIPTION[3];
            }
            initializeDescription();
        }
        PlayOnce = false;
        super.use(p, m);
    }
}
