package ThePilotCharacter.cards.chargingCards;

import ThePilotCharacter.actions.SfxActionVolume;
import ThePilotCharacter.cards.BaseCard;
import ThePilotCharacter.character.PilotCharacter;
import ThePilotCharacter.powers.ChargePower;
import ThePilotCharacter.powers.EfficiencyPower;
import ThePilotCharacter.ui.ConfigPanel;
import ThePilotCharacter.util.CardStats;
import ThePilotCharacter.vfx.EjectLightingEffect;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class ComboBreaker extends BaseCard {
    public static final String ID = makeID("ComboBreaker");
    private static final CardStats info = new CardStats(
            PilotCharacter.Meta.CARD_COLOR,
            CardType.SKILL,
            CardRarity.UNCOMMON,
            CardTarget.SELF,
            -2
    );
    private static final int UPG_MAGIC = 1;
    private static final int MAGIC = 3;
    private int count = 0;

    public ComboBreaker() {
        super(ID, info);
        returnToHand = true;
        setMagic(MAGIC, UPG_MAGIC);
        setEthereal(true, false);
        checkEvolve();
    }

    @Override
    public void evolveCard() {
        setMagic(5);
        setEthereal(false);
        super.evolveCard();
    }

    @Override
    protected Texture getPortraitImage() {
        if (this.alreadyEvolved)
            return ImageMaster.loadImage("ThePilotCharacter/images/cards/skill/ComboBreaker5_p.png");
        else if (count > 0)
            return ImageMaster.loadImage("ThePilotCharacter/images/cards/skill/ComboBreaker" + Math.min(count, 5) + "_p.png");
        else {
            return super.getPortraitImage();
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(p, p, new ChargePower(p, magicNumber)));
        PlayOnce = false;
    }

    public void triggerOnManualDiscard() {
        combo();
    }

    public void onScrySelected() {
        combo();
    }

    public void combo() {
        if (AbstractDungeon.player.hasPower(EfficiencyPower.POWER_ID))
            ((EfficiencyPower) AbstractDungeon.player.getPower(EfficiencyPower.POWER_ID)).triggerEject();
        this.magicNumber *= this.alreadyEvolved ? 3 : 2;
        this.baseMagicNumber = this.magicNumber;
        this.isMagicNumberModified = true;
        if (ConfigPanel.experimentalSounds)
            addToBot(new SfxActionVolume("RELIC_DROP_MAGICAL", -0.1f + 0.05F * count, 1.5F + 0.4F * Math.min(count, 6)));
        for (int i = 0; i < (ConfigPanel.lessParticles ? 10 : 20); i++)
            AbstractDungeon.effectsQueue.add(new EjectLightingEffect((float) Settings.WIDTH * 0.96F, (float) Settings.HEIGHT * 0.06F, i));
        count++;
        if (this.alreadyEvolved) {
            loadCardImage("ThePilotCharacter/images/cards/skill/ComboBreaker5.png");
        } else
            loadCardImage("ThePilotCharacter/images/cards/skill/ComboBreaker" + Math.min(count, 5) + ".png");
        this.initializeDescription();
    }

    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        return PlayOnce && super.canUse(p, m);
    }
}
