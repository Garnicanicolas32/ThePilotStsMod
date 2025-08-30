package autoplaycharactermod.character;

import autoplaycharactermod.BasicMod;
import autoplaycharactermod.actions.InnateAction;
import autoplaycharactermod.cards.basic.*;
import autoplaycharactermod.cards.equipment.Coolant;
import autoplaycharactermod.cards.equipment.Drill;
import autoplaycharactermod.cards.equipment.FireSupport;
import autoplaycharactermod.cards.scrap.ScrapCommon;
import autoplaycharactermod.cards.scrap.ScrapCommonDef;
import autoplaycharactermod.potions.SneckoOilRework;
import autoplaycharactermod.powers.TargetedPower;
import autoplaycharactermod.relics.*;
import autoplaycharactermod.util.PlayTurnStartModifier;
import basemod.BaseMod;
import basemod.abstracts.CustomEnergyOrb;
import basemod.abstracts.CustomPlayer;
import basemod.animations.SpineAnimation;
import basemod.helpers.CardModifierManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.MathUtils;
import com.esotericsoftware.spine.AnimationState;
import com.evacipated.cardcrawl.modthespire.lib.SpireEnum;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.actions.utility.NewQueueCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.EnergyManager;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.cutscenes.CutscenePanel;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.ScreenShake;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.potions.SneckoOil;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.screens.CharSelectInfo;
import com.megacrit.cardcrawl.vfx.FastCardObtainEffect;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static autoplaycharactermod.BasicMod.characterPath;
import static autoplaycharactermod.BasicMod.makeID;

public class MyCharacter extends CustomPlayer {

    //Stats
    public static final int ENERGY_PER_TURN = 4;
    public static final int MAX_HP = 68;
    public static final int STARTING_GOLD = 99;
    public static final int CARD_DRAW = 0;
    public static final int ORB_SLOTS = 0;

    //Strings
    private static final String ID = makeID("ThePilot"); //This should match whatever you have in the CharacterStrings.json file
    //In-game images
    private static final String SHOULDER_1 = characterPath("shoulder.png"); //Shoulder 1 and 2 are used at rest sites.
    private static final String SHOULDER_2 = characterPath("shoulder2.png");
    private static final String CORPSE = characterPath("corpse.png"); //Corpse is when you die.
    //Textures used for the energy orb
    private static final String[] orbTextures = {
            characterPath("energyorb/layer1.png"), //When you have energy
            characterPath("energyorb/layer2.png"),
            characterPath("energyorb/layer3.png"),
            characterPath("energyorb/layer4.png"),
            characterPath("energyorb/layer5.png"),
            characterPath("energyorb/cover.png"), //"container"
            characterPath("energyorb/layer1d.png"), //When you don't have energy
            characterPath("energyorb/layer2d.png"),
            characterPath("energyorb/layer3d.png"),
            characterPath("energyorb/layer4d.png"),
            characterPath("energyorb/layer5d.png")
    };
    //Speeds at which each layer of the energy orb texture rotates. Negative is backwards.
    private static final float[] layerSpeeds = new float[]{
            -20.0F,
            20.0F,
            -40.0F,
            40.0F,
            360.0F
    };
    private final Color cardRenderColor = new Color(0.15F, 0.9F, 0.7F, 1.0F); //Used for some vfx on moving cards (sometimes) (maybe)
    private final Color cardTrailColor = new Color(0.15F, 0.9F, 0.7F, 1.0F); //Used for card trail vfx during gameplay.
    private final Color slashAttackColor = new Color(0.15F, 0.9F, 0.7F, 1.0F); //Used for a screen tint effect when you attack the heart.


    //Actual character class code below this point
    //--------------------------------------------------------------------
    public static boolean skipTargetSwitchThisTurn = true;
    public int innateAmount = 0;

