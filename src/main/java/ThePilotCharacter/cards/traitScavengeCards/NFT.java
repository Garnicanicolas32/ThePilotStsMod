package ThePilotCharacter.cards.traitScavengeCards;

import ThePilotCharacter.cards.TraitCard;
import ThePilotCharacter.character.PilotCharacter;
import ThePilotCharacter.ui.ConfigPanel;
import ThePilotCharacter.util.CardStats;
import ThePilotCharacter.vfx.NFTcardEffect;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.GainPennyEffect;

import java.util.Arrays;

public class NFT extends TraitCard {
    public static final String ID = makeID("NFT");
    public static final int BLOCK = 7;
    public static final int UPG_BLOCK = 4;
    public static final int MAGIC = 2;
    private static final CardStats info = new CardStats(
            PilotCharacter.Meta.CARD_COLOR,
            CardType.SKILL,
            CardRarity.COMMON,
            CardTarget.SELF,
            -2 
    );

    public NFT() {
        super(ID, info, TraitCard.TraitColor.SCAVENGE, false);
        setBlock(BLOCK, UPG_BLOCK);
        setMagic(MAGIC);
        checkEvolve();
    }

    @Override
    public void evolveCard() {
        setBlock(12);
        super.evolveCard();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        for (int i = 0; i < (ConfigPanel.lessParticles ? 10 : 20); ++i) {
            AbstractDungeon.effectList.add(new NFTcardEffect());
        }
        int count = getNFTCountBattle();
        if (this.alreadyEvolved) {
            p.gainGold(count * 15);
            for (int i = 0; i < count * 15; i++) {
                AbstractDungeon.topLevelEffects.add(new GainPennyEffect(Settings.WIDTH / 2f, Settings.HEIGHT / 2f));
            }
        }else {
            this.baseBlock = Math.max(0,(upgraded ? BLOCK + UPG_BLOCK : BLOCK) - (count - 1) * magicNumber);
        }
        applyPowersToBlock();
        addToBot(new GainBlockAction(p, p, block));
        PlayOnce = false;
        super.use(p, m);
    }

    private static int getNFTCountBattle(){
        int nftCount = 0;
        for (CardGroup group : Arrays.asList(
                AbstractDungeon.player.hand,
                AbstractDungeon.player.drawPile,
                AbstractDungeon.player.discardPile)) {
            if (group != null) {
                for (AbstractCard ca : group.group) {
                    if (ca instanceof NFT) {
                        nftCount++;
                    }
                }
            }
        }
        return nftCount;
    }
}
