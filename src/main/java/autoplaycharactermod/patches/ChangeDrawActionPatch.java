package autoplaycharactermod.patches;

import autoplaycharactermod.character.MyCharacter;
import autoplaycharactermod.potions.SneckoOilRework;
import com.badlogic.gdx.Gdx;
import com.evacipated.cardcrawl.modthespire.lib.ByRef;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PotionHelper;
import com.megacrit.cardcrawl.potions.AbstractPotion;

import java.util.Objects;

@SpirePatch(
        clz = DrawCardAction.class,
        method = "update"
)
public class ChangeDrawActionPatch {
    @SpirePrefixPatch
    public static SpireReturn<Void> Prefix(DrawCardAction __instance, @ByRef float[] ___duration ) {
        if (AbstractDungeon.player instanceof MyCharacter){
            if (__instance.amount <= 0) {
                __instance.isDone = true;
                return SpireReturn.Return();
            }

            ___duration[0] -= Gdx.graphics.getDeltaTime();
            if (__instance.amount > 0 && ___duration[0] < 0.0F) {

                if (Settings.FAST_MODE) {
                    ___duration[0] = Settings.ACTION_DUR_XFAST;
                } else {
                    ___duration[0] = Settings.ACTION_DUR_FASTER;
                }

                __instance.amount--;

                AbstractDungeon.player.draw(1);

                if (__instance.amount == 0) {
                    __instance.isDone = true;
                }
            }
            return SpireReturn.Return();
        }
        else
            return SpireReturn.Continue();
    }

}
