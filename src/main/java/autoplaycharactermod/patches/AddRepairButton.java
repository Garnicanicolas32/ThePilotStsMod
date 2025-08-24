package autoplaycharactermod.patches;

import autoplaycharactermod.cards.EquipmentCard;
import autoplaycharactermod.character.MyCharacter;
import autoplaycharactermod.ui.RepairEquipmentButton;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.CampfireUI;
import com.megacrit.cardcrawl.ui.campfire.AbstractCampfireOption;
import javassist.CtBehavior;

import java.util.ArrayList;

@SpirePatch(
        clz = CampfireUI.class,
        method = "initializeButtons"
)
public class AddRepairButton {
    @SpireInsertPatch(
            locator = Locator.class
    )
    public static void Insert(CampfireUI __instance, @ByRef ArrayList<AbstractCampfireOption>[] ___buttons) {
        boolean hasRepairableEquipment = AbstractDungeon.player.masterDeck.group.stream()
                .filter(c -> c instanceof EquipmentCard)
                .anyMatch(c -> ((EquipmentCard) c).equipmentHp < ((EquipmentCard) c).equipmentMaxHp);

        if (hasRepairableEquipment || AbstractDungeon.player instanceof MyCharacter) {
            ___buttons[0].add(new RepairEquipmentButton(hasRepairableEquipment));
        }
    }

    private static class Locator extends SpireInsertLocator {
        @Override
        public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
            Matcher matcher = new Matcher.FieldAccessMatcher(AbstractPlayer.class, "relics");
            return LineFinder.findInOrder(ctMethodToPatch, matcher);
        }
    }
}

