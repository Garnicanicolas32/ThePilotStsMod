package autoplaycharactermod.patches;

import autoplaycharactermod.BasicMod;
import autoplaycharactermod.cards.traitScavengeCards.CouponStamp;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

@SpirePatch(
        clz = AbstractPlayer.class,
        method = "loseGold"
)
public class AbstractPlayerLoseGoldPatch {
    @SpirePostfixPatch
    public static void Postfix(AbstractPlayer __instance, int goldAmount) {
        if (AbstractDungeon.getCurrRoom() instanceof com.megacrit.cardcrawl.rooms.ShopRoom) {
            BasicMod.purchases++;
            for (AbstractCard c : AbstractDungeon.player.masterDeck.group) {
                if (c instanceof CouponStamp) {
                    c.initializeDescription();
                }
            }
        }
    }
}

