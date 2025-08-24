package autoplaycharactermod.cards.traitBastionCards;

import autoplaycharactermod.cards.TraitCard;
import autoplaycharactermod.character.MyCharacter;
import autoplaycharactermod.util.CardStats;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.PlatedArmorPower;

public class TakeCover extends TraitCard {
    public static final String ID = makeID("TakeCover");
    private static final CardStats info = new CardStats(
            MyCharacter.Meta.CARD_COLOR,
            CardType.SKILL,
            CardRarity.UNCOMMON,
            CardTarget.SELF,
            -2 
    );
    private static final int BLOCK = 3;
    private static final int UPG_BLOCK = 2;
    private static final int MAGIC = 2;
    private static final int UPG_MAGIC = 1;

    public TakeCover() {
        super(ID, info, TraitCard.TraitColor.BASTION, false);
        setBlock(BLOCK, UPG_BLOCK);
        setMagic(MAGIC, UPG_MAGIC);
        checkEvolve();
    }

    @Override
    public void evolveCard() {
        setBlock(10);
        setMagic(6);
        super.evolveCard();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new GainBlockAction(p, p, block));
        addToBot(new ApplyPowerAction(p, p, new PlatedArmorPower(p, magicNumber)));
        PlayOnce = false;
        super.use(p, m);
    }
}