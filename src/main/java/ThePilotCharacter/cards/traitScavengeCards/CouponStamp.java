package ThePilotCharacter.cards.traitScavengeCards;

import ThePilotCharacter.ThePilotMod;
import ThePilotCharacter.cards.TraitCard;
import ThePilotCharacter.character.PilotCharacter;
import ThePilotCharacter.util.CardStats;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.WeightyImpactEffect;

public class CouponStamp extends TraitCard {
    public static final String ID = makeID("CouponStamp");
    private static final CardStats info = new CardStats(
            PilotCharacter.Meta.CARD_COLOR,
            CardType.ATTACK,
            CardRarity.UNCOMMON,
            CardTarget.NONE,
            -2 
    );
    private static final int MAGIC = 2;
    private static final int UPG_MAGIC = 1;

    public CouponStamp() {
        super(ID, info, TraitCard.TraitColor.SCAVENGE, false);
        setMagic(MAGIC, UPG_MAGIC);
        initializeDescription();
        checkEvolve();
    }

    @Override
    public void evolveCard() {
        setMagic(8);
        super.evolveCard();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new SFXAction("SHOP_PURCHASE"));
        AbstractMonster mon = PilotCharacter.getTarget();
        this.addToBot(new VFXAction(new WeightyImpactEffect(mon.hb.cX, mon.hb.cY, Color.GOLD.cpy())));
        this.baseDamage = ThePilotMod.purchases * this.magicNumber;
        this.calculateCardDamage(mon);
        addToBot(new DamageAction(mon, new DamageInfo(p, damage)));
        PlayOnce = false;

        super.use(p, m);
    }

    @Override
    public void applyPowers() {
        this.baseDamage = ThePilotMod.purchases * this.magicNumber;
        super.applyPowers();
    }

    @Override
    public void calculateCardDamage(AbstractMonster mo) {
        this.baseDamage = ThePilotMod.purchases * this.magicNumber;
        super.calculateCardDamage(mo);
    }

    @Override
    public void initializeDescription() {
        if (cardStrings != null && CardCrawlGame.isInARun()) {
            this.rawDescription = this.alreadyEvolved ? cardStrings.DESCRIPTION : cardStrings.EXTENDED_DESCRIPTION[0];
            if (ThePilotMod.purchases > 0) {
                this.rawDescription += cardStrings.EXTENDED_DESCRIPTION[1]
                        + ThePilotMod.purchases
                        + cardStrings.EXTENDED_DESCRIPTION[2];
            }
        }
        super.initializeDescription();
    }
}
