package autoplaycharactermod.vfx;

import autoplaycharactermod.cards.EquipmentCard;
import autoplaycharactermod.relics.Schematics;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.CampfireUI;
import com.megacrit.cardcrawl.rooms.RestRoom;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardBrieflyEffect;

import static autoplaycharactermod.BasicMod.makeID;

public class CampfireRepairEffect extends AbstractGameEffect {
    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(makeID("repairButton"));

    public static final String[] TEXT = uiStrings.TEXT;

    private static final float DUR = 1.5F;
    private final Color screenColor = AbstractDungeon.fadeColor.cpy();
    private boolean openedScreen = false;
    private boolean hasRelic = false;

    public CampfireRepairEffect() {
        this.duration = 1.5F;
        this.screenColor.a = 0.0F;
        AbstractDungeon.overlayMenu.proceedButton.hide();
    }

    public void update() {
        if (!AbstractDungeon.isScreenUp) {
            this.duration -= Gdx.graphics.getDeltaTime();
            updateBlackScreenColor();
            hasRelic = AbstractDungeon.player.hasRelic(Schematics.ID);
        }
        if (!AbstractDungeon.isScreenUp && !AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) {
            AbstractDungeon.overlayMenu.cancelButton.hide();

            float w = AbstractCard.IMG_WIDTH + 30f * Settings.scale;
            float startX = Settings.WIDTH * 0.5f - (w * (AbstractDungeon.gridSelectScreen.selectedCards.size() - 1)) / 2f;
            int count = 0;
            for (AbstractCard c : AbstractDungeon.gridSelectScreen.selectedCards) {
                if (c instanceof EquipmentCard) {
                    ((EquipmentCard) c).equipmentHp = ((EquipmentCard) c).equipmentMaxHp;

                    CardCrawlGame.sound.playAV("CARD_UPGRADE", 0.2f, 1.0F);
                    CardCrawlGame.sound.playAV("BLOCK_ATTACK", MathUtils.random(-0.2F, 0.2F), 0.5F);
                    AbstractDungeon.effectsQueue.add(new ShowCardBrieflyEffect(
                            c.makeStatEquivalentCopy(), startX + count++ * w, Settings.HEIGHT * 0.5f
                    ));
                }
            }
            AbstractDungeon.gridSelectScreen.selectedCards.clear();
            ((RestRoom) AbstractDungeon.getCurrRoom()).fadeIn();
        }
        if (this.duration < 1.0F && !this.openedScreen) {
            this.openedScreen = true;
            CardGroup cards = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
            for (AbstractCard c : AbstractDungeon.player.masterDeck.group) {
                if (c instanceof EquipmentCard) {
                    EquipmentCard base = (EquipmentCard) c;
                    if (base.equipmentHp < base.equipmentMaxHp) {
                        cards.group.add(c);
                    }
                }
            }
            AbstractDungeon.gridSelectScreen.open(cards, Math.min(cards.size(), hasRelic ? 4 : 2), TEXT[2], false, false, true, true);
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