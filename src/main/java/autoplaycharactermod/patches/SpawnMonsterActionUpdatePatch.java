package autoplaycharactermod.patches;

import autoplaycharactermod.character.MyCharacter;
import autoplaycharactermod.powers.TargetedPower;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.SpawnMonsterAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import javassist.CtBehavior;

@SpirePatch(
        clz = SpawnMonsterAction.class,
        method = "update"
)
public class SpawnMonsterActionUpdatePatch {
    @SpireInsertPatch(
            locator = Locator.class
    )
    public static void Insert(SpawnMonsterAction __instance, AbstractMonster ___m) {
        if (AbstractDungeon.player instanceof MyCharacter) {
            boolean found = false;
            for (AbstractMonster mon : AbstractDungeon.getMonsters().monsters) {
                if (mon.currentHealth > 0 && mon.hasPower(TargetedPower.POWER_ID)) {
                    found = true;
                }
            }
            if (!found) {
                AbstractDungeon.actionManager.addToTop(new ApplyPowerAction(___m, ___m, new TargetedPower(___m, -1)));
                MyCharacter.targetCheck(___m, true);
            }
        }
    }

    private static class Locator extends SpireInsertLocator {
        @Override
        public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
            Matcher matcher = new Matcher.FieldAccessMatcher(SpawnMonsterAction.class, "minion");
            return LineFinder.findInOrder(ctMethodToPatch, matcher);
        }
    }
}

