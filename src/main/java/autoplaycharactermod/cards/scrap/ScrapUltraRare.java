package autoplaycharactermod.cards.scrap;

import autoplaycharactermod.BasicMod;
import autoplaycharactermod.actions.SfxActionVolume;
import autoplaycharactermod.cards.BaseCard;
import autoplaycharactermod.character.MyCharacter;
import autoplaycharactermod.ui.ConfigPanel;
import autoplaycharactermod.util.CardStats;
import autoplaycharactermod.vfx.EvolveCardEffect;
import autoplaycharactermod.vfx.UltraRareScrapStarsEffect;
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
            MyCharacter.Meta.CARD_COLOR,
            CardType.SKILL,
            CardRarity.SPECIAL,
            CardTarget.SELF,
            0 
    );

    public static final UIStrings uiStringsShow = CardCrawlGame.languagePack.getUIString(makeID("SelectCustomAction"));

    public ScrapUltraRare() {
        super(ID, info);
        returnToHand = true;
        tags.add(BasicMod.CustomTags.NoEnergyText);
        this.setBannerTexture(BasicMod.imagePath("cards/EvolvedBanner.png"), BasicMod.imagePath("cards/EvolvedBanner_p.png"));
        setBackgroundTexture(BasicMod.imagePath("character/cardback/Evolved/bg_skill.png"), BasicMod.imagePath("character/cardback/Evolved/bg_skill_p.png"));
        if (CardCrawlGame.isInARun() && AbstractDungeon.getCurrMapNode() != null && AbstractDungeon.getCurrRoom() != null) {
            BasicMod.evolved = true;
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
    public void upgrade(){
    }

    public boolean canUpgrade() {
        return false;
    }

    @Override
    public boolean freeToPlay() {
        return true;
    }
}
