package autoplaycharactermod.cards.equipment;

import autoplaycharactermod.actions.SfxActionVolume;
import autoplaycharactermod.cards.EquipmentCard;
import autoplaycharactermod.cards.statusAndCurses.Melt;
import autoplaycharactermod.character.MyCharacter;
import autoplaycharactermod.util.CardStats;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.cards.tempCards.Insight;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class InstructionManual extends EquipmentCard {
    public static final String ID = makeID("InstructionManual");

    private static final CardStats info = new CardStats(
            MyCharacter.Meta.CARD_COLOR,
            CardType.SKILL,
            CardRarity.UNCOMMON,
            CardTarget.NONE,
            0 
    );
    private static final int BASE_HP = 12;
    private static final int BLOCK = 8;
    private static final int BLOCKUPG = 4;
    private static final int MAGIC = 1;
    private static final int MAGICUPG = 0;


    public InstructionManual() {
        super(ID, info, BASE_HP);
        setBlock(BLOCK, BLOCKUPG);
        setMagic(MAGIC, MAGICUPG);
        cardsToPreview = new Insight();
        checkEvolve();
    }

    @Override
    public void evolveCard() {
        setBlock(20);
        setMagic(3);
        super.evolveCard();
    }

    @Override
    public void Activate() {
        if (!Equipped) return;
        AbstractPlayer p = AbstractDungeon.player;
        addToBot(new SfxActionVolume("STAB_BOOK_DEATH", 0.8F, 1.8F));
        this.addToBot(new MakeTempCardInDrawPileAction(new Insight(), magicNumber, true, true, false));
        calculateCardDamage(null);
        addToBot(new GainBlockAction(p, p, block));
        super.Activate();
    }

    @Override
    protected int getUpgradeDurability() {
        return 3;
    }

    public void triggerOnShuffle() {
        Activate();
    }


}
