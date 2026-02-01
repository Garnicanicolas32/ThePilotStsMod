package ThePilotCharacter.cards.equipment;

import ThePilotCharacter.actions.SfxActionVolume;
import ThePilotCharacter.cards.EquipmentCard;
import ThePilotCharacter.character.PilotCharacter;
import ThePilotCharacter.powers.LoseThornsPower;
import ThePilotCharacter.ui.ConfigPanel;
import ThePilotCharacter.util.CardStats;
import ThePilotCharacter.vfx.ShredderLinesEffect;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.ThornsPower;

public class Shredder extends EquipmentCard {
    public static final String ID = makeID("Shredder");
    private static final CardStats info = new CardStats(
            PilotCharacter.Meta.CARD_COLOR,
            CardType.SKILL,
            CardRarity.UNCOMMON,
            CardTarget.NONE,
            0
    );
    private static final int BASE_HP = 45;
    private static final int UPG_THORNS = 1;
    private static final int THORNS = 1;
    public boolean skipNext = false;

    public Shredder() {
        super(ID, info, BASE_HP);
        setMagic(THORNS, UPG_THORNS);
        checkEvolve();
    }

    @Override
    public void evolveCard() {
        setMagic(3);
        super.evolveCard();
    }

    @Override
    public void Activate() {
        if (!Equipped) return;
        if (skipNext) {
            skipNext = false;
            return;
        }
        AbstractPlayer p = AbstractDungeon.player;
        if (ConfigPanel.experimentalSounds){
        addToBot(new SfxActionVolume("ATTACK_DAGGER_4", MathUtils.random(-1.3f, 1.3f), 1.8F));
        addToBot(new SfxActionVolume("ATTACK_DAGGER_1", MathUtils.random(-1.3f, 1.3f), 1.8F));}
        for (int i = 0; i < (ConfigPanel.lessParticles ? 10 : 20); i++) {
            AbstractDungeon.topLevelEffectsQueue.add(new ShredderLinesEffect(
                    this.hb.cX + MathUtils.random(10.0F, -10.0F) * Settings.scale,
                    this.hb.cY + MathUtils.random(10.0F, -10.0F) * Settings.scale));
        }
        addToBot(new ApplyPowerAction(p, p, new ThornsPower(p, magicNumber)));
        if (!this.alreadyEvolved)
            addToBot(new ApplyPowerAction(p, p, new LoseThornsPower(p, magicNumber)));

        super.Activate();
    }

    @Override
    protected int getUpgradeDurability() {
        return 15;
    }

    public void onGainBlock() {
        Activate();
    }
}
