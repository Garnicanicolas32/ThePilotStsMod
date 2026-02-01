package ThePilotCharacter.cards.consumable;

import ThePilotCharacter.cards.ConsumableCards;
import ThePilotCharacter.character.PilotCharacter;
import ThePilotCharacter.util.CardStats;
import ThePilotCharacter.vfx.MicroMisilesParticle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.common.ObtainPotionAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.potions.PotionSlot;
import com.megacrit.cardcrawl.vfx.ThoughtBubble;

public class Dispenser extends ConsumableCards {
    public static final String ID = makeID("Dispenser");
    private static final CardStats info = new CardStats(
            PilotCharacter.Meta.CARD_COLOR,
            CardType.SKILL,
            CardRarity.UNCOMMON,
            CardTarget.SELF,
            -2
    );

    public Dispenser() {
        super(ID, info, 3, 3);
        checkEvolve();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        boolean found = false;

        for (AbstractPotion po : p.potions) {
            if (po instanceof PotionSlot) {
                AbstractDungeon.topLevelEffectsQueue.add(new MicroMisilesParticle(
                        Settings.WIDTH / 2f + MathUtils.random(20.0F, -20.0F) * Settings.scale,
                        Settings.HEIGHT / 2f + MathUtils.random(20.0F, -20.0F) * Settings.scale,
                        po.hb.cX, po.hb.cY - 3f * Settings.scale,
                        AbstractDungeon.player.flipHorizontal,
                        Color.GREEN.cpy(), false, 1.3f));
                found = true;
                break;
            }
        }
        setExhaust(found);
        addToBot(new ObtainPotionAction(AbstractDungeon.returnRandomPotion(true)));
        if (found) {
            if (alreadyEvolved)
                addToBot(new ObtainPotionAction(AbstractDungeon.returnRandomPotion(true)));
            super.use(p, m);
        }else {
            AbstractDungeon.effectList.add(new ThoughtBubble(AbstractDungeon.player.dialogX, AbstractDungeon.player.dialogY, 3.0F, cardStrings.EXTENDED_DESCRIPTION[1], true));
            PlayOnce = false;
            returnToHand = false;
        }
    }
    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        return PlayOnce && super.canUse(p, m);
    }
}
