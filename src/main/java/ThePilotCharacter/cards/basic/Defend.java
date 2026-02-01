package ThePilotCharacter.cards.basic;

import ThePilotCharacter.cards.BaseCard;
import ThePilotCharacter.character.PilotCharacter;
import ThePilotCharacter.util.CardStats;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class Defend extends BaseCard {
    public static final String ID = makeID("Defend");
    private static final CardStats info = new CardStats(
            PilotCharacter.Meta.CARD_COLOR,
            AbstractCard.CardType.SKILL,
            AbstractCard.CardRarity.BASIC,
            AbstractCard.CardTarget.SELF,
            -2
    );
    private static final int BLOCK = 5;
    private static final int UPG_BLOCK = 3;

    public Defend() {
        super(ID, info);
        setBlock(BLOCK, UPG_BLOCK);
        tags.add(CardTags.STARTER_DEFEND);
        returnToHand = true;
        checkEvolve();
    }

    @Override
    public void evolveCard() {
        setBlock(10);
        super.evolveCard();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new GainBlockAction(p, p, block));
        PlayOnce = false;
    }

    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        return PlayOnce && super.canUse(p, m);
    }
}
