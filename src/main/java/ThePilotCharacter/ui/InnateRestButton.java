package ThePilotCharacter.ui;

import ThePilotCharacter.ThePilotMod;
import ThePilotCharacter.vfx.CampfireInnateEffect;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.ui.campfire.AbstractCampfireOption;

import static ThePilotCharacter.ThePilotMod.makeID;

public class InnateRestButton extends AbstractCampfireOption {
    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(makeID("InnateRest"));
    public static final String[] TEXT = uiStrings.TEXT;

    public InnateRestButton(boolean active) {
        this.label = TEXT[0];
        this.usable = active;
        this.description = TEXT[1];
        this.img = ImageMaster.loadImage(ThePilotMod.imagePath("button/prepare.png"));
    }

    public void useOption() {
        if (this.usable) {
            CardCrawlGame.sound.playAV("MONSTER_DONU_DEFENSE", 0.3F,0.85f);
            AbstractDungeon.effectList.add(new CampfireInnateEffect());
        }
    }

}