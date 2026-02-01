package ThePilotCharacter.ui;

import ThePilotCharacter.ThePilotMod;
import basemod.EasyConfigPanel;

public class ConfigPanel extends EasyConfigPanel {
    public static boolean lessParticles = false;
    public static boolean experimentalSounds = false;
    public static int debugScry = 4;

    public ConfigPanel() {
        super(ThePilotMod.modID, ThePilotMod.makeID("ConfigPanel"));
        setNumberRange("debugScry", 1, 6);
    }
}
