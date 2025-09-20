package autoplaycharactermod.actions;

import autoplaycharactermod.cards.EquipmentCard;
import autoplaycharactermod.character.PilotCharacter;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.PoisonPower;

public class RustyBladeAction extends AbstractGameAction {
    private static final float DURATION = 0.1F;
    private static final float POST_ATTACK_WAIT_DUR = 0.1F;
    private boolean skipWait;
    private boolean muteSfx;
    private AbstractCard card;
    private int durability;

    public RustyBladeAction(EquipmentCard card, AttackEffect effect) {
        this.skipWait = false;
        this.muteSfx = false;
        this.actionType = ActionType.DAMAGE;
        this.attackEffect = effect;
        this.duration = 0.1F;
        this.durability = card.equipmentMaxHp - card.equipmentHp + 1;
        this.card = card.makeStatEquivalentCopy();
    }

    public void update() {
        if (this.duration == Settings.ACTION_DUR_XFAST) {
            AbstractMonster target = PilotCharacter.getTarget();
            card.calculateCardDamage(target);
            addToTop(new ApplyPowerAction(target, AbstractDungeon.player, new PoisonPower(target, AbstractDungeon.player, durability)));
            addToTop(new DamageAction(target, new DamageInfo(AbstractDungeon.player, card.damage), this.attackEffect, this.skipWait, this.muteSfx));
        }
        this.tickDuration();
        this.isDone = true;
    }
}
