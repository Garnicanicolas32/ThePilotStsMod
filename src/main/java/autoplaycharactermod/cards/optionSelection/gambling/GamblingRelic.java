package autoplaycharactermod.cards.optionSelection.gambling;

import autoplaycharactermod.BasicMod;
import autoplaycharactermod.cards.BaseCard;
import autoplaycharactermod.cards.traitScavengeCards.GachaPull;
import autoplaycharactermod.util.CardStats;
import basemod.patches.com.megacrit.cardcrawl.dungeons.AbstractDungeon.NoPools;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.MonsterRoomBoss;


@NoPools
public class GamblingRelic extends BaseCard {
    public static final String ID = makeID("GamblingRelic");
    private static final CardStats info = new CardStats(
            CardColor.COLORLESS,
            CardType.POWER,
            CardRarity.SPECIAL,
            CardTarget.NONE,
            -2
    );

    public GamblingRelic() {
        super(ID, info);
    }

    @Override
    public void use(AbstractPlayer abstractPlayer, AbstractMonster abstractMonster) {
        onChoseThisOption();
    }

    public void onChoseThisOption() {

        if (AbstractDungeon.getCurrRoom() instanceof MonsterRoomBoss && (AbstractDungeon.id.equals("TheBeyond") || AbstractDungeon.id.equals("TheEnding"))) {// 114
            AbstractDungeon.getCurrRoom().spawnRelicAndObtain(Settings.WIDTH / 2F, Settings.HEIGHT / 2F, BasicMod.returnTrueRandomScreenlessRelic());
        } else {
            if (AbstractDungeon.cardRandomRng.randomBoolean())
                AbstractDungeon.getCurrRoom().addRelicToRewards(AbstractRelic.RelicTier.COMMON);
            else
                AbstractDungeon.getCurrRoom().addRelicToRewards(AbstractRelic.RelicTier.UNCOMMON);
            GachaPull.cardsList.removeIf(c -> c instanceof GamblingRelic);
        }
    }
}
