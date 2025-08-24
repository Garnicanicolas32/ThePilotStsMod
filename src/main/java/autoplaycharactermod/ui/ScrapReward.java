package autoplaycharactermod.ui;

import autoplaycharactermod.BasicMod;
import autoplaycharactermod.cards.scrap.ScrapCommon;
import autoplaycharactermod.cards.scrap.ScrapCommonDef;
import autoplaycharactermod.patches.ScrapRewardPatch;
import autoplaycharactermod.util.TextureLoader;
import basemod.abstracts.CustomReward;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;

import static autoplaycharactermod.BasicMod.makeID;

public class ScrapReward extends CustomReward {
    private static final Texture ICON = TextureLoader.getTexture(BasicMod.imagePath("Reward.png"));
    public static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(makeID("ScrapReward"));

    public ScrapReward() {
        super(ICON, uiStrings.TEXT[0], ScrapRewardPatch.SCRAPREWARD);

    }

    @Override
    public boolean claimReward() {
        if (AbstractDungeon.screen == AbstractDungeon.CurrentScreen.COMBAT_REWARD) {
            this.cards.clear();
            this.cards.add(new ScrapCommon());
            this.cards.add(new ScrapCommonDef());
            AbstractDungeon.cardRewardScreen.open(this.cards, this, uiStrings.TEXT[1]);
            AbstractDungeon.previousScreen = AbstractDungeon.CurrentScreen.COMBAT_REWARD;
        }
        return false;
    }
}
