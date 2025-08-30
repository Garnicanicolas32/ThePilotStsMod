package autoplaycharactermod.cards.optionSelection.gambling;

import autoplaycharactermod.cards.BaseCard;
import autoplaycharactermod.cards.traitScavengeCards.GachaPull;
import autoplaycharactermod.util.CardStats;
import basemod.patches.com.megacrit.cardcrawl.dungeons.AbstractDungeon.NoPools;
import com.megacrit.cardcrawl.actions.utility.ScryAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;


@NoPools
public class GamblingScry extends BaseCard {
    public static final String ID = makeID("GamblingScry");
    private static final CardStats info = new CardStats(
            CardColor.COLORLESS,
            CardType.POWER,
            CardRarity.SPECIAL,
            CardTarget.NONE,
            -2
    );
    private static final int MAGIC = 3;
    private static final int MAGIC_UPG = 5;

    public GamblingScry() {
        super(ID, info);
        setMagic(MAGIC,MAGIC_UPG);
    }

    @Override
    public void use(AbstractPlayer abstractPlayer, AbstractMonster abstractMonster) {
        onChoseThisOption();
    }

    public void onChoseThisOption() {
        addToBot(new ScryAction(this.magicNumber));
        
        GachaPull.cardsList.removeIf(c -> c instanceof GamblingScry);
    }
}
