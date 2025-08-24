package autoplaycharactermod.patches;

import autoplaycharactermod.character.MyCharacter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.RelicLibrary;
import com.megacrit.cardcrawl.screens.CharSelectInfo;
import com.megacrit.cardcrawl.screens.charSelect.CharacterOption;

@SpirePatch(
        clz = CharacterOption.class,
        method = "renderRelics",
        paramtypez = {SpriteBatch.class}
)
public class SelectScreenRelicPatch {
    @SpirePrefixPatch
    public static SpireReturn<Void> Prefix(CharacterOption __instance, SpriteBatch sb, CharSelectInfo ___charInfo, float ___infoX, float ___infoY) {
        if (___charInfo.player instanceof MyCharacter) {
            sb.setColor(Settings.QUARTER_TRANSPARENT_BLACK_COLOR);
            float var10002 = ___infoX - 52.0F * Settings.scale;
            sb.draw(RelicLibrary.getRelic((String)___charInfo.relics.get(0)).outlineImg, var10002, ___infoY - 60.0F * Settings.scale - 64.0F, 64.0F, 64.0F, 128.0F, 128.0F, Settings.scale, Settings.scale, 0.0F, 0, 0, 128, 128, false, false);
            sb.draw(RelicLibrary.getRelic((String)___charInfo.relics.get(1)).outlineImg, var10002 -9f * Settings.scale, ___infoY - 150.0F * Settings.scale - 64.0F, 64.0F, 64.0F, 128.0F, 128.0F, Settings.scale, Settings.scale, 0.0F, 0, 0, 128, 128, false, false);

            sb.setColor(Color.WHITE);
            var10002 = ___infoX - 56.0F * Settings.scale;
            sb.draw(RelicLibrary.getRelic((String)___charInfo.relics.get(0)).img, var10002, ___infoY - 60.0F * Settings.scale - 64.0F, 64.0F, 64.0F, 128.0F, 128.0F, Settings.scale, Settings.scale, 0.0F, 0, 0, 128, 128, false, false);
            sb.draw(RelicLibrary.getRelic((String)___charInfo.relics.get(1)).img, var10002 - 9f * Settings.scale, ___infoY - 150.0F * Settings.scale - 64.0F, 64.0F, 64.0F, 128.0F, 128.0F, Settings.scale, Settings.scale, 0.0F, 0, 0, 128, 128, false, false);

            float var10003 = ___infoX + 44.0F * Settings.scale;
            FontHelper.renderSmartText(sb, FontHelper.tipHeaderFont, RelicLibrary.getRelic((String)___charInfo.relics.get(0)).name, var10003, ___infoY - 40.0F * Settings.scale, 10000.0F, 10000.0F, Settings.GOLD_COLOR);
            FontHelper.renderSmartText(sb, FontHelper.tipHeaderFont, RelicLibrary.getRelic((String)___charInfo.relics.get(1)).name, var10003, ___infoY - 135.0F * Settings.scale, 10000.0F, 10000.0F, Settings.GOLD_COLOR);

            String relicString = RelicLibrary.getRelic((String)___charInfo.relics.get(0)).description;
            String relicString2 = RelicLibrary.getRelic((String)___charInfo.relics.get(1)).DESCRIPTIONS[13];
            FontHelper.renderSmartText(sb, FontHelper.tipBodyFont, relicString, ___infoX + 44.0F * Settings.scale, ___infoY - 66.0F * Settings.scale, 10000.0F, 10000.0F, Settings.CREAM_COLOR);
            FontHelper.renderSmartText(sb, FontHelper.tipBodyFont, relicString2, ___infoX + 44.0F * Settings.scale, ___infoY - 162.0F * Settings.scale, 10000.0F, 10000.0F, Settings.CREAM_COLOR);
            return SpireReturn.Return();
        } else
            return SpireReturn.Continue();
    }

}
