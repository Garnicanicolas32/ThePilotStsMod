package autoplaycharactermod.cards.scrap;

import autoplaycharactermod.BasicMod;
import autoplaycharactermod.actions.DamageCurrentTargetAction;
import autoplaycharactermod.actions.SfxActionVolume;
import autoplaycharactermod.cards.BaseCard;
import autoplaycharactermod.character.MyCharacter;
import autoplaycharactermod.powers.Crafting;
import autoplaycharactermod.util.CardStats;
import basemod.patches.com.megacrit.cardcrawl.cards.AbstractCard.MultiCardPreview;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.PenNibPower;
import com.megacrit.cardcrawl.powers.watcher.VigorPower;

public class ScrapCommon extends BaseCard {
    public static final String ID = makeID("ScrapCommon");
    private static final CardStats info = new CardStats(
            MyCharacter.Meta.CARD_COLOR,
            CardType.ATTACK,
            CardRarity.COMMON,
            CardTarget.NONE,
            0 
    );
    private static final int DAMAGE = 4;
    private static final int UPG = 4;
    private static final int MAGIC = 3;
    private static final int MAGICUPG = 4;

    public ScrapCommon() {
        super(ID, info);
        returnToHand = true;
        tags.add(BasicMod.CustomTags.ScrapCommon);
        setDamage(DAMAGE, UPG);
        setMagic(MAGIC, MAGICUPG);
        tags.add(BasicMod.CustomTags.NoEnergyText);
        tags.add(BasicMod.CustomTags.skipVigor);
        if (BasicMod.evolved && CardCrawlGame.isInARun()
                && AbstractDungeon.player.masterDeck != null){
            MultiCardPreview.add(this, new ScrapUncommonAttStr(), new ScrapUncommonDefDex());
            this.evolveCard();
        }

    }

    @Override
    public void evolveCard() {
        MultiCardPreview.clear(this);
        setDamage(10);
        super.evolveCard();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (PlayOnce) {
            PlayOnce = false;
            addToBot(new DamageCurrentTargetAction(this, AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
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
            if (this.alreadyEvolved) {
                addToBot(new HealAction(p, p, 3));
            } else {
                addToBot(new SfxActionVolume("MAP_SELECT_1", 0f,3.8F));
                addToBot(new ApplyPowerAction(p, p, new Crafting(p, 1)));
                addToBot(new ApplyPowerAction(p, p, new VigorPower(p, magicNumber)));
            }
            returnToHand = false;
            setExhaust(true);
        }
    }

    @Override
    public boolean freeToPlay() {
        return true;
    }
}