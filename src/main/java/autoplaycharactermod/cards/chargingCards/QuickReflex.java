package autoplaycharactermod.cards.chargingCards;

import autoplaycharactermod.BasicMod;
import autoplaycharactermod.actions.EjectedEffectAction;
import autoplaycharactermod.cards.BaseCard;
import autoplaycharactermod.character.MyCharacter;
import autoplaycharactermod.powers.SavePower;
import autoplaycharactermod.util.CardStats;
import autoplaycharactermod.vfx.EjectLightingEffect;
import basemod.helpers.TooltipInfo;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDiscardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.status.Dazed;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.ArrayList;
import java.util.List;

public class QuickReflex extends BaseCard {
    public static final String ID = makeID("QuickReflex");
    private static final CardStats info = new CardStats(
            MyCharacter.Meta.CARD_COLOR,
            CardType.SKILL,
            CardRarity.UNCOMMON,
            CardTarget.SELF,
            -2 
    );
    private static final int MAGIC = 4;
    private static final int UPG_MAGIC = 2;
    private static final int CAPACITY = 4;
    private static final int UPG_CAPACITY = 3;
    private static final int BLOCK = 4;
    private static final int UPG_BLOCK = 3;

    public QuickReflex() {
        super(ID, info);
        returnToHand = true;
        setMagic(MAGIC, UPG_MAGIC);
        setBlock(BLOCK, UPG_BLOCK);
        setCustomVar("CAPACITY", CAPACITY, UPG_CAPACITY);
        checkEvolve();
    }

    @Override
    public void evolveCard() {
        setMagic(2);
        setBlock(12);
        super.evolveCard();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new GainBlockAction(p, p, block));
        addToBot(new ApplyPowerAction(p, p, new SavePower(p, alreadyEvolved ? 12 : customVar("CAPACITY"))));
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
        if (this.alreadyEvolved) {
            addToBot(new EjectedEffectAction());
            addToBot(new GainBlockAction(AbstractDungeon.player, AbstractDungeon.player, block));
            addToBot(new GainEnergyAction(2));
        }else if (BasicMod.energySpentTurn <= magicNumber){
            addToBot(new EjectedEffectAction());
            addToBot(new GainEnergyAction(upgraded ? 2 : 1));
        }
    }

    public void triggerOnGlowCheck() {
        this.glowColor = BasicMod.energySpentTurn <= this.magicNumber ? AbstractCard.GOLD_BORDER_GLOW_COLOR : AbstractCard.BLUE_BORDER_GLOW_COLOR;
    }

    public void onScrySelected() {
        eject();
    }

    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        return PlayOnce && super.canUse(p, m);
    }
}
