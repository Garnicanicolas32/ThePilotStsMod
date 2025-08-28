package autoplaycharactermod.cards.traitScavengeCards;

import autoplaycharactermod.BasicMod;
import autoplaycharactermod.cards.TraitCard;
import autoplaycharactermod.character.MyCharacter;
import autoplaycharactermod.util.CardStats;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.WeightyImpactEffect;

public class CouponStamp extends TraitCard {
    public static final String ID = makeID("CouponStamp");
    private static final CardStats info = new CardStats(
            MyCharacter.Meta.CARD_COLOR,
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
        this.addToBot(new VFXAction(new WeightyImpactEffect(m.hb.cX, m.hb.cY, Color.GOLD.cpy())));
        this.baseDamage = BasicMod.purchases * this.magicNumber;
        this.calculateCardDamage(MyCharacter.getTarget());
        addToBot(new DamageAction(MyCharacter.getTarget(), new DamageInfo(p, damage)));
        PlayOnce = false;
        super.use(p, m);
    }

    @Override
    public void applyPowers() {
        this.baseDamage = BasicMod.purchases * this.magicNumber;
        super.applyPowers();
    }

    @Override
    public void calculateCardDamage(AbstractMonster mo) {
        this.baseDamage = BasicMod.purchases * this.magicNumber;
        super.calculateCardDamage(mo);
    }

    @Override
    public void initializeDescription() {
        if (!this.alreadyEvolved) {
            this.rawDescription = cardStrings != null ? cardStrings.DESCRIPTION : "";
            if (BasicMod.purchases > 0 && cardStrings != null) {
                this.rawDescription += cardStrings.EXTENDED_DESCRIPTION[1]
                        + BasicMod.purchases
                        + cardStrings.EXTENDED_DESCRIPTION[2];
            }
        }
        super.initializeDescription();
    }
}
