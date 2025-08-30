package autoplaycharactermod.cards.optionSelection.traitReward;

import autoplaycharactermod.BasicMod;
import autoplaycharactermod.cards.BaseCard;
import autoplaycharactermod.util.CardStats;
import basemod.patches.com.megacrit.cardcrawl.dungeons.AbstractDungeon.NoPools;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;


@NoPools
public class RedOptionTwo extends BaseCard {
    public static final String ID = makeID("RedOptionTwo");
    public static final int MAGIC = 15;
    private static final CardStats info = new CardStats(
            CardColor.COLORLESS,
            CardType.POWER,
            CardRarity.SPECIAL,
            CardTarget.NONE,
            -2 
    );

    public RedOptionTwo() {
        super(ID, info);
        setMagic(MAGIC);
        setBackgroundTexture(BasicMod.imagePath("character/cardback/bg_evolution3_power.png"), BasicMod.imagePath("character/cardback/bg_evolution3_power_p.png"));
        tags.add(BasicMod.CustomTags.Evolution);
    }

    @Override
    public void use(AbstractPlayer abstractPlayer, AbstractMonster abstractMonster) {
        onChoseThisOption();
    }

    public void onChoseThisOption() {
        BasicMod.startWithLessHP = true;
        BasicMod.usedRedJACKPOT = true;
    }
}
