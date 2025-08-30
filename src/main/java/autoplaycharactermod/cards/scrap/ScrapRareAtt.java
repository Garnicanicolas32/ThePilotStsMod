package autoplaycharactermod.cards.scrap;

import autoplaycharactermod.BasicMod;
import autoplaycharactermod.actions.DamageCurrentTargetAction;
import autoplaycharactermod.cards.BaseCard;
import autoplaycharactermod.character.MyCharacter;
import autoplaycharactermod.patches.VigorPenNibDuplicationPatch;
import autoplaycharactermod.powers.Crafting;
import autoplaycharactermod.util.CardStats;
import basemod.patches.com.megacrit.cardcrawl.dungeons.AbstractDungeon.NoPools;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.RitualPower;
import com.megacrit.cardcrawl.vfx.stance.StanceChangeParticleGenerator;

@NoPools
public class ScrapRareAtt extends BaseCard {
    public static final String ID = makeID("ScrapRareAtt");
    private static final CardStats info = new CardStats(
            MyCharacter.Meta.CARD_COLOR,
            CardType.ATTACK,
            CardRarity.SPECIAL,
            CardTarget.SELF,
            0
    );
    private static final int DAMAGE = 13;
    private static final int DAMAGEUPG = 5;
    private static final int MAGIC = 1;
    private static final int MAGICUPG = 1;

    public ScrapRareAtt() {
        super(ID, info);
        returnToHand = true;
        tags.add(BasicMod.CustomTags.ScrapRare);
        setDamage(DAMAGE);
        setMagic(MAGIC);
        tags.add(BasicMod.CustomTags.NoEnergyText);
        tags.add(BasicMod.CustomTags.skipVigor);
        this.setDisplayRarity(CardRarity.RARE);
    }

    public boolean canUpgrade() {
        return false;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (PlayOnce && !Duplicated) {
            PlayOnce = false;
            addToBot(new DamageCurrentTargetAction(this, AbstractGameAction.AttackEffect.BLUNT_HEAVY));
            VigorPenNibDuplicationPatch.checkPenNibVigor();
            returnToHand = true;
        } else {
            addToBot(new SFXAction("STANCE_ENTER_WRATH"));
            addToBot(new VFXAction(new StanceChangeParticleGenerator(AbstractDungeon.player.hb.cX, AbstractDungeon.player.hb.cY, "Wrath")));
            if (!Duplicated)
                addToBot(new ApplyPowerAction(p, p, new Crafting(p, 3)));
            addToBot(new ApplyPowerAction(p, p, new RitualPower(p, magicNumber, true)));
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
