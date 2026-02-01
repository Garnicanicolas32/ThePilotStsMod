package ThePilotCharacter.cards.optionSelection.traitReward;

import ThePilotCharacter.ThePilotMod;
import ThePilotCharacter.cards.BaseCard;
import ThePilotCharacter.util.CardStats;
import basemod.patches.com.megacrit.cardcrawl.dungeons.AbstractDungeon.NoPools;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;


@NoPools
public class RedOptionOne extends BaseCard {
    public static final String ID = makeID("RedOptionOne");
    private static final CardStats info = new CardStats(
            CardColor.COLORLESS,
            CardType.POWER,
            CardRarity.SPECIAL,
            CardTarget.NONE,
            -2
    );

    public RedOptionOne() {
        super(ID, info);
        setBackgroundTexture(ThePilotMod.imagePath("character/cardback/bg_evolution3_power.png"), ThePilotMod.imagePath("character/cardback/bg_evolution3_power_p.png"));
        tags.add(ThePilotMod.CustomTags.Evolution);
    }

    @Override
    public void use(AbstractPlayer abstractPlayer, AbstractMonster abstractMonster) {
        onChoseThisOption();
    }

    public void onChoseThisOption() {
        ThePilotMod.startWithDemon = true;
        ThePilotMod.usedRedJACKPOT = true;
    }
}
