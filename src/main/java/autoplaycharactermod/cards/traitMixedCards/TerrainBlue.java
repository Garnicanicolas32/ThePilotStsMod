package autoplaycharactermod.cards.traitMixedCards;

import autoplaycharactermod.BasicMod;
import autoplaycharactermod.actions.ModifiedCardInHandAction;
import autoplaycharactermod.actions.ScryWithChargeAction;
import autoplaycharactermod.cards.TraitCard;
import autoplaycharactermod.cards.traitScavengeCards.DuctTape;
import autoplaycharactermod.character.MyCharacter;
import autoplaycharactermod.util.CardStats;
import basemod.patches.com.megacrit.cardcrawl.dungeons.AbstractDungeon.NoPools;
import com.megacrit.cardcrawl.actions.common.EmptyDeckShuffleAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

@NoPools
public class TerrainBlue extends TraitCard {
    public static final String ID = makeID("TerrainBlue");
    private static final CardStats info = new CardStats(
            MyCharacter.Meta.CARD_COLOR,
            CardType.SKILL,
            CardRarity.SPECIAL,
            CardTarget.NONE,
            0 
    );

    public TerrainBlue() {
        super(ID, info, TraitColor.BASTION, true);
        setSelfRetain(true);
        tags.add(BasicMod.CustomTags.NoEnergyText);
        checkEvolve();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        for (AbstractCard c : AbstractDungeon.player.discardPile.group) {
            if (c instanceof DuctTape && c.uuid != this.uuid) {
                ((DuctTape) c).triggerReturnToHand();
            }
        }
        if (PlayOnce) {
            addPower();
            PlayOnce = false;
            returnToHand = true;
        } else {
            setExhaust(true);
            returnToHand = false;
        }

    }

    @Override
    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        return this.cardPlayable(m) && this.hasEnoughEnergy();
    }

    @Override
    public boolean freeToPlay() {
        return true;
    }

    @Override
    public void onRetained() {
        super.onRetained();
        if (this.alreadyEvolved) {
            addToBot(new GainBlockAction(AbstractDungeon.player, 15));
        }
    }

    public void onChoseThisOption() {
        addToBot(new ModifiedCardInHandAction(this.makeStatEquivalentCopy()));
    }
}
