package autoplaycharactermod.cards.optionSelection.TraitReward;

import autoplaycharactermod.BasicMod;
import autoplaycharactermod.cards.BaseCard;
import autoplaycharactermod.util.CardStats;
import basemod.patches.com.megacrit.cardcrawl.dungeons.AbstractDungeon.NoPools;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;


@NoPools
public class BlueOptionTwo extends BaseCard {
    public static final String ID = makeID("BlueOptionTwo");
    public static final int MAGIC = 2;
    private static final CardStats info = new CardStats(
            CardColor.COLORLESS,
            CardType.POWER,
            CardRarity.SPECIAL,
            CardTarget.NONE,
            -2 
    );

    public BlueOptionTwo() {
        super(ID, info);
        setMagic(MAGIC);
        setBackgroundTexture(BasicMod.imagePath("character/cardback/bg_evolution1_power.png"), BasicMod.imagePath("character/cardback/bg_evolution1_power_p.png"));
        tags.add(BasicMod.CustomTags.Evolution);
    }

    @Override
    public void use(AbstractPlayer abstractPlayer, AbstractMonster abstractMonster) {
        onChoseThisOption();
    }

    public void onChoseThisOption() {
        BasicMod.startWithBuffer = true;
        BasicMod.usedBlueJACKPOT = true;
    }
}
