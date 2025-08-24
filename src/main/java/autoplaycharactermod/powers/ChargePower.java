package autoplaycharactermod.powers;

import autoplaycharactermod.cards.equipment.PowerBank;
import autoplaycharactermod.character.MyCharacter;
import com.evacipated.cardcrawl.mod.stslib.actions.common.DamageCallbackAction;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.vfx.combat.MindblastEffect;
import com.megacrit.cardcrawl.vfx.combat.SmallLaserEffect;

import static autoplaycharactermod.BasicMod.makeID;

public class ChargePower extends BasePower {
    public static final String POWER_ID = makeID("ChargePower");
    private static final PowerType TYPE = PowerType.BUFF;
    private static final boolean TURN_BASED = false;

    public ChargePower(AbstractCreature owner, int amount) {
        super(POWER_ID, TYPE, TURN_BASED, owner, amount);
    }

    public void onInitialApplication() {
        checkHand();
    }

    @Override
    public void stackPower(int stackAmount){
        super.stackPower(stackAmount);
        checkHand();
    }

    public void playApplyPowerSfx() {
        CardCrawlGame.sound.playV("ORB_LIGHTNING_CHANNEL", 2F);
    }

    public void atEndOfTurn(boolean isPlayer) {
        AbstractMonster m = MyCharacter.getTarget();
        AbstractPlayer p = AbstractDungeon.player;

        if (amount < 50)
            addToBot(new VFXAction(new SmallLaserEffect(m.hb.cX, m.hb.cY, p.hb.cX, p.hb.cY), 0.1F));
        else
            addToBot(new VFXAction(p, new MindblastEffect(p.dialogX, p.dialogY, p.flipHorizontal), 0.1F));

        int saveAmount = p.hasPower(SavePower.POWER_ID)
                ? p.getPower(SavePower.POWER_ID).amount
                : 0;

        if (m == null || m.isDying || m.currentHealth <= 0 || m.halfDead) {
            int toKeep = Math.min(amount, Math.max(amount / 2, saveAmount));
            amount = Math.max(0, toKeep);
            if (amount == 0) {
                addToBot(new RemoveSpecificPowerAction(owner, owner, ChargePower.POWER_ID));
            }
            return;
        }

        CardCrawlGame.sound.playV("ATTACK_DEFECT_BEAM", 1.6F);
        addToBot(new DamageCallbackAction(m, new DamageInfo(owner, amount, DamageInfo.DamageType.THORNS), AbstractGameAction.AttackEffect.NONE, (lastDmgTaken) -> {
            boolean killed = (m.isDying || m.currentHealth <= 0) && !m.halfDead;
            int toKeep;
            if (killed) {
                toKeep = Math.min(amount, Math.max(amount / 2, saveAmount));
            } else {
                toKeep = Math.min(amount, saveAmount);
            }
            amount = Math.max(0, toKeep);
            if (amount == 0) {
                addToBot(new RemoveSpecificPowerAction(owner, owner, ChargePower.POWER_ID));
            }
        }));
    }

    private void checkHand() {
        for (AbstractCard c : AbstractDungeon.player.hand.group) {
            if (c instanceof PowerBank) {
                ((PowerBank) c).Activate();
            }
        }
    }

    public void updateDescription() {
        if (DESCRIPTIONS != null && DESCRIPTIONS.length > 1)
            this.description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1];
        else
            this.description = "[MISSING DESCRIPTION]";
    }
}
