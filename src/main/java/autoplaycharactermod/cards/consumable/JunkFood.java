package autoplaycharactermod.cards.consumable;

import autoplaycharactermod.cards.ConsumableCards;
import autoplaycharactermod.character.PilotCharacter;
import autoplaycharactermod.ui.ConfigPanel;
import autoplaycharactermod.util.CardStats;
import com.badlogic.gdx.math.MathUtils;
import com.evacipated.cardcrawl.mod.stslib.actions.tempHp.AddTemporaryHPAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.BiteEffect;

public class JunkFood extends ConsumableCards {
    public static final String ID = makeID("JunkFood");
    private static final CardStats info = new CardStats(
            PilotCharacter.Meta.CARD_COLOR,
            CardType.SKILL,
            CardRarity.UNCOMMON,
            CardTarget.SELF,
            -2 
    );
    private static final int MAGIC = 4;
    private static final int MAGIC_UPG = 2;

    public JunkFood() {
        super(ID, info, 4, 2);
        tags.add(CardTags.HEALING);
        setMagic(MAGIC, MAGIC_UPG);
        checkEvolve();
    }

    @Override
    public void evolveCard() {
        setMagic(7);
        super.evolveCard();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        for (int i = 0; i < (ConfigPanel.lessParticles ? 1 : 3); i++) {
            addToBot(new VFXAction(p, new BiteEffect(
                    Settings.WIDTH / 2f + MathUtils.random(120.0F, -120.0F) * Settings.scale,
                    Settings.HEIGHT / 2f + MathUtils.random(120.0F, -120.0F) * Settings.scale), 0.25f, true));
        }
        addToBot(new HealAction(p, p, magicNumber));
        addToBot(new AddTemporaryHPAction(p, p, magicNumber));
        super.use(p, m);
    }
}
