package autoplaycharactermod.cards.optionSelection.evolutionCards;

import autoplaycharactermod.BasicMod;
import autoplaycharactermod.cards.BaseCard;
import autoplaycharactermod.relics.RunicEngine;
import autoplaycharactermod.util.CardStats;
import basemod.BaseMod;
import basemod.patches.com.megacrit.cardcrawl.dungeons.AbstractDungeon.NoPools;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.AbstractRelic;


@NoPools
public class EvolutionThreeA extends BaseCard {
    public static final String ID = makeID("EvolutionThreeA");
    private static final CardStats info = new CardStats(
            CardColor.COLORLESS,
            CardType.POWER,
            CardRarity.SPECIAL,
            CardTarget.NONE,
            -2 
    );


    public EvolutionThreeA() {
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
            ((RunicEngine) r).lvl3 = 1;
            BaseMod.MAX_HAND_SIZE += 2;
            ((RunicEngine) r).updateDescription(null);
        }
    }
}
