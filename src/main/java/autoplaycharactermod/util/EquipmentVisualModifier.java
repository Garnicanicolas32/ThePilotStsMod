package autoplaycharactermod.util;

import autoplaycharactermod.BasicMod;
import autoplaycharactermod.cards.BaseCard;
import autoplaycharactermod.cards.EquipmentCard;
import basemod.abstracts.AbstractCardModifier;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.mod.stslib.util.extraicons.ExtraIcons;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;

import static autoplaycharactermod.BasicMod.makeID;

public class EquipmentVisualModifier extends AbstractCardModifier {

    public static final String ID = makeID("EquipmentVisualModifier");
    private static final Texture texDefault = TextureLoader.getTexture(BasicMod.imagePath("cards/EquipmentBack.png"));
    private static final Texture texCommon = TextureLoader.getTexture(BasicMod.imagePath("cards/EquipmentBackCommon.png"));
    private static final Texture texUncommon = TextureLoader.getTexture(BasicMod.imagePath("cards/EquipmentBackUncommon.png"));
    private static final Texture texRare = TextureLoader.getTexture(BasicMod.imagePath("cards/EquipmentBackRare.png"));
    private static final Texture texEvolved = TextureLoader.getTexture(BasicMod.imagePath("cards/EquipmentBackEvolved.png"));
    private final Color iconColour = new Color(1, 1, 1, 1);

    public EquipmentVisualModifier() {
    }

    @Override
    public void onRender(AbstractCard card, SpriteBatch sb) {
        Texture capacityImg;
        if (((BaseCard) card).alreadyEvolved)
            capacityImg = texEvolved;
        else
            switch (card.rarity) {
                case COMMON:
                    capacityImg = texCommon;
                    break;
                case UNCOMMON:
                    capacityImg = texUncommon;
                    break;
                case RARE:
                    capacityImg = texRare;
                    break;
                default:
                    capacityImg = texDefault;
                    break;
            }
        iconColour.a = card.transparency;
        FontHelper.cardEnergyFont_L.getData().setScale(card.drawScale);
        ExtraIcons.icon(capacityImg)
                .text(((EquipmentCard) card).equipmentHp + "/" + ((EquipmentCard) card).equipmentMaxHp)
                .drawColor(iconColour)
                .textOffsetY(-4f)
                .offsetY(104f)
                .offsetX(135f)
                .render(card);
    }

    @Override
    public void onSingleCardViewRender(AbstractCard card, SpriteBatch sb) {
        Texture capacityImg;
        switch (card.rarity) {
            case COMMON:
                capacityImg = texCommon;
                break;
            case UNCOMMON:
                capacityImg = texUncommon;
                break;
            case RARE:
                capacityImg = texRare;
                break;
            default:
                capacityImg = texDefault;
                break;
        }
        iconColour.a = card.transparency;

        float x = -2 - capacityImg.getWidth() / 2f;
        sb.setColor(iconColour);

        sb.draw(capacityImg, x + (Settings.WIDTH / 2.0f), card.current_y + 1270f * card.drawScale * Settings.scale,
                -x, 0f, (float) capacityImg.getWidth(), (float) capacityImg.getHeight(),
                Settings.scale * 2, Settings.scale * 2, 0f,
                0, 0, capacityImg.getWidth(), capacityImg.getHeight(), false, false);


        FontHelper.renderFontCentered(sb, FontHelper.SCP_cardEnergyFont,
                ((EquipmentCard) card).equipmentHp + "/" + ((EquipmentCard) card).equipmentMaxHp,
                Settings.WIDTH / 2f, card.current_y + 1370f * card.drawScale * Settings.scale, Color.WHITE.cpy());

    }

    @Override
    public String identifier(AbstractCard card) {
        return ID;
    }

    @Override
    public AbstractCardModifier makeCopy() {
        return new EquipmentVisualModifier();
    }
}

