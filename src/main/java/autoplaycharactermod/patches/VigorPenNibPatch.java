package autoplaycharactermod.patches;

import autoplaycharactermod.BasicMod;
import autoplaycharactermod.cards.EquipmentCard;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.powers.watcher.VigorPower;
import com.megacrit.cardcrawl.relics.PenNib;

public class VigorPenNibPatch {
    @SpirePatch(clz = VigorPower.class, method = "onUseCard")
    public static class DontConsumeVigorPatch {
        @SpirePrefixPatch
        public static SpireReturn<Void> patch(VigorPower __instance, AbstractCard card, UseCardAction action) {
            if (card instanceof EquipmentCard || card.hasTag(BasicMod.CustomTags.skipVigor)){
                return SpireReturn.Return(null);
            }
            return SpireReturn.Continue();
        }
    }

    @SpirePatch(clz = PenNib.class, method = "onUseCard")
    public static class DontConsumePenNibPatch {
        @SpirePrefixPatch
        public static SpireReturn<Void> patch(PenNib __instance, AbstractCard card, UseCardAction action) {
            if ((card instanceof EquipmentCard || card.hasTag(BasicMod.CustomTags.skipVigor)) && __instance.counter > 8){
                return SpireReturn.Return(null);
            }
            return SpireReturn.Continue();
        }
    }
}