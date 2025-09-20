package autoplaycharactermod.actions;

import autoplaycharactermod.ThePilotMod;
import autoplaycharactermod.cards.equipment.TeslaCoil;
import autoplaycharactermod.ui.DurabilityTutorial;
import autoplaycharactermod.ui.PilotTutorials;
import autoplaycharactermod.ui.TraitTutorials;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import java.io.IOException;

public class TutorialCaller extends AbstractGameAction {
    private float startingDuration;
    public int code;

    public TutorialCaller(int code) {
        this.startingDuration = Settings.ACTION_DUR_FAST;
        this.duration = this.startingDuration;
        this.code = code;
    }

    public void update() {
        if (ThePilotMod.unseenTutorials[code]) {
            switch (code){
                case 0:
                    AbstractDungeon.ftue = new PilotTutorials();
                    break;
                case 1:
                    AbstractDungeon.ftue = new TraitTutorials();
                    break;
                case 2:
                    AbstractDungeon.ftue = new DurabilityTutorial(new TeslaCoil());
                    break;
            }
            ThePilotMod.unseenTutorials[code] = false;
            try {
                ThePilotMod.saveTutorialsSeen();
                this.isDone = true;
            } catch (IOException e) {
                e.printStackTrace();
                this.isDone = true;
            }
        }else {
            this.isDone = true;
        }
    }
}