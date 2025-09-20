package autoplaycharactermod.cards.basic;

import autoplaycharactermod.cards.BaseCard;
import autoplaycharactermod.character.PilotCharacter;
import autoplaycharactermod.util.CardStats;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class DefenseAlgo extends BaseCard {
    public static final String ID = makeID("DefenseAlgo");
    private static final CardStats info = new CardStats(
            PilotCharacter.Meta.CARD_COLOR,
            CardType.SKILL,
            CardRarity.BASIC,
            CardTarget.SELF,
            -2 
    );
    private static final int BLOCK = 5;
    private static final int UPG_BLOCK = 2;

    public DefenseAlgo() {
        super(ID, info);
        setBlock(BLOCK, UPG_BLOCK);
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
        for (AbstractMonster mon : AbstractDungeon.getMonsters().monsters) {
            if (!mon.isDeadOrEscaped()) {
                addToBot(new GainBlockAction(p, p, block));
                if (this.alreadyEvolved) {
                    addToBot(new GainEnergyAction(1));
                }
            }
        }
        PlayOnce = false;
    }

    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        return PlayOnce && super.canUse(p, m);
    }
}
