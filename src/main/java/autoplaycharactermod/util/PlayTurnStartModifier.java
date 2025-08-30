package autoplaycharactermod.util;

import basemod.abstracts.AbstractCardModifier;
import com.megacrit.cardcrawl.cards.AbstractCard;

import static autoplaycharactermod.BasicMod.makeID;

public class PlayTurnStartModifier extends AbstractCardModifier {
    public static String ID = makeID("PlayTurnStartModifier");

    public AbstractCardModifier makeCopy() {
        return new PlayTurnStartModifier();
    }

    public String identifier(AbstractCard card) {
        return ID;
    }

    public boolean removeOnCardPlayed(AbstractCard card) {
        return true;
    }
}
