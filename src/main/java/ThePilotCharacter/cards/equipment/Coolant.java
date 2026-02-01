package ThePilotCharacter.cards.equipment;

import ThePilotCharacter.cards.EquipmentCard;
import ThePilotCharacter.character.PilotCharacter;
import ThePilotCharacter.ui.ConfigPanel;
import ThePilotCharacter.util.CardStats;
import ThePilotCharacter.vfx.CoolantEffect;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.ExhaustSpecificCardAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.watcher.VigorPower;

public class Coolant extends EquipmentCard {
    public static final String ID = makeID("Coolant");
    private static final CardStats info = new CardStats(
            PilotCharacter.Meta.CARD_COLOR,
            CardType.SKILL,
            CardRarity.UNCOMMON,
            CardTarget.NONE,
            0 
    );
    private static final int BASE_HP = 14;
    private static final int MAGIC = 2;
    private static final int MAGIC_UPG = 1;
    public Coolant() {
        super(ID, info, BASE_HP);
        setMagic(MAGIC, MAGIC_UPG);
        setInnate(false,true);
        checkEvolve();
    }

    public void triggerOnCardPlayed(AbstractCard cardPlayed) {
        if (!Equipped) return;
        AbstractPlayer p = AbstractDungeon.player;
        if (cardPlayed.type == CardType.STATUS) {
            addToBot(new SFXAction("CARD_BURN"));
            for (int i = 0; i < (ConfigPanel.lessParticles ? 10 : 20); i++) {
                AbstractDungeon.effectsQueue.add(new CoolantEffect(this.hb.cX, this.hb.cY));
            }
            addToBot(new ExhaustSpecificCardAction(cardPlayed, p.hand));
            addToBot(new GainEnergyAction(alreadyEvolved ? 3 : 1));
            addToBot(new ApplyPowerAction(p,p, new VigorPower(p, magicNumber)));
            Activate();
        }
    }

    @Override
    protected int getUpgradeDurability() {
        return 4;
    }

    @Override
    public void evolveCard() {
        setInnate(true);
        setMagic(3);
        super.evolveCard();
    }
}
