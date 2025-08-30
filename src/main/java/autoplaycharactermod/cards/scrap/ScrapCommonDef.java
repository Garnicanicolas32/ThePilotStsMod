package autoplaycharactermod.cards.scrap;

import autoplaycharactermod.BasicMod;
import autoplaycharactermod.actions.SfxActionVolume;
import autoplaycharactermod.cards.BaseCard;
import autoplaycharactermod.character.MyCharacter;
import autoplaycharactermod.powers.Crafting;
import autoplaycharactermod.util.CardStats;
import basemod.patches.com.megacrit.cardcrawl.cards.AbstractCard.MultiCardPreview;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.ThornsPower;

public class ScrapCommonDef extends BaseCard {
    public static final String ID = makeID("ScrapCommonDef");
    private static final CardStats info = new CardStats(
            MyCharacter.Meta.CARD_COLOR,
            CardType.SKILL,
            CardRarity.COMMON,
            CardTarget.SELF,
            0
    );
    private static final int UPG = 4;
    private static final int SHIELD = 4;
    private static final int MAGIC = 1;
    private static final int MAGICUPG = 1;

    public ScrapCommonDef() {
        super(ID, info);
        returnToHand = true;
        tags.add(BasicMod.CustomTags.ScrapCommon);
        setBlock(SHIELD);
        setMagic(MAGIC);
        tags.add(BasicMod.CustomTags.NoEnergyText);
        if (BasicMod.evolved && CardCrawlGame.isInARun()
                && AbstractDungeon.player.masterDeck != null) {
            MultiCardPreview.add(this, new ScrapUncommonAttStr(), new ScrapUncommonDefDex());
            this.evolveCard();
        }
    }

    public boolean canUpgrade() {
        return false;
    }

    @Override
    public void evolveCard() {
        MultiCardPreview.clear(this);
        setBlock(10);
        super.evolveCard();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {

        if (PlayOnce && !Duplicated) {
            PlayOnce = false;
            addToBot(new GainBlockAction(p, p, block));
            returnToHand = true;
        } else {
            if (this.alreadyEvolved) {
                addToBot(new HealAction(p, p, 3));
            } else {
                addToBot(new SfxActionVolume("MAP_SELECT_1", 0f, 3.8F));
                if (!Duplicated)
                    addToBot(new ApplyPowerAction(p, p, new Crafting(p, 1)));
                addToBot(new ApplyPowerAction(p, p, new ThornsPower(p, magicNumber)));
            }
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
