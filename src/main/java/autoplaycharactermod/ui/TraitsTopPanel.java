package autoplaycharactermod.ui;

import autoplaycharactermod.BasicMod;
import autoplaycharactermod.character.MyCharacter;
import autoplaycharactermod.patches.TipHelperRenderTipBoxPatch;
import autoplaycharactermod.powers.BluePower;
import autoplaycharactermod.powers.RedPower;
import autoplaycharactermod.powers.YellowPower;
import autoplaycharactermod.util.TextureLoader;
import basemod.TopPanelItem;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.TipHelper;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.localization.UIStrings;

import static autoplaycharactermod.BasicMod.makeID;

public class TraitsTopPanel extends TopPanelItem {
    public static final String ID = makeID("TraitsTopPanel");
    public static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(makeID("ComboTags"));
    public static final PowerStrings uiStringsRed = CardCrawlGame.languagePack.getPowerStrings(makeID("RedPower"));
    public static final PowerStrings uiStringsBlue = CardCrawlGame.languagePack.getPowerStrings(makeID("BluePower"));
    public static final PowerStrings uiStringsYellow = CardCrawlGame.languagePack.getPowerStrings(makeID("YellowPower"));
    private static final Texture IMG = TextureLoader.getTexture(BasicMod.imagePath("topPanel/Ignite.png"));
    private static final Texture IMG2 = TextureLoader.getTexture(BasicMod.imagePath("topPanel/Bastion.png"));
    private static final Texture IMG3 = TextureLoader.getTexture(BasicMod.imagePath("topPanel/Scavenge.png"));
    private static final float tipYpos = Settings.HEIGHT - (120.0f * Settings.scale);
    private static final float offsetLeft = 180;
    private int current = 1;
    private String DescriptionsLinesRed;

    private String DescriptionsLinesBlue;
    private String DescriptionsLinesYellow;

    public TraitsTopPanel() {
        super(IMG, ID);
        buildLines();
    }

    @Override
    public void render(SpriteBatch sb) {
        if (AbstractDungeon.player instanceof MyCharacter) {
            if (uiStringsRed == null || uiStringsBlue == null || uiStringsYellow == null)
                buildLines();
            render(sb, Color.WHITE);
            if (getHitbox().hovered) {
                switch (current) {
                    case 1:
                        TipHelperRenderTipBoxPatch.MyTooltipColor.setOverride(Color.valueOf("#f77979"));
                        TipHelper.renderGenericTip(getHitbox().x - offsetLeft, tipYpos, uiStrings.TEXT[0], uiStrings.TEXT[4] + DescriptionsLinesRed);
                        break;
                    case 2:
                        TipHelperRenderTipBoxPatch.MyTooltipColor.setOverride(Color.valueOf("#a2affc"));
                        TipHelper.renderGenericTip(getHitbox().x - offsetLeft, tipYpos, uiStrings.TEXT[1], uiStrings.TEXT[4] + DescriptionsLinesBlue);
                        break;
                    case 3:
                        buildLinesYellow();
                        TipHelperRenderTipBoxPatch.MyTooltipColor.setOverride(Color.valueOf("#f7e979"));
                        TipHelper.renderGenericTip(getHitbox().x - offsetLeft, tipYpos, uiStrings.TEXT[2], uiStrings.TEXT[4] + DescriptionsLinesYellow);
                        break;
                }
            }
        }
    }

    @Override
    public boolean isClickable() {
        return true;
    }

    @Override
    protected void onClick() {
        switch (current) {
            case 1:
                current = 2;
                this.image = IMG2;
                break;
            case 2:
                current = 3;
                this.image = IMG3;
                buildLinesYellow();
                break;
            case 3:
                current = 1;
                this.image = IMG;
                break;
        }
    }

    public void buildLines() {
        DescriptionsLinesRed =
                uiStringsRed.DESCRIPTIONS[0] + RedPower.RANDOMDAMAGE + uiStringsRed.DESCRIPTIONS[1] +
                        uiStringsRed.DESCRIPTIONS[2] + RedPower.TARGETDAMAGE + uiStringsRed.DESCRIPTIONS[3] +
                        uiStringsRed.DESCRIPTIONS[4] + RedPower.AOEDAMAGE + uiStringsRed.DESCRIPTIONS[5] +
                        uiStringsRed.DESCRIPTIONS[6] + RedPower.STRENGTHTOADD + uiStringsRed.DESCRIPTIONS[7] +
                        uiStringsRed.DESCRIPTIONS[8] +
                        uiStringsRed.DESCRIPTIONS[9] +
                        uiStringsRed.DESCRIPTIONS[10];
        DescriptionsLinesBlue =
                uiStringsBlue.DESCRIPTIONS[0] + BluePower.SHIELDAMOUNT + uiStringsBlue.DESCRIPTIONS[1] +
                        uiStringsBlue.DESCRIPTIONS[3] + BluePower.THORNS + uiStringsBlue.DESCRIPTIONS[4] +
                        uiStringsBlue.DESCRIPTIONS[2] +
                        uiStringsBlue.DESCRIPTIONS[5] + BluePower.PLATEDARMOR + uiStringsBlue.DESCRIPTIONS[6] +
                        uiStringsBlue.DESCRIPTIONS[8] +
                        uiStringsBlue.DESCRIPTIONS[7] +
                        uiStringsBlue.DESCRIPTIONS[9];
        buildLinesYellow();
    }

    public void buildLinesYellow() {
        DescriptionsLinesYellow =
                uiStringsYellow.DESCRIPTIONS[0] + YellowPower.PUNISHMENTAMOUNT + uiStringsYellow.DESCRIPTIONS[1] +
                        uiStringsYellow.DESCRIPTIONS[6] +
                        uiStringsYellow.DESCRIPTIONS[2] + BasicMod.scavengeCount + uiStringsYellow.DESCRIPTIONS[3] + YellowPower.GOLDAMOUNT + uiStringsYellow.DESCRIPTIONS[4] +
                        uiStringsYellow.DESCRIPTIONS[5] +
                        uiStringsYellow.DESCRIPTIONS[7] +
                        uiStringsYellow.DESCRIPTIONS[8] +
                        uiStringsYellow.DESCRIPTIONS[9];
    }
}
