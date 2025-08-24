package autoplaycharactermod.cards.basic;

import autoplaycharactermod.BasicMod;
import autoplaycharactermod.actions.SfxActionVolume;
import autoplaycharactermod.cards.BaseCard;
import autoplaycharactermod.character.MyCharacter;
import autoplaycharactermod.powers.TargetedPower;
import autoplaycharactermod.relics.ScheduledUpdate;
import autoplaycharactermod.util.CardStats;
import basemod.patches.com.megacrit.cardcrawl.dungeons.AbstractDungeon.NoPools;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.powers.watcher.VigorPower;

@NoPools
public class SelectTarget extends BaseCard {
    public static final String ID = makeID("SelectTarget");
    public static final int MAGIC = 2;
    public static final int MAGIC_UPG = 3;
    private static final CardStats info = new CardStats(
            MyCharacter.Meta.CARD_COLOR,
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
        tags.add(BasicMod.CustomTags.NoEnergyText);
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
        MyCharacter.targetCheck(m);
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
