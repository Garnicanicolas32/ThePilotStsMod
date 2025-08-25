package autoplaycharactermod.patches;

import autoplaycharactermod.BasicMod;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.backends.lwjgl.audio.OpenALAudio;
import com.badlogic.gdx.backends.lwjgl.audio.OpenALMusic;
import com.badlogic.gdx.utils.Array;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import org.lwjgl.BufferUtils;
import org.lwjgl.openal.AL10;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.IntBuffer;

@SpirePatch2(
        clz = OpenALMusic.class,
        method = "play"
)
public class ScrewAudioBuffers {
    private static Method fill;
    private static Field noDeviceField;
    private static java.lang.reflect.Field musicField;
    private static Method obtainSourceMethod;
    static {
        try {
            noDeviceField = OpenALAudio.class.getDeclaredField("noDevice");
            noDeviceField.setAccessible(true);

            musicField = OpenALAudio.class.getDeclaredField("music");
            musicField.setAccessible(true);
        }  catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
        try {
            obtainSourceMethod = OpenALAudio.class.getDeclaredMethod("obtainSource", boolean.class);
            obtainSourceMethod.setAccessible(true);

            fill = OpenALMusic.class.getDeclaredMethod("fill", int.class);
            fill.setAccessible(true);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }
    @SpirePrefixPatch
    public static SpireReturn<?> play(OpenALMusic __instance, @ByRef OpenALAudio[] ___audio, @ByRef int[] ___sourceID, @ByRef IntBuffer[] ___buffers,
                                      @ByRef Music.OnCompletionListener[] ___onCompletionListener,
                                      @ByRef float[] ___pan, @ByRef float[] ___volume, @ByRef boolean[] ___isPlaying) throws InvocationTargetException, IllegalAccessException {
        System.out.println("Using replacement play logic.");
        boolean noDevice = (boolean) noDeviceField.get(___audio[0]);
        if (!noDevice) {
            if (___sourceID[0] == -1) {
                System.out.println("Obtaining source.");
                int sourceId = (int) obtainSourceMethod.invoke(___audio[0], true);
                ___sourceID[0] = sourceId;

                if (___sourceID[0] == -1) {
                    System.out.println("Failed to obtain source.");
                    return SpireReturn.Return();
                }

                int errorCode = AL10.alGetError();
                if (errorCode != 0) {
                    System.out.println("Residual AL Error: " + errorCode);
                }

                if (___buffers[0] == null) {
                    System.out.println("Prepping buffers.");
                    ___buffers[0] = BufferUtils.createIntBuffer(3);
                    AL10.alGenBuffers(___buffers[0]);
                    errorCode = AL10.alGetError();
                    if (errorCode != 0) {
                        if (errorCode == AL10.AL_INVALID_VALUE) {
                            System.out.println("Unable to allocate audio buffers; buffer array too small to hold request number of buffers. AL Error: " + errorCode);
                        }
                        else if (errorCode == AL10.AL_OUT_OF_MEMORY) {
                            System.out.println("Unable to allocate audio buffers; not enough memory available to generate requested buffers. AL Error: " + errorCode);
                        }
                        else {
                            System.out.println("Unable to allocate audio buffers. AL Error: " + errorCode);
                        }
                        return SpireReturn.Return();
                    }
                }
                @SuppressWarnings("unchecked")
                Array<OpenALMusic> musicArray = (Array<OpenALMusic>) musicField.get(___audio[0]);
                musicArray.add(__instance);

                AL10.alSourcei(___sourceID[0], AL10.AL_LOOPING, 0);
                __instance.setPan(___pan[0], ___volume[0]);
                boolean filled = false;

                for(int i = 0; i < 3; ++i) {
                    int bufferID = ___buffers[0].get(i);
                    if (!((Boolean) fill.invoke(__instance, bufferID))) {
                        break;
                    }
                    filled = true;
                    AL10.alSourceQueueBuffers(___sourceID[0], bufferID);
                }

                if (!filled && ___onCompletionListener[0] != null) {
                    ___onCompletionListener[0].onCompletion(__instance);
                }

                if (AL10.alGetError() != 0) {
                    __instance.stop();
                    return SpireReturn.Return();
                }
            }

            if (!___isPlaying[0]) {
                AL10.alSourcePlay(___sourceID[0]);
                ___isPlaying[0] = true;
            }
        }
        return SpireReturn.Return();
    }
}