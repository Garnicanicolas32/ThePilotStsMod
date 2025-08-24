package autoplaycharactermod.cards.optionSelection.TraitReward;

import autoplaycharactermod.BasicMod;
import autoplaycharactermod.cards.BaseCard;
import autoplaycharactermod.util.CardStats;
import basemod.patches.com.megacrit.cardcrawl.dungeons.AbstractDungeon.NoPools;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;


@NoPools
public class RedOptionOne extends BaseCard {
    public static final String ID = makeID("RedOptionOne");
    private static final CardStats info = new CardStats(
            CardColor.COLORLESS,
            CardType.POWER,
            CardRarity.SPECIAL,
            CardTarget.NONE,
            -2
    );

    public RedOptionOne() {
        super(ID, info);
        setBackgroundTexture(BasicMod.imagePath("character/cardback/bg_evolution3_power.png"), BasicMod.imagePath("character/cardback/bg_evolution3_power_p.png"));
        tags.add(BasicMod.CustomTags.Evolution);
    }

    @Override
    public void use(AbstractPlayer abstractPlayer, AbstractMonster abstractMonster) {
        onChoseThisOption();
    }

    public void onChoseThisOption() {
        BasicMod.startWithDemon = true;
    }
}
