package autoplaycharactermod.cards.consumable;

import autoplaycharactermod.cards.ConsumableCards;
import autoplaycharactermod.character.MyCharacter;
import autoplaycharactermod.powers.BubblePower;
import autoplaycharactermod.util.CardStats;
import autoplaycharactermod.vfx.FieldBubbleEffect;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class FieldBubble extends ConsumableCards {
    public static final String ID = makeID("FieldBubble");
    private static final CardStats info = new CardStats(
            MyCharacter.Meta.CARD_COLOR,
            CardType.SKILL,
            CardRarity.COMMON,
            CardTarget.SELF,
            -2 
    );
    private static final int MAGIC = 5;
    private static final int UPG_MAGIC = 3;

    public FieldBubble() {
        super(ID, info, 4, 3);
        setMagic(MAGIC, UPG_MAGIC);
        checkEvolve();
    }

    @Override
    public void evolveCard() {
        setMagic(12);
        super.evolveCard();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.effectsQueue.add(new FieldBubbleEffect());
        addToBot(new ApplyPowerAction(p, p, new BubblePower(p,magicNumber)));
        super.use(p, m);
    }
}
