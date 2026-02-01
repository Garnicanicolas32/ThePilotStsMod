package ThePilotCharacter.vfx;

import ThePilotCharacter.cards.scrap.*;
import ThePilotCharacter.ui.ConfigPanel;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.colorless.Madness;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

import java.util.ArrayList;
import java.util.List;

public class OmegaGroupEffect extends AbstractGameEffect {
    private List<AbstractCard> currentCards = new ArrayList<>();
    private int tier;
    private boolean spawnedParticles = false;
    private float waitTimer = 0.8f;
    private boolean clank = false;

    public OmegaGroupEffect(int tier, int count) {
        this.tier = tier;
        this.duration = 10f;
        CardCrawlGame.sound.play("CARD_OBTAIN");
        for (int i = 0; i < count; i++) {
            currentCards.add(makeRandomCardForTier(tier));
        }
    }

    private AbstractCard makeRandomCardForTier(int tier) {
        switch (tier) {
            case 1:
                return MathUtils.randomBoolean() ? new ScrapCommon() : new ScrapCommonDef();
            case 2:
                return MathUtils.randomBoolean() ? new ScrapUncommonAttStr() : new ScrapUncommonDefDex();
            case 3:
                return MathUtils.randomBoolean() ? new ScrapRareAtt() : new ScrapRareDef();
            default:
                return new Madness();
        }
    }

    public void update() {
        if (!spawnedParticles) {
            spawnedParticles = true;
            for (AbstractCard c : currentCards) {
                c.current_x = MathUtils.random(Settings.WIDTH * 0.1f, Settings.WIDTH * 0.9f);
                c.current_y = MathUtils.random(Settings.HEIGHT * 0.2f, Settings.HEIGHT * 0.8f);
                float targetX = Settings.WIDTH / 2f;
                float targetY = Settings.HEIGHT / 1.75f;

                AbstractDungeon.effectsQueue.add(
                        new OmegaParticleEffect(c, c.current_x, c.current_y, targetX, targetY, tier)
                );
            }
            if (!ConfigPanel.lessParticles)
                for (int i = 0; i < 100; i++) {
                    AbstractDungeon.effectsQueue.add(new TransformParticles(Settings.WIDTH / 2f, Settings.HEIGHT / 1.75f, 0.6f + tier * 0.1f));
                }
        }
        waitTimer -= Gdx.graphics.getDeltaTime();

        if (!ConfigPanel.lessParticles && !clank && waitTimer <= 0.8f) {
            clank = true;
            for (int i = 0; i < 8 - tier * 2; i++)
                AbstractDungeon.topLevelEffects.add(new ScrapUpgradeEffect(MathUtils.random(Settings.WIDTH * 0.2f, Settings.WIDTH * 0.8f),
                        MathUtils.random(Settings.HEIGHT * 0.2f, Settings.HEIGHT * 0.8f), tier));
            CardCrawlGame.sound.playAV("CARD_UPGRADE", 0.45F - 0.15F * tier, 1f);
        }
        if (waitTimer <= 0f) {
            boolean allDone = AbstractDungeon.effectList.stream()
                    .filter(e -> e instanceof OmegaParticleEffect)
                    .anyMatch(e -> !e.isDone);

            if (!allDone) {
                isDone = true;

                if (tier == 1) {
                    AbstractDungeon.effectsQueue.add(new OmegaGroupEffect(2, 9));
                } else if (tier == 2) {
                    AbstractDungeon.effectsQueue.add(new OmegaGroupEffect(3, 3));
                } else if (tier == 3) {
                    AbstractDungeon.effectsQueue.add(new ShowOmegaEffect(new ScrapUltraRare(), Settings.WIDTH / 2f, Settings.HEIGHT / 1.93f));
                }
            }
        }
    }

    public void render(SpriteBatch sb) {
        sb.setColor(0f, 0f, 0f, 0.7f);
        sb.draw(
                ImageMaster.WHITE_SQUARE_IMG,
                0, 0,
                Settings.WIDTH, Settings.HEIGHT
        );
        sb.setColor(1f, 1f, 1f, 1f);
        for (AbstractCard c : currentCards) {
            c.render(sb);
        }
    }

    public void dispose() {
    }
}