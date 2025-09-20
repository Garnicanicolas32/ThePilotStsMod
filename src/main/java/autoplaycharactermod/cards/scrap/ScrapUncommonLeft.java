package autoplaycharactermod.cards.scrap;

import autoplaycharactermod.ThePilotMod;
import autoplaycharactermod.cards.BaseCard;
import autoplaycharactermod.util.CardStats;
import autoplaycharactermod.vfx.EvolveCardEffect;
import com.evacipated.cardcrawl.mod.stslib.fields.cards.AbstractCard.FleetingField;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Objects;

public class ScrapUncommonLeft extends BaseCard {
    public static final String ID = makeID("ScrapUncommonLeft");
    private static final CardStats info = new CardStats(
            CardColor.COLORLESS,
            CardType.SKILL,
            CardRarity.SPECIAL,
            CardTarget.SELF,
            -2 
    );

    public ScrapUncommonLeft() {
        super(ID, info);
        FleetingField.fleeting.set(this, true);
        this.setBannerTexture(ThePilotMod.imagePath("cards/EvolvedBanner.png"), ThePilotMod.imagePath("cards/EvolvedBanner_p.png"));
        setBackgroundTexture(ThePilotMod.imagePath("character/cardback/Evolved/bg_skill.png"), ThePilotMod.imagePath("character/cardback/Evolved/bg_skill_p.png"));
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        ArrayList<AbstractCard> possibleCards = new ArrayList<>();
        for (AbstractCard c : p.masterDeck.group){
            if (c instanceof BaseCard && c.rarity == CardRarity.UNCOMMON && !((BaseCard) c).alreadyEvolved){
                possibleCards.add(c);
            }
        }
        if (!possibleCards.isEmpty()) {
            Collections.shuffle(possibleCards, AbstractDungeon.cardRandomRng.random);
            AbstractCard card = possibleCards.get(0);
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
