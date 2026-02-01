package ThePilotCharacter.cards.optionSelection.evolutionCards;

import ThePilotCharacter.ThePilotMod;
import ThePilotCharacter.cards.BaseCard;
import ThePilotCharacter.relics.RunicEngine;
import ThePilotCharacter.util.CardStats;
import basemod.patches.com.megacrit.cardcrawl.dungeons.AbstractDungeon.NoPools;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.AbstractRelic;


@NoPools
public class EvolutionOneA extends BaseCard {
    public static final String ID = makeID("EvolutionOneA");
    private static final CardStats info = new CardStats(
            CardColor.COLORLESS,
            CardType.POWER,
            CardRarity.SPECIAL,
            CardTarget.NONE,
            -2 
    );

    public EvolutionOneA() {
        super(ID, info);
        setBackgroundTexture(ThePilotMod.imagePath("character/cardback/bg_evolution1_power.png"), ThePilotMod.imagePath("character/cardback/bg_evolution1_power_p.png"));
        tags.add(ThePilotMod.CustomTags.Evolution);
    }

    @Override
    public void use(AbstractPlayer abstractPlayer, AbstractMonster abstractMonster) {
        onChoseThisOption();
    }

    public void onChoseThisOption() {
        AbstractRelic r = AbstractDungeon.player.getRelic(RunicEngine.ID);
        if (r != null) {
            ((RunicEngine) r).lvl1 = 1;
            r.updateDescription(null);
        }
    }
}
