package ThePilotCharacter.ui;

import ThePilotCharacter.ThePilotMod;
import ThePilotCharacter.relics.Schematics;
import ThePilotCharacter.vfx.CampfireRepairEffect;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.ui.campfire.AbstractCampfireOption;

import static ThePilotCharacter.ThePilotMod.makeID;

public class RepairEquipmentButton extends AbstractCampfireOption {
    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(makeID("repairButton"));
    public static final String[] TEXT = uiStrings.TEXT;

    public RepairEquipmentButton(boolean active) {
        this.label = TEXT[0];
        this.usable = active;
        if (active) {
            int repairAmount = AbstractDungeon.player.hasRelic(Schematics.ID) ? 4 : 2;
            this.description = TEXT[1] + repairAmount + TEXT[2];
        } else {
            this.description = TEXT[3];
        }
        this.img = ImageMaster.loadImage(ThePilotMod.imagePath("button/repair.png"));
    }

    public void useOption() {
        if (this.usable) {
            CardCrawlGame.sound.playAV("BLOCK_ATTACK",0.2F,1.2F);
            AbstractDungeon.effectList.add(new CampfireRepairEffect());
        }
    }

}