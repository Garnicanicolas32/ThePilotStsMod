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
public class EvolutionTwoC extends BaseCard {
    public static final String ID = makeID("EvolutionTwoC");
    private static final CardStats info = new CardStats(
            CardColor.COLORLESS,
            CardType.POWER,
            CardRarity.SPECIAL,
            CardTarget.NONE,
            -2 
    );
    private static final int DAMAGE = 6;
    private static final int UPG_DAMAGE = 3;


    public EvolutionTwoC() {
        super(ID, info);
        setBackgroundTexture(ThePilotMod.imagePath("character/cardback/bg_evolution2_power.png"), ThePilotMod.imagePath("character/cardback/bg_evolution2_power_p.png"));
        tags.add(ThePilotMod.CustomTags.Evolution);
    }

    @Override
    public void use(AbstractPlayer abstractPlayer, AbstractMonster abstractMonster) {
        onChoseThisOption();
    }

    public void onChoseThisOption() {
        AbstractRelic r = AbstractDungeon.player.getRelic(RunicEngine.ID);
        if (r != null) {
            ((RunicEngine) r).lvl2 = 3;
            ((RunicEngine) r).updateDescription(null);
        }
    }
}
