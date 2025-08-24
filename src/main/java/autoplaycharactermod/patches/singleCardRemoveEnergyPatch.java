package autoplaycharactermod.patches;

import autoplaycharactermod.BasicMod;
import autoplaycharactermod.cards.EquipmentCard;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.screens.SingleCardViewPopup;
import javassist.CtBehavior;

@SpirePatch(
        clz = SingleCardViewPopup.class,
        method = "renderCost",
        paramtypez = {SpriteBatch.class}
)
public class singleCardRemoveEnergyPatch {
    @SpireInsertPatch(
            locator = Locator.class
    )
    public static SpireReturn<Void> Insert(SingleCardViewPopup __instance, SpriteBatch sb, AbstractCard ___card) {
        if (___card instanceof EquipmentCard || ___card.hasTag(BasicMod.CustomTags.NoEnergyText))
            return SpireReturn.Return();
        return SpireReturn.Continue();
    }

    private static class Locator extends SpireInsertLocator {
        @Override
        public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
            Matcher matcher = new Matcher.FieldAccessMatcher(AbstractCard.class, "isCostModified");
            return LineFinder.findInOrder(ctMethodToPatch, matcher);
        }
    }
}


