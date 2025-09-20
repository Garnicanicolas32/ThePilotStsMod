package autoplaycharactermod.cards.equipment;

import autoplaycharactermod.cards.EquipmentCard;
import autoplaycharactermod.character.PilotCharacter;
import autoplaycharactermod.ui.ConfigPanel;
import autoplaycharactermod.util.CardStats;
import autoplaycharactermod.vfx.VisorParticleEffect;
import com.badlogic.gdx.Gdx;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.actions.utility.ScryAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class TacticalVisor extends EquipmentCard {
    public static final String ID = makeID("TacticalVisor");

    private static final CardStats info = new CardStats(
            PilotCharacter.Meta.CARD_COLOR,
            CardType.SKILL,
            CardRarity.RARE,
            CardTarget.NONE,
            0 
    );
    private static final int MAGIC = 3;
    private static final int UPG_MAGIC = 0;
    private static final int BASE_HP = 10;

    private boolean particleUse = false;
    public boolean unused = false;
    private float particleTimer = 0.0F;

    public TacticalVisor() {
        super(ID, info, BASE_HP);
        setMagic(MAGIC, UPG_MAGIC);
        setInnate(false, true);
        checkEvolve();
    }

    @Override
    public void evolveCard() {
        setInnate(true);
        setMagic(5);
        super.evolveCard();
    }

    protected void onEquip() {
        if ((isInnate || this.alreadyEvolved) && unused) {
            Activate();
            unused = false;
        }
    }

    @Override
    public void Activate() {
        if (!Equipped) return;
        AbstractPlayer p = AbstractDungeon.player;
        addToBot(new SFXAction("ORB_SLOT_GAIN"));
        addToBot(new AbstractGameAction() {
            @Override
            public void update() {
                particleUse = true;
                isDone = true;
            }
        });
        addToBot(new ScryAction(magicNumber));
        addToBot(new AbstractGameAction() {
            @Override
            public void update() {
                particleUse = false;
                isDone = true;
            }
        });
        addToBot(new GainEnergyAction(this.alreadyEvolved ? 2 : 1));

        super.Activate();
    }

    @Override
    public void update() {
        super.update();
        if (!Settings.DISABLE_EFFECTS && particleUse) {
            this.particleTimer -= Gdx.graphics.getDeltaTime();
            if (this.particleTimer < 0.0F) {
                this.particleTimer = ConfigPanel.lessParticles ? 0.6f : 0.3F;
                AbstractDungeon.topLevelEffectsQueue.add(new VisorParticleEffect(this.hb.cX, this.hb.cY));
            }
        }
    }

    @Override
    protected int getUpgradeDurability() {
        return 3;
    }

    public void atTurnStart() {
        Activate();
    }


}
