package autoplaycharactermod.cards.optionSelection.gambling;

import autoplaycharactermod.ThePilotMod;
import autoplaycharactermod.cards.BaseCard;
import autoplaycharactermod.cards.traitScavengeCards.GachaPull;
import autoplaycharactermod.util.CardStats;
import basemod.patches.com.megacrit.cardcrawl.dungeons.AbstractDungeon.NoPools;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;


@NoPools
public class GamblingCardReward extends BaseCard {
    public static final String ID = makeID("GamblingCardReward");
    private static final CardStats info = new CardStats(
            CardColor.COLORLESS,
            CardType.POWER,
            CardRarity.SPECIAL,
            CardTarget.NONE,
            -2 
    );

    public GamblingCardReward() {
        super(ID, info);
    }

    @Override
    public void use(AbstractPlayer abstractPlayer, AbstractMonster abstractMonster) {
        onChoseThisOption();
    }

    public void onChoseThisOption() {
        if (upgraded){
            ThePilotMod.extracards++;
        }else {
            ThePilotMod.extracardsoption = true;
        }
        GachaPull.cardsList.removeIf(c -> c instanceof GamblingCardReward);
    }
}
