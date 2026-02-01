package ThePilotCharacter.cards.scrap;

import ThePilotCharacter.ThePilotMod;
import ThePilotCharacter.cards.BaseCard;
import ThePilotCharacter.character.PilotCharacter;
import ThePilotCharacter.powers.Crafting;
import ThePilotCharacter.util.CardStats;
import basemod.patches.com.megacrit.cardcrawl.dungeons.AbstractDungeon.NoPools;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.IntangiblePlayerPower;
import com.megacrit.cardcrawl.vfx.combat.SanctityEffect;

@NoPools
public class ScrapRareDef extends BaseCard {
    public static final String ID = makeID("ScrapRareDef");
    private static final CardStats info = new CardStats(
            PilotCharacter.Meta.CARD_COLOR,
            CardType.SKILL,
            CardRarity.SPECIAL,
            CardTarget.SELF,
            0
    );
    private static final int BLOCK = 13;
    private static final int BLOCKUPG = 5;
    private static final int MAGIC = 1;
    private static final int MAGICUPG = 1;

    public ScrapRareDef() {
        super(ID, info);
        returnToHand = true;
        tags.add(ThePilotMod.CustomTags.ScrapRare);
        setBlock(BLOCK);
        setMagic(MAGIC);
        tags.add(ThePilotMod.CustomTags.NoEnergyText);
        this.setDisplayRarity(CardRarity.RARE);
    }

    public boolean canUpgrade() {
        return false;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (PlayOnce&&!Duplicated) {
            PlayOnce = false;
            addToBot(new GainBlockAction(p, p, block));
            returnToHand = true;
        } else {
            addToBot(new SFXAction("STANCE_ENTER_CALM"));
            addToBot(new VFXAction(new SanctityEffect(AbstractDungeon.player.hb.cX, AbstractDungeon.player.hb.cY)));
            addToBot(new ApplyPowerAction(p, p, new IntangiblePlayerPower(p, magicNumber)));
            if (!Duplicated)
                addToBot(new ApplyPowerAction(p, p, new Crafting(p, 3)));
            returnToHand = false;
            setExhaust(true);
        }
    }

    @Override
    public void upgrade(){
    }

    @Override
    public boolean freeToPlay() {
        return true;
    }

}
