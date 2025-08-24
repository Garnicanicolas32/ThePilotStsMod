package autoplaycharactermod.cards.optionSelection.evolutionCards;

import autoplaycharactermod.BasicMod;
import autoplaycharactermod.cards.BaseCard;
import autoplaycharactermod.relics.RunicEngine;
import autoplaycharactermod.ui.ScryButton;
import autoplaycharactermod.util.CardStats;
import basemod.patches.com.megacrit.cardcrawl.dungeons.AbstractDungeon.NoPools;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.AbstractRelic;


@NoPools
public class EvolutionThreeC extends BaseCard {
    public static final String ID = makeID("EvolutionThreeC");
    private static final CardStats info = new CardStats(
            CardColor.COLORLESS,
            CardType.POWER,
            CardRarity.SPECIAL,
            CardTarget.NONE,
            -2 
    );
    private static final int DAMAGE = 6;
    private static final int UPG_DAMAGE = 3;


    public EvolutionThreeC() {
        super(ID, info);
        setBackgroundTexture(BasicMod.imagePath("character/cardback/bg_evolution3_power.png"), BasicMod.imagePath("character/cardback/bg_evolution3_power_p.png"));
        tags.add(BasicMod.CustomTags.Evolution);
    }

    @Override
    public void use(AbstractPlayer abstractPlayer, AbstractMonster abstractMonster) {
        onChoseThisOption();
    }

    public void onChoseThisOption() {
        AbstractRelic r = AbstractDungeon.player.getRelic(RunicEngine.ID);
        if (r != null) {
            ((RunicEngine) r).lvl3 = 3;
            ScryButton.scryAmount += 1;
            ((RunicEngine) r).updateDescription(null);
        }
    }
}
