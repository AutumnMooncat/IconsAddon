package IconsAddon.util;

import IconsAddon.icons.*;

import java.util.ArrayList;
import java.util.HashMap;

public class DamageTypeIconHelper {
    private static ArrayList<AbstractDamageTypeIcon> allIcons = new ArrayList<>();
    private static HashMap<String, AbstractDamageTypeIcon> icons = new HashMap<>();

    public static void addIcon(AbstractDamageTypeIcon icon) {
        allIcons.add(icon);
        icons.put(icon.cardCode(), icon);
    }

    public static AbstractDamageTypeIcon getIcon(String key) {
        return icons.get(key);
    }

    public static ArrayList<AbstractDamageTypeIcon> getAllIcons() {
        return allIcons;
    }

    public static void addDefaultIcons() {
        addIcon(BleedIcon.get());
        addIcon(BlindIcon.get());
        addIcon(DarkIcon.get());
        addIcon(DazedIcon.get());
        addIcon(DeathIcon.get());
        addIcon(DecreaseIcon.get());
        addIcon(DrainIcon.get());
        addIcon(EarthIcon.get());
        addIcon(ElectricIcon.get());
        addIcon(FireIcon.get());
        addIcon(GearIcon.get());
        addIcon(HeartIcon.get());
        addIcon(HolyIcon.get());
        addIcon(IceIcon.get());
        addIcon(ImpactIcon.get());
        addIcon(IncreaseIcon.get());
        addIcon(InfatuatedIcon.get());
        addIcon(LightIcon.get());
        addIcon(MusicIcon.get());
        addIcon(MuteIcon.get());
        addIcon(ParalysisIcon.get());
        addIcon(PoisonIcon.get());
        addIcon(PunctureIcon.get());
        addIcon(RangedIcon.get());
        addIcon(RepeatIcon.get());
        addIcon(RunningIcon.get());
        addIcon(ShieldIcon.get());
        addIcon(SlashIcon.get());
        addIcon(SleepIcon.get());
        addIcon(TauntIcon.get());
        addIcon(SpellIcon.get());
        addIcon(WaterIcon.get());
        addIcon(WindIcon.get());
    }
}
