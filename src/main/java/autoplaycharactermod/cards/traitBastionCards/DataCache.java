package autoplaycharactermod.cards.traitBastionCards;

import autoplaycharactermod.BasicMod;
import autoplaycharactermod.cards.TraitCard;
import autoplaycharactermod.character.MyCharacter;
import autoplaycharactermod.ui.ConfigPanel;
import autoplaycharactermod.util.CardStats;
import autoplaycharactermod.vfx.DatacacheEffect;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class DataCache extends TraitCard {
    public static final String ID = makeID("DataCache");
    private static final CardStats info = new CardStats(
            MyCharacter.Meta.CARD_COLOR,
            CardType.SKILL,
            CardRarity.UNCOMMON,
            CardTarget.SELF,
            -2
    );
    private static final int BLOCK = 1;
    private static final int UPG_BLOCK = 0;
    public int bufferCounter = 0;

    public DataCache() {
        super(ID, info, TraitCard.TraitColor.BASTION, false);
        setBlock(BLOCK, UPG_BLOCK);
        checkEvolve();
    }

    @Override
    public void evolveCard() {
        setBlock(3);
        super.evolveCard();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int amount = BasicMod.energySpentCombat - bufferCounter;
        if (this.alreadyEvolved)
            amount = BasicMod.energySpentCombat;

        int blockFix = Math.max(block, 1);
        for (int i = 0; i < (ConfigPanel.lessParticles ? 5 : amount); i++) {
            AbstractDungeon.effectList.add(new DatacacheEffect(i));
        }
        addToBot(new GainBlockAction(p, p, upgraded || this.alreadyEvolved ? blockFix * amount : blockFix * amount / 2));
        bufferCounter = BasicMod.energySpentCombat;
        PlayOnce = false;
        super.use(p, m);
    }

    public void updateTextCount() {
        if (!this.alreadyEvolved) {
            if (upgraded)
                this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;
            else
                this.rawDescription = cardStrings.DESCRIPTION;

            if (BasicMod.energySpentCombat > 0)
                this.rawDescription += cardStrings.EXTENDED_DESCRIPTION[1] + (BasicMod.energySpentCombat - bufferCounter) + cardStrings.EXTENDED_DESCRIPTION[2];
            initializeDescription();
        }
    }
}