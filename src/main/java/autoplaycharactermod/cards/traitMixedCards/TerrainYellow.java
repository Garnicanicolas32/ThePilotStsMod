package autoplaycharactermod.cards.traitMixedCards;

import autoplaycharactermod.BasicMod;
import autoplaycharactermod.actions.ModifiedCardInHandAction;
import autoplaycharactermod.cards.TraitCard;
import autoplaycharactermod.cards.traitScavengeCards.DuctTape;
import autoplaycharactermod.character.MyCharacter;
import autoplaycharactermod.util.CardStats;
import basemod.patches.com.megacrit.cardcrawl.dungeons.AbstractDungeon.NoPools;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.GainPennyEffect;

@NoPools
public class TerrainYellow extends TraitCard {
    public static final String ID = makeID("TerrainYellow");
    private static final CardStats info = new CardStats(
            MyCharacter.Meta.CARD_COLOR,
            CardType.SKILL,
            CardRarity.SPECIAL,
            CardTarget.NONE,
            0 
    );

    public TerrainYellow() {
        super(ID, info, TraitColor.SCAVENGE, true);
        setSelfRetain(true);
        tags.add(BasicMod.CustomTags.NoEnergyText);
        tags.add(BasicMod.CustomTags.ignoreDuplication);
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
            AbstractDungeon.player.gainGold(30);
            for (int i = 0; i < 30; i++) {
                AbstractDungeon.topLevelEffects.add(new GainPennyEffect(this.hb.cX, this.hb.cY));
            }
        }
    }


    public void onChoseThisOption() {
        addToBot(new ModifiedCardInHandAction(this.makeStatEquivalentCopy()));
    }
}
