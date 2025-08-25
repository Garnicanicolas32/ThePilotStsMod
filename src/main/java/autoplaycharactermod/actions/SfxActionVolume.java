package autoplaycharactermod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.AbstractRoom;

public class SfxActionVolume extends AbstractGameAction {
    private String key;
    private float pitch;
    private float volume;


    public SfxActionVolume(String key, float pitch, float volume) {
        this.key = key;
        this.pitch = pitch;
        this.volume = volume;
        this.actionType = AbstractGameAction.ActionType.WAIT;
    }

    public void update() {
        if (CardCrawlGame.isInARun() && AbstractDungeon.currMapNode != null && AbstractDungeon.getCurrRoom() != null
                && AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT
                && !AbstractDungeon.getCurrRoom().monsters.areMonstersBasicallyDead()) {
            try {
                CardCrawlGame.sound.playAV(this.key, this.pitch, this.volume);
            } catch (Exception e) {
            }
        }
        this.isDone = true;
    }
}
