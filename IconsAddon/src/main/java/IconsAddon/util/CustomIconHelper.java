package IconsAddon.util;

import IconsAddon.icons.*;

import java.util.ArrayList;
import java.util.HashMap;

public class CustomIconHelper {
    private static final ArrayList<AbstractCustomIcon> allIcons = new ArrayList<>();
    private static final HashMap<String, AbstractCustomIcon> icons = new HashMap<>();
    private static final HashMap<String, AbstractCustomIcon> customIcons = new HashMap<>();

    public static void addCustomIcon(AbstractCustomIcon icon) {
        addBaseIcon(icon);
        customIcons.put(icon.cardCode(), icon);
    }

    private static void addBaseIcon(AbstractCustomIcon icon) {
        allIcons.add(icon);
        icons.put(icon.cardCode(), icon);
    }

    public static AbstractCustomIcon getIcon(String key) {
        return icons.get(key);
    }

    public static boolean isCustomIcon(String key) {
        return customIcons.containsKey(key);
    }

    public static ArrayList<AbstractCustomIcon> getAllIcons() {
        return allIcons;
    }

    public static void addDefaultIcons() {
        addBaseIcon(BleedIcon.get());
        addBaseIcon(BlindIcon.get());
        addBaseIcon(DarkIcon.get());
        addBaseIcon(DazedIcon.get());
        addBaseIcon(DeathIcon.get());
        addBaseIcon(DecreaseIcon.get());
        addBaseIcon(DrainIcon.get());
        addBaseIcon(EarthIcon.get());
        addBaseIcon(ElectricIcon.get());
        addBaseIcon(FireIcon.get());
        addBaseIcon(GearIcon.get());
        addBaseIcon(HeartIcon.get());
        addBaseIcon(HolyIcon.get());
        addBaseIcon(IceIcon.get());
        addBaseIcon(ImpactIcon.get());
        addBaseIcon(IncreaseIcon.get());
        addBaseIcon(InfatuatedIcon.get());
        addBaseIcon(LightIcon.get());
        addBaseIcon(MusicIcon.get());
        addBaseIcon(MuteIcon.get());
        addBaseIcon(ParalysisIcon.get());
        addBaseIcon(PoisonIcon.get());
        addBaseIcon(PunctureIcon.get());
        addBaseIcon(RangedIcon.get());
        addBaseIcon(RepeatIcon.get());
        addBaseIcon(RunningIcon.get());
        addBaseIcon(ShieldIcon.get());
        addBaseIcon(SlashIcon.get());
        addBaseIcon(SleepIcon.get());
        addBaseIcon(TauntIcon.get());
        addBaseIcon(SpellIcon.get());
        addBaseIcon(WaterIcon.get());
        addBaseIcon(WindIcon.get());
    }
}
