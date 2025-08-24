package autoplaycharactermod.cards.consumable;

import autoplaycharactermod.cards.ConsumableCards;
import autoplaycharactermod.character.MyCharacter;
import autoplaycharactermod.util.CardStats;
import autoplaycharactermod.vfx.MicroMisilesParticle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.common.ObtainPotionAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.potions.PotionSlot;

public class Dispenser extends ConsumableCards {
    public static final String ID = makeID("Dispenser");
    private static final CardStats info = new CardStats(
            MyCharacter.Meta.CARD_COLOR,
            CardType.SKILL,
            CardRarity.UNCOMMON,
            CardTarget.SELF,
            -2 
    );

    public Dispenser() {
        super(ID, info, 3, 6);
        checkEvolve();
    }

    @Override
    public void evolveCard() {
        super.evolveCard();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        for (AbstractPotion po : p.potions) {
            if (po instanceof PotionSlot) {
                AbstractDungeon.topLevelEffectsQueue.add(new MicroMisilesParticle(
                        Settings.WIDTH / 2f + MathUtils.random(20.0F, -20.0F) * Settings.scale,
                        Settings.HEIGHT / 2f + MathUtils.random(20.0F, -20.0F) * Settings.scale,
                        po.hb.cX, po.hb.cY - 3f * Settings.scale,
                        AbstractDungeon.player.flipHorizontal,
                        Color.GREEN.cpy(), false, 1.3f));
                break;
            }
        }
        addToBot(new ObtainPotionAction(AbstractDungeon.returnRandomPotion(true)));
        if (alreadyEvolved)
            addToBot(new ObtainPotionAction(AbstractDungeon.returnRandomPotion(true)));
        super.use(p, m);
    }
}
