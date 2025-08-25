package autoplaycharactermod.cards.scrap;

import autoplaycharactermod.BasicMod;
import autoplaycharactermod.actions.DamageCurrentTargetAction;
import autoplaycharactermod.cards.BaseCard;
import autoplaycharactermod.character.MyCharacter;
import autoplaycharactermod.powers.Crafting;
import autoplaycharactermod.util.CardStats;
import basemod.ReflectionHacks;
import basemod.patches.com.megacrit.cardcrawl.dungeons.AbstractDungeon.NoPools;
import com.badlogic.gdx.Gdx;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardHelper;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.PenNibPower;
import com.megacrit.cardcrawl.powers.RitualPower;
import com.megacrit.cardcrawl.powers.watcher.VigorPower;
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
        setDamage(DAMAGE, DAMAGEUPG);
        setMagic(MAGIC, MAGICUPG);
        tags.add(BasicMod.CustomTags.NoEnergyText);
        tags.add(BasicMod.CustomTags.skipVigor);
        this.setDisplayRarity(CardRarity.RARE);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (PlayOnce) {
            PlayOnce = false;
            addToBot(new DamageCurrentTargetAction(this, AbstractGameAction.AttackEffect.BLUNT_HEAVY));
            if (p.hasPower(VigorPower.POWER_ID)) {
                p.getPower(VigorPower.POWER_ID).flash();
                addToBot(new RemoveSpecificPowerAction(p, p, "Vigor"));
            }
            if (p.hasPower(PenNibPower.POWER_ID)) {
                p.getPower(PenNibPower.POWER_ID).flash();
                addToBot(new RemoveSpecificPowerAction(p, p, PenNibPower.POWER_ID));
            }
            returnToHand = true;
        } else {
            addToBot(new SFXAction("STANCE_ENTER_WRATH"));
            addToBot(new VFXAction(new StanceChangeParticleGenerator(AbstractDungeon.player.hb.cX, AbstractDungeon.player.hb.cY, "Wrath")));
            addToBot(new ApplyPowerAction(p, p, new Crafting(p, 3)));
            addToBot(new ApplyPowerAction(p, p, new RitualPower(p, magicNumber, true)));
            returnToHand = false;
            setExhaust(true);
        }
    }

    @Override
    public boolean freeToPlay() {
        return true;
    }
}
