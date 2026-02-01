package ThePilotCharacter.patches;

import ThePilotCharacter.character.PilotCharacter;
import ThePilotCharacter.vfx.PilotEndingScreen;
import ThePilotCharacter.vfx.PilotEyeScreen;
import com.badlogic.gdx.Gdx;
import com.evacipated.cardcrawl.modthespire.lib.ByRef;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.screens.VictoryScreen;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.scene.SlowFireParticleEffect;

import java.util.ArrayList;

@SpirePatch(
        clz = VictoryScreen.class,
        method = "updateVfx"
)
public class VictoryScreenUpdateVfxPatch {

    @SpirePostfixPatch
    public static void Postfix(VictoryScreen __instance, @ByRef ArrayList<AbstractGameEffect>[] ___effect, @ByRef float[] ___effectTimer) {
        if (AbstractDungeon.player instanceof PilotCharacter) {
            ___effectTimer[0] -= Gdx.graphics.getDeltaTime();
            if (___effectTimer[0] < 0.0F) {
                ___effect[0].add(new SlowFireParticleEffect());
                ___effect[0].add(new SlowFireParticleEffect());
                ___effectTimer[0] = 0.1F;
            }

            boolean foundEyeVfx = false;
            int amount = 0;
            for(AbstractGameEffect e : ___effect[0]) {
                if (e instanceof PilotEyeScreen) {
                    foundEyeVfx = true;
                } else if (e instanceof PilotEndingScreen) {
                    amount++;
                }
            }

            if (!foundEyeVfx) {
                ___effect[0].add(new PilotEyeScreen());
            }

            if (amount < 15) {
                ___effect[0].add(new PilotEndingScreen());
            }
        }
    }
}


