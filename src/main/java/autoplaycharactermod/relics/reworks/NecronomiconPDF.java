package autoplaycharactermod.relics.reworks;

import autoplaycharactermod.character.MyCharacter;
import autoplaycharactermod.relics.BaseRelic;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.curses.Necronomicurse;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.DuplicationPower;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;

import static autoplaycharactermod.BasicMod.makeID;

public class NecronomiconPDF extends BaseRelic {
    private static final String NAME = "NecronomiconPDF"; //The name will be used for determining the image file as well as the ID.
    public static final String ID = makeID(NAME); //This adds the mod's prefix to the relic ID, resulting in modID:MyRelic
    private static final RelicTier RARITY = RelicTier.SPECIAL; //The relic's rarity.
    private static final LandingSound SOUND = LandingSound.FLAT; //The sound played when the relic is clicked.

    public NecronomiconPDF() {
        super(ID, NAME, MyCharacter.Meta.CARD_COLOR, RARITY, SOUND);
    }

    public void onPlayCard(AbstractCard c, AbstractMonster m) {
        if (c.type == AbstractCard.CardType.CURSE) {
            addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));
            addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new DuplicationPower(AbstractDungeon.player, 2)));
        }
    }

    public void onCardDraw(AbstractCard drawnCard) {
        if (drawnCard.type == AbstractCard.CardType.CURSE) {
            addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));
            addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new DuplicationPower(AbstractDungeon.player, 1)));
        }
    }

    public void onEquip() {
        CardCrawlGame.sound.play("NECRONOMICON");
        AbstractDungeon.effectList.add(new ShowCardAndObtainEffect(new Necronomicurse(), (float) Settings.WIDTH / 2.0F, (float) Settings.HEIGHT / 2.0F));
    }

    public void onUnequip() {
        AbstractCard cardToRemove = null;

        for (AbstractCard c : AbstractDungeon.player.masterDeck.group) {
            if (c instanceof Necronomicurse) {
                cardToRemove = c;
                break;
            }
        }

        if (cardToRemove != null) {
            AbstractDungeon.player.masterDeck.group.remove(cardToRemove);
        }
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }
}
