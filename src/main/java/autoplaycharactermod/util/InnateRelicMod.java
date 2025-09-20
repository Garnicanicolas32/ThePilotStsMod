package autoplaycharactermod.util;

import autoplaycharactermod.ThePilotMod;
import basemod.abstracts.AbstractCardModifier;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.mod.stslib.util.extraicons.ExtraIcons;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.UIStrings;

import static autoplaycharactermod.ThePilotMod.makeID;

public class InnateRelicMod extends AbstractCardModifier {
    private static final UIStrings uiStrings;
    private static final Texture texDefault = TextureLoader.getTexture(ThePilotMod.imagePath("relics/MemoryCard.png"));
    public static String ID = makeID("InnateRelicMod");

    static {
        uiStrings = CardCrawlGame.languagePack.getUIString(ID);
    }

    private final Color iconColour = new Color(1, 1, 1, 1);

    public String modifyDescription(String rawDescription, AbstractCard card) {
        return uiStrings.TEXT[0] + rawDescription;
    }

    public boolean shouldApply(AbstractCard card) {
        return !card.isInnate;
    }

    public void onInitialApplication(AbstractCard card) {
        card.isInnate = true;
    }

    public void onRemove(AbstractCard card) {
        card.isInnate = false;
    }

    public AbstractCardModifier makeCopy() {
        return new InnateRelicMod();
    }

    public String identifier(AbstractCard card) {
        return ID;
    }

    @Override
    public void onRender(AbstractCard card, SpriteBatch sb) {
        iconColour.a = card.transparency;
        ExtraIcons.icon(texDefault)
                .drawColor(iconColour)
                .render(card);
    }

    @Override
    public void onSingleCardViewRender(AbstractCard card, SpriteBatch sb) {
        iconColour.a = card.transparency;
        ExtraIcons.icon(texDefault)
                .drawColor(iconColour)
                .render(card);
    }
}