    public MyCharacter() {
        super(getNames()[0], Meta.THEPILOTCHARACTER,
                new CustomEnergyOrb(orbTextures, characterPath("energyorb/vfx.png"), layerSpeeds), //Energy Orb
                new SpineAnimation(characterPath("SkeletonThePilot.atlas"), characterPath("SkeletonThePilot.json"), 1f)); //Animation

        initializeClass(null,
                SHOULDER_2,
                SHOULDER_1,
                CORPSE,
                getLoadout(),
                20.0F, -20.0F, 200.0F, 250.0F, //Character hitbox. x y position, then width and height.
                new EnergyManager(ENERGY_PER_TURN));

        //Location for text bubbles. You can adjust it as necessary later. For most characters, these values are fine.
        dialogX = (drawX + 0.0F * Settings.scale);
        dialogY = (drawY + 220.0F * Settings.scale);
        AnimationState.TrackEntry e = state.setAnimation(0, "Idle", true);
    }

    private static String[] getNames() {
        return CardCrawlGame.languagePack.getCharacterString(ID).NAMES;
    }

    /*- Below this is methods that you should *probably* adjust, but don't have to. -*/

    private static String[] getText() {
        return CardCrawlGame.languagePack.getCharacterString(ID).TEXT;
    }

    @Override
    public ArrayList<String> getStartingDeck() {
        ArrayList<String> retVal = new ArrayList<>();
        retVal.add(Strike.ID);
        retVal.add(Strike.ID);
        retVal.add(Strike.ID);
        retVal.add(Strike.ID);
        retVal.add(Defend.ID);
        retVal.add(Defend.ID);
        retVal.add(Defend.ID);
        retVal.add(Defend.ID);
        retVal.add(CoverFire.ID);
        retVal.add(DefenseAlgo.ID);

        return retVal;
    }

    @Override
    public ArrayList<String> getStartingRelics() {
        ArrayList<String> retVal = new ArrayList<>();
        retVal.add(Lightbulb.ID);
        retVal.add(RunicEngine.ID);
        return retVal;
    }

    @Override
    public AbstractCard getStartCardForEvent() {
        //This card is used for the Gremlin card matching game.
        //It should be a non-strike non-defend starter card, but it doesn't have to be.
        return AbstractDungeon.miscRng.randomBoolean() ? new CoverFire() : new DefenseAlgo();
    }

    @Override
    public int getAscensionMaxHPLoss() {
        return 4; //Max hp reduction at ascension 14+
    }

    @Override
    public AbstractGameAction.AttackEffect[] getSpireHeartSlashEffect() {
        //These attack effects will be used when you attack the heart.
        return new AbstractGameAction.AttackEffect[]{
                AbstractGameAction.AttackEffect.SLASH_VERTICAL,
                AbstractGameAction.AttackEffect.SHIELD,
                AbstractGameAction.AttackEffect.SLASH_DIAGONAL,
                AbstractGameAction.AttackEffect.FIRE,
                AbstractGameAction.AttackEffect.BLUNT_LIGHT,
                AbstractGameAction.AttackEffect.FIRE
        };
    }

    @Override
    public Color getCardRenderColor() {
        return cardRenderColor;
    }

    @Override
    public Color getCardTrailColor() {
        return cardTrailColor;
    }

    @Override
    public Color getSlashAttackColor() {
        return slashAttackColor;
    }

    @Override
    public BitmapFont getEnergyNumFont() {
        //Font used to display your current energy.
        //energyNumFontRed, Blue, Green, and Purple are used by the basegame characters.
        //It is possible to make your own, but not convenient.
        return FontHelper.energyNumFontBlue;
    }

    @Override
    public void doCharSelectScreenSelectEffect() {
        CardCrawlGame.sound.playAV("ATTACK_DEFECT_BEAM", MathUtils.random(-0.2F, 0.2F), 1.7F);
        CardCrawlGame.screenShake.shake(ScreenShake.ShakeIntensity.MED, ScreenShake.ShakeDur.SHORT, false);
    }

