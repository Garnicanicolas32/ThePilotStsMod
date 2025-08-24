package autoplaycharactermod.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpireEnum;
import com.megacrit.cardcrawl.rewards.RewardItem;

public class ScrapRewardPatch {
    @SpireEnum
    public static RewardItem.RewardType SCRAPREWARD;
    @SpireEnum
    public static RewardItem.RewardType EXTRACARDREWARD;
}
