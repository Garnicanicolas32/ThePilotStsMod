package autoplaycharactermod.cards.traitMixedCards;

import autoplaycharactermod.cards.BaseCard;
import autoplaycharactermod.character.MyCharacter;
import autoplaycharactermod.util.CardStats;
import basemod.patches.com.megacrit.cardcrawl.cards.AbstractCard.MultiCardPreview;
import com.megacrit.cardcrawl.actions.watcher.ChooseOneAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.ArrayList;

public class FavorableTerrain extends BaseCard {
    public static final String ID = makeID("FavorableTerrain");
    private static final CardStats info = new CardStats(
            MyCharacter.Meta.CARD_COLOR,
            CardType.SKILL,
            CardRarity.RARE,
            CardTarget.NONE,
            -2 
    );

    public FavorableTerrain() {
        super(ID, info);
        MultiCardPreview.add(this, new TerrainRed(), new TerrainBlue(), new TerrainYellow());
        setExhaust(true);
        checkEvolve();
    }

    @Override
    public void evolveCard() {
        for (AbstractCard c : MultiCardPreview.multiCardPreview.get(this)) {
            if (c instanceof BaseCard)
                ((BaseCard)c).evolveCard();
        }
        super.evolveCard();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        ArrayList<AbstractCard> stanceChoices = new ArrayList<>();
        stanceChoices.add(new TerrainRed());
        stanceChoices.add(new TerrainBlue());
        stanceChoices.add(new TerrainYellow());
        if (this.upgraded && !alreadyEvolved) {
            for (AbstractCard c : stanceChoices)
                c.upgrade();
        }else if(this.alreadyEvolved){
            for (AbstractCard c : stanceChoices)
                ((BaseCard)c).evolveCard();
        }
        addToBot(new ChooseOneAction(stanceChoices));
    }

    public void upgrade() {
        super.upgrade();
        for (AbstractCard c : MultiCardPreview.multiCardPreview.get(this)) {
            c.upgrade();
        }
    }

    @Override
    public boolean freeToPlay() {
        return true;
    }
}
