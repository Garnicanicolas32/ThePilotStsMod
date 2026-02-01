package ThePilotCharacter.cards.optionSelection.gambling;

import ThePilotCharacter.cards.BaseCard;
import ThePilotCharacter.cards.traitScavengeCards.GachaPull;
import ThePilotCharacter.util.CardStats;
import basemod.patches.com.megacrit.cardcrawl.dungeons.AbstractDungeon.NoPools;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.StrengthPower;


@NoPools
public class GamblingStrength extends BaseCard {
    public static final String ID = makeID("GamblingStrength");
    private static final CardStats info = new CardStats(
            CardColor.COLORLESS,
            CardType.POWER,
            CardRarity.SPECIAL,
            CardTarget.NONE,
            -2 
    );
    private static final int MAGIC = 1;
    private static final int MAGIC_UPG = 1;

    public GamblingStrength() {
        super(ID, info);
        setMagic(MAGIC, MAGIC_UPG);
    }

    @Override
    public void use(AbstractPlayer abstractPlayer, AbstractMonster abstractMonster) {
        onChoseThisOption();
    }

    public void onChoseThisOption() {
        addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new StrengthPower(AbstractDungeon.player, magicNumber)));
        
        GachaPull.cardsList.removeIf(c -> c instanceof GamblingStrength);
    }
}
