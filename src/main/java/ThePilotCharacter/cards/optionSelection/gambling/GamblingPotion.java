package ThePilotCharacter.cards.optionSelection.gambling;

import ThePilotCharacter.cards.BaseCard;
import ThePilotCharacter.cards.traitScavengeCards.GachaPull;
import ThePilotCharacter.util.CardStats;
import basemod.patches.com.megacrit.cardcrawl.dungeons.AbstractDungeon.NoPools;
import com.megacrit.cardcrawl.actions.common.ObtainPotionAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;


@NoPools
public class GamblingPotion extends BaseCard {
    public static final String ID = makeID("GamblingPotion");
    private static final CardStats info = new CardStats(
            CardColor.COLORLESS,
            CardType.POWER,
            CardRarity.SPECIAL,
            CardTarget.NONE,
            -2 
    );

    public GamblingPotion() {
        super(ID, info);
        setMagic(1,1);
    }

    @Override
    public void use(AbstractPlayer abstractPlayer, AbstractMonster abstractMonster) {
        onChoseThisOption();
    }

    public void onChoseThisOption() {
        for (int i = 0; i < magicNumber; i++)
            addToBot(new ObtainPotionAction(AbstractDungeon.returnRandomPotion(true)));
        GachaPull.cardsList.removeIf(c -> c instanceof GamblingPotion);
        
    }
}
