package autoplaycharactermod.cards.traitIgnitionCards;

import autoplaycharactermod.actions.ModifiedCardInHandAction;
import autoplaycharactermod.cards.TraitCard;
import autoplaycharactermod.cards.statusAndCurses.Melt;
import autoplaycharactermod.character.PilotCharacter;
import autoplaycharactermod.util.CardStats;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.vfx.combat.GiantFireEffect;
import com.megacrit.cardcrawl.vfx.combat.RedFireballEffect;

public class IgnitionProtocol extends TraitCard {
    public static final String ID = makeID("IgnitionProtocol");
    private static final CardStats info = new CardStats(
            PilotCharacter.Meta.CARD_COLOR,
            CardType.ATTACK,
            CardRarity.RARE,
            CardTarget.ALL_ENEMY,
            -2
    );
    private static final int DAMAGE = 12;
    private static final int UPG_DAMAGE = 4;

    public IgnitionProtocol() {
        super(ID, info, TraitColor.IGNITE, false);
        setDamage(DAMAGE, UPG_DAMAGE);
        setMagic(2, 0);
        this.isMultiDamage = true;
        checkEvolve();
        if (!this.alreadyEvolved)
            cardsToPreview = new Melt();
    }

    @Override
    public void evolveCard() {
        cardsToPreview = null;
        setDamage(25);
        setMagic(99);
        super.evolveCard();
    }


    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {

        addToBot(new DamageAllEnemiesAction(p, this.multiDamage, DamageInfo.DamageType.NORMAL, AbstractGameAction.AttackEffect.FIRE));
        for (int i = 0; i < 10; i++) {
            AbstractDungeon.effectsQueue.add(new GiantFireEffect());
        }
        for (AbstractMonster mo : AbstractDungeon.getMonsters().monsters) {
            if (!mo.isDeadOrEscaped()) {
                AbstractDungeon.effectsQueue.add(new RedFireballEffect(mo.hb.cX - 130 * Settings.scale, mo.hb.cY, mo.hb.cX + 130 * Settings.scale, mo.hb.cY - 50.0F * Settings.scale, 2));
                addToBot(new ApplyPowerAction(mo, p, new VulnerablePower(mo, magicNumber, false), magicNumber, true));
            }
        }
        if (!this.alreadyEvolved)
            addToBot(new ModifiedCardInHandAction(new Melt(), 1));
        PlayOnce = false;
        super.use(p, m);
    }
}