    @Override
    public String getCustomModeCharacterButtonSoundKey() {
        return "ATTACK_DEFECT_BEAM";
    }

    /*- You shouldn't need to edit any of the following methods. -*/

    //Don't adjust these four directly, adjust the contents of the CharacterStrings.json file.
    @Override
    public String getLocalizedCharacterName() {
        return getNames()[0];
    }

    @Override
    public String getTitle(PlayerClass playerClass) {
        return getNames()[1];
    }

    @Override
    public String getSpireHeartText() {
        return getText()[1];
    }

    @Override
    public String getVampireText() {
        return getText()[2]; //Generally, the only difference in this text is how the vampires refer to the player.
    }

    //This is used to display the character's information on the character selection screen.
    @Override
    public CharSelectInfo getLoadout() {

        return new CharSelectInfo(getNames()[0], getText()[0],
                MAX_HP, MAX_HP, ORB_SLOTS, STARTING_GOLD, CARD_DRAW, this,
                getStartingRelics(), getStartingDeck(), false);
    }

    @Override
    public AbstractCard.CardColor getCardColor() {
        return Meta.CARD_COLOR;
    }

    @Override
    public AbstractPlayer newInstance() {
        //Makes a new instance of your character class.
        return new MyCharacter();
    }

    public int savedEnergy = 0;

    @Override
    public void draw(int numCards) {
        for (int i = 0; i < numCards; ++i) {
            if (this.drawPile.isEmpty()) {
                this.gainEnergy(1);
                break;
            }
            AbstractCard c = this.drawPile.getTopCard();
            if (AbstractDungeon.actionManager.turnHasEnded) {
                savedEnergy++;
            }
            this.gainEnergy(1);
        }
    }

    private void prepareCardForDraw(AbstractCard c) {
        c.current_x = CardGroup.DRAW_PILE_X;
        c.current_y = CardGroup.DRAW_PILE_Y;
        c.setAngle(0.0F, true);
        c.lighten(false);
        c.drawScale = 0.12F;
        c.targetDrawScale = 0.75F;
        c.triggerWhenDrawn();
    }

    /// //////////////////////////////////////////////////

    private void triggerDrawEffects(AbstractCard c) {
        for (AbstractPower p : this.powers) {
            p.onCardDraw(c);
        }
        for (AbstractRelic r : this.relics) {
            r.onCardDraw(c);
        }
        if (c.type == AbstractCard.CardType.STATUS)
            for (AbstractCard ca : this.hand.group) {
                if (ca instanceof Coolant)
                    ca.triggerOnCardPlayed(c);
            }
    }

    public static AbstractMonster getTarget() {
        Optional<AbstractMonster> target = AbstractDungeon.getMonsters().monsters.stream()
                .filter(mon -> mon.hasPower(TargetedPower.POWER_ID) && !mon.isDeadOrEscaped())
                .findFirst();

        if (target.isPresent()) return target.get();

        AbstractMonster randomAlive = AbstractDungeon.getMonsters().getRandomMonster(null, true, AbstractDungeon.cardRandomRng);
        if (randomAlive != null) return randomAlive;

        for (AbstractMonster mon : AbstractDungeon.getMonsters().monsters) {
            if (mon != null) return mon;
        }

        return null;
    }

    public static void targetCheck(AbstractMonster m, boolean procsCamera) {

        if (procsCamera && AbstractDungeon.player.hasRelic(SecurityCamera.ID) && m != null && !m.isDeadOrEscaped()) {
            AbstractDungeon.actionManager.addToBottom(new RelicAboveCreatureAction(AbstractDungeon.player, AbstractDungeon.player.getRelic(SecurityCamera.ID)));
            //AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(m, AbstractDungeon.player, new WeakPower(m, 1, false)));
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(m, AbstractDungeon.player, new VulnerablePower(m, 1, false)));
        }
        boolean activated = false;
        for (AbstractCard c : AbstractDungeon.player.hand.group) {
            if (c instanceof FireSupport) {
                activated = true;
                break;
            }
        }

