package autoplaycharactermod.actions;

import autoplaycharactermod.character.MyCharacter;
import autoplaycharactermod.vfx.SmartGunEffect;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class SmartGunLocateAnimation extends AbstractGameAction {

    private AbstractCard card;
    public SmartGunLocateAnimation(AbstractCard card) {
        this.card = card;
        this.actionType = ActionType.SPECIAL;
    }

    public void update() {
        AbstractMonster m = MyCharacter.getTarget();
        AbstractDungeon.topLevelEffects.add(new SmartGunEffect(m.hb.cX + MathUtils.random(-80.0F, 80.0F) * Settings.scale,
                m.hb.cY + MathUtils.random(-80.0F, 80.0F) * Settings.scale,
                card.hb.cX + MathUtils.random(-120.0F, 120.0F) * Settings.scale
                , card.hb.cY + MathUtils.random(-50.0F, 150.0F) * Settings.scale));
        this.isDone = true;
    }
}