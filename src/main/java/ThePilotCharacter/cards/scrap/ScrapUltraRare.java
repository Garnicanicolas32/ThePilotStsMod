package ThePilotCharacter.cards.scrap;

import ThePilotCharacter.ThePilotMod;
import ThePilotCharacter.actions.SfxActionVolume;
import ThePilotCharacter.cards.BaseCard;
import ThePilotCharacter.character.PilotCharacter;
import ThePilotCharacter.ui.ConfigPanel;
import ThePilotCharacter.util.CardStats;
import ThePilotCharacter.vfx.EvolveCardEffect;
import ThePilotCharacter.vfx.UltraRareScrapStarsEffect;
import basemod.patches.com.megacrit.cardcrawl.dungeons.AbstractDungeon.NoPools;
import com.evacipated.cardcrawl.mod.stslib.actions.common.SelectCardsAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.Arrays;
import java.util.Objects;

@NoPools
public class ScrapUltraRare extends BaseCard {
    public static final String ID = makeID("ScrapUltraRare");
    private static final CardStats info = new CardStats(
            PilotCharacter.Meta.CARD_COLOR,
            CardType.SKILL,
            CardRarity.SPECIAL,
            CardTarget.SELF,
            0
    );

    public static final UIStrings uiStringsShow = CardCrawlGame.languagePack.getUIString(makeID("SelectCustomAction"));

    public ScrapUltraRare() {
        super(ID, info);
        returnToHand = true;
        tags.add(ThePilotMod.CustomTags.NoEnergyText);
        this.setBannerTexture(ThePilotMod.imagePath("cards/EvolvedBanner.png"), ThePilotMod.imagePath("cards/EvolvedBanner_p.png"));
        setBackgroundTexture(ThePilotMod.imagePath("character/cardback/Evolved/bg_skill.png"), ThePilotMod.imagePath("character/cardback/Evolved/bg_skill_p.png"));
        if (CardCrawlGame.isInARun() && AbstractDungeon.getCurrMapNode() != null && AbstractDungeon.getCurrRoom() != null) {
            ThePilotMod.evolved = true;
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (PlayOnce) {
            PlayOnce = false;
            returnToHand = true;
            addToBot(new SfxActionVolume("HEART_BEAT", 0.2f, 0.8f));
            for (int i = 0; i < (ConfigPanel.lessParticles ? 15 : 30); ++i) {
                AbstractDungeon.effectList.add(new UltraRareScrapStarsEffect());
            }
        } else {
            addToBot(new SelectCardsAction(p.masterDeck.group, 1, uiStringsShow.TEXT[1], false, c -> !(c instanceof ScrapUltraRare) && c instanceof BaseCard
                    && c.rarity != CardRarity.SPECIAL && c.rarity != CardRarity.CURSE
                    && !((BaseCard) c).alreadyEvolved, cards -> {
                if (!cards.isEmpty()) {
                    for (AbstractCard card : cards) {
                        addToBot(new VFXAction(new EvolveCardEffect(card, Settings.WIDTH / 2f, Settings.HEIGHT / 2f)));
                        ((BaseCard) card).evolveCard();
                        for (CardGroup group : Arrays.asList(
                                AbstractDungeon.player.hand,
                                AbstractDungeon.player.drawPile,
                                AbstractDungeon.player.discardPile,
                                AbstractDungeon.player.masterDeck)) {
                            if (group != null) {
                                for (AbstractCard ca : group.group) {
                                    if (Objects.equals(ca.cardID, card.cardID)) {
                                        ((BaseCard) ca).evolveCard();
                                    }
                                }
                            }
                        }
                    }
                }
            }));
            this.fadingOut = true;
            setExhaust(true);
            returnToHand = false;
        }
    }

    @Override
    public void upgrade() {
    }

    public boolean canUpgrade() {
        return false;
    }

    @Override
    public boolean freeToPlay() {
        return true;
    }
}
