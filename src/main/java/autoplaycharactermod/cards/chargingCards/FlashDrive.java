package autoplaycharactermod.cards.chargingCards;

import autoplaycharactermod.BasicMod;
import autoplaycharactermod.actions.EjectedEffectAction;
import autoplaycharactermod.actions.ModifiedCardFromDeckToHandAction;
import autoplaycharactermod.cards.BaseCard;
import autoplaycharactermod.character.MyCharacter;
import autoplaycharactermod.powers.EfficiencyPower;
import autoplaycharactermod.powers.SavePower;
import autoplaycharactermod.util.CardStats;
import basemod.helpers.TooltipInfo;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDiscardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.status.Dazed;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.ArrayList;
import java.util.List;

public class FlashDrive extends BaseCard {
    public static final String ID = makeID("FlashDrive");
    private static final CardStats info = new CardStats(
            MyCharacter.Meta.CARD_COLOR,
            CardType.SKILL,
            CardRarity.UNCOMMON,
            CardTarget.SELF,
            -2 
    );
    private static final int MAGIC = 6;
    private static final int UPG_MAGIC = 4;

    public FlashDrive() {
        super(ID, info);
        returnToHand = true;
        setMagic(MAGIC, UPG_MAGIC);
        checkEvolve();
    }

    @Override
    public void evolveCard() {
        setMagic(20);
        super.evolveCard();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(p, p, new SavePower(p, magicNumber)));
        PlayOnce = false;
    }

    @Override
    public List<TooltipInfo> getCustomTooltips() {
        ArrayList<TooltipInfo> customTooltips = new ArrayList<>();
        customTooltips.add(new TooltipInfo(BasicMod.keywords.get("Charge").PROPER_NAME, BasicMod.keywords.get("Charge").DESCRIPTION));
        return customTooltips;
    }

    public void triggerOnManualDiscard() {
        eject();
    }

    public void eject() {
        if (AbstractDungeon.player.hasPower(EfficiencyPower.POWER_ID))
            ((EfficiencyPower)AbstractDungeon.player.getPower(EfficiencyPower.POWER_ID)).triggerEject();
        addToBot(new EjectedEffectAction());
        if (this.alreadyEvolved) {
            addToBot(new ModifiedCardFromDeckToHandAction(1, true));
        } else {
            addToBot(new ModifiedCardFromDeckToHandAction(1, true, AbstractCard.CardType.SKILL));
            addToBot(new MakeTempCardInDiscardAction(new Dazed(), 1));
        }
    }

    public void onScrySelected() {
        eject();
    }

    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        return PlayOnce && super.canUse(p, m);
    }
}
