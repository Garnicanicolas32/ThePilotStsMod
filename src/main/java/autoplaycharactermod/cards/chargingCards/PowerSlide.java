package autoplaycharactermod.cards.chargingCards;

import autoplaycharactermod.BasicMod;
import autoplaycharactermod.cards.BaseCard;
import autoplaycharactermod.character.MyCharacter;
import autoplaycharactermod.powers.ChargePower;
import autoplaycharactermod.ui.ConfigPanel;
import autoplaycharactermod.util.CardStats;
import autoplaycharactermod.vfx.AfterImageEffectVFX;
import com.badlogic.gdx.Gdx;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.utility.DiscardToHandAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class PowerSlide extends BaseCard {
    public static final String ID = makeID("PowerSlide");
    private static final CardStats info = new CardStats(
            MyCharacter.Meta.CARD_COLOR,
            CardType.SKILL,
            CardRarity.UNCOMMON,
            CardTarget.SELF,
            0 
    );
    private static final int MAGIC = 3;
    private static final int UPG_MAGIC = 2;
    private float rotationTimer;

    public PowerSlide() {
        super(ID, info);
        returnToHand = true;
        setMagic(MAGIC, UPG_MAGIC);
        tags.add(BasicMod.CustomTags.NoEnergyText);
        checkEvolve();
    }

    @Override
    public void evolveCard() {
        setMagic(6);
        super.evolveCard();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (PlayOnce && !Duplicated) {
            PlayOnce = false;
            returnToHand = true;
        } else {
            addToBot(new SFXAction("ORB_DARK_CHANNEL"));
            addToBot(new ApplyPowerAction(p, p, new ChargePower(p, magicNumber)));
            if (alreadyEvolved)
                evolvePower();
            returnToHand = false;
        }
    }

    @Override
    public boolean freeToPlay() {
        return true;
    }

    public void evolvePower(){
        this.magicNumber += 2;
        this.baseMagicNumber = this.magicNumber;
        this.isMagicNumberModified = true;
        this.initializeDescription();
    }

    public void triggerOnScry() {
        this.addToBot(new DiscardToHandAction(this));
        PlayOnce = false;
    }

    @Override
    public void update() {
        super.update();
        if (!ConfigPanel.lessParticles && AbstractDungeon.player != null && AbstractDungeon.player.isDraggingCard && this == AbstractDungeon.player.hoveredCard) {
            if (rotationTimer <= 0F) {
                rotationTimer = 0.15F;
                AbstractDungeon.effectList.add(new AfterImageEffectVFX(this));
            } else {
                rotationTimer -= Gdx.graphics.getDeltaTime();
            }
        }
    }
}
