package autoplaycharactermod.cards.traitBastionCards;

import autoplaycharactermod.BasicMod;
import autoplaycharactermod.actions.SfxActionVolume;
import autoplaycharactermod.cards.TraitCard;
import autoplaycharactermod.cards.traitScavengeCards.DuctTape;
import autoplaycharactermod.character.MyCharacter;
import autoplaycharactermod.util.CardStats;
import autoplaycharactermod.util.GeneralUtils;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDiscardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.ShockWaveEffect;

public class Beacon extends TraitCard {
    public static final String ID = makeID("Beacon");
    private static final CardStats info = new CardStats(
            MyCharacter.Meta.CARD_COLOR,
            CardType.SKILL,
            CardRarity.UNCOMMON,
            CardTarget.SELF,
            0 
    );
    private static final int BLOCK = 4;
    private static final int UPG_BLOCK = 3;
    private static final int MAGIC = 2;
    private static final int UPG_MAGIC = -1;

    public Beacon() {
        super(ID, info, TraitCard.TraitColor.BASTION, false);
        setBlock(BLOCK, UPG_BLOCK);
        setMagic(MAGIC, UPG_MAGIC);
        setCostUpgrade(-2);
        checkEvolve();
        if (!this.alreadyEvolved)
            tags.add(BasicMod.CustomTags.NoEnergyText);
    }

    @Override
    public void evolveCard() {
        cost = -2;
        setBlock(12);
        tags.remove(BasicMod.CustomTags.NoEnergyText);
        super.evolveCard();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (PlayOnce) {
            PlayOnce = false;
            addToBot(new GainBlockAction(p, p, block));
            addPower();
            for (AbstractCard c : AbstractDungeon.player.discardPile.group) {
                if (c instanceof DuctTape && c.uuid != this.uuid) {
                    ((DuctTape) c).triggerReturnToHand();
                }
            }
            if (upgraded || this.alreadyEvolved) {
                addToBot(new SfxActionVolume("POWER_MANTRA", 0f, 1F));
                this.addToBot(new VFXAction(p, new ShockWaveEffect(p.hb.cX, p.hb.cY, Settings.BLUE_TEXT_COLOR, ShockWaveEffect.ShockWaveType.ADDITIVE), 0.5F));
                addToBot(new MakeTempCardInDiscardAction(GeneralUtils.getRandomBastionCard(), 1));
                if (this.alreadyEvolved)
                    addToBot(new MakeTempCardInDiscardAction(GeneralUtils.getRandomBastionCard(), 1));
            }
            returnToHand = true;
        } else {
            removePower();
            for (AbstractCard c : AbstractDungeon.player.discardPile.group) {
                if (c instanceof DuctTape && c.uuid != this.uuid) {
                    ((DuctTape) c).triggerReturnToHand();
                }
            }
            addToBot(new SfxActionVolume("POWER_MANTRA",0f, 1F));
            this.addToBot(new VFXAction(p, new ShockWaveEffect(p.hb.cX, p.hb.cY, Settings.BLUE_TEXT_COLOR, ShockWaveEffect.ShockWaveType.ADDITIVE), 0.5F));
            addToBot(new MakeTempCardInDiscardAction(GeneralUtils.getRandomBastionCard(), 1));
            returnToHand = false;
        }

    }

    @Override
    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        boolean canuse = (PlayOnce || (!upgraded && !this.alreadyEvolved)) && super.canUse(p, m);
        return (PlayOnce || (!upgraded && !this.alreadyEvolved)) && super.canUse(p, m);
    }

    @Override
    public boolean freeToPlay() {
        return !upgraded && !this.alreadyEvolved;
    }

    @Override
    public void upgrade() {
        super.upgrade();
        tags.remove(BasicMod.CustomTags.NoEnergyText);
    }
}