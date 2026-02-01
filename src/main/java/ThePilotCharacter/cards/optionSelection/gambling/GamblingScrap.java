package ThePilotCharacter.cards.optionSelection.gambling;

import ThePilotCharacter.cards.BaseCard;
import ThePilotCharacter.cards.scrap.ScrapCommon;
import ThePilotCharacter.cards.scrap.ScrapCommonDef;
import ThePilotCharacter.cards.traitScavengeCards.GachaPull;
import ThePilotCharacter.util.CardStats;
import basemod.patches.com.megacrit.cardcrawl.cards.AbstractCard.MultiCardPreview;
import basemod.patches.com.megacrit.cardcrawl.dungeons.AbstractDungeon.NoPools;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDiscardAction;
import com.megacrit.cardcrawl.actions.unique.AddCardToDeckAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;


@NoPools
public class GamblingScrap extends BaseCard {
    public static final String ID = makeID("GamblingScrap");
    private static final CardStats info = new CardStats(
            CardColor.COLORLESS,
            CardType.POWER,
            CardRarity.SPECIAL,
            CardTarget.NONE,
            -2 
    );

    public GamblingScrap() {
        super(ID, info);
        MultiCardPreview.add(this, new ScrapCommon(), new ScrapCommonDef());
    }

    @Override
    public void use(AbstractPlayer abstractPlayer, AbstractMonster abstractMonster) {
        onChoseThisOption();
    }

    public void onChoseThisOption() {
        for (int i = 0; i < (upgraded ? 2 : 1); i++) {
            AbstractCard ca = AbstractDungeon.cardRandomRng.randomBoolean() ? new ScrapCommon() : new ScrapCommonDef();
            addToBot(new AddCardToDeckAction(ca));
            addToBot(new MakeTempCardInDiscardAction(ca, true));
        }
        GachaPull.cardsList.removeIf(c -> c instanceof GamblingScrap);
    }
}
