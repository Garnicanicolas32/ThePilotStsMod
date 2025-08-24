package autoplaycharactermod.util;

import autoplaycharactermod.BasicMod;
import basemod.abstracts.AbstractCardModifier;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.mod.stslib.util.extraicons.ExtraIcons;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.UIStrings;

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
