package ThePilotCharacter.cards.traitIgnitionCards;

import ThePilotCharacter.actions.ModifiedCardInHandAction;
import ThePilotCharacter.cards.TraitCard;
import ThePilotCharacter.cards.statusAndCurses.Melt;
import ThePilotCharacter.character.PilotCharacter;
import ThePilotCharacter.util.CardStats;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.InflameEffect;

public class ScorchedCore extends TraitCard {
    public static final String ID = makeID("ScorchedCore");
    private static final CardStats info = new CardStats(
            PilotCharacter.Meta.CARD_COLOR,
            CardType.SKILL,
            CardRarity.UNCOMMON,
            CardTarget.SELF,
            -2 
    );
    private static final int MAGIC = 1;
    private static final int UPG_MAGIC = 1;
    private static final int BLOCK = 5;
    private static final int UPG_BLOCK = 1;

    public ScorchedCore() {
        super(ID, info, TraitColor.IGNITE, false);
        setMagic(MAGIC, UPG_MAGIC);
        setBlock(BLOCK, UPG_BLOCK);
        checkEvolve();
        if (!this.alreadyEvolved)
            cardsToPreview = new Melt();
    }

    @Override
    public void evolveCard() {
        setMagic(3);
        setBlock(12);
        cardsToPreview = null;
        super.evolveCard();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new VFXAction(p, new InflameEffect(p), 1.0F));
        if (!this.alreadyEvolved) {
            addToBot(new ModifiedCardInHandAction(new Melt(), 1));
        }
        addToBot(new GainEnergyAction(magicNumber));
        addToBot(new GainBlockAction(p, p, block));

        PlayOnce = false;
        super.use(p, m);
    }
}
