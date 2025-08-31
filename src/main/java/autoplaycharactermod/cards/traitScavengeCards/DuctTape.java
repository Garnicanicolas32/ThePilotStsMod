package autoplaycharactermod.cards.traitScavengeCards;

import autoplaycharactermod.BasicMod;
import autoplaycharactermod.actions.DuctTapeAction;
import autoplaycharactermod.cards.TraitCard;
import autoplaycharactermod.character.MyCharacter;
import autoplaycharactermod.patches.OnUseCardPowersAndRelicsPatch;
import autoplaycharactermod.util.CardStats;
import com.evacipated.cardcrawl.mod.stslib.actions.tempHp.AddTemporaryHPAction;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class DuctTape extends TraitCard {
    public static final String ID = makeID("DuctTape");
    private static final CardStats info = new CardStats(
            MyCharacter.Meta.CARD_COLOR,
            CardType.ATTACK,
            CardRarity.UNCOMMON,
            CardTarget.ENEMY,
            0
    );
    private static final int DAMAGE = 3;
    private static final int UPG_DAMAGE = 2;

    public DuctTape() {
        super(ID, info, TraitCard.TraitColor.SCAVENGE, false);
        setDamage(DAMAGE, UPG_DAMAGE);
        tags.add(BasicMod.CustomTags.NoEnergyText);
        tags.add(BasicMod.CustomTags.skipVigor);
        checkEvolve();
    }

    @Override
    public void evolveCard() {
        setDamage(10);
        super.evolveCard();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (PlayOnce && !Duplicated) {
            PlayOnce = false;
            returnToHand = true;
            addPower();
        } else {
            removePower();
            addToBot(new DamageAction(m, new DamageInfo(p, damage, DamageInfo.DamageType.NORMAL), AbstractGameAction.AttackEffect.BLUNT_LIGHT));
            if (alreadyEvolved) {
                addToBot(new AddTemporaryHPAction(p, p, 2));
            }
            OnUseCardPowersAndRelicsPatch.checkPenNibVigor();
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

    public void triggerReturnToHand() {
        addToBot(new DuctTapeAction(this));
    }
}
