package ThePilotCharacter.cards.traitMixedCards;

import ThePilotCharacter.ThePilotMod;
import ThePilotCharacter.actions.ModifiedCardInHandAction;
import ThePilotCharacter.cards.TraitCard;
import ThePilotCharacter.cards.traitScavengeCards.DuctTape;
import ThePilotCharacter.character.PilotCharacter;
import ThePilotCharacter.powers.BluePower;
import ThePilotCharacter.util.CardStats;
import basemod.patches.com.megacrit.cardcrawl.dungeons.AbstractDungeon.NoPools;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

@NoPools
public class TerrainBlue extends TraitCard {
    public static final String ID = makeID("TerrainBlue");
    private static final CardStats info = new CardStats(
            PilotCharacter.Meta.CARD_COLOR,
            CardType.SKILL,
            CardRarity.SPECIAL,
            CardTarget.NONE,
            0 
    );

    public TerrainBlue() {
        super(ID, info, TraitColor.BASTION, true);
        setSelfRetain(true);
        tags.add(ThePilotMod.CustomTags.NoEnergyText);
        tags.add(ThePilotMod.CustomTags.ignoreDuplication);
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

    @Override
    public void upgrade(){
        super.upgrade();
        if (ThePilotMod.isInCombat() && AbstractDungeon.player.hand.contains(this)){
            addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new BluePower(AbstractDungeon.player, 1)));
        }
    }

    public void onChoseThisOption() {
        addToBot(new ModifiedCardInHandAction(this.makeStatEquivalentCopy()));
    }
}
