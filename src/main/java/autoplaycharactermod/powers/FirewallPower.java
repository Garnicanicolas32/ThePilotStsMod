package autoplaycharactermod.powers;

import com.evacipated.cardcrawl.mod.stslib.patches.bothInterfaces.OnCreateCardInterface;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static autoplaycharactermod.BasicMod.makeID;

public class FirewallPower extends BasePower implements OnCreateCardInterface {
    public static final String POWER_ID = makeID("FirewallPower");
    private static final PowerType TYPE = PowerType.BUFF;
    private static final boolean TURN_BASED = false;

    //The only thing TURN_BASED controls is the color of the number on the power icon.
    //Turn based powers are white, non-turn based powers are red or green depending on if their amount is positive or negative.
    //For a power to actually decrease/go away on its own they do it themselves.
    //Look at powers that do this like VulnerablePower and DoubleTapPower.

    public FirewallPower(AbstractCreature owner, int amount) {
        super(POWER_ID, TYPE, TURN_BASED, owner, amount);
    }

    public void playApplyPowerSfx() {
        CardCrawlGame.sound.play("ATTACK_FLAME_BARRIER", 0.05F);
    }

    public void updateDescription() {
        if (DESCRIPTIONS != null && DESCRIPTIONS.length > 1) {
            this.description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1];
        } else
            this.description = "[MISSING DESCRIPTION]";
    }

    @Override
    public void onCreateCard(AbstractCard abstractCard) {
        if (abstractCard.type == AbstractCard.CardType.STATUS) {
            flash();
            addToBot(new GainBlockAction(this.owner, this.owner, amount));
        }
    }

    public void onCardDraw(AbstractCard card) {
        if (card.type == AbstractCard.CardType.STATUS) {
            flash();
            addToBot(new GainBlockAction(this.owner, this.owner, amount));
        }
    }

    public void onPlayCard(AbstractCard card, AbstractMonster m) {
        if (card.type == AbstractCard.CardType.STATUS) {
            addToBot(new GainBlockAction(this.owner, this.owner, amount));
        }
    }
}