        if (activated) {
            AbstractDungeon.actionManager.addToBottom(new AbstractGameAction() {
                @Override
                public void update() {
                    for (AbstractCard c : AbstractDungeon.player.hand.group) {
                        if (c instanceof FireSupport) {
                            ((FireSupport) c).Activate();
                        }
                    }
                    this.isDone = true;
                }
            });
        }
    }

    public static void ApplyRandomTarget(boolean scheduledVer) {
        boolean camera = true;
        AbstractMonster target = AbstractDungeon.getMonsters().getRandomMonster(null, true, AbstractDungeon.cardRandomRng);
        if (target == getTarget() && scheduledVer)
            camera = false;
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(target, AbstractDungeon.player, new TargetedPower(target, -1)));

        if (!AbstractDungeon.getCurrRoom().monsters.areMonstersBasicallyDead() && AbstractDungeon.player.hasPower("Surrounded"))
            AbstractDungeon.player.flipHorizontal = (target.drawX < AbstractDungeon.player.drawX);

        if (!skipTargetSwitchThisTurn)
            targetCheck(target, camera);
    }

    @Override
    public void applyStartOfCombatPreDrawLogic() {
        super.applyStartOfCombatPreDrawLogic();
        skipTargetSwitchThisTurn = true;
        savedEnergy = 0;
        ApplyRandomTarget(false);
    }

    @Override
    public void applyStartOfCombatLogic() {
        super.applyStartOfCombatLogic();
        if (innateAmount > 0) {
            for (int i = 0; i < innateAmount; i++) {
                AbstractDungeon.actionManager.addToBottom(new InnateAction());
            }
        }

        AbstractRelic scryStarter = getRelic(Lightbulb.ID);
        if (hasRelic(Lightbulb.ID)) {
            ((Lightbulb) scryStarter).doAction();
        }
        AbstractRelic scryBoss = getRelic(RGBLED.ID);
        if (hasRelic(RGBLED.ID)) {
            ((RGBLED) scryBoss).doAction();
        }
        AbstractRelic evolution = getRelic(RunicEngine.ID);
        if (evolution != null) {
            ((RunicEngine) evolution).doAction();
        }
    }

    @Override
    public void applyStartOfTurnRelics() {
        long aliveCount = AbstractDungeon.getMonsters().monsters.stream()
                .filter(m -> !m.halfDead && !m.isDying && !m.isEscaping)
                .count();

        if (!skipTargetSwitchThisTurn) {
            boolean targetFound = AbstractDungeon.getMonsters().monsters.stream()
                    .anyMatch(m -> !m.isDeadOrEscaped() && m.hasPower(TargetedPower.POWER_ID));
            if (!targetFound || (hasRelic(ScheduledUpdate.ID) && aliveCount > 1)) {
                ApplyRandomTarget((hasRelic(ScheduledUpdate.ID) && aliveCount > 1));
            }
        }

        if (savedEnergy > 0) {
            AbstractDungeon.actionManager.addToBottom(new GainEnergyAction(savedEnergy));
            savedEnergy = 0;
        }

        if (aliveCount > 1 && AbstractDungeon.player.hand.size() < BaseMod.MAX_HAND_SIZE) {
            AbstractDungeon.player.hand.addToTop(new SelectTarget());
            AbstractDungeon.player.hand.refreshHandLayout();
            AbstractDungeon.player.hand.applyPowers();
        }

        skipTargetSwitchThisTurn = false;
        super.applyStartOfTurnRelics();
    }

    @Override
    public void applyStartOfTurnCards() {
        for (AbstractCard c : this.hand.group) {
            if (c != null && CardModifierManager.hasModifier(c, PlayTurnStartModifier.ID))
                AbstractDungeon.actionManager.addToBottom(new NewQueueCardAction(c, MyCharacter.getTarget(), false, true));
        }
        super.applyStartOfTurnCards();
    }

    @Override
    public void onVictory() {
        if (!this.isDying) {
            for (AbstractCard c : hand.group) {
                if (c instanceof Drill && !((Drill) c).alreadyEvolved)
                    AbstractDungeon.topLevelEffects.add(new FastCardObtainEffect(AbstractDungeon.miscRng.randomBoolean() ? new ScrapCommon() : new ScrapCommonDef(), c.current_x, c.current_y));
            }
        }
        super.onVictory();
    }

    @Override
    public boolean obtainPotion(AbstractPotion potionToObtain) {
        if (potionToObtain instanceof SneckoOil) {
            return super.obtainPotion(new SneckoOilRework());
        } else {
            return super.obtainPotion(potionToObtain);
        }
    }

    @Override
    public List<CutscenePanel> getCutscenePanels() {
        ArrayList<CutscenePanel> panels = new ArrayList<>();
        panels.add(new CutscenePanel(BasicMod.characterPath("cutscene/EndingSlice_1.png"), "ATTACK_DAGGER_2"));
        panels.add(new CutscenePanel(BasicMod.characterPath("cutscene/EndingSlice_2.png"), "ORB_PLASMA_CHANNEL"));
        panels.add(new CutscenePanel(BasicMod.characterPath("cutscene/EndingSlice_3.png"), "ATTACK_MAGIC_BEAM_SHORT"));
        return panels;
    }


    //This static class is necessary to avoid certain quirks of Java classloading when registering the character.
    public static class Meta {
        //Character select images
        private static final String CHAR_SELECT_BUTTON = characterPath("select/button.png");
        private static final String CHAR_SELECT_PORTRAIT = characterPath("select/portrait.png");
        //Character card images
        private static final String BG_ATTACK = characterPath("cardback/bg_attack.png");
        private static final String BG_ATTACK_P = characterPath("cardback/bg_attack_p.png");
        private static final String BG_SKILL = characterPath("cardback/bg_skill.png");
        private static final String BG_SKILL_P = characterPath("cardback/bg_skill_p.png");
        private static final String BG_POWER = characterPath("cardback/bg_power.png");
        private static final String BG_POWER_P = characterPath("cardback/bg_power_p.png");
        private static final String ENERGY_ORB = characterPath("cardback/energy_orb.png");
        private static final String ENERGY_ORB_P = characterPath("cardback/energy_orb_p.png");
        private static final String SMALL_ORB = characterPath("cardback/small_orb.png");
        //This is used to color *some* images, but NOT the actual cards. For that, edit the images in the cardback folder!
        private static final Color cardColor = new Color(128f / 255f, 128f / 255f, 128f / 255f, 1f);
        //These are used to identify your character, as well as your character's card color.
        //Library color is basically the same as card color, but you need both because that's how the game was made.
        @SpireEnum
        public static PlayerClass THEPILOTCHARACTER;
        @SpireEnum(name = "THEPILOT_CHARACTER_COLOR")
        // These two MUST match. Change it to something unique for your character.
        public static AbstractCard.CardColor CARD_COLOR;
        @SpireEnum(name = "THEPILOT_CHARACTER_COLOR")
        @SuppressWarnings("unused")
        public static CardLibrary.LibraryType LIBRARY_COLOR;

        //Methods that will be used in the main mod file
        public static void registerColor() {
            BaseMod.addColor(CARD_COLOR, cardColor,
                    BG_ATTACK, BG_SKILL, BG_POWER, ENERGY_ORB,
                    BG_ATTACK_P, BG_SKILL_P, BG_POWER_P, ENERGY_ORB_P,
                    SMALL_ORB);
        }

        public static void registerCharacter() {
            BaseMod.addCharacter(new MyCharacter(), CHAR_SELECT_BUTTON, CHAR_SELECT_PORTRAIT);
        }
    }
}
