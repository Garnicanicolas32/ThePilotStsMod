package autoplaycharactermod.cards.basic;

import autoplaycharactermod.cards.BaseCard;
import autoplaycharactermod.character.PilotCharacter;
import autoplaycharactermod.powers.TargetedPower;
import autoplaycharactermod.util.CardStats;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class CoverFire extends BaseCard {
    public static final String ID = makeID("CoverFire");
    private static final CardStats info = new CardStats(
            PilotCharacter.Meta.CARD_COLOR,
            CardType.ATTACK,
            CardRarity.BASIC,
            CardTarget.ALL_ENEMY,
            -2
    );
    private static final int DAMAGE = 6;
    private static final int DAMAGE_UPG = 2;

    public CoverFire() {
        super(ID, info);
        setDamage(DAMAGE, DAMAGE_UPG);
        returnToHand = true;
        checkEvolve();
    }

    @Override
    public void evolveCard() {
        setDamage(10);
        super.evolveCard();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int multiplier = this.alreadyEvolved ? 3 : 2;
        for (AbstractMonster mon : AbstractDungeon.getMonsters().monsters) {
            if (!mon.isDeadOrEscaped()) {
                calculateCardDamage(mon);
                addToBot(new DamageAction(mon, new DamageInfo(p, mon.hasPower(TargetedPower.POWER_ID) ? damage * multiplier : damage, DamageInfo.DamageType.NORMAL), AbstractGameAction.AttackEffect.BLUNT_LIGHT));
            }
        }
        PlayOnce = false;

    }

    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        return PlayOnce && super.canUse(p, m);
    }
}
