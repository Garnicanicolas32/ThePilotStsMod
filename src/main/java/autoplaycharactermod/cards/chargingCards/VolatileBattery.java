package autoplaycharactermod.cards.chargingCards;

import autoplaycharactermod.BasicMod;
import autoplaycharactermod.actions.EjectedEffectAction;
import autoplaycharactermod.cards.BaseCard;
import autoplaycharactermod.character.MyCharacter;
import autoplaycharactermod.powers.ChargePower;
import autoplaycharactermod.powers.EfficiencyPower;
import autoplaycharactermod.ui.ConfigPanel;
import autoplaycharactermod.util.CardStats;
import autoplaycharactermod.vfx.EjectLightingEffect;
import autoplaycharactermod.vfx.ShowVolatileCopies;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDiscardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.Objects;

public class VolatileBattery extends BaseCard {
    public static final String ID = makeID("VolatileBattery");
    private static final CardStats info = new CardStats(
            MyCharacter.Meta.CARD_COLOR,
            CardType.SKILL,
            CardRarity.COMMON,
            CardTarget.SELF,
            -2 
    );
    private static final int MAGIC = 3;
    private static final int UPG_MAGIC = 1;

    public VolatileBattery() {
        super(ID, info);
        returnToHand = true;
        setMagic(MAGIC, UPG_MAGIC);
        checkEvolve();
    }

    @Override
    public void evolveCard() {
        setMagic(8);
        super.evolveCard();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int amt = 0;
        for (AbstractCard c : AbstractDungeon.player.hand.group) {
            if (Objects.equals(c.cardID, this.cardID)) amt++;
        }
        for (AbstractCard c : AbstractDungeon.player.drawPile.group) {
            if (Objects.equals(c.cardID, this.cardID)) amt++;
        }
        for (AbstractCard c : AbstractDungeon.player.discardPile.group) {
            if (Objects.equals(c.cardID, this.cardID)) amt++;
        }
        addToBot(new ApplyPowerAction(p, p, new ChargePower(p, amt * magicNumber)));
        for (int i = 0; i < (ConfigPanel.lessParticles ? 4 : amt) - 1; i++) {
            AbstractDungeon.effectList.add(new ShowVolatileCopies(this.makeStatEquivalentCopy()));
        }
        PlayOnce = false;
    }

    public void triggerOnManualDiscard() {
        if (AbstractDungeon.player.hasPower(EfficiencyPower.POWER_ID))
            ((EfficiencyPower)AbstractDungeon.player.getPower(EfficiencyPower.POWER_ID)).triggerEject();
        addToBot(new EjectedEffectAction());
        addToBot(new MakeTempCardInDiscardAction(this.makeStatEquivalentCopy(), this.alreadyEvolved ? 2 : 1));
    }

    public void onScrySelected() {
        if (AbstractDungeon.player.hasPower(EfficiencyPower.POWER_ID))
            ((EfficiencyPower)AbstractDungeon.player.getPower(EfficiencyPower.POWER_ID)).triggerEject();
        addToBot(new EjectedEffectAction());
        addToBot(new MakeTempCardInDiscardAction(this.makeStatEquivalentCopy(), this.alreadyEvolved ? 2 : 1));
    }

    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        return PlayOnce && super.canUse(p, m);
    }
}
