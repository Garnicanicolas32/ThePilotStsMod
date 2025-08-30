package autoplaycharactermod.patches;

import autoplaycharactermod.cards.EquipmentCard;
import autoplaycharactermod.cards.equipment.FailSafe;
import autoplaycharactermod.powers.BubblePower;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.powers.AbstractPower;
import javassist.CtBehavior;

@SpirePatch(
        clz = AbstractPlayer.class,
        method = "damage"
)
public class AbstractPlayerDamagePatch {
    @SpireInsertPatch(
            locator = LocatorCheckOnFatal.class,
            localvars = {"damageAmount"}
    )
    public static SpireReturn<Void> InsertCheckOnFatal(AbstractPlayer __instance, DamageInfo info, @ByRef int[] damageAmount) {
        for (AbstractCard c : __instance.hand.group) {
            if (c instanceof FailSafe) {
                if (((EquipmentCard) c).isEquipped() && ((EquipmentCard) c).equipmentHp > 0) {
                    c.flash();
                    __instance.currentHealth = 0;
                    __instance.isDead = false;
                    ((FailSafe) c).Activate();
                    return SpireReturn.Return();
                }
            }
        }
        return SpireReturn.Continue();
    }

    @SpireInsertPatch(
            locator = LocatorCardOnDamage.class,
            localvars = {"damageAmount"}
    )
    public static void InsertCardOnDamage(AbstractPlayer __instance, DamageInfo info, @ByRef int[] damageAmount) {
        for (AbstractCard r : __instance.hand.group) {
            if (r instanceof EquipmentCard){
                damageAmount[0] = ((EquipmentCard) r).customOnLoseHpLast(damageAmount[0]);
                ((EquipmentCard) r).damageReceived(info.output);
            }
        }
    }

    @SpireInsertPatch(
            locator = LocatorBubblePower.class,
            localvars = {"damageAmount"}
    )
    public static void BubblePowerOnDamage(AbstractPlayer __instance, DamageInfo info, @ByRef int[] damageAmount) {
        for (AbstractPower p : __instance.powers) {
            if (p instanceof BubblePower){
                damageAmount[0] = ((BubblePower)p).ChangeDamage(info, damageAmount[0]);
            }
        }
    }

    private static class LocatorBubblePower extends SpireInsertLocator {
        @Override
        public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
            Matcher matcher = new Matcher.MethodCallMatcher(AbstractPlayer.class, "decrementBlock");
            return LineFinder.findInOrder(ctMethodToPatch, matcher);
        }
    }

    private static class LocatorCheckOnFatal extends SpireInsertLocator {
        @Override
        public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
            Matcher matcher = new Matcher.MethodCallMatcher(AbstractPlayer.class, "hasRelic");
            return LineFinder.findInOrder(ctMethodToPatch, matcher);
        }
    }

    private static class LocatorCardOnDamage extends SpireInsertLocator {
        @Override
        public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
            Matcher matcher = new Matcher.MethodCallMatcher(Math.class, "min");
            return LineFinder.findInOrder(ctMethodToPatch, matcher);
        }
    }
}
