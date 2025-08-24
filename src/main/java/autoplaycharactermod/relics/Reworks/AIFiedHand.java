package autoplaycharactermod.relics.Reworks;

import autoplaycharactermod.actions.AutoplayTopCardAction;
import autoplaycharactermod.character.MyCharacter;
import autoplaycharactermod.relics.BaseRelic;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static autoplaycharactermod.BasicMod.makeID;

public class AIFiedHand extends BaseRelic {
    private static final String NAME = "AIFiedHand"; //The name will be used for determining the image file as well as the ID.
    public static final String ID = makeID(NAME); //This adds the mod's prefix to the relic ID, resulting in modID:MyRelic
    private static final RelicTier RARITY = RelicTier.UNCOMMON; //The relic's rarity.
    private static final LandingSound SOUND = LandingSound.FLAT; //The sound played when the relic is clicked.

    public AIFiedHand() {
        super(ID, NAME, MyCharacter.Meta.CARD_COLOR, RARITY, SOUND);
    }

    public void onPlayCard(AbstractCard c, AbstractMonster m) {
        if (c.type == AbstractCard.CardType.POWER) {
            addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));
            addToBot(new AutoplayTopCardAction());
        }
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }
}
