package autoplaycharactermod.actions;

import autoplaycharactermod.ui.ConfigPanel;
import autoplaycharactermod.vfx.EjectLightingEffect;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class EjectedEffectAction extends AbstractGameAction {

    public EjectedEffectAction() {
        this.duration = Settings.ACTION_DUR_XFAST;
        this.actionType = ActionType.WAIT;
        this.source = AbstractDungeon.player;
    }

    @Override
    public void update() {
        if (this.duration == Settings.ACTION_DUR_XFAST) {
            CardCrawlGame.sound.play("RELIC_DROP_MAGICAL");
            for (int i = 0; i < (ConfigPanel.lessParticles ? 10 : 20); i++)
                AbstractDungeon.effectsQueue.add(new EjectLightingEffect((float) Settings.WIDTH * 0.96F, (float) Settings.HEIGHT * 0.06F, i));
            this.isDone = true;
        }
    }
}