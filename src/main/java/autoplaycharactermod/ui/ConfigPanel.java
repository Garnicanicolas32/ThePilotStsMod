package autoplaycharactermod.ui;

import autoplaycharactermod.BasicMod;
import basemod.EasyConfigPanel;

public class ConfigPanel extends EasyConfigPanel {
    public static boolean lessParticles = false;

    public ConfigPanel() {
        super(BasicMod.modID, BasicMod.makeID("ConfigPanel"));

    }
}
