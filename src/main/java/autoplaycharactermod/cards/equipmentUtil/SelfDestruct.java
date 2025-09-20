package autoplaycharactermod.cards.equipmentUtil;

import autoplaycharactermod.ThePilotMod;
import autoplaycharactermod.actions.SfxActionVolume;
import autoplaycharactermod.cards.BaseCard;
import autoplaycharactermod.cards.EquipmentCard;
import autoplaycharactermod.cards.traitBastionCards.FeedbackLoop;
import autoplaycharactermod.character.PilotCharacter;
import autoplaycharactermod.patches.OnUseCardPowersAndRelicsPatch;
import autoplaycharactermod.ui.ConfigPanel;
import autoplaycharactermod.util.CardStats;
import com.evacipated.cardcrawl.mod.stslib.actions.common.SelectCardsInHandAction;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.common.ExhaustSpecificCardAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.ExplosionSmallEffect;

import java.util.ArrayList;

public class SelfDestruct extends BaseCard {
    public static final String ID = makeID("SelfDestruct");
    private static final CardStats info = new CardStats(
            PilotCharacter.Meta.CARD_COLOR,
            CardType.ATTACK,
            CardRarity.RARE,
            CardTarget.ALL_ENEMY,
            0 
    );
    private static final int DAMAGE = 7;
    private static final int UPG_DAMAGE = 3;
    public static final UIStrings uiStringsShow = CardCrawlGame.languagePack.getUIString(makeID("SelectCustomAction"));

    public SelfDestruct() {
        super(ID, info);
        returnToHand = true;
        setDamage(DAMAGE, UPG_DAMAGE);
        this.isMultiDamage = true;
        tags.add(ThePilotMod.CustomTags.NoEnergyText);
        tags.add(ThePilotMod.CustomTags.skipVigor);
        returnToHand = true;
        checkEvolve();
    }

    @Override
    public void evolveCard() {
        setDamage(22);
        super.evolveCard();
    }

    @Override
    public AbstractCard replaceWith(ArrayList<AbstractCard> currentRewardCards) {
        if (ThePilotMod.unseenTutorials[2]) {
            return new FeedbackLoop();
        }
        return this;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (PlayOnce && !Duplicated) {
            PlayOnce = false;
            returnToHand = true;
        } else {
            returnToHand = false;
            addToBot(new SfxActionVolume("KEY_OBTAIN", 0f, 1.8F));

            addToBot(new SelectCardsInHandAction(10, uiStringsShow.TEXT[0], true, true, c -> c instanceof EquipmentCard, cards -> {
                if (!cards.isEmpty()) {
                    for (AbstractCard card : cards) {
                        addToBot(new SFXAction("ORB_FROST_EVOKE", 0.3f));
                        addToBot(new ExhaustSpecificCardAction(card, p.hand, Settings.FAST_MODE));
                    }
                    for (AbstractMonster mon : AbstractDungeon.getMonsters().monsters) {
                        if (!mon.isDeadOrEscaped() && !ConfigPanel.lessParticles)
                            addToBot(new VFXAction(new ExplosionSmallEffect(mon.hb.cX, mon.hb.cY)));
                    }
                    for (int i = 0; i < cards.size(); i++) {
                        addToBot(new DamageAllEnemiesAction(p, multiDamage, DamageInfo.DamageType.NORMAL, AbstractGameAction.AttackEffect.FIRE));
                    }
                    OnUseCardPowersAndRelicsPatch.checkPenNibVigor();
                }
            }));
            setExhaust(true);
        }
    }

    @Override
    public boolean freeToPlay() {
        return true;
    }
}
