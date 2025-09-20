package autoplaycharactermod.cards.traitBastionCards;

import autoplaycharactermod.actions.SfxActionVolume;
import autoplaycharactermod.cards.TraitCard;
import autoplaycharactermod.character.PilotCharacter;
import autoplaycharactermod.util.CardStats;
import autoplaycharactermod.vfx.FlashbangEffect;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.powers.WeakPower;

public class Flash extends TraitCard {
    public static final String ID = makeID("Flash");
    private static final CardStats info = new CardStats(
            PilotCharacter.Meta.CARD_COLOR,
            CardType.SKILL,
            CardRarity.COMMON,
            CardTarget.ALL_ENEMY,
            -2 
    );
    private static final int MAGIC = 1;
    private static final int UPG_MAGIC = 1;

    public Flash() {
        super(ID, info, TraitCard.TraitColor.BASTION, false);
        setMagic(MAGIC, UPG_MAGIC);
        checkEvolve();
    }

    @Override
    public void evolveCard() {
        setMagic(2);
        super.evolveCard();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new SfxActionVolume("MONSTER_COLLECTOR_SUMMON", 0f,2f));
        for (AbstractMonster mo : AbstractDungeon.getMonsters().monsters) {
            if (!mo.isDeadOrEscaped()) {
                addToBot(new VFXAction(new FlashbangEffect(mo.hb.cX, mo.hb.cY)));
                if (this.alreadyEvolved) {
                    addToBot(new ApplyPowerAction(m, p, new StrengthPower(m, -this.magicNumber), -this.magicNumber));
                } else {
                    addToBot(new ApplyPowerAction(mo, p, new WeakPower(mo, magicNumber, false), magicNumber, true, AbstractGameAction.AttackEffect.NONE));
                }
            }
        }
        PlayOnce = false;
        super.use(p, m);
    }
}
