package autoplaycharactermod.cards.consumable;

import autoplaycharactermod.actions.DamageCurrentTargetAction;
import autoplaycharactermod.cards.ConsumableCards;
import autoplaycharactermod.character.MyCharacter;
import autoplaycharactermod.util.CardStats;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.TheBombPower;
import com.megacrit.cardcrawl.vfx.combat.ExplosionSmallEffect;

public class Grenades extends ConsumableCards {
    public static final String ID = makeID("Grenades");
    private static final CardStats info = new CardStats(
            MyCharacter.Meta.CARD_COLOR,
            CardType.SKILL,
            CardRarity.COMMON,
            CardTarget.NONE,
            -2
    );
    private static final int DAMAGE = 14;
    private static final int DAMAGEUPG = 5;

    public Grenades() {
        super(ID, info, 4, 3);
        setMagic(DAMAGE, DAMAGEUPG);
        checkEvolve();
    }

    @Override
    public void evolveCard() {
        setMagic(25);
        super.evolveCard();

    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new ApplyPowerAction(p, p, new TheBombPower(p, 2, this.magicNumber), 2));
        super.use(p, m);
    }

}
