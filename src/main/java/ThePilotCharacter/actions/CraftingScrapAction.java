package ThePilotCharacter.actions;

import ThePilotCharacter.ThePilotMod;
import ThePilotCharacter.cards.scrap.*;
import ThePilotCharacter.ui.ConfigPanel;
import ThePilotCharacter.vfx.OmegaGroupEffect;
import ThePilotCharacter.vfx.ScrapUpgradeEffect;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDiscardAction;
import com.megacrit.cardcrawl.actions.unique.AddCardToDeckAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import java.util.ArrayList;

public class CraftingScrapAction extends AbstractGameAction {
    private final AbstractCard.CardTags customTag;
    private final int option;

    public CraftingScrapAction(AbstractCard.CardTags customTag, int option) {
        this.actionType = ActionType.SPECIAL;
        this.duration = Settings.ACTION_DUR_FAST;
        this.customTag = customTag;
        this.option = option;
    }

    @Override
    public void update() {
        if (this.duration == Settings.ACTION_DUR_FAST) {
            int countStrike = 0;
            int countUpgrade = 0;
            ArrayList<AbstractCard> scrapsToRemove = new ArrayList<>();
            int found = 0;
            for (AbstractCard c : AbstractDungeon.player.exhaustPile.group) {
                if (c.hasTag(customTag) && found < 3) {
                    scrapsToRemove.add(c);
                    found++;
                }
            }
            for (AbstractCard c : scrapsToRemove) {
                if (c.type == AbstractCard.CardType.ATTACK)
                    countStrike++;
                if (c.upgraded)
                    countUpgrade++;
                AbstractDungeon.player.masterDeck.group.removeIf(card -> card.uuid.equals(c.uuid));
                AbstractDungeon.player.exhaustPile.removeCard(c);
            }
            AbstractCard c = null;
            switch (option) {
                case 1:
                    c = countStrike > 1 ? new ScrapUncommonAttStr() : new ScrapUncommonDefDex();
                    break;
                case 2:
                    c = countStrike > 1 ? new ScrapRareAtt() : new ScrapRareDef();
                    break;
                case 3:
                    c = new ScrapUltraRare();
                    break;
            }
            if (c != null) {
                ThePilotMod.fusionsmade++;
                if (countUpgrade > 1)
                    c.upgrade();

                if (c instanceof ScrapUltraRare) {
                    AbstractDungeon.effectsQueue.add(new OmegaGroupEffect(1, 27));
                    addToTop(new AbstractGameAction() {
                        @Override
                        public void update() {
                            replaceLeftovers();
                            this.isDone = true;
                        }
                    });
                    AbstractDungeon.player.masterDeck.addToBottom(new ScrapCommonLeft());
                    AbstractDungeon.player.masterDeck.addToBottom(new ScrapUncommonLeft());
                    AbstractDungeon.player.masterDeck.addToBottom(new ScrapRareLeft());
                } else {
                    addToTop(new MakeTempCardInDiscardAction(c, true));
                    addToTop(new AddCardToDeckAction(c));
                    if (!ConfigPanel.lessParticles)
                        addToTop(new VFXAction(new ScrapUpgradeEffect((float) Settings.WIDTH / 2.0F, (float) Settings.HEIGHT / 2.0F, option)));
                    addToTop(new SfxActionVolume("CARD_UPGRADE", 0.4F, 0.6F));
                }
            }

            this.isDone = true;
        }
    }

    public void replaceLeftovers() {
        ArrayList<AbstractCard> toRemove = new ArrayList<>();
        for (AbstractCard c : AbstractDungeon.player.masterDeck.group) {
            if (c.hasTag(ThePilotMod.CustomTags.ScrapCommon) || c.hasTag(ThePilotMod.CustomTags.ScrapUncommon) || c.hasTag(ThePilotMod.CustomTags.ScrapRare)) {
                toRemove.add(c);
            }
        }
        for (AbstractCard c : toRemove) {
            AbstractCard replace;
            if (c.hasTag(ThePilotMod.CustomTags.ScrapRare))
                replace = new ScrapRareLeft();
            else if (c.hasTag(ThePilotMod.CustomTags.ScrapUncommon))
                replace = new ScrapUncommonLeft();
            else
                replace = new ScrapCommonLeft();
            AbstractDungeon.player.masterDeck.addToTop(replace);
            AbstractDungeon.player.discardPile.addToTop(replace.makeSameInstanceOf());
            AbstractDungeon.player.masterDeck.group.removeIf(card -> card.uuid.equals(c.uuid));
        }
        AbstractDungeon.player.discardPile.group.removeIf(card -> card.hasTag(ThePilotMod.CustomTags.ScrapCommon) || card.hasTag(ThePilotMod.CustomTags.ScrapUncommon) || card.hasTag(ThePilotMod.CustomTags.ScrapRare));
        AbstractDungeon.player.drawPile.group.removeIf(card -> card.hasTag(ThePilotMod.CustomTags.ScrapCommon) || card.hasTag(ThePilotMod.CustomTags.ScrapUncommon) || card.hasTag(ThePilotMod.CustomTags.ScrapRare));
        AbstractDungeon.player.hand.group.removeIf(card -> card.hasTag(ThePilotMod.CustomTags.ScrapCommon) || card.hasTag(ThePilotMod.CustomTags.ScrapUncommon) || card.hasTag(ThePilotMod.CustomTags.ScrapRare));
    }

}
