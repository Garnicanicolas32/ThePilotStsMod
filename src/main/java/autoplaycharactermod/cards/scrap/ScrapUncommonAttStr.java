package autoplaycharactermod.cards.scrap;

import autoplaycharactermod.BasicMod;
import autoplaycharactermod.actions.DamageCurrentTargetAction;
import autoplaycharactermod.cards.BaseCard;
import autoplaycharactermod.character.MyCharacter;
import autoplaycharactermod.patches.OnUseCardPowersAndRelicsPatch;
import autoplaycharactermod.powers.Crafting;
import autoplaycharactermod.util.CardStats;
import basemod.patches.com.megacrit.cardcrawl.cards.AbstractCard.MultiCardPreview;
import basemod.patches.com.megacrit.cardcrawl.dungeons.AbstractDungeon.NoPools;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.StrengthPower;

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
        setMagic(MAGIC);
        setDamage(DAMAGE);
        MultiCardPreview.add(this, new ScrapRareAtt(), new ScrapRareDef());
        tags.add(BasicMod.CustomTags.NoEnergyText);
        tags.add(BasicMod.CustomTags.skipVigor);
        this.setDisplayRarity(CardRarity.UNCOMMON);
    }

    public boolean canUpgrade() {
        return false;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (PlayOnce && !Duplicated) {
            PlayOnce = false;
            addToBot(new DamageCurrentTargetAction(this, AbstractGameAction.AttackEffect.SLASH_HEAVY));
            OnUseCardPowersAndRelicsPatch.checkPenNibVigor();
            returnToHand = true;
        } else {
            if (!Duplicated)
                addToBot(new ApplyPowerAction(p, p, new Crafting(p, 2)));
            addToBot(new ApplyPowerAction(p, p, new StrengthPower(p, magicNumber)));
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
