package autoplaycharactermod.cards.traitMixedCards;

import autoplaycharactermod.BasicMod;
import autoplaycharactermod.actions.ModifiedCardInHandAction;
import autoplaycharactermod.cards.TraitCard;
import autoplaycharactermod.cards.traitScavengeCards.DuctTape;
import autoplaycharactermod.character.MyCharacter;
import autoplaycharactermod.powers.RedPower;
import autoplaycharactermod.util.CardStats;
import basemod.patches.com.megacrit.cardcrawl.dungeons.AbstractDungeon.NoPools;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

@NoPools
public class TerrainRed extends TraitCard {
    public static final String ID = makeID("TerrainRed");
    private static final CardStats info = new CardStats(
            MyCharacter.Meta.CARD_COLOR,
            CardType.SKILL,
            CardRarity.SPECIAL,
            CardTarget.NONE,
            0
    );

    public TerrainRed() {
        super(ID, info, TraitCard.TraitColor.IGNITE, true);
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
    public void upgrade(){
        super.upgrade();
        if (BasicMod.isInCombat() && AbstractDungeon.player.hand.contains(this)){
            addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new RedPower(AbstractDungeon.player, 1)));
        }
    }

    @Override
    public void onRetained() {
        super.onRetained();
        if (this.alreadyEvolved) {
            addToBot(new DamageAction(MyCharacter.getTarget(), new DamageInfo(AbstractDungeon.player, 15, DamageInfo.DamageType.THORNS), AbstractGameAction.AttackEffect.FIRE));
        }
    }

    public void onChoseThisOption() {
        addToBot(new ModifiedCardInHandAction(this.makeStatEquivalentCopy()));
    }
}
