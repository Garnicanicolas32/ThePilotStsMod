package autoplaycharactermod.cards.optionSelection.traitReward;

import autoplaycharactermod.BasicMod;
import autoplaycharactermod.cards.BaseCard;
import autoplaycharactermod.util.CardStats;
import basemod.patches.com.megacrit.cardcrawl.dungeons.AbstractDungeon.NoPools;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;


@NoPools
public class BlueOptionThree extends BaseCard {
    public static final String ID = makeID("BlueOptionThree");
    public static final int MAGIC = 3;
    private static final CardStats info = new CardStats(
            CardColor.COLORLESS,
            CardType.POWER,
            CardRarity.SPECIAL,
            CardTarget.NONE,
            -2 
    );

    public BlueOptionThree() {
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
        BasicMod.startWithArtifact = true;
        BasicMod.usedBlueJACKPOT = true;
    }
}
