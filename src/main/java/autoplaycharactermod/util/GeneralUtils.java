package autoplaycharactermod.util;

import autoplaycharactermod.cards.equipment.*;
import autoplaycharactermod.cards.traitBastionCards.*;
import autoplaycharactermod.cards.traitIgnitionCards.*;
import autoplaycharactermod.cards.traitScavengeCards.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import java.util.Arrays;
import java.util.List;

public class GeneralUtils {
    private static final List<AbstractCard> bastionCardPool = Arrays.asList(
            new DataCache(),
            new Beacon(),
            new Cryostasis(),
            new TakeCover(),
            new Flash(),
            new HeavyShield()
    );
    private static final List<AbstractCard> scavengeCardPool = Arrays.asList(
            new GachaPull(),
            new CouponStamp(),
            new DuctTape(),
            new LostAndFound(),
            new NFT(),
            new TrashCannon()
    );
    private static final List<AbstractCard> IgnitionCardPool = Arrays.asList(
            new BurningPayload(),
            new CombustionRounds(),
            new IgnitionProtocol(),
            new ScorchedCore(),
            new ThermalSurge(),
            new Flamethrower()
    );
    private static final List<AbstractCard> EquipmentPool = Arrays.asList(
            new Coolant(),
            new EnergyChamber(),
            new FailSafe(),
            new FireSupport(),
            new Flamethrower(),
            new GrapplingHook(),
            new HeavyShield(),
            new InstructionManual(),
            new Metronome(),
            new MicroMissiles(),
            new PoisonMine(),
            new PowerBank(),
            new RocketPunch(),
            new RustyBlade(),
            new Shredder(),
            new SmartGun(),
            new TacticalVisor(),
            new TeslaCoil(),
            new TrashCannon(),
            new TungstenCoat()
    );

    public static String arrToString(Object[] arr) {
        if (arr == null)
            return null;
        if (arr.length == 0)
            return "";

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < arr.length - 1; ++i) {
            sb.append(arr[i]).append(", ");
        }
        sb.append(arr[arr.length - 1]);
        return sb.toString();
    }

    public static String removePrefix(String ID) {
        return ID.substring(ID.indexOf(":") + 1);
    }

    public static AbstractCard getRandomIgitionCard() {
        int index = AbstractDungeon.cardRandomRng.random(IgnitionCardPool.size() - 1);
        return IgnitionCardPool.get(index).makeCopy();
    }

    public static AbstractCard getRandomBastionCard() {
        int index = AbstractDungeon.cardRandomRng.random(bastionCardPool.size() - 1);
        return bastionCardPool.get(index).makeCopy();
    }

    public static AbstractCard getRandomScavengeCard() {
        int index = AbstractDungeon.cardRandomRng.random(scavengeCardPool.size() - 1);
        return scavengeCardPool.get(index).makeCopy();
    }

    public static AbstractCard getRandomEquipmentCard() {
        int index = AbstractDungeon.cardRandomRng.random(EquipmentPool.size() - 1);
        return EquipmentPool.get(index).makeCopy();
    }
}
