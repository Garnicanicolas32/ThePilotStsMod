package autoplaycharactermod.cards.equipment;

import autoplaycharactermod.actions.SfxActionVolume;
import autoplaycharactermod.cards.EquipmentCard;
import autoplaycharactermod.character.MyCharacter;
import autoplaycharactermod.ui.ConfigPanel;
import autoplaycharactermod.util.CardStats;
import autoplaycharactermod.vfx.PoisonMineDrillEffect;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.PoisonPower;

public class PoisonMine extends EquipmentCard {
    public static final String ID = makeID("PoisonMine");
    private static final CardStats info = new CardStats(
            MyCharacter.Meta.CARD_COLOR,
            CardType.SKILL,
            CardRarity.UNCOMMON,
            CardTarget.NONE,
            0 
    );
    private static final int BASE_HP = 21;
    private static final int POISON = 1;
    private static final int UPG_POISON = 1;

    public PoisonMine() {
        super(ID, info, BASE_HP);
        setMagic(POISON, UPG_POISON);
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
        int poisonAmount = (p.hand.group.size() / (this.alreadyEvolved ? 1 : 2)) * magicNumber;
        if (poisonAmount > 0) {
            addToBot(new SfxActionVolume("SPORE_CLOUD_RELEASE", 1f, 2F));
            for (int i = 0; i < (ConfigPanel.lessParticles ? 20 : 50); ++i) {
                AbstractDungeon.effectsQueue.add(new PoisonMineDrillEffect(this.hb.cX, this.hb.cY, false));
            }

            for (AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
                if (!m.isDeadOrEscaped()) {
                    addToBot(new ApplyPowerAction(m, p, new PoisonPower(m, p, poisonAmount)));
                }
            }
        }
        super.Activate();
    }

    @Override
    protected int getUpgradeDurability() {
        return 5;
    }

    public void triggerOnEndOfTurnForPlayingCard() {
        Activate();
    }


}
