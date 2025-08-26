package autoplaycharactermod.cards.consumable;

import autoplaycharactermod.cards.ConsumableCards;
import autoplaycharactermod.cards.EquipmentCard;
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

public class SpareParts extends ConsumableCards {
    public static final String ID = makeID("SpareParts");
    private static final CardStats info = new CardStats(
            MyCharacter.Meta.CARD_COLOR,
            CardType.SKILL,
            CardRarity.COMMON,
            CardTarget.SELF,
            -2 
    );
    private static final int MAGIC = 2;
    private static final int MAGIC_UPG = 1;

    public SpareParts() {
        super(ID, info, 3, 4);
        setMagic(MAGIC, MAGIC_UPG);
        checkEvolve();
    }

    @Override
    public void evolveCard() {
        setMagic(6);
        super.evolveCard();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int count = 0;
        for (AbstractCard c : p.hand.group) {
            if (c instanceof EquipmentCard) {
                count++;
                ((EquipmentCard) c).healEquipment(magicNumber, false, true);
                c.flash(Color.GREEN.cpy());
            }
        }
        for (AbstractCard c : p.drawPile.group) {
            if (c instanceof EquipmentCard) {
                count++;
                ((EquipmentCard) c).healEquipment(magicNumber, false, true);
            }
        }
        for (AbstractCard c : p.discardPile.group) {
            if (c instanceof EquipmentCard) {
                count++;
                ((EquipmentCard) c).healEquipment(magicNumber, false, true);
            }
        }
        if (count > 0) {
            addToBot(new SFXAction("HEAL_2"));
            AbstractDungeon.topLevelEffectsQueue.add(new HealEquipmentEffect(this, (float) Settings.WIDTH / 2, (float) Settings.HEIGHT / 2, count * magicNumber));
        }
        super.use(p, m);
    }
}
