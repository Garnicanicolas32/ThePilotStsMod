package autoplaycharactermod.cards.equipment;

import autoplaycharactermod.cards.EquipmentCard;
import autoplaycharactermod.character.MyCharacter;
import autoplaycharactermod.ui.ConfigPanel;
import autoplaycharactermod.util.CardStats;
import autoplaycharactermod.vfx.CoolantEffect;
import com.megacrit.cardcrawl.actions.common.ExhaustSpecificCardAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class Coolant extends EquipmentCard {
    public static final String ID = makeID("Coolant");
    private static final CardStats info = new CardStats(
            MyCharacter.Meta.CARD_COLOR,
            CardType.SKILL,
            CardRarity.UNCOMMON,
            CardTarget.NONE,
            0 
    );
    private static final int BASE_HP = 14;
    private static final int MAGIC = 1;
    private static final int MAGIC_UPG = 0;

    public Coolant() {
        super(ID, info, BASE_HP);
        setMagic(MAGIC, MAGIC_UPG);
        setInnate(false,true);
        checkEvolve();
    }

    @Override
    public void evolveCard() {
        setInnate(true);
        setMagic(3);
        super.evolveCard();

    }

    public void triggerOnCardPlayed(AbstractCard cardPlayed) {
        if (!Equipped) return;
        if (cardPlayed.type == CardType.STATUS) {
            addToBot(new SFXAction("CARD_BURN"));
            for (int i = 0; i < (ConfigPanel.lessParticles ? 10 : 20); i++) {
                AbstractDungeon.effectsQueue.add(new CoolantEffect(this.hb.cX, this.hb.cY));
            }
            AbstractPlayer p = AbstractDungeon.player;
            addToBot(new ExhaustSpecificCardAction(cardPlayed, p.hand));
            addToBot(new GainEnergyAction(magicNumber));
            Activate();
        }
    }

    @Override
    protected int getUpgradeDurability() {
        return 6;
    }
}
