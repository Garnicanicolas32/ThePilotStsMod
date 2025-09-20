package autoplaycharactermod.cards.optionSelection.evolutionCards;

import autoplaycharactermod.ThePilotMod;
import autoplaycharactermod.cards.BaseCard;
import autoplaycharactermod.relics.RunicEngine;
import autoplaycharactermod.util.CardStats;
import basemod.patches.com.megacrit.cardcrawl.dungeons.AbstractDungeon.NoPools;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.tempCards.Miracle;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.AbstractRelic;


@NoPools
public class EvolutionOneB extends BaseCard {
    public static final String ID = makeID("EvolutionOneB");
    private static final CardStats info = new CardStats(
            CardColor.COLORLESS,
            CardType.POWER,
            CardRarity.SPECIAL,
            CardTarget.NONE,
            -2 
    );


    public EvolutionOneB() {
        super(ID, info);
        setBackgroundTexture(ThePilotMod.imagePath("character/cardback/bg_evolution1_power.png"), ThePilotMod.imagePath("character/cardback/bg_evolution1_power_p.png"));
        AbstractCard preview = new Miracle();
        preview.upgrade();
        cardsToPreview = preview;
        tags.add(ThePilotMod.CustomTags.Evolution);
    }

    @Override
    public void use(AbstractPlayer abstractPlayer, AbstractMonster abstractMonster) {
        onChoseThisOption();
    }

    public void onChoseThisOption() {
        AbstractRelic r = AbstractDungeon.player.getRelic(RunicEngine.ID);
        if (r != null) {
            ((RunicEngine) r).lvl1 = 2;
            r.updateDescription(null);
        }
    }
}
