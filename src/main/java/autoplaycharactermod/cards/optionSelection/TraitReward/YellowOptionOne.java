package autoplaycharactermod.cards.optionSelection.TraitReward;

import autoplaycharactermod.BasicMod;
import autoplaycharactermod.cards.BaseCard;
import autoplaycharactermod.util.CardStats;
import basemod.patches.com.megacrit.cardcrawl.dungeons.AbstractDungeon.NoPools;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.GainPennyEffect;


@NoPools
public class YellowOptionOne extends BaseCard {
    public static final String ID = makeID("YellowOptionOne");
    private static final CardStats info = new CardStats(
            CardColor.COLORLESS,
            CardType.POWER,
            CardRarity.SPECIAL,
            CardTarget.NONE,
            -2
    );
    private static final int MAGIC = 200;

    public YellowOptionOne() {
        super(ID, info);
        setMagic(MAGIC);
        setBackgroundTexture(BasicMod.imagePath("character/cardback/bg_evolution4_power.png"), BasicMod.imagePath("character/cardback/bg_evolution4_power_p.png"));
        tags.add(BasicMod.CustomTags.Evolution);
    }

    @Override
    public void use(AbstractPlayer abstractPlayer, AbstractMonster abstractMonster) {
        onChoseThisOption();
    }

    public void onChoseThisOption() {
        AbstractDungeon.player.gainGold(magicNumber);
        for (int i = 0; i < magicNumber; i++)
            AbstractDungeon.effectList.add(new GainPennyEffect(this.current_x, this.current_y));
        BasicMod.usedYellowJACKPOT = true;
    }
}
