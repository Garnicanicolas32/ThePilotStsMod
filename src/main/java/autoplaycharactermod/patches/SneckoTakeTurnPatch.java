package autoplaycharactermod.patches;

import autoplaycharactermod.character.PilotCharacter;
import autoplaycharactermod.powers.HackedPower;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.city.Snecko;
import com.megacrit.cardcrawl.powers.ConfusionPower;
import javassist.CannotCompileException;
import javassist.expr.ExprEditor;
import javassist.expr.NewExpr;

@SpirePatch(
        clz = Snecko.class,
        method = "takeTurn"
)
public class SneckoTakeTurnPatch {
    public static ExprEditor Instrument() {
        return new ExprEditor() {
            @Override
            public void edit(NewExpr e) throws CannotCompileException {
                if (e.getClassName().equals(ConfusionPower.class.getName())) {
                    e.replace(
                            "{ " +
                                    "  if (" + SneckoTakeTurnPatch.class.getName() + ".isMyCharacter()) { " +
                                    "    $_ = new " + HackedPower.class.getName() + "($1); " +
                                    "  } else { " +
                                    "    $_ = new " + ConfusionPower.class.getName() + "($1); " +
                                    "  } " +
                                    "}"
                    );
                }
            }
        };
    }

    public static boolean isMyCharacter() {
        return AbstractDungeon.player instanceof PilotCharacter;
    }
}

