package autoplaycharactermod.actions;

import autoplaycharactermod.character.MyCharacter;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.LightningEffect;

public class DamageCurrentTargetAction extends AbstractGameAction {
    private static final float DURATION = 0.1F;
    private static final float POST_ATTACK_WAIT_DUR = 0.1F;
    private boolean skipWait;
    private boolean muteSfx;
    private AbstractCard card;

    public DamageCurrentTargetAction(AbstractCard card, AbstractGameAction.AttackEffect effect) {
        this.skipWait = false;
        this.muteSfx = false;
        this.actionType = ActionType.DAMAGE;
        this.attackEffect = effect;
        this.duration = 0.1F;
        this.card = card.makeStatEquivalentCopy();
    }

    public DamageCurrentTargetAction(AbstractCard card) {
        this(card, AttackEffect.NONE);
    }

    public DamageCurrentTargetAction(AbstractCard card, boolean superFast) {
        this(card, AttackEffect.NONE);
        this.skipWait = superFast;
    }

    public DamageCurrentTargetAction(AbstractCard card, AbstractGameAction.AttackEffect effect, boolean superFast) {
        this(card, effect);
        this.skipWait = superFast;
    }

    public DamageCurrentTargetAction(AbstractCard card, AbstractGameAction.AttackEffect effect, boolean superFast, boolean muteSfx) {
        this(card, effect, superFast);
        this.muteSfx = muteSfx;
    }

    public void update() {
        if (this.duration == Settings.ACTION_DUR_XFAST) {
            AbstractMonster target = MyCharacter.getTarget();
            card.calculateCardDamage(target);
            if (this.attackEffect == AttackEffect.LIGHTNING) {
                addToTop(new DamageAction(target, new DamageInfo(AbstractDungeon.player, card.damage), AttackEffect.NONE, this.skipWait, this.muteSfx));
                this.addToTop(new VFXAction(new LightningEffect(target.drawX, target.drawY), 0.1f));
                this.addToTop(new SFXAction("ORB_LIGHTNING_EVOKE"));
            } else {
                addToTop(new DamageAction(target, new DamageInfo(AbstractDungeon.player, card.damage), this.attackEffect, this.skipWait, this.muteSfx));
            }

        }
        this.tickDuration();
        this.isDone = true;
    }
}
