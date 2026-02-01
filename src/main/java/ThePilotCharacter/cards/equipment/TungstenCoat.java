package ThePilotCharacter.cards.equipment;

import ThePilotCharacter.actions.SfxActionVolume;
import ThePilotCharacter.cards.EquipmentCard;
import ThePilotCharacter.character.PilotCharacter;
import ThePilotCharacter.ui.ConfigPanel;
import ThePilotCharacter.util.CardStats;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.combat.BlockImpactLineEffect;

public class TungstenCoat extends EquipmentCard {
    public static final String ID = makeID("TungstenCoat");

    private static final CardStats info = new CardStats(
            PilotCharacter.Meta.CARD_COLOR,
            CardType.SKILL,
            CardRarity.UNCOMMON,
            CardTarget.NONE,
            0
    );
    private static final int BASE_HP = 18;

    public TungstenCoat() {
        super(ID, info, BASE_HP);
        setInnate(false, true);
        checkEvolve();
    }

    @Override
    public void Activate() {
        if (!Equipped) return;
        super.Activate();
    }

    @Override
    public void evolveCard() {
        setInnate(true);
        super.evolveCard();
    }

    public int customOnLoseHpLast(int damageAmount) {
        if (damageAmount > 0 && Equipped) {
            if (ConfigPanel.experimentalSounds)
                addToBot(new SfxActionVolume("RELIC_DROP_CLINK", MathUtils.random(-0.4F, 0.4F), 0.7F));
            for (int i = 0; i < 10; i++) {
                AbstractDungeon.topLevelEffectsQueue.add(new BlockImpactLineEffect(
                        this.hb.cX + MathUtils.random(10.0F, -10.0F) * Settings.scale,
                        this.hb.cY + MathUtils.random(10.0F, -10.0F) * Settings.scale));
            }
            flash();
            Activate();
            return Math.max(damageAmount - (alreadyEvolved ? 3 : 1), 0);
        }
        return damageAmount;
    }

    @Override
    protected int getUpgradeDurability() {
        return 6;
    }
}
