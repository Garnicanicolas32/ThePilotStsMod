package autoplaycharactermod.cards.optionSelection.gambling;

import autoplaycharactermod.cards.BaseCard;
import autoplaycharactermod.cards.traitScavengeCards.GachaPull;
import autoplaycharactermod.character.MyCharacter;
import autoplaycharactermod.util.CardStats;
import basemod.patches.com.megacrit.cardcrawl.dungeons.AbstractDungeon.NoPools;
import com.evacipated.cardcrawl.mod.stslib.actions.common.StunMonsterAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

@NoPools
public class GamblingStun extends BaseCard {
    public static final String ID = makeID("GamblingStun");
    private static final CardStats info = new CardStats(
            CardColor.COLORLESS,
            CardType.POWER,
            CardRarity.SPECIAL,
            CardTarget.NONE,
            -2 
    );

    public GamblingStun() {
        super(ID, info);
    }

    @Override
    public void use(AbstractPlayer abstractPlayer, AbstractMonster abstractMonster) {
        onChoseThisOption();
    }

    public void onChoseThisOption() {
        addToBot(new StunMonsterAction(MyCharacter.getTarget(), AbstractDungeon.player));
        GachaPull.cardsList.removeIf(c -> c instanceof GamblingStun);
    }
}
