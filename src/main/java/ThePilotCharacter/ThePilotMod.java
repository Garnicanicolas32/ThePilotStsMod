package ThePilotCharacter;

import ThePilotCharacter.actions.TutorialCaller;
import ThePilotCharacter.cards.BaseCard;
import ThePilotCharacter.cards.optionSelection.traitReward.BlueOptionOne;
import ThePilotCharacter.cards.optionSelection.traitReward.BlueOptionThree;
import ThePilotCharacter.cards.optionSelection.traitReward.BlueOptionTwo;
import ThePilotCharacter.cards.optionSelection.traitReward.RedOptionTwo;
import ThePilotCharacter.cards.traitScavengeCards.GachaPull;
import ThePilotCharacter.character.PilotCharacter;
import ThePilotCharacter.patches.CombatRewardScreenSetupItemRewardPatch;
import ThePilotCharacter.patches.RewardItemScrapPatch;
import ThePilotCharacter.potions.BasePotion;
import ThePilotCharacter.powers.RedPower;
import ThePilotCharacter.powers.YellowPower;
import ThePilotCharacter.relics.BaseRelic;
import ThePilotCharacter.ui.*;
import ThePilotCharacter.util.GeneralUtils;
import ThePilotCharacter.util.KeywordInfo;
import ThePilotCharacter.util.TextureLoader;
import basemod.AutoAdd;
import basemod.BaseMod;
import basemod.abstracts.CustomSavable;
import basemod.interfaces.*;
import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglFileHandle;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.evacipated.cardcrawl.modthespire.Loader;
import com.evacipated.cardcrawl.modthespire.ModInfo;
import com.evacipated.cardcrawl.modthespire.Patcher;
import com.evacipated.cardcrawl.modthespire.lib.SpireConfig;
import com.evacipated.cardcrawl.modthespire.lib.SpireEnum;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.google.gson.Gson;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.RelicLibrary;
import com.megacrit.cardcrawl.localization.*;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.*;
import com.megacrit.cardcrawl.relics.*;
import com.megacrit.cardcrawl.rewards.RewardSave;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.scannotation.AnnotationDB;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.IntConsumer;
import java.util.function.IntSupplier;
import java.util.function.Supplier;

import static ThePilotCharacter.powers.YellowPower.GOLDAMOUNTSTART;

