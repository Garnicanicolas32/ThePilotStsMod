package ThePilotCharacter.actions;

import ThePilotCharacter.character.PilotCharacter;
import ThePilotCharacter.ui.ConfigPanel;
import ThePilotCharacter.vfx.EnergyChamberEffect;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class EnergyChamberAction extends AbstractGameAction {
    private AbstractCard card;
    private AbstractCard original;

    public EnergyChamberAction(AbstractCard card, AttackEffect effect) {
        this.actionType = ActionType.DAMAGE;
        this.attackEffect = effect;
        this.duration = 0.1F;
        this.card = card.makeStatEquivalentCopy();
        this.original = card;
    }

    public void update() {
        if (this.duration == Settings.ACTION_DUR_XFAST) {
            AbstractMonster target = PilotCharacter.getTarget();
            card.calculateCardDamage(target);
            addToTop(new DamageAction(target, new DamageInfo(AbstractDungeon.player, card.damage), this.attackEffect, false, false));
            addToTop(new VFXAction(new EnergyChamberEffect(original.hb.cX, original.hb.cY, target.hb.cX, target.hb.cY, card.damage, true), 0.5F));
            if (ConfigPanel.experimentalSounds)
                addToTop(new SfxActionVolume("ORB_DARK_EVOKE", 0.1f, 2f));
        }
        this.tickDuration();
        this.isDone = true;
    }
}
