package autoplaycharactermod.cards.scrap;

import autoplaycharactermod.BasicMod;
import autoplaycharactermod.actions.DamageCurrentTargetAction;
import autoplaycharactermod.cards.BaseCard;
import autoplaycharactermod.character.MyCharacter;
import autoplaycharactermod.powers.Crafting;
import autoplaycharactermod.util.CardStats;
import basemod.patches.com.megacrit.cardcrawl.cards.AbstractCard.MultiCardPreview;
import basemod.patches.com.megacrit.cardcrawl.dungeons.AbstractDungeon.NoPools;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.powers.watcher.VigorPower;

@NoPools
public class ScrapUncommonAttStr extends BaseCard {
    public static final String ID = makeID("ScrapUncommonAttStr");
    private static final CardStats info = new CardStats(
            MyCharacter.Meta.CARD_COLOR,
            CardType.ATTACK,
            CardRarity.SPECIAL,
            CardTarget.NONE,
            0
    );
    private static final int MAGIC = 1;
    private static final int MAGICUPG = 1;
    private static final int DAMAGE = 7;
    private static final int DAMAGE_UPG = 4;

    public ScrapUncommonAttStr() {
        super(ID, info);
        returnToHand = true;
        tags.add(BasicMod.CustomTags.ScrapUncommon);
        setMagic(MAGIC, MAGICUPG);
        setDamage(DAMAGE, DAMAGE_UPG);
        MultiCardPreview.add(this, new ScrapRareAtt(), new ScrapRareDef());
        tags.add(BasicMod.CustomTags.NoEnergyText);
        tags.add(BasicMod.CustomTags.skipVigor);
        this.setDisplayRarity(CardRarity.UNCOMMON);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (PlayOnce) {
            PlayOnce = false;
            addToBot(new DamageCurrentTargetAction(this, AbstractGameAction.AttackEffect.SLASH_HEAVY));
            if (p.hasPower(VigorPower.POWER_ID)) {
                p.getPower(VigorPower.POWER_ID).flash();
                addToBot(new RemoveSpecificPowerAction(p, p, "Vigor"));
            }
            returnToHand = true;
        } else {
            addToBot(new ApplyPowerAction(p, p, new Crafting(p, 2)));
            addToBot(new ApplyPowerAction(p, p, new StrengthPower(p, magicNumber)));
            returnToHand = false;
            setExhaust(true);
        }
    }

    @Override
    public boolean freeToPlay() {
        return true;
    }
}
