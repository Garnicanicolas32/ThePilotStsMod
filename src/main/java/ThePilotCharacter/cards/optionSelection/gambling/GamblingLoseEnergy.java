package ThePilotCharacter.cards.optionSelection.gambling;

import ThePilotCharacter.cards.BaseCard;
import ThePilotCharacter.cards.traitScavengeCards.GachaPull;
import ThePilotCharacter.util.CardStats;
import basemod.patches.com.megacrit.cardcrawl.dungeons.AbstractDungeon.NoPools;
import com.megacrit.cardcrawl.actions.unique.LoseEnergyAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;


@NoPools
public class GamblingLoseEnergy extends BaseCard {
    public static final String ID = makeID("GamblingLoseEnergy");
    private static final CardStats info = new CardStats(
            CardColor.COLORLESS,
            CardType.POWER,
            CardRarity.SPECIAL,
            CardTarget.NONE,
            -2
    );

    public GamblingLoseEnergy() {
        super(ID, info);
    }

    @Override
    public void use(AbstractPlayer abstractPlayer, AbstractMonster abstractMonster) {
        onChoseThisOption();
    }

    public void onChoseThisOption() {
        addToBot(new LoseEnergyAction(EnergyPanel.getCurrentEnergy()));
        GachaPull.cardsList.removeIf(c -> c instanceof GamblingLoseEnergy);
        
    }
}
