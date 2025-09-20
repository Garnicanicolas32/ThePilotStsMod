package autoplaycharactermod.cards.optionSelection.traitReward;

import autoplaycharactermod.ThePilotMod;
import autoplaycharactermod.cards.BaseCard;
import autoplaycharactermod.util.CardStats;
import basemod.patches.com.megacrit.cardcrawl.dungeons.AbstractDungeon.NoPools;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.UpgradeShineEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardBrieflyEffect;

import java.util.ArrayList;


@NoPools
public class YellowOptionTwo extends BaseCard {
    public static final String ID = makeID("YellowOptionTwo");
    private static final CardStats info = new CardStats(
            CardColor.COLORLESS,
            CardType.POWER,
            CardRarity.SPECIAL,
            CardTarget.NONE,
            -2 
    );
    private static final int UPG_AMOUNT = 5;

    public YellowOptionTwo() {
        super(ID, info);
        setMagic(UPG_AMOUNT);
        setBackgroundTexture(ThePilotMod.imagePath("character/cardback/bg_evolution4_power.png"), ThePilotMod.imagePath("character/cardback/bg_evolution4_power_p.png"));
        tags.add(ThePilotMod.CustomTags.Evolution);
    }

    @Override
    public void use(AbstractPlayer abstractPlayer, AbstractMonster abstractMonster) {
        onChoseThisOption();
    }

    public void onChoseThisOption() {
        ArrayList<AbstractCard> possibleCards = new ArrayList<>();
        for (AbstractCard c : AbstractDungeon.player.masterDeck.group) {
            if (c.canUpgrade())
                possibleCards.add(c);
        }
        for (int i = 0; i < magicNumber; i++) {
            if (!possibleCards.isEmpty()) {
                int rng = AbstractDungeon.miscRng.random(0, possibleCards.size() - 1);
                AbstractCard theCard = possibleCards.get(rng);
                theCard.upgrade();
                AbstractDungeon.player.bottledCardUpgradeCheck(theCard);
                possibleCards.remove(rng);
                float x = MathUtils.random(0.1F, 0.9F) * (float) Settings.WIDTH;
                float y = MathUtils.random(0.2F, 0.8F) * (float) Settings.HEIGHT;
                AbstractDungeon.effectList.add(new ShowCardBrieflyEffect(theCard.makeStatEquivalentCopy(), x, y));
                AbstractDungeon.topLevelEffects.add(new UpgradeShineEffect(x, y));
            } else
                break;
        }
        ThePilotMod.usedYellowJACKPOT = true;
    }
}
