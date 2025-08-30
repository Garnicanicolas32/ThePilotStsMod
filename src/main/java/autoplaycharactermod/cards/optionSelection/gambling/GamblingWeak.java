package autoplaycharactermod.cards.optionSelection.gambling;

import autoplaycharactermod.cards.BaseCard;
import autoplaycharactermod.cards.traitScavengeCards.GachaPull;
import autoplaycharactermod.util.CardStats;
import basemod.patches.com.megacrit.cardcrawl.dungeons.AbstractDungeon.NoPools;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.WeakPower;


@NoPools
public class GamblingWeak extends BaseCard {
    public static final String ID = makeID("GamblingWeak");
    private static final CardStats info = new CardStats(
            CardColor.COLORLESS,
            CardType.POWER,
            CardRarity.SPECIAL,
            CardTarget.NONE,
            -2 
    );
    private static final int MAGIC = 2;

    public GamblingWeak() {
        super(ID, info);
        setMagic(MAGIC);
    }

    @Override
    public void use(AbstractPlayer abstractPlayer, AbstractMonster abstractMonster) {
        onChoseThisOption();
    }

    public void onChoseThisOption() {
        addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new WeakPower(AbstractDungeon.player, magicNumber, false)));
        GachaPull.cardsList.removeIf(c -> c instanceof GamblingWeak);
        
    }
}
