package autoplaycharactermod.cards.optionSelection.Gambling;

import autoplaycharactermod.cards.BaseCard;
import autoplaycharactermod.cards.traitScavengeCards.GachaPull;
import autoplaycharactermod.util.CardStats;
import autoplaycharactermod.util.GeneralUtils;
import basemod.patches.com.megacrit.cardcrawl.dungeons.AbstractDungeon.NoPools;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;


@NoPools
public class GamblingCreateScavenge extends BaseCard {
    public static final String ID = makeID("GamblingCreateScavenge");
    private static final CardStats info = new CardStats(
            CardColor.COLORLESS,
            CardType.POWER,
            CardRarity.SPECIAL,
            CardTarget.NONE,
            -2
    );
    private static final int MAGIC = 1;
    private static final int MAGIC_UPG = 1;

    public GamblingCreateScavenge() {
        super(ID, info);
        setMagic(MAGIC, MAGIC_UPG);
    }

    @Override
    public void use(AbstractPlayer abstractPlayer, AbstractMonster abstractMonster) {
        onChoseThisOption();
    }

    public void onChoseThisOption() {
        for (int i = 0; i < magicNumber; i++)
            addToBot(new MakeTempCardInDrawPileAction(GeneralUtils.getRandomScavengeCard(), 1, true, true));
        GachaPull.cardsList.removeIf(c -> c instanceof GamblingCreateScavenge);
        
    }
}
