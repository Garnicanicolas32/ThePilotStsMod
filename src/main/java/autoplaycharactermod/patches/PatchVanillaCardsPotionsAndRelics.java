package autoplaycharactermod.patches;

import autoplaycharactermod.actions.ModifiedCardFromDeckToHandAction;
import autoplaycharactermod.actions.ModifiedDiscardPileToHandAction;
import autoplaycharactermod.actions.modifiedDrawPileToHandAction;
import autoplaycharactermod.character.MyCharacter;
import autoplaycharactermod.potions.SneckoOilRework;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.colorless.SecretTechnique;
import com.megacrit.cardcrawl.cards.colorless.SecretWeapon;
import com.megacrit.cardcrawl.cards.colorless.Violence;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PotionHelper;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.potions.LiquidMemories;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.relics.CentennialPuzzle;
import com.megacrit.cardcrawl.relics.Torii;
import com.megacrit.cardcrawl.rooms.AbstractRoom;

import java.util.Objects;

public class PatchVanillaCardsPotionsAndRelics {
    @SpirePatch(
            clz = SecretTechnique.class,
            method = "use",
            paramtypez={
                    AbstractPlayer.class,
                    AbstractMonster.class,
            }
    )
    public static class patchSecretTechniqueSkill {
        @SpirePrefixPatch
        public static SpireReturn<Void> Prefix(SecretTechnique __instance, AbstractPlayer p, AbstractMonster m) {
            if (AbstractDungeon.player instanceof MyCharacter){
                AbstractDungeon.actionManager.addToBottom(new ModifiedCardFromDeckToHandAction(1, false, AbstractCard.CardType.SKILL));
                return SpireReturn.Return();
            }
            else
                return SpireReturn.Continue();
        }
    }

    @SpirePatch(
            clz = SecretWeapon.class,
            method = "use",
            paramtypez={
                    AbstractPlayer.class,
                    AbstractMonster.class,
            }
    )
    public static class patchSecretTechniqueAttack {
        @SpirePrefixPatch
        public static SpireReturn<Void> Prefix(SecretWeapon __instance, AbstractPlayer p, AbstractMonster m) {
            if (AbstractDungeon.player instanceof MyCharacter){
                AbstractDungeon.actionManager.addToBottom(new ModifiedCardFromDeckToHandAction(1, false, AbstractCard.CardType.ATTACK));
                return SpireReturn.Return();
            }
            else
                return SpireReturn.Continue();
        }
    }

    @SpirePatch(
            clz = Violence.class,
            method = "use",
            paramtypez={
                    AbstractPlayer.class,
                    AbstractMonster.class,
            }
    )
    public static class patchViolence {
        @SpirePrefixPatch
        public static SpireReturn<Void> Prefix(Violence __instance, AbstractPlayer p, AbstractMonster m) {
            if (AbstractDungeon.player instanceof MyCharacter){
                AbstractDungeon.actionManager.addToBottom(new modifiedDrawPileToHandAction(3, AbstractCard.CardType.ATTACK));
                return SpireReturn.Return();
            }
            else
                return SpireReturn.Continue();
        }
    }

    @SpirePatch(
            clz = LiquidMemories.class,
            method = "use",
            paramtypez={
                    AbstractCreature.class,
            }
    )
    public static class patchLiquidMemories {
        @SpirePrefixPatch
        public static SpireReturn<Void> Prefix(LiquidMemories __instance, AbstractCreature target) {
            if (AbstractDungeon.player instanceof MyCharacter){
                AbstractDungeon.actionManager.addToBottom(new ModifiedDiscardPileToHandAction(__instance.getPotency(), 0));
                return SpireReturn.Return();
            }
            else
                return SpireReturn.Continue();
        }
    }

    @SpirePatch(
            clz = PotionHelper.class,
            method = "getPotion",
            paramtypez={
                    String.class,
            }
    )
    public static class patchSneckoOil {
        @SpirePrefixPatch
        public static SpireReturn<AbstractPotion> Prefix(String name) {
            if (AbstractDungeon.player instanceof MyCharacter && Objects.equals(name, "SneckoOil")){
                return SpireReturn.Return(new SneckoOilRework());
            }
            else
                return SpireReturn.Continue();
        }
    }
}

