package autoplaycharactermod.cards.statusAndCurses;

import autoplaycharactermod.cards.BaseCard;
import autoplaycharactermod.util.CardStats;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class Melt extends BaseCard {
    public static final String ID = makeID("Melt");
    private static final CardStats info = new CardStats(
            CardColor.COLORLESS,
            CardType.STATUS,
            CardRarity.COMMON,
            CardTarget.SELF,
            1 
    );

    public Melt() {
        super(ID, info);
        this.selfRetain = true;
        this.exhaust = true;
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
    }

    public void upgrade() {
    }

    public AbstractCard makeCopy() {
        return new Melt();
    }
}
