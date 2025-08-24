package autoplaycharactermod.cards.traitScavengeCards;

import autoplaycharactermod.cards.TraitCard;
import autoplaycharactermod.cards.optionSelection.Gambling.*;
import autoplaycharactermod.character.MyCharacter;
import autoplaycharactermod.ui.ConfigPanel;
import autoplaycharactermod.util.CardStats;
import autoplaycharactermod.vfx.GachaPullEffect;
import com.badlogic.gdx.Gdx;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.actions.watcher.ChooseOneAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.ThoughtBubble;

import java.util.ArrayList;
import java.util.Collections;

public class GachaPull extends TraitCard {
    public static final String ID = makeID("GachaPull");
    private static final CardStats info = new CardStats(
            MyCharacter.Meta.CARD_COLOR,
            CardType.SKILL,
            CardRarity.RARE,
            CardTarget.NONE,
            -2 
    );
    private static final int MAGIC = 2;
    private static final int UPG_MAGIC = 1;
    private static final int COST = 20;
    private static final int UPG_COST = 10;
    public static ArrayList<AbstractCard> cardsList = new ArrayList<>();
    private float rotationTimer;
    private int previewIndex;


    public GachaPull() {
        super(ID, info, TraitCard.TraitColor.SCAVENGE, false);
        setMagic(MAGIC, UPG_MAGIC);
        tags.add(CardTags.HEALING);
        setCustomVar("COST", COST, UPG_COST);
        resetPool();
        checkEvolve();
    }

    public static void resetPool() {
        cardsList.clear();
        cardsList.add(new GamblingWeak());
        cardsList.add(new GamblingFrail());
        cardsList.add(new GamblingGainCurse());
        cardsList.add(new GamblingLoseEnergy());
        cardsList.add(new GamblingLoseGold());
        cardsList.add(new GamblingLoseHP());
        cardsList.add(new GamblingVulnerable());
        cardsList.add(new GamblingNoBlock());

        cardsList.add(new GamblingScrap());
        cardsList.add(new GamblingBlock());
        cardsList.add(new GamblingBuffer());
        cardsList.add(new GamblingDamage());
        cardsList.add(new GamblingCardReward());
        cardsList.add(new GamblingCreateScavenge());
        cardsList.add(new GamblingDexterity());
        cardsList.add(new GamblingEnergy());
        cardsList.add(new GamblingGold());
        cardsList.add(new GamblingHeal());
        cardsList.add(new GamblingPlatedArmor());
        cardsList.add(new GamblingPotion());
        cardsList.add(new GamblingRelic());
        cardsList.add(new GamblingScry());
        cardsList.add(new GamblingStrength());
        cardsList.add(new GamblingStun());
        cardsList.add(new GamblingUpgradeCards());
        for (AbstractCard c : cardsList){
            c.upgrade();
        }
    }

    private static ArrayList<AbstractCard> evolvedlist() {
        ArrayList<AbstractCard> listreturn = new ArrayList<AbstractCard>();
        listreturn.add(new GamblingScrap());
        listreturn.add(new GamblingBlock());
        listreturn.add(new GamblingBuffer());
        listreturn.add(new GamblingDamage());
        listreturn.add(new GamblingCardReward());
        listreturn.add(new GamblingCreateScavenge());
        listreturn.add(new GamblingDexterity());
        listreturn.add(new GamblingEnergy());
        listreturn.add(new GamblingGold());
        listreturn.add(new GamblingHeal());
        listreturn.add(new GamblingPlatedArmor());
        listreturn.add(new GamblingPotion());
        listreturn.add(new GamblingRelic());
        listreturn.add(new GamblingScry());
        listreturn.add(new GamblingStrength());
        listreturn.add(new GamblingStun());
        listreturn.add(new GamblingUpgradeCards());
        for (AbstractCard c : listreturn){
            c.upgrade();
        }
        return listreturn;
    }


    @Override
    public void evolveCard() {
        setMagic(3);
        super.evolveCard();
    }

    @Override
    public void update() {
        super.update();
        if (hb.hovered) {
            if (rotationTimer <= 0F) {
                rotationTimer = 1F;
                if (cardsList.isEmpty()) {
                    cardsToPreview = CardLibrary.cards.get("Madness");
                } else {
                    cardsToPreview = cardsList.get(previewIndex);
                }
                if (previewIndex == cardsList.size() - 1) {
                    previewIndex = 0;
                } else {
                    previewIndex++;
                }
            } else {
                rotationTimer -= Gdx.graphics.getDeltaTime();
            }
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (p.gold < customVar("COST") && !this.alreadyEvolved) {
            AbstractDungeon.effectList.add(new ThoughtBubble(AbstractDungeon.player.dialogX, AbstractDungeon.player.dialogY, 3.0F, cardStrings.EXTENDED_DESCRIPTION[1], true));
        } else if (!cardsList.isEmpty() && !this.alreadyEvolved) {
            Collections.shuffle(cardsList, AbstractDungeon.cardRandomRng.random);
            ArrayList<AbstractCard> stanceChoices = new ArrayList<>();
            for (int i = 0; i < magicNumber && i < cardsList.size(); i++) {
                AbstractCard option = cardsList.get(i).makeCopy();
                stanceChoices.add(option);
            }
            if (!stanceChoices.isEmpty()) {
                p.loseGold(customVar("COST"));
                addToBot(new SFXAction("UNLOCK_PING"));
                AbstractDungeon.effectList.add(new GachaPullEffect(ConfigPanel.lessParticles ? 50 : 100));
                addToBot(new ChooseOneAction(stanceChoices));
            }
        } else if (alreadyEvolved) {
            ArrayList<AbstractCard> possiblecards = evolvedlist();
            Collections.shuffle(possiblecards, AbstractDungeon.cardRandomRng.random);
            ArrayList<AbstractCard> stanceChoices = new ArrayList<>();
            for (int i = 0; i < magicNumber && i < possiblecards.size(); i++) {
                AbstractCard option = possiblecards.get(i).makeCopy();
                stanceChoices.add(option);
            }
            if (!stanceChoices.isEmpty()) {
                addToBot(new SFXAction("UNLOCK_PING"));
                AbstractDungeon.effectList.add(new GachaPullEffect(ConfigPanel.lessParticles ? 50 : 250));
                addToBot(new ChooseOneAction(stanceChoices));
            }
        } else {
            AbstractDungeon.effectList.add(new ThoughtBubble(AbstractDungeon.player.dialogX, AbstractDungeon.player.dialogY, 3.0F, cardStrings.EXTENDED_DESCRIPTION[2], true));
        }
        PlayOnce = false;
        super.use(p, m);
    }
}
