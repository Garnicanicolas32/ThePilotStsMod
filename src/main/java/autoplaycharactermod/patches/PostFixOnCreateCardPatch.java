package autoplaycharactermod.patches;

import autoplaycharactermod.BasicMod;
import autoplaycharactermod.actions.OnCreatePlayAction;
import autoplaycharactermod.cards.traitScavengeCards.NFT;
import autoplaycharactermod.character.MyCharacter;
import com.evacipated.cardcrawl.mod.stslib.StSLib;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.actions.utility.NewQueueCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.ThoughtBubble;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SpirePatch(
        clz = StSLib.class,
        method = "onCreateCard",
        paramtypez = {AbstractCard.class}
)
public class PostFixOnCreateCardPatch {

    @SpirePostfixPatch
    public static void Postfix(AbstractCard c) {
        if (AbstractDungeon.player instanceof MyCharacter && c.color == MyCharacter.Meta.CARD_COLOR && AbstractDungeon.player.hand.contains(c)) {
            AbstractDungeon.actionManager.addToBottom(new OnCreatePlayAction(c));
        }
        if (c instanceof NFT) {
            List<AbstractCard> allNFTs = new ArrayList<>();
            for (CardGroup group : Arrays.asList(
                    AbstractDungeon.player.hand,
                    AbstractDungeon.player.drawPile,
                    AbstractDungeon.player.discardPile)) {
                if (group != null) {
                    for (AbstractCard ca : group.group) {
                        if (ca instanceof NFT) {
                            allNFTs.add(ca);
                        }
                    }
                }
            }

            int nftCount = allNFTs.size();
            for (AbstractCard nft : allNFTs) {
                if (!((NFT)nft).alreadyEvolved)
                    nft.baseBlock = Math.max(0, (nft.upgraded ? NFT.BLOCK + NFT.UPG_BLOCK : NFT.BLOCK) - nftCount * NFT.MAGIC);
                nft.initializeDescription();
            }
        }
    }
}


