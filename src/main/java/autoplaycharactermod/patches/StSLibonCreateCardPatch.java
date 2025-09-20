package autoplaycharactermod.patches;

import autoplaycharactermod.actions.OnCreatePlayAction;
import autoplaycharactermod.character.PilotCharacter;
import com.evacipated.cardcrawl.mod.stslib.StSLib;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

@SpirePatch(
        clz = StSLib.class,
        method = "onCreateCard",
        paramtypez = {AbstractCard.class}
)
public class StSLibonCreateCardPatch {
    @SpirePostfixPatch
    public static void Postfix(AbstractCard c) {
        if (AbstractDungeon.player instanceof PilotCharacter && c.color == PilotCharacter.Meta.CARD_COLOR && AbstractDungeon.player.hand.contains(c)) {
            AbstractDungeon.actionManager.addToBottom(new OnCreatePlayAction(c));
        }
    }
}


