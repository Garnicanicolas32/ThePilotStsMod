package autoplaycharactermod.cards.equipment;

import autoplaycharactermod.ThePilotMod;
import autoplaycharactermod.cards.EquipmentCard;
import autoplaycharactermod.character.PilotCharacter;
import autoplaycharactermod.util.CardStats;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.actions.watcher.FollowUpAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class GrapplingHook extends EquipmentCard {
    public static final String ID = makeID("GrapplingHook");
    private static final CardStats info = new CardStats(
            PilotCharacter.Meta.CARD_COLOR,
            CardType.SKILL,
            CardRarity.UNCOMMON,
            CardTarget.NONE,
            0 
    );
    private static final int BASE_HP = 24;
    private static final int BLOCK = 5;
    private static final int UPG_BLOCK = 2;
    private boolean skipCheck = false;

    public GrapplingHook() {
        super(ID, info, BASE_HP);
        setBlock(BLOCK, UPG_BLOCK);
        checkEvolve();
        this.tags.remove(ThePilotMod.CustomTags.ignoreDuplication);
    }

    @Override
    public void evolveCard() {
        setBlock(12);
        super.evolveCard();

    }

    @Override
    public void Activate() {
        if (!Equipped && !skipCheck) return;
        skipCheck = false;
        AbstractPlayer p = AbstractDungeon.player;
        calculateCardDamage(null);
        addToBot(new SFXAction("POWER_ENTANGLED"));
        addToBot(new GainBlockAction(p, p, block));
        addToBot(new FollowUpAction());
        if (this.alreadyEvolved)
            addToBot(new FollowUpAction());
        super.Activate();
    }

    @Override
    protected int getUpgradeDurability() {
        return 10;
    }

    protected void onEquip() {
        Activate();
    }

    protected void onUnequip() {
        skipCheck = true;
        Activate();
    }

}
