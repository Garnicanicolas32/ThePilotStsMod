package autoplaycharactermod.patches;

import autoplaycharactermod.cards.EquipmentCard;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import javassist.CtBehavior;

@SpirePatch(
        clz = AbstractCreature.class,
        method = "addBlock"
)
public class OnGainBlockTrigger {
    @SpireInsertPatch(
            locator = Locator.class
    )
    public static void Insert(AbstractCreature __instance) {
        for (AbstractCard c : AbstractDungeon.player.hand.group) {
            if (c instanceof EquipmentCard) {
                ((EquipmentCard) c).onGainBlock();
            }
        }
    }

    private static class Locator extends SpireInsertLocator {
        @Override
        public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
            Matcher matcher = new Matcher.FieldAccessMatcher(AbstractDungeon.class, "player");
            return LineFinder.findInOrder(ctMethodToPatch, matcher);
        }
    }
}


