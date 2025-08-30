package autoplaycharactermod.cards.optionSelection.traitReward;

import autoplaycharactermod.BasicMod;
import autoplaycharactermod.cards.BaseCard;
import autoplaycharactermod.util.CardStats;
import basemod.patches.com.megacrit.cardcrawl.dungeons.AbstractDungeon.NoPools;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.AbstractRelic;


@NoPools
public class YellowOptionThree extends BaseCard {
    public static final String ID = makeID("YellowOptionThree");
    private static final CardStats info = new CardStats(
            CardColor.COLORLESS,
            CardType.POWER,
            CardRarity.SPECIAL,
            CardTarget.NONE,
            -2
    );

    public YellowOptionThree() {
        super(ID, info);
        setBackgroundTexture(BasicMod.imagePath("character/cardback/bg_evolution4_power.png"), BasicMod.imagePath("character/cardback/bg_evolution4_power_p.png"));
        tags.add(BasicMod.CustomTags.Evolution);
    }

    @Override
    public void use(AbstractPlayer abstractPlayer, AbstractMonster abstractMonster) {
        onChoseThisOption();
    }

    public void onChoseThisOption() {
        AbstractDungeon.getCurrRoom().addRelicToRewards(AbstractRelic.RelicTier.COMMON);
        AbstractDungeon.getCurrRoom().addRelicToRewards(AbstractRelic.RelicTier.UNCOMMON);
        BasicMod.usedYellowJACKPOT = true;
    }
}
