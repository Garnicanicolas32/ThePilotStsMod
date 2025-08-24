package autoplaycharactermod.relics.Reworks;

import autoplaycharactermod.character.MyCharacter;
import autoplaycharactermod.relics.BaseRelic;
import basemod.BaseMod;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DiscardAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.AbstractRoom;

import static autoplaycharactermod.BasicMod.makeID;

public class UnceasingBottom extends BaseRelic {
    private static final String NAME = "UnceasingBottom"; //The name will be used for determining the image file as well as the ID.
    public static final String ID = makeID(NAME); //This adds the mod's prefix to the relic ID, resulting in modID:MyRelic
    private static final RelicTier RARITY = RelicTier.RARE; //The relic's rarity.
    private static final LandingSound SOUND = LandingSound.CLINK; //The sound played when the relic is clicked.
    boolean lockTrigger = false;

    public UnceasingBottom() {
        super(ID, NAME, MyCharacter.Meta.CARD_COLOR, RARITY, SOUND);
    }

    @Override
    public void update() {
        super.update();
        if (lockTrigger)
            return;
        if (!isObtained || AbstractDungeon.getCurrRoom() == null || AbstractDungeon.getCurrRoom().phase != AbstractRoom.RoomPhase.COMBAT || AbstractDungeon.actionManager.turnHasEnded) {
            return;
        }
        if (AbstractDungeon.player.hand.size() >= BaseMod.MAX_HAND_SIZE) {
            lockTrigger = true;
            addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));
            addToBot(new DiscardAction(AbstractDungeon.player, AbstractDungeon.player, 1, false));
            AbstractDungeon.actionManager.addToBottom(new AbstractGameAction() {
                @Override
                public void update() {
                    lockTrigger = false;
                    this.isDone = true;
                }
            });
        }
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }
}
