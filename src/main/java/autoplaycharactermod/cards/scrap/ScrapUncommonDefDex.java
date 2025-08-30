package autoplaycharactermod.cards.scrap;

import autoplaycharactermod.BasicMod;
import autoplaycharactermod.cards.BaseCard;
import autoplaycharactermod.character.MyCharacter;
import autoplaycharactermod.powers.Crafting;
import autoplaycharactermod.util.CardStats;
import basemod.patches.com.megacrit.cardcrawl.cards.AbstractCard.MultiCardPreview;
import basemod.patches.com.megacrit.cardcrawl.dungeons.AbstractDungeon.NoPools;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.DexterityPower;

@NoPools
public class ScrapUncommonDefDex extends BaseCard {
    public static final String ID = makeID("ScrapUncommonDefDex");
    private static final CardStats info = new CardStats(
            MyCharacter.Meta.CARD_COLOR,
            CardType.SKILL,
            CardRarity.SPECIAL,
            CardTarget.SELF,
            0
    );
    private static final int MAGIC = 1;
    private static final int MAGICUPG = 1;
    private static final int BLOCK = 7;
    private static final int BLOCKUPG = 4;

    public ScrapUncommonDefDex() {
        super(ID, info);
        returnToHand = true;
        tags.add(BasicMod.CustomTags.ScrapUncommon);
        setMagic(MAGIC);
        setBlock(BLOCK);
        MultiCardPreview.add(this, new ScrapRareAtt(), new ScrapRareDef());
        tags.add(BasicMod.CustomTags.NoEnergyText);
        this.setDisplayRarity(CardRarity.UNCOMMON);
    }

    public boolean canUpgrade() {
        return false;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (PlayOnce && !Duplicated) {
            PlayOnce = false;
            addToBot(new GainBlockAction(p, p, block));
            returnToHand = true;
        } else {
            if (!Duplicated)
                addToBot(new ApplyPowerAction(p, p, new Crafting(p, 2)));
            addToBot(new ApplyPowerAction(p, p, new DexterityPower(p, magicNumber)));
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
