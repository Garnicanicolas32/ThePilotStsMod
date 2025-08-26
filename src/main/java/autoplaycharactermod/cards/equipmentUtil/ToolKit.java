package autoplaycharactermod.cards.equipmentUtil;

import autoplaycharactermod.BasicMod;
import autoplaycharactermod.cards.BaseCard;
import autoplaycharactermod.cards.EquipmentCard;
import autoplaycharactermod.cards.chargingCards.PreemptiveStrike;
import autoplaycharactermod.cards.traitIgnitionCards.Firewall;
import autoplaycharactermod.character.MyCharacter;
import autoplaycharactermod.util.CardStats;
import autoplaycharactermod.vfx.HealEquipmentEffect;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.ArrayList;

public class ToolKit extends BaseCard {
    public static final String ID = makeID("ToolKit");
    private static final CardStats info = new CardStats(
            MyCharacter.Meta.CARD_COLOR,
            CardType.SKILL,
            CardRarity.UNCOMMON,
            CardTarget.SELF,
            -2
    );
    private static final int MAGIC = 2;
    private static final int UPG_MAGIC = 1;

    public ToolKit() {
        super(ID, info);
        returnToHand = true;
        setMagic(MAGIC, UPG_MAGIC);
        tags.add(CardTags.HEALING);
        checkEvolve();
    }

    @Override
    public void evolveCard() {
        setMagic(10);
        super.evolveCard();
    }

    @Override
    public AbstractCard replaceWith(ArrayList<AbstractCard> currentRewardCards) {
        if (BasicMod.unseenTutorials[2]) {
            return new Firewall();
        }
        return this;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int count = 0;
        addToBot(new SFXAction("POWER_DEXTERITY"));
        for (AbstractCard c : p.hand.group) {
            if (c instanceof EquipmentCard) {
                ((EquipmentCard) c).healEquipment(magicNumber, false, true);
                c.flash(Color.GREEN.cpy());
                count++;
            }
        }
        if (count > 0) {
            addToBot(new SFXAction("HEAL_2"));
            AbstractDungeon.topLevelEffectsQueue.add(new HealEquipmentEffect(this, (float) Settings.WIDTH / 2, (float) Settings.HEIGHT / 2, count * magicNumber));
        }
        PlayOnce = false;
    }

    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        return PlayOnce && super.canUse(p, m);
    }
}
