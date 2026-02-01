package ThePilotCharacter.patches;

import ThePilotCharacter.ThePilotMod;
import ThePilotCharacter.cards.BaseCard;
import ThePilotCharacter.cards.EquipmentCard;
import basemod.ReflectionHacks;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardQueueItem;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.DuplicationPower;
import com.megacrit.cardcrawl.powers.PenNibPower;
import com.megacrit.cardcrawl.powers.watcher.VigorPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.relics.PenNib;

public class OnUseCardPowersAndRelicsPatch {
    @SpirePatch(clz = VigorPower.class, method = "onUseCard")
    public static class DontConsumeVigorPatch {
        @SpirePrefixPatch
        public static SpireReturn<Void> patch(VigorPower __instance, AbstractCard card, UseCardAction action) {
            if (card instanceof EquipmentCard || card.hasTag(ThePilotMod.CustomTags.skipVigor)) {
                return SpireReturn.Return(null);
            }
            return SpireReturn.Continue();
        }
    }

    @SpirePatch(clz = PenNib.class, method = "onUseCard")
    public static class DontConsumePenNibPatch {
        @SpirePrefixPatch
        public static SpireReturn<Void> patch(PenNib __instance, AbstractCard card, UseCardAction action) {
            if ((card instanceof EquipmentCard || card.hasTag(ThePilotMod.CustomTags.skipVigor)) && __instance.counter > 8) {
                return SpireReturn.Return(null);
            }
            return SpireReturn.Continue();
        }
    }

    @SpirePatch(clz = PenNibPower.class, method = "onUseCard")
    public static class DontConsumePenNibPowerPatch {
        @SpirePrefixPatch
        public static SpireReturn<Void> patch(PenNibPower __instance, AbstractCard card, UseCardAction action) {
            if ((card instanceof EquipmentCard || card.hasTag(ThePilotMod.CustomTags.skipVigor))) {
                return SpireReturn.Return(null);
            }
            return SpireReturn.Continue();
        }
    }

    @SpirePatch(clz = DuplicationPower.class, method = "onUseCard")
    public static class DontConsumeDuplicationPowerPatch {
        @SpirePrefixPatch
        public static SpireReturn<Void> patch(DuplicationPower __instance, AbstractCard card, UseCardAction action) {
            if (card.hasTag(ThePilotMod.CustomTags.ignoreDuplication)) {
                return SpireReturn.Return(null);

            }else if(card instanceof BaseCard){
                if (!card.purgeOnUse && __instance.amount > 0) {
                    __instance.flash();
                    AbstractMonster m = null;
                    if (action.target != null) {
                        m = (AbstractMonster) action.target;
                    }
                    AbstractCard tmp = card.makeSameInstanceOf();
                    ((BaseCard) tmp).Duplicated = true;
                    AbstractDungeon.player.limbo.addToBottom(tmp);
                    tmp.current_x = card.current_x;
                    tmp.current_y = card.current_y;
                    tmp.target_x = (float) Settings.WIDTH / 2.0F - 300.0F * Settings.scale;
                    tmp.target_y = (float) Settings.HEIGHT / 2.0F;
                    if (m != null) {
                        tmp.calculateCardDamage(m);
                    }
                    tmp.purgeOnUse = true;
                    AbstractDungeon.actionManager.addCardQueueItem(new CardQueueItem(tmp, m, card.energyOnUse, true, true), true);
                    --__instance.amount;
                    if (__instance.amount == 0) {
                        AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(__instance.owner, __instance.owner, "DuplicationPower"));
                    }
                }
                return SpireReturn.Return(null);
            }
            return SpireReturn.Continue();
        }
    }

    public static void checkPenNibVigor() {
        AbstractPlayer p = AbstractDungeon.player;

        if (p.hasPower(VigorPower.POWER_ID)) {
            p.getPower(VigorPower.POWER_ID).flash();
            AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(p, p, "Vigor"));
        }
        if (p.hasPower(PenNibPower.POWER_ID)) {
            p.getPower(PenNibPower.POWER_ID).flash();
            AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(p, p, PenNibPower.POWER_ID));
            if (p.hasRelic(PenNib.ID)) {
                AbstractRelic r = p.getRelic(PenNib.ID);
                r.counter = -1;
                r.flash();
                ReflectionHacks.setPrivate(r, AbstractRelic.class, "pulse", false);
            }
        }
    }
}