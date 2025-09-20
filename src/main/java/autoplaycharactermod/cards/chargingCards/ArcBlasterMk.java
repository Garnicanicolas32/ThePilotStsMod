package autoplaycharactermod.cards.chargingCards;

import autoplaycharactermod.actions.SfxActionVolume;
import autoplaycharactermod.cards.BaseCard;
import autoplaycharactermod.character.PilotCharacter;
import autoplaycharactermod.powers.ChargePower;
import autoplaycharactermod.powers.EfficiencyPower;
import autoplaycharactermod.ui.ConfigPanel;
import autoplaycharactermod.util.CardStats;
import autoplaycharactermod.vfx.EjectLightingEffect;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class ArcBlasterMk extends BaseCard {
    public static final String ID = makeID("ArcBlasterMk");

    private static final CardStats info = new CardStats(
            PilotCharacter.Meta.CARD_COLOR,
            CardType.SKILL,
            CardRarity.UNCOMMON,
            CardTarget.SELF,
            -2
    );
    private static final int UPG_MAGIC = 5;
    private static final int MAGIC = 6;

    public ArcBlasterMk() {
        super(ID, info);
        returnToHand = true;
        setMagic(MAGIC);
        this.name = cardStrings.NAME + 1;
        checkEvolve();
    }

    @Override
    public void evolveCard() {
        super.evolveCard();
        loadCardImage("autoplaycharactermod/images/cards/skill/ArcBlasterMk5.png");
        this.name = cardStrings.NAME + (this.timesUpgraded + 1) + "++";
        this.initializeTitle();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(p, p, new ChargePower(p, magicNumber)));
        PlayOnce = false;
    }

    public void triggerOnManualDiscard() {
        eject();
    }

    public void eject() {
        if (ConfigPanel.experimentalSounds)
            addToBot(new SfxActionVolume("ATTACK_DEFECT_BEAM", -0.1f + 0.05f * timesUpgraded, 1));
        for (int i = 0; i < (ConfigPanel.lessParticles ? 10 : 20); i++)
            AbstractDungeon.effectsQueue.add(new EjectLightingEffect((float) Settings.WIDTH * 0.96F, (float) Settings.HEIGHT * 0.06F, i));
        if (AbstractDungeon.player.hasPower(EfficiencyPower.POWER_ID))
            ((EfficiencyPower)AbstractDungeon.player.getPower(EfficiencyPower.POWER_ID)).triggerEject();
        upgrade();
        if (this.alreadyEvolved) {
            for (AbstractCard c : AbstractDungeon.player.masterDeck.group) {
                if (c.uuid == this.uuid) {
                    c.upgrade();
                    break;
                }
            }
        }
    }

    public void onScrySelected() {
        eject();
    }

    public boolean canUpgrade() {
        return !this.alreadyEvolved;
    }

    @Override
    protected Texture getPortraitImage() {
        if (this.alreadyEvolved) {
            return ImageMaster.loadImage("autoplaycharactermod/images/cards/skill/ArcBlasterMk5_p.png");
        } else if (upgraded)
            switch (this.timesUpgraded) {
                case 1:
                    return ImageMaster.loadImage("autoplaycharactermod/images/cards/skill/ArcBlasterMk2_p.png");
                case 2:
                    return ImageMaster.loadImage("autoplaycharactermod/images/cards/skill/ArcBlasterMk3_p.png");
                case 3:
                    return ImageMaster.loadImage("autoplaycharactermod/images/cards/skill/ArcBlasterMk4_p.png");
                case 4:
                default:
                    return ImageMaster.loadImage("autoplaycharactermod/images/cards/skill/ArcBlasterMk5_p.png");
            }
        else {
            return super.getPortraitImage();
        }
    }

    @Override
    public void upgrade() {
        this.upgradeMagicNumber(UPG_MAGIC + this.timesUpgraded);
        ++this.timesUpgraded;
        this.upgraded = true;
        int number = this.timesUpgraded + 1;
        this.name = cardStrings.NAME + number;
        this.misc += UPG_MAGIC;
        if (this.alreadyEvolved) {
            loadCardImage("autoplaycharactermod/images/cards/skill/ArcBlasterMk5.png");
            this.name += "++";
            this.initializeTitle();
        } else if (this.timesUpgraded == 1) {
            loadCardImage("autoplaycharactermod/images/cards/skill/ArcBlasterMk2.png");
        } else if (this.timesUpgraded == 2) {
            loadCardImage("autoplaycharactermod/images/cards/skill/ArcBlasterMk3.png");
        } else if (this.timesUpgraded == 3) {
            loadCardImage("autoplaycharactermod/images/cards/skill/ArcBlasterMk4.png");
        } else if (this.timesUpgraded >= 4) {
            loadCardImage("autoplaycharactermod/images/cards/skill/ArcBlasterMk5.png");
        }
        if (!this.alreadyEvolved) {
            this.initializeDescription();
        }
    }

    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        return PlayOnce && super.canUse(p, m);
    }
}
