package autoplaycharactermod.cards.equipment;

import autoplaycharactermod.cards.EquipmentCard;
import autoplaycharactermod.character.PilotCharacter;
import autoplaycharactermod.ui.ConfigPanel;
import autoplaycharactermod.util.CardStats;
import autoplaycharactermod.vfx.FailSafeParticle;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardBrieflyEffect;

public class FailSafe extends EquipmentCard {
    public static final String ID = makeID("FailSafe");
    private static final CardStats info = new CardStats(
            PilotCharacter.Meta.CARD_COLOR,
            CardType.SKILL,
            CardRarity.RARE,
            CardTarget.NONE,
            0
    );
    private static final int BASE_HP = 1;
    private static final int MAGIC = 10;
    private static final int MAGICUPG = 20;

    public FailSafe() {
        super(ID, info, BASE_HP);
        tags.add(CardTags.HEALING);
        setMagic(MAGIC, MAGICUPG);
        checkEvolve();
    }

    @Override
    public void evolveCard() {
        setInnate(true);
        setMagic(60);
        super.evolveCard();
    }

    @Override
    public void Activate() {
        if (!Equipped) return;
        AbstractPlayer p = AbstractDungeon.player;
        addToBot(new SFXAction("HEAL_1"));
        addToBot(new SFXAction("SELECT_WATCHER"));
        AbstractDungeon.topLevelEffects.add(new ShowCardBrieflyEffect(this.makeStatEquivalentCopy(),Settings.WIDTH / 2f,Settings.HEIGHT/ 2f));
        for (int layer = 0; layer < (ConfigPanel.lessParticles ? 3: 6); layer++) {
            int count = 7 + layer * 2;
            float radius = 270.0F * Settings.scale + layer * 50.0F * Settings.scale;
            for (int i = 0; i < count; i++) {
                float angleOffset = (layer % 2 == 0) ? 0.0F : (float)Math.PI / count;
                float angle = (float)(2 * Math.PI * i / count + angleOffset);
                float x = Settings.WIDTH / 2f + (float)Math.cos(angle) * radius;
                float y = Settings.HEIGHT / 2f + (float)Math.sin(angle) * radius;
                float duration = 1F + (2 - layer) * 0.4F;
                AbstractDungeon.effectsQueue.add(new FailSafeParticle(x, y, duration));
            }
        }
        int healAmt = p.maxHealth * magicNumber / 100;
        if (healAmt < 1)
            healAmt = 1;
        p.heal(healAmt, true);
        super.Activate();
    }

    @Override
    protected int getUpgradeDurability() {
        return 0;
    }
}
