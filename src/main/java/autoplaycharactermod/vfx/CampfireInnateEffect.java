package autoplaycharactermod.vfx;

import autoplaycharactermod.relics.MemoryCard;
import autoplaycharactermod.util.InnateRelicMod;
import basemod.cardmods.InnateMod;
import basemod.helpers.CardModifierManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.ScreenShake;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.CampfireUI;
import com.megacrit.cardcrawl.rooms.RestRoom;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardBrieflyEffect;

import static autoplaycharactermod.BasicMod.makeID;

public class CampfireInnateEffect extends AbstractGameEffect {
    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(makeID("InnateRest"));

    public static final String[] TEXT = uiStrings.TEXT;

    private static final float DUR = 1.5F;
    private final Color screenColor = AbstractDungeon.fadeColor.cpy();
    private boolean openedScreen = false;

    public CampfireInnateEffect() {
        this.duration = 1.5F;
        this.screenColor.a = 0.0F;
        AbstractDungeon.overlayMenu.proceedButton.hide();
    }

    public void update() {
        if (!AbstractDungeon.isScreenUp) {
            this.duration -= Gdx.graphics.getDeltaTime();
            updateBlackScreenColor();
        }
        if (!AbstractDungeon.isScreenUp && !AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) {
            for (AbstractCard c : AbstractDungeon.gridSelectScreen.selectedCards) {
                if (!CardModifierManager.hasModifier(c, InnateMod.ID) && !c.isInnate) {
                    CardModifierManager.addModifier(c, new InnateRelicMod());
                    if (AbstractDungeon.player.hasRelic(MemoryCard.ID)) {
                        AbstractDungeon.player.getRelic(MemoryCard.ID).flash();
                        ++AbstractDungeon.player.getRelic(MemoryCard.ID).counter;
                        CardCrawlGame.sound.play("RELIC_DROP_MAGICAL");
                        AbstractDungeon.effectsQueue.add(new ShowCardBrieflyEffect(
                                c.makeStatEquivalentCopy(), Settings.WIDTH * 0.5f, Settings.HEIGHT * 0.5f
                        ));
                        CardCrawlGame.screenShake.shake(ScreenShake.ShakeIntensity.HIGH, ScreenShake.ShakeDur.SHORT, true);
                    }
                }
            }
            AbstractDungeon.gridSelectScreen.selectedCards.clear();
            ((RestRoom) AbstractDungeon.getCurrRoom()).fadeIn();
        }
        if (this.duration < 1.0F && !this.openedScreen) {
            this.openedScreen = true;
            CardGroup cards = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
            for (AbstractCard c : AbstractDungeon.player.masterDeck.group) {
                if (!CardModifierManager.hasModifier(c, InnateRelicMod.ID) && !c.isInnate && !c.inBottleFlame && !c.inBottleLightning && !c.inBottleTornado) {
                    cards.group.add(c);
                }
            }
            AbstractDungeon.gridSelectScreen.open(cards, 1, TEXT[2], false, false, true, true);
        }
        if (this.duration < 0.0F) {
            this.isDone = true;
            if (CampfireUI.hidden) {
                AbstractRoom.waitTimer = 0.0F;
                (AbstractDungeon.getCurrRoom()).phase = AbstractRoom.RoomPhase.COMPLETE;
                ((RestRoom) AbstractDungeon.getCurrRoom()).cutFireSound();
            }
        }
    }

    private void updateBlackScreenColor() {
        if (this.duration > 1.0F) {
            this.screenColor.a = Interpolation.fade.apply(1.0F, 0.0F, (this.duration - 1.0F) * 2.0F);
        } else {
            this.screenColor.a = Interpolation.fade.apply(0.0F, 1.0F, this.duration / 1.5F);
        }
    }

    public void render(SpriteBatch sb) {
        sb.setColor(this.screenColor);
        sb.draw(ImageMaster.WHITE_SQUARE_IMG, 0.0F, 0.0F, Settings.WIDTH, Settings.HEIGHT);
        if (AbstractDungeon.screen == AbstractDungeon.CurrentScreen.GRID)
            AbstractDungeon.gridSelectScreen.render(sb);
    }

    public void dispose() {
    }
}