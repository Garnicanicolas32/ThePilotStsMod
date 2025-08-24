package autoplaycharactermod.cards;

import autoplaycharactermod.BasicMod;
import autoplaycharactermod.cards.equipment.TrashCannon;
import autoplaycharactermod.util.CardStats;
import basemod.abstracts.CustomSavable;
import com.google.gson.reflect.TypeToken;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public abstract class ConsumableCards extends BaseCard {

    public static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(makeID("ConsumableCards"));
    protected int AMOUNT = 5;
    protected int AMOUNTUPG = 5;
    protected int uses;

    public ConsumableCards(String ID, CardStats info, int amount, int amountupg) {
        super(ID, info);
        setExhaust(true);
        AMOUNT = amount;
        AMOUNTUPG = amountupg;
        uses = AMOUNT;
        updateDescription();
    }

    @Override
    public void evolveCard() {
        uses = 99;
        setExhaust(false);
        returnToHand = true;
        super.evolveCard();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        PlayOnce = false;
        if (uses > 1) {
            uses--;
            updateMasterDeckCopies();
        } else {
            if (alreadyEvolved)
                setExhaust(true);
            removeFromMasterDeck();
        }
    }

    private void updateMasterDeckCopies() {
        AbstractPlayer p = AbstractDungeon.player;
        for (AbstractCard c : p.masterDeck.group) {
            if (c.uuid.equals(this.uuid) && c instanceof ConsumableCards) {
                ((ConsumableCards) c).uses = this.uses;
                c.applyPowers();
                break;
            }
        }
    }

    private void removeFromMasterDeck() {
        AbstractPlayer p = AbstractDungeon.player;
        p.masterDeck.group.removeIf(c -> c.uuid.equals(this.uuid));
        List<CardGroup> groups = Arrays.asList(
                p.masterDeck,
                p.hand,
                p.discardPile,
                p.drawPile
        );
        for (CardGroup group : groups) {
            for (AbstractCard c : group.group) {
                if (c instanceof TrashCannon) {
                    ((TrashCannon) c).AddStack();
                }
            }
        }
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            this.uses += AMOUNTUPG;
            updateDescription();
        }
        super.upgrade();
    }

    @Override
    public void applyPowers() {
        super.applyPowers();
        updateDescription();
    }

    public void updateDescription() {
        if (cardStrings.DESCRIPTION == null || uiStrings == null || uiStrings.TEXT.length < 3)
            return;
        this.rawDescription = uiStrings.TEXT[0] + uses + (uses == 1 ? uiStrings.TEXT[2] : uiStrings.TEXT[1]);
        if (!this.alreadyEvolved || cardStrings.EXTENDED_DESCRIPTION == null || cardStrings.EXTENDED_DESCRIPTION.length < 1) {
            this.rawDescription += cardStrings.DESCRIPTION;
        } else {
            this.rawDescription += cardStrings.EXTENDED_DESCRIPTION[0];
        }
        initializeDescription();
    }

    @Override
    public boolean freeToPlay() {
        return true;
    }

    @Override
    public AbstractCard makeStatEquivalentCopy() {
        ConsumableCards copy = (ConsumableCards) super.makeStatEquivalentCopy();
        copy.uses = this.uses;
        copy.updateDescription();
        return copy;
    }

    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        return (PlayOnce || !alreadyEvolved) && super.canUse(p, m);
    }
}
