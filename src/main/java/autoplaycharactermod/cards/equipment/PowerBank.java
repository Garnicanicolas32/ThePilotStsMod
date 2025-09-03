package autoplaycharactermod.cards.equipment;

import autoplaycharactermod.BasicMod;
import autoplaycharactermod.actions.SfxActionVolume;
import autoplaycharactermod.cards.EquipmentCard;
import autoplaycharactermod.character.MyCharacter;
import autoplaycharactermod.powers.SavePower;
import autoplaycharactermod.ui.ConfigPanel;
import autoplaycharactermod.util.CardStats;
import basemod.helpers.TooltipInfo;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.stance.StanceChangeParticleGenerator;

import java.util.ArrayList;
import java.util.List;

public class PowerBank extends EquipmentCard {
    public static final String ID = makeID("PowerBank");

    private static final CardStats info = new CardStats(
            MyCharacter.Meta.CARD_COLOR,
            CardType.SKILL,
            CardRarity.UNCOMMON,
            CardTarget.NONE,
            0
    );
    private static final int BASE_HP = 45;
    private static final int MAGIC = 3;
    private static final int UPG_MAGIC = 1;
    private static final int BLOCK = 3;
    private static final int UPG_BLOCK = 1;


    public PowerBank() {
        super(ID, info, BASE_HP);
        setMagic(MAGIC, UPG_MAGIC);
        setBlock(BLOCK, UPG_BLOCK);
        checkEvolve();
    }

    @Override
    public void evolveCard() {
        setBlock(12);
        setMagic(12);
        super.evolveCard();
    }

    @Override
    public void Activate() {
        if (!Equipped) return;
        AbstractPlayer p = AbstractDungeon.player;
        if (ConfigPanel.experimentalSounds)
            addToBot(new SfxActionVolume("ORB_PLASMA_EVOKE", 1f, 0.8f));
        AbstractDungeon.topLevelEffects.add(new StanceChangeParticleGenerator(this.hb.cX, this.hb.cY, "Divinity"));
        addToBot(new ApplyPowerAction(p, p, new SavePower(p, magicNumber)));
        calculateCardDamage(null);
        addToBot(new GainBlockAction(p, block));
        super.Activate();
    }

    public void onGainCharge() {
        Activate();
    }

    @Override
    public List<TooltipInfo> getCustomTooltips() {
        ArrayList<TooltipInfo> customTooltips = new ArrayList<>();
        customTooltips.add(new TooltipInfo(BasicMod.keywords.get("Equipment").PROPER_NAME, BasicMod.keywords.get("Equipment").DESCRIPTION));
        customTooltips.add(new TooltipInfo(BasicMod.keywords.get("Charge").PROPER_NAME, BasicMod.keywords.get("Charge").DESCRIPTION));
        return customTooltips;
    }

    @Override
    protected int getUpgradeDurability() {
        return 15;
    }
}
