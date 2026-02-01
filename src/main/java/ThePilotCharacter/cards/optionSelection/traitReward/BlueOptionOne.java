package ThePilotCharacter.cards.optionSelection.traitReward;

import ThePilotCharacter.ThePilotMod;
import ThePilotCharacter.cards.BaseCard;
import ThePilotCharacter.util.CardStats;
import basemod.patches.com.megacrit.cardcrawl.dungeons.AbstractDungeon.NoPools;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

@NoPools
public class BlueOptionOne extends BaseCard {
    public static final String ID = makeID("BlueOptionOne");
    public static final int MAGIC = 2;
    private static final CardStats info = new CardStats(
            CardColor.COLORLESS,
            CardType.POWER,
            CardRarity.SPECIAL,
            CardTarget.NONE,
            -2 
    );

    public BlueOptionOne() {
        super(ID, info);
        setMagic(MAGIC);
        setBackgroundTexture(ThePilotMod.imagePath("character/cardback/bg_evolution1_power.png"), ThePilotMod.imagePath("character/cardback/bg_evolution1_power_p.png"));
        tags.add(ThePilotMod.CustomTags.Evolution);
    }

    @Override
    public void use(AbstractPlayer abstractPlayer, AbstractMonster abstractMonster) {
        onChoseThisOption();
    }

    public void onChoseThisOption() {
        ThePilotMod.startWithBarricade = true;
        ThePilotMod.usedBlueJACKPOT = true;
    }
}
