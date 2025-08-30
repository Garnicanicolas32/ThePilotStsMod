package autoplaycharactermod.cards.optionSelection.gambling;

import autoplaycharactermod.cards.BaseCard;
import autoplaycharactermod.cards.traitScavengeCards.GachaPull;
import autoplaycharactermod.util.CardStats;
import basemod.patches.com.megacrit.cardcrawl.dungeons.AbstractDungeon.NoPools;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;


@NoPools
public class GamblingDamage extends BaseCard {
    public static final String ID = makeID("GamblingDamage");
    private static final CardStats info = new CardStats(
            CardColor.COLORLESS,
            CardType.POWER,
            CardRarity.SPECIAL,
            CardTarget.NONE,
            -2 
    );
    private static final int MAGIC = 5;
    private static final int MAGIC_UPG = 15;

    public GamblingDamage() {
        super(ID, info);
        setDamage(MAGIC, MAGIC_UPG);
    }

    @Override
    public void use(AbstractPlayer abstractPlayer, AbstractMonster abstractMonster) {
        onChoseThisOption();
    }

    public void onChoseThisOption() {
        for (AbstractMonster mon : AbstractDungeon.getMonsters().monsters) {
            if (!mon.isDeadOrEscaped()) {
                calculateCardDamage(mon);
                addToBot(new DamageAction(mon, new DamageInfo(AbstractDungeon.player, damage, DamageInfo.DamageType.THORNS), AbstractGameAction.AttackEffect.BLUNT_HEAVY));
            }
        }
        GachaPull.cardsList.removeIf(c -> c instanceof GamblingDamage);
        
    }
}
