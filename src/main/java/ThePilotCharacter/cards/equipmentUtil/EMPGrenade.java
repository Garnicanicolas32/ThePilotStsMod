package ThePilotCharacter.cards.equipmentUtil;

import ThePilotCharacter.ThePilotMod;
import ThePilotCharacter.actions.DamageCurrentTargetAction;
import ThePilotCharacter.actions.SfxActionVolume;
import ThePilotCharacter.cards.BaseCard;
import ThePilotCharacter.cards.EquipmentCard;
import ThePilotCharacter.cards.chargingCards.Thrusters;
import ThePilotCharacter.character.PilotCharacter;
import ThePilotCharacter.util.CardStats;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.ArrayList;

public class EMPGrenade extends BaseCard {
    public static final String ID = makeID("EMPGrenade");
    private static final CardStats info = new CardStats(
            PilotCharacter.Meta.CARD_COLOR,
            CardType.ATTACK,
            CardRarity.COMMON,
            CardTarget.SELF,
            -2
    );
    private static final int MAGIC = 3;
    private static final int UPG_MAGIC = 2;
    private static final int DAMAGE = 7;
    private static final int UPG_DAMAGE = 0;

    public EMPGrenade() {
        super(ID, info);
        returnToHand = true;
        setMagic(MAGIC, UPG_MAGIC);
        setDamage(DAMAGE, UPG_DAMAGE);
        checkEvolve();
    }

    public static int countCards(boolean flash) {
        int count = 0;
        for (AbstractCard c : AbstractDungeon.player.hand.group) {
            if (c instanceof EquipmentCard) {
                count++;
                if (flash)
                    c.flash(Color.BLUE.cpy());
            }
        }
        return count;
    }

    @Override
    public void evolveCard() {
        setMagic(10);
        setDamage(12);
        super.evolveCard();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        countCards(true);
        addToBot(new SfxActionVolume("ORB_LIGHTNING_CHANNEL", -0.25F, 1.9F));
        addToBot(new DamageCurrentTargetAction(this, AbstractGameAction.AttackEffect.BLUNT_LIGHT));
        PlayOnce = false;
    }

    @Override
    public AbstractCard replaceWith(ArrayList<AbstractCard> currentRewardCards) {
        if (ThePilotMod.unseenTutorials[2]) {
            return new Thrusters();
        }
        return this;
    }

    @Override
    public void calculateCardDamage(AbstractMonster mo) {
        int realBaseDamage = this.baseDamage;
        this.baseDamage += this.magicNumber * countCards(false);
        super.calculateCardDamage(mo);
        this.baseDamage = realBaseDamage;
        this.isDamageModified = this.damage != this.baseDamage;
    }

    @Override
    public void applyPowers() {
        int realBaseDamage = this.baseDamage;
        this.baseDamage += this.magicNumber * countCards(false);
        super.applyPowers();
        this.baseDamage = realBaseDamage;
        this.isDamageModified = this.damage != this.baseDamage;
    }

    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        return PlayOnce && super.canUse(p, m);
    }
}
