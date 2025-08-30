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

    public Beacon() {
        super(ID, info, TraitCard.TraitColor.BASTION, false);
        setBlock(BLOCK, UPG_BLOCK);
        checkEvolve();
        tags.add(BasicMod.CustomTags.NoEnergyText);
    }

    @Override
    public void evolveCard() {
        setBlock(12);
        super.evolveCard();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (PlayOnce && !Duplicated) {
            PlayOnce = false;
            addToBot(new GainBlockAction(p, p, block));
            addPower();
            for (AbstractCard c : AbstractDungeon.player.discardPile.group) {
                if (c instanceof DuctTape && c.uuid != this.uuid) {
                    ((DuctTape) c).triggerReturnToHand();
                }
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
    public boolean freeToPlay() {
        return true;
    }

}