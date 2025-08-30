package autoplaycharactermod.cards.equipment;

import autoplaycharactermod.actions.SfxActionVolume;
import autoplaycharactermod.cards.EquipmentCard;
import autoplaycharactermod.character.MyCharacter;
import autoplaycharactermod.util.CardStats;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.LoseDexterityPower;
import com.megacrit.cardcrawl.powers.LoseStrengthPower;
import com.megacrit.cardcrawl.powers.StrengthPower;

public class Metronome extends EquipmentCard {
    public static final String ID = makeID("Metronome");
    private static final CardStats info = new CardStats(
            MyCharacter.Meta.CARD_COLOR,
            CardType.SKILL,
            CardRarity.UNCOMMON,
            CardTarget.NONE,
            0 
    );
    private static final int BASE_HP = 12;
    private static final int STR = 2;
    private static final int STR_UPG = 1;

    public Metronome() {
        super(ID, info, BASE_HP);
        setMagic(STR, STR_UPG);
        checkEvolve();
    }

    @Override
    public void evolveCard() {
        setMagic(3);
        super.evolveCard();
    }

    @Override
    public void Activate() {
        if (!Equipped) return;
        AbstractPlayer p = AbstractDungeon.player;
        addToBot(new SfxActionVolume("UNLOCK_WHIR", 0.1F, 2.6F));
        addToBot(new ApplyPowerAction(p, p, new StrengthPower(p, magicNumber)));
        addToBot(new ApplyPowerAction(p, p, new DexterityPower(p, magicNumber)));
        if (!this.alreadyEvolved) {
            addToBot(new ApplyPowerAction(p, p, new LoseStrengthPower(p, magicNumber)));
            addToBot(new ApplyPowerAction(p, p, new LoseDexterityPower(p, magicNumber)));
        }
        super.Activate();
    }

    @Override
    protected int getUpgradeDurability() {
        return 3;
    }

    @Override
    public void atTurnStart() {
        Activate();
    }
}
