package ThePilotCharacter.ui;

import ThePilotCharacter.ThePilotMod;
import basemod.ReflectionHacks;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.TipHelper;
import com.megacrit.cardcrawl.helpers.controller.CInputActionSet;
import com.megacrit.cardcrawl.localization.TutorialStrings;
import com.megacrit.cardcrawl.ui.FtueTip;
import com.megacrit.cardcrawl.ui.buttons.GotItButton;

import java.util.ArrayList;

public class DurabilityTutorial extends FtueTip {
    private static final TutorialStrings tutorialStrings = CardCrawlGame.languagePack.getTutorialString(ThePilotMod.makeID("DurabilityTutorials"));
    public static final String[] MSG = tutorialStrings.TEXT;
    public static final String[] LABEL = tutorialStrings.LABEL;
    public static final String header = ThePilotMod.keywords.get("Equipment").PROPER_NAME;
    public static final String body = ThePilotMod.keywords.get("Equipment").DESCRIPTION;
    private final GotItButton button;
    private AbstractCard cardUsed;
    private final ArrayList<String> emptyKeywords = new ArrayList<>();
    public DurabilityTutorial(AbstractCard card) {
        super(LABEL[0], MSG[0], Settings.WIDTH / 2.0f + (320.0f * Settings.scale), Settings.HEIGHT / 2.0f - 135f * Settings.scale , card);
        cardUsed = (ReflectionHacks.getPrivateInherited(this, DurabilityTutorial.class, "c"));
        button = (ReflectionHacks.getPrivateInherited(this, DurabilityTutorial.class, "button"));
    }

    @Override
    public void update() {
        this.button.update();
        if (button.hb.clicked || CInputActionSet.proceed.isJustPressed()) {
            super.update();
            AbstractDungeon.effectList.clear();
        }
    }

    @Override
    public void render(SpriteBatch sb){
        super.render(sb);
        TipHelper.renderGenericTip(Settings.WIDTH / 2.0f + (25.0f * Settings.scale), Settings.HEIGHT / 2f + (150f * Settings.scale), header, body);
    }
}

