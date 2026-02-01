package ThePilotCharacter.cards.equipment;

import ThePilotCharacter.cards.EquipmentCard;
import ThePilotCharacter.character.PilotCharacter;
import ThePilotCharacter.patches.OnUseCardPowersAndRelicsPatch;
import ThePilotCharacter.util.CardStats;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.powers.watcher.VigorPower;

public class RocketPunch extends EquipmentCard {
    public static final String ID = makeID("RocketPunch");
    private static final CardStats info = new CardStats(
            PilotCharacter.Meta.CARD_COLOR,
            CardType.ATTACK,
            CardRarity.COMMON,
            CardTarget.NONE,
            0 
    );
    private static final int BASE_HP = 37;
    private static final int DAMAGE = 5;
    private static final int UPG_DAMAGE = 1;
    private static final int MAGIC = 3;
    private static final int UPG_MAGIC = 2;

    public RocketPunch() {
        super(ID, info, BASE_HP);
        setDamage(DAMAGE, UPG_DAMAGE);
        setMagic(MAGIC, UPG_MAGIC);
        this.isMultiDamage = true;
        checkEvolve();
    }

    @Override
    public void evolveCard() {
        setDamage(10);
        setMagic(3);
        super.evolveCard();
    }

    @Override
    public void Activate() {
        if (!Equipped) return;
        AbstractPlayer p = AbstractDungeon.player;
        calculateCardDamage(null);
        addToBot(new DamageAllEnemiesAction(p, multiDamage, DamageInfo.DamageType.NORMAL, AbstractGameAction.AttackEffect.BLUNT_LIGHT));
        OnUseCardPowersAndRelicsPatch.checkPenNibVigor();
        super.Activate();
    }

    @Override
    protected int getUpgradeDurability() {
        return 6;
    }

    public void atTurnStart() {
        if (!Equipped) return;
        AbstractPlayer p = AbstractDungeon.player;
        if (this.alreadyEvolved)
            addToBot(new ApplyPowerAction(p, p, new StrengthPower(p, magicNumber)));
        else
            addToBot(new ApplyPowerAction(p, p, new VigorPower(p, magicNumber)));
        super.Activate();
    }

    public void triggerOnEndOfTurnForPlayingCard() {
        Activate();
    }


}
