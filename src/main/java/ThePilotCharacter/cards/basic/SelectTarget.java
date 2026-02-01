package ThePilotCharacter.cards.basic;

import ThePilotCharacter.ThePilotMod;
import ThePilotCharacter.actions.SfxActionVolume;
import ThePilotCharacter.cards.BaseCard;
import ThePilotCharacter.character.PilotCharacter;
import ThePilotCharacter.powers.TargetedPower;
import ThePilotCharacter.relics.ScheduledUpdate;
import ThePilotCharacter.util.CardStats;
import basemod.patches.com.megacrit.cardcrawl.dungeons.AbstractDungeon.NoPools;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.watcher.VigorPower;

@NoPools
public class SelectTarget extends BaseCard {
    public static final String ID = makeID("SelectTarget");
    public static final int MAGIC = 2;
    public static final int MAGIC_UPG = 2;
    private static final CardStats info = new CardStats(
            PilotCharacter.Meta.CARD_COLOR,
            CardType.SKILL,
            CardRarity.SPECIAL,
            CardTarget.ENEMY,
            2
    );

    public SelectTarget() {
        super(ID, info);
        setExhaust(true);
        setEthereal(true);
        setMagic(MAGIC, MAGIC_UPG);
        tags.add(ThePilotMod.CustomTags.NoEnergyText);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new SfxActionVolume("POWER_FOCUS", 0f,1.8F));
        if (!m.hasPower(TargetedPower.POWER_ID)) {
            for (AbstractMonster mon : AbstractDungeon.getMonsters().monsters) {
                if (mon.hasPower(TargetedPower.POWER_ID))
                    addToTop(new RemoveSpecificPowerAction(mon, p, TargetedPower.POWER_ID));
            }
            addToTop(new ApplyPowerAction(m, p, new TargetedPower(m, -1)));
        } else {
                addToBot(new ApplyPowerAction(p, p, new VigorPower(p, magicNumber)));
        }
        PilotCharacter.targetCheck(m, true);
    }

    @Override
    public boolean freeToPlay() {
        AbstractPlayer p = AbstractDungeon.player;
        if (p != null)
            return !AbstractDungeon.player.hasRelic(ScheduledUpdate.ID);
        else
            return true;
    }
}
