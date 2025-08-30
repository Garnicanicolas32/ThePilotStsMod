package autoplaycharactermod.cards.optionSelection.gambling;

import autoplaycharactermod.cards.BaseCard;
import autoplaycharactermod.cards.traitScavengeCards.GachaPull;
import autoplaycharactermod.util.CardStats;
import basemod.patches.com.megacrit.cardcrawl.dungeons.AbstractDungeon.NoPools;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.UpgradeShineEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardBrieflyEffect;

import java.util.ArrayList;


@NoPools
public class GamblingUpgradeCards extends BaseCard {
    public static final String ID = makeID("GamblingUpgradeCards");
    private static final CardStats info = new CardStats(
            CardColor.COLORLESS,
            CardType.POWER,
            CardRarity.SPECIAL,
            CardTarget.NONE,
            -2 
    );
    private static final int MAGIC = 1;

    public GamblingUpgradeCards() {
        super(ID, info);
        setMagic(MAGIC);
    }

    @Override
    public void use(AbstractPlayer abstractPlayer, AbstractMonster abstractMonster) {
        onChoseThisOption();
    }

    public void onChoseThisOption() {
        ArrayList<AbstractCard> possibleCards = new ArrayList<>();
        for (AbstractCard c : AbstractDungeon.player.masterDeck.group) {
            if (c.canUpgrade() && upgraded) {
                possibleCards.add(c);
            }else if(c.canUpgrade() && !upgraded && (c.hasTag(CardTags.STARTER_STRIKE) || c.hasTag(CardTags.STARTER_DEFEND))){
                possibleCards.add(c);
            }
        }
        for (int i = 0; i < magicNumber; i++) {
            if (!possibleCards.isEmpty()) {
                int choice = AbstractDungeon.miscRng.random(0, possibleCards.size() - 1);
                AbstractCard theCard = possibleCards.get(choice);
                possibleCards.remove(choice);
                theCard.upgrade();
                AbstractDungeon.player.bottledCardUpgradeCheck(theCard);
                AbstractDungeon.effectsQueue.add(new UpgradeShineEffect((float) Settings.WIDTH / 2.0F, (float) Settings.HEIGHT / 2.0F));
                AbstractDungeon.topLevelEffectsQueue.add(new ShowCardBrieflyEffect(theCard.makeStatEquivalentCopy()));
            }
        }
        GachaPull.cardsList.removeIf(c -> c instanceof GamblingUpgradeCards);
    }
}