@SpireInitializer
public class ThePilotMod implements
        EditRelicsSubscriber,
        EditCardsSubscriber,
        EditCharactersSubscriber,
        EditStringsSubscriber,
        EditKeywordsSubscriber,
        PostInitializeSubscriber,
        OnPlayerTurnStartSubscriber,
        OnStartBattleSubscriber,
        StartGameSubscriber,
        PostDungeonInitializeSubscriber {
    public static final Map<String, KeywordInfo> keywords = new HashMap<>();
    private static final String resourcesFolder = checkResourcesPath();
    private static final String defaultLanguage = "eng";
    public static ModInfo info;
    public static String modID; //Edit your pom.xml to change this
    public static final Logger logger = LogManager.getLogger(modID);
    //----------------------------------------------------// My things
    public static int energySpentCombat = 0;
    public static int energySpentTurn = 0;
    public static int purchases = 0;
    public static int extracards = 0;
    public static int fusionsmade = 0;
    public static boolean extracardsoption = false;
    public static boolean evolved = false;
    public static boolean usedRedJACKPOT = false;
    public static boolean usedBlueJACKPOT = false;
    public static boolean usedYellowJACKPOT = false;
    public static boolean startWithDemon = false;
    public static boolean startWithBarricade = false;
    public static boolean startWithArtifact = false;
    public static boolean startWithBuffer = false;
    public static boolean startWithLessHP = false;
    public static int scavengeCount = GOLDAMOUNTSTART;

    public static Properties tutorialSaves = new Properties();
    public static boolean[] unseenTutorials = new boolean[]{
            true, // First Room
            true, // Traits
            true // Equipment
    };

    static {
        loadModInfo();
    }

    public ThePilotMod() {
        BaseMod.subscribe(this);
        logger.info(modID + " subscribed to BaseMod.");
    }

    public static String makeID(String id) {
        return modID + ":" + id;
    }

    public static void initialize() {
        new ThePilotMod();
        PilotCharacter.Meta.registerColor();
        try {
            for (int i = 0; i < unseenTutorials.length; i++) {
                tutorialSaves.setProperty("activeTutorials" + i, "true");
            }
            SpireConfig config = new SpireConfig("thePilot", "TutorialsViewed", tutorialSaves);
            for (int j = 0; j < unseenTutorials.length; j++) {
                unseenTutorials[j] = config.getBool("activeTutorials" + j);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String getLangString() {
        return Settings.language.name().toLowerCase();
    }

    public static String localizationPath(String lang, String file) {
        return resourcesFolder + "/localization/" + lang + "/" + file;
    }

    public static String imagePath(String file) {
        return resourcesFolder + "/images/" + file;
    }

    public static String characterPath(String file) {
        return resourcesFolder + "/images/character/" + file;
    }

    public static String powerPath(String file) {
        return resourcesFolder + "/images/powers/" + file;
    }

    public static String relicPath(String file) {
        return resourcesFolder + "/images/relics/" + file;
    }

    private static String checkResourcesPath() {
        String name = ThePilotMod.class.getName(); //getPackage can be iffy with patching, so class name is used instead.
        int separator = name.indexOf('.');
        if (separator > 0)
            name = name.substring(0, separator);

        FileHandle resources = new LwjglFileHandle(name, Files.FileType.Internal);
        if (!resources.exists()) {
            throw new RuntimeException("\n\tFailed to find resources folder; expected it to be at  \"resources/" + name + "\"." +
                    " Either make sure the folder under resources has the same name as your mod's package, or change the line\n" +
                    "\t\"private static final String resourcesFolder = checkResourcesPath();\"\n" +
                    "\tat the top of the " + ThePilotMod.class.getSimpleName() + " java file.");
        }
        if (!resources.child("images").exists()) {
            throw new RuntimeException("\n\tFailed to find the 'images' folder in the mod's 'resources/" + name + "' folder; Make sure the " +
                    "images folder is in the correct location.");
        }
        if (!resources.child("localization").exists()) {
            throw new RuntimeException("\n\tFailed to find the 'localization' folder in the mod's 'resources/" + name + "' folder; Make sure the " +
                    "localization folder is in the correct location.");
        }
        return name;
    }

    private static void loadModInfo() {
        Optional<ModInfo> infos = Arrays.stream(Loader.MODINFOS).filter((modInfo) -> {
            AnnotationDB annotationDB = Patcher.annotationDBMap.get(modInfo.jarURL);
            if (annotationDB == null)
                return false;
            Set<String> initializers = annotationDB.getAnnotationIndex().getOrDefault(SpireInitializer.class.getName(), Collections.emptySet());
            return initializers.contains(ThePilotMod.class.getName());
        }).findFirst();
        if (infos.isPresent()) {
            info = infos.get();
            modID = info.ID;
        } else {
            throw new RuntimeException("Failed to determine mod info/ID based on initializer.");
        }
    }

    @Override
    public void receivePostInitialize() {
        //This loads the image used as an icon in the in-game mods menu.
        Texture badgeTexture = TextureLoader.getTexture(imagePath("badge.png"));

        //If you want to set up a config panel, that will be done here.
        //You can find information about this on the BaseMod wiki page "Mod Config and Panel".

        BaseMod.registerModBadge(badgeTexture, info.Name, GeneralUtils.arrToString(info.Authors), info.Description, new ConfigPanel());
        registerPotions();
        BaseMod.addTopPanelItem(new TraitsTopPanel());

        BaseMod.registerCustomReward(
                RewardItemScrapPatch.SCRAPREWARD,
                (rewardSave) -> { // this handles what to do when this quest type is loaded.
                    return new ScrapReward();
                },
                (customReward) -> { // this handles what to do when this quest type is saved.
                    return new RewardSave(customReward.type.toString(), null, 0, 0);
                });

        registerBooleanSave(makeID("evolved"), () -> evolved, v -> evolved = v);
        registerBooleanSave(makeID("usedRedJACKPOT"), () -> usedRedJACKPOT, v -> usedRedJACKPOT = v);
        registerBooleanSave(makeID("usedBlueJACKPOT"), () -> usedBlueJACKPOT, v -> usedBlueJACKPOT = v);
        registerBooleanSave(makeID("usedYellowJACKPOT"), () -> usedYellowJACKPOT, v -> usedYellowJACKPOT = v);
        registerBooleanSave(makeID("startWithDemon"), () -> startWithDemon, v -> startWithDemon = v);
        registerBooleanSave(makeID("startWithBarricade"), () -> startWithBarricade, v -> startWithBarricade = v);
        registerBooleanSave(makeID("startWithArtifact"), () -> startWithArtifact, v -> startWithArtifact = v);
        registerBooleanSave(makeID("startWithBuffer"), () -> startWithBuffer, v -> startWithBuffer = v);
        registerBooleanSave(makeID("startWithLessHP"), () -> startWithLessHP, v -> startWithLessHP = v);
        registerBooleanSave(makeID("extracardsoption"), () -> extracardsoption, v -> extracardsoption = v);
        registerIntSave(makeID("extracards"), () -> extracards, v -> extracards = v);
        registerIntSave(makeID("fusionsmade"), () -> fusionsmade, v -> fusionsmade = v);
        registerIntSave(makeID("purchases"), () -> purchases, v -> purchases = v);
        registerIntSave(makeID("scavengeCount"), () -> scavengeCount, v -> scavengeCount = v);
        registerIntSave(makeID("scryAmount"), () -> ScryButton.scryAmount, v -> ScryButton.scryAmount = v);
    }

    public static void registerPotions() {
        new AutoAdd(modID) //Loads files from this mod
                .packageFilter(BasePotion.class) //In the same package as this class
                .any(BasePotion.class, (info, potion) -> { //Run this code for any classes that extend this class
                    //These three null parameters are colors.
                    //If they're not null, they'll overwrite whatever color is set in the potions themselves.
                    //This is an old feature added before having potions determine their own color was possible.
                    BaseMod.addPotion(potion.getClass(), null, null, null, potion.ID, potion.playerClass);
                    //playerClass will make a potion character-specific. By default, it's null and will do nothing.
                });
    }

    public static void registerBooleanSave(String key, Supplier<Boolean> getter, Consumer<Boolean> setter) {
        BaseMod.addSaveField(key, new CustomSavable<Boolean>() {
            @Override
            public Boolean onSave() {
                return getter.get();
            }

            @Override
            public void onLoad(Boolean value) {
                setter.accept(value != null && value);
            }
        });
    }

    public static void registerIntSave(String key, IntSupplier getter, IntConsumer setter) {
        BaseMod.addSaveField(key, new CustomSavable<Integer>() {
            @Override
            public Integer onSave() {
                return getter.getAsInt();
            }

            @Override
            public void onLoad(Integer value) {
                if (value != null) {
                    setter.accept(value);
                }
            }
        });
    }

    @Override
    public void receiveEditStrings() {
        loadLocalization(defaultLanguage); //no exception catching for default localization; you better have at least one that works.
        if (!defaultLanguage.equals(getLangString())) {
            try {
                loadLocalization(getLangString());
            } catch (GdxRuntimeException e) {
                e.printStackTrace();
            }
        }
    }

    private void loadLocalization(String lang) {
        //While this does load every type of localization, most of these files are just outlines so that you can see how they're formatted.
        //Feel free to comment out/delete any that you don't end up using.
        BaseMod.loadCustomStringsFile(CardStrings.class,
                localizationPath(lang, "CardStrings.json"));
        BaseMod.loadCustomStringsFile(CharacterStrings.class,
                localizationPath(lang, "CharacterStrings.json"));
        BaseMod.loadCustomStringsFile(PotionStrings.class,
                localizationPath(lang, "PotionStrings.json"));
        BaseMod.loadCustomStringsFile(PowerStrings.class,
                localizationPath(lang, "PowerStrings.json"));
        BaseMod.loadCustomStringsFile(RelicStrings.class,
                localizationPath(lang, "RelicStrings.json"));
        BaseMod.loadCustomStringsFile(UIStrings.class,
                localizationPath(lang, "UIStrings.json"));
        BaseMod.loadCustomStringsFile(TutorialStrings.class,
                localizationPath(lang, "TutorialStrings.json"));
    }

    @Override
    public void receiveEditKeywords() {
        Gson gson = new Gson();
        String json = Gdx.files.internal(localizationPath(defaultLanguage, "Keywords.json")).readString(String.valueOf(StandardCharsets.UTF_8));
        KeywordInfo[] keywords = gson.fromJson(json, KeywordInfo[].class);
        for (KeywordInfo keyword : keywords) {
            keyword.prep();
            registerKeyword(keyword);
        }

        if (!defaultLanguage.equals(getLangString())) {
            try {
                json = Gdx.files.internal(localizationPath(getLangString(), "Keywords.json")).readString(String.valueOf(StandardCharsets.UTF_8));
                keywords = gson.fromJson(json, KeywordInfo[].class);
                for (KeywordInfo keyword : keywords) {
                    keyword.prep();
                    registerKeyword(keyword);
                }
            } catch (Exception e) {
                logger.warn(modID + " does not support " + getLangString() + " keywords.");
            }
        }
    }

    private void registerKeyword(KeywordInfo info) {
        BaseMod.addKeyword(modID.toLowerCase(), info.PROPER_NAME, info.NAMES, info.DESCRIPTION);
        if (!info.ID.isEmpty()) {
            keywords.put(info.ID, info);
        }
    }

    @Override
    public void receiveEditCharacters() {
        PilotCharacter.Meta.registerCharacter();
    }

    @Override
    public void receiveEditCards() {
        new AutoAdd(modID) //Loads files from this mod
                .packageFilter(BaseCard.class) //In the same package as this class
                .setDefaultSeen(true) //And marks them as seen in the compendium
                .cards(); //Adds the cards
    }

    @Override
    public void receiveEditRelics() { //somewhere in the class
        new AutoAdd(modID) //Loads files from this mod
                .packageFilter(BaseRelic.class) //In the same package as this class
                .any(BaseRelic.class, (info, relic) -> { //Run this code for any classes that extend this class
                    if (relic.pool != null)
                        BaseMod.addRelicToCustomPool(relic, relic.pool); //Register a custom character specific relic
                    else
                        BaseMod.addRelic(relic, relic.relicType); //Register a shared or base game character specific relic

                    //If the class is annotated with @AutoAdd.Seen, it will be marked as seen, making it visible in the relic library.
                    //If you want all your relics to be visible by default, just remove this if statement.
//                    if (info.seen)
                    UnlockTracker.markRelicAsSeen(relic.relicId);
                });
    }

    // custom things ////////////////////////////////////////////

    public static void energySpentTrigger() {
        energySpentCombat++;
        energySpentTurn++;
        for (CardGroup group : Arrays.asList(
                AbstractDungeon.player.hand,
                AbstractDungeon.player.drawPile,
                AbstractDungeon.player.discardPile)) {
            if (group != null) {
                for (AbstractCard ca : group.group) {
                    if (ca instanceof BaseCard) {
                        ((BaseCard)ca).updateTextCount();
                    }
                }
            }
        }
    }

    @Override
    public void receiveOnPlayerTurnStart() {
        energySpentTurn = 0;
        for (CardGroup group : Arrays.asList(
                AbstractDungeon.player.hand,
                AbstractDungeon.player.drawPile,
                AbstractDungeon.player.discardPile)) {
            if (group != null) {
                for (AbstractCard ca : group.group) {
                    if (ca instanceof BaseCard) {
                        ((BaseCard)ca).updateTextCount();
                    }
                }
            }
        }
    }

    @Override
    public void receiveOnBattleStart(AbstractRoom abstractRoom) {
        AbstractPlayer p = AbstractDungeon.player;
        if (AbstractDungeon.player instanceof PilotCharacter && unseenTutorials[0]) {
            AbstractDungeon.actionManager.addToTop(new TutorialCaller(0));
        }
        GachaPull.cardsList = GachaPull.getList(true);
        energySpentCombat = 0;
        YellowPower.usedScrap = false;
        YellowPower.usedPotion = false;
        YellowPower.usedCardReward = false;
        YellowPower.usedUpgrade = false;
        YellowPower.usesGold = 0;
        RedPower.usedStun = false;

        if (startWithDemon)
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new DemonFormPower(p,1)));
        if (startWithLessHP) {
            for (AbstractMonster m : (AbstractDungeon.getCurrRoom()).monsters.monsters) {
                int newHP = Math.max(1, m.maxHealth - (m.maxHealth * RedOptionTwo.MAGIC / 100));
                if (m.currentHealth > newHP) {
                    m.currentHealth = newHP;
                    m.healthBarUpdatedEvent();
                }
            }
        }
        if (startWithBarricade)
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new BlurPower(p, BlueOptionOne.MAGIC)));
        if (startWithBuffer)
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new BufferPower(p, BlueOptionTwo.MAGIC)));
        if (startWithArtifact)
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new ArtifactPower(p, BlueOptionThree.MAGIC)));
    }
    //System.out.println("");

    @Override
    public void receivePostDungeonInitialize() {
        if (AbstractDungeon.player instanceof PilotCharacter) {
            AbstractDungeon.bossRelicPool.remove(SneckoEye.ID);
            AbstractDungeon.bossRelicPool.remove(RunicPyramid.ID);
            AbstractDungeon.shopRelicPool.remove(ChemicalX.ID);
            AbstractDungeon.shopRelicPool.remove(StrangeSpoon.ID);
            AbstractDungeon.rareRelicPool.remove(UnceasingTop.ID);
            AbstractDungeon.rareRelicPool.remove(GamblingChip.ID);
            AbstractDungeon.uncommonRelicPool.remove(MummifiedHand.ID);
        }
    }

    @Override
    public void receiveStartGame() {
        CombatRewardScreenSetupItemRewardPatch.calledonce = false;
        if (!CardCrawlGame.loadingSave) {
            //System.out.println("Resetting stats!");
            evolved = false;
            usedRedJACKPOT = false;
            usedBlueJACKPOT = false;
            usedYellowJACKPOT = false;
            startWithDemon = false;
            startWithBarricade = false;
            startWithArtifact = false;
            startWithBuffer = false;
            startWithLessHP = false;
            ScryButton.scryAmount = ConfigPanel.debugScry;
            purchases = 0;
            extracards = 0;
            fusionsmade = 0;
            extracardsoption = false;
            scavengeCount = GOLDAMOUNTSTART;
        }
    }

    public static AbstractRelic returnTrueRandomScreenlessRelic() {
        ArrayList<AbstractRelic> eligibleRelicsList = new ArrayList<>();
        ArrayList<AbstractRelic> myGoodStuffList = new ArrayList<>();
        for (String r : AbstractDungeon.commonRelicPool) {
            eligibleRelicsList.add(RelicLibrary.getRelic(r));
        }
        for (String r : AbstractDungeon.uncommonRelicPool) {
            eligibleRelicsList.add(RelicLibrary.getRelic(r));
        }
        for (String r : AbstractDungeon.rareRelicPool) {
            eligibleRelicsList.add(RelicLibrary.getRelic(r));
        }
        try {
            for (AbstractRelic r : eligibleRelicsList)
                if (r.getClass().getMethod("onEquip").getDeclaringClass() == AbstractRelic.class && r.getClass().getMethod("onUnequip").getDeclaringClass() == AbstractRelic.class) {
                    myGoodStuffList.add(r);
                }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        if (myGoodStuffList.isEmpty()) {
            return new Circlet();
        } else {
            myGoodStuffList.removeIf(r -> AbstractDungeon.player.hasRelic(r.relicId));
            return myGoodStuffList.get(AbstractDungeon.cardRandomRng.random(myGoodStuffList.size() - 1));
        }
    }

    public static void saveTutorialsSeen() throws IOException {
        SpireConfig config = new SpireConfig("thePilot", "TutorialsViewed");
        int i;
        for (i = 0; i < unseenTutorials.length; i++) {
            config.setBool("activeTutorials" + i, unseenTutorials[i]);
        }
        config.save();
    }

    public static class CustomTags {
        @SpireEnum
        public static AbstractCard.CardTags ScrapCommon;
        @SpireEnum
        public static AbstractCard.CardTags ScrapUncommon;
        @SpireEnum
        public static AbstractCard.CardTags ScrapRare;
        @SpireEnum
        public static AbstractCard.CardTags Evolution;
        @SpireEnum
        public static AbstractCard.CardTags NoEnergyText;
        @SpireEnum
        public static AbstractCard.CardTags skipVigor;
        @SpireEnum
        public static AbstractCard.CardTags ignoreDuplication;
    }

    public static boolean isInCombat() {
        return CardCrawlGame.isInARun() && AbstractDungeon.currMapNode != null && AbstractDungeon.getCurrRoom() != null && AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT;
    }
}
